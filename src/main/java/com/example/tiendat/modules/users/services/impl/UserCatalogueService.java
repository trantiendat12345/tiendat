package com.example.tiendat.modules.users.services.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tiendat.modules.users.services.interfaces.UserCatalogueServiceInterface;
import com.example.tiendat.services.BaseService;
import com.example.tiendat.specifications.BaseSpecification;

import jakarta.persistence.EntityNotFoundException;

import com.example.tiendat.helpers.FilterParameter;
import com.example.tiendat.modules.users.entities.UserCatalogue;
import com.example.tiendat.modules.users.mappers.UserCatalogueMapper;
import com.example.tiendat.modules.users.requests.UserCatalogue.StoreRequest;
import com.example.tiendat.modules.users.requests.UserCatalogue.UpdateRequest;
import com.example.tiendat.modules.users.repositories.UserCatalogueRepository;

@Service
public class UserCatalogueService extends BaseService implements UserCatalogueServiceInterface {

    private static final Logger logger = LoggerFactory.getLogger(UserCatalogueService.class);

    @Autowired
    private UserCatalogueRepository userCatalogueRepository;

    private final UserCatalogueMapper userCatalogueMapper;

    public UserCatalogueService(UserCatalogueMapper userCatalogueMapper) {
        this.userCatalogueMapper = userCatalogueMapper;
    }

    @Override
    public List<UserCatalogue> getAll(Map<String, String[]> parameters) {

        String sortParam = parameters.containsKey("sort") ? parameters.get("sort")[0] : null;
        Sort sort = createSort(sortParam);

        String keyword = FilterParameter.filterKeyword(parameters);
        Map<String, String> filterSimple = FilterParameter.filterSimple(parameters);
        Map<String, Map<String, String>> filterComplex = FilterParameter.filterComplex(parameters);

        Specification<UserCatalogue> specs = Specification.where(
            BaseSpecification.<UserCatalogue>keywordSpec(keyword, "name")
        )
        .and(BaseSpecification.<UserCatalogue>whereSpec(filterSimple))
        .and(BaseSpecification.<UserCatalogue>complexWhereSpec(filterComplex));

        return userCatalogueRepository.findAll(specs, sort);

    }

    @Override
    public Page<UserCatalogue> paginate(Map<String, String[]> parameters) {

        int page = parameters.containsKey("page") ? Integer.parseInt(parameters.get("page")[0]) : 1;
        int perpage = parameters.containsKey("perpage") ? Integer.parseInt(parameters.get("perpage")[0]) : 20;
        String sortParam = parameters.containsKey("sort") ? parameters.get("sort")[0] : null;
        Sort sort = createSort(sortParam);

        String keyword = FilterParameter.filterKeyword(parameters);
        Map<String, String> filterSimple = FilterParameter.filterSimple(parameters);
        Map<String, Map<String, String>> filterComplex = FilterParameter.filterComplex(parameters);
        // Map<String, String> dateRangeFilters = FilterParameter.filterDateRange(parameters);

        logger.info("keyword: " + keyword);
        logger.info("filterSimple: {}", filterSimple);
        logger.info("filterComplex: {}", filterComplex);
        // logger.info("dateRangeFilters: " + dateRangeFilters);

        Specification<UserCatalogue> specs = Specification.where(
            BaseSpecification.<UserCatalogue>keywordSpec(keyword, "name")
        )
        .and(BaseSpecification.<UserCatalogue>whereSpec(filterSimple))
        .and(BaseSpecification.<UserCatalogue>complexWhereSpec(filterComplex));

        Pageable pageable = PageRequest.of(page - 1, perpage, sort);

        return userCatalogueRepository.findAll(specs, pageable);

    }

    @Override
    @Transactional
    public UserCatalogue create(StoreRequest request) {

        try {

            UserCatalogue payload = userCatalogueMapper.toEntity(request);

            // UserCatalogue payload = UserCatalogue.builder()
            //     .name(request.getName())
            //     .publish(request.getPublish())
            //     .build();

            return userCatalogueRepository.save(payload);

        } catch (Exception e) {

            throw new RuntimeException("Transaction failed: " + e.getMessage());

        }

    }

    @Override
    @Transactional
    public UserCatalogue update(Long id, UpdateRequest request) {

        UserCatalogue userCatalogue = userCatalogueRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Khong tim thay nhom thanh vien"));

        userCatalogueMapper.updateEntityFromRequest(request, userCatalogue);

        // UserCatalogue payload = userCatalogue.toBuilder()
        //     .name(request.getName())
        //     .publish(request.getPublish())
        //     .build();

        return userCatalogueRepository.save(userCatalogue);
    }

}
