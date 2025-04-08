package com.example.tiendat.services;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tiendat.helpers.FilterParameter;
import com.example.tiendat.mappers.BaseMapper;
import com.example.tiendat.modules.users.entities.UserCatalogue;
import com.example.tiendat.specifications.BaseSpecification;

import jakarta.persistence.EntityNotFoundException;

@Service
public abstract class BaseService<T, M extends BaseMapper<T, ?, C, U>, C, U, R extends JpaRepository<T, Long> & JpaSpecificationExecutor<T>> {

    protected abstract String[] getSearchFields();
    protected abstract R getRepository();
    protected abstract M getMapper();

    public List<T> getAll(Map<String, String[]> parameters) {
        Sort sort = parseSort(parameters);
        Specification<T> specs = buildSpecification(parameters, getSearchFields());
        return getRepository().findAll(specs, sort);
    }

    public Page<T> paginate(Map<String, String[]> parameters) {
        int page = parameters.containsKey("page") ? Integer.parseInt(parameters.get("page")[0]) : 1;
        int perpage = parameters.containsKey("perpage") ? Integer.parseInt(parameters.get("perpage")[0]) : 20;
        Sort sort = parseSort(parameters);
        Specification<T> specs = buildSpecification(parameters, getSearchFields());
        Pageable pageable = PageRequest.of(page - 1, perpage, sort);
        return getRepository().findAll(specs, pageable);
    }

    @Transactional
    public T create(C request) {
        T payload = getMapper().toEntity(request);

            // UserCatalogue payload = UserCatalogue.builder()
            //     .name(request.getName())
            //     .publish(request.getPublish())
            //     .build();

        return getRepository().save(payload);
    }

    @Transactional
    public T update(Long id, U request) {
        T entity = getRepository().findById(id)
            .orElseThrow(() -> new EntityNotFoundException("BAN GHI KHONG TON TAI"));
        getMapper().updateEntityFromRequest(request, entity);

        // UserCatalogue payload = userCatalogue.toBuilder()
        //     .name(request.getName())
        //     .publish(request.getPublish())
        //     .build();

        return getRepository().save(entity);
    }

    @Transactional
    public Boolean deleteMultipleEntity(List<Long> Ids) {
        List<T> entity = getRepository().findAllById(Ids);
        if(entity.size() != Ids.size()) {
            throw new RuntimeException("SO LUONG BAN GHI CAN XOA KHONG KHOP");
        }
        getRepository().deleteAll(entity);
        return true;
    }

    @Transactional
    public Boolean delete(Long id) {
        getRepository().findById(id)
            .orElseThrow(() -> new EntityNotFoundException("BAN GHI KHONG TON TAI"));
        getRepository().deleteById(id);
        return true;
    }

    protected Sort parseSort(Map<String, String[]> parameters) {
        String sortParam = parameters.containsKey("sort") ? parameters.get("sort")[0] : null;
        return createSort(sortParam);
    }

    protected Specification<T> buildSpecification(Map<String, String[]> parameters, String[] searchFields) {
        String keyword = FilterParameter.filterKeyword(parameters);
        Map<String, String> filterSimple = FilterParameter.filterSimple(parameters);
        Map<String, Map<String, String>> filterComplex = FilterParameter.filterComplex(parameters);
        return Specification.where(BaseSpecification.<T>keywordSpec(keyword, searchFields))
            .and(BaseSpecification.<T>whereSpec(filterSimple))
            .and(BaseSpecification.<T>complexWhereSpec(filterComplex));
    }

    protected Sort createSort(String sortParam) {
        if(sortParam == null || sortParam.isEmpty()) {
            return Sort.by(Sort.Order.desc("id"));
        }
        String[] parts = sortParam.split(",");
        String field = parts[0];
        String sortDirection = (parts.length > 1) ? parts[1] : "asc";
        if("desc".equalsIgnoreCase(sortDirection)) {
            return Sort.by(Sort.Order.desc(field));
        } else {
            return Sort.by(Sort.Order.asc(field));
        }
    }
}
