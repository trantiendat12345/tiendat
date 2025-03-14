package com.example.tiendat.modules.users.services.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tiendat.modules.users.services.interfaces.UserCatalogueServiceInterface;
import com.example.tiendat.services.BaseService;

import jakarta.persistence.EntityNotFoundException;

import com.example.tiendat.modules.users.entities.UserCatalogue;
import com.example.tiendat.modules.users.requests.UserCatalogue.StoreRequest;
import com.example.tiendat.modules.users.requests.UserCatalogue.UpdateRequest;
import com.example.tiendat.modules.users.repositories.UserCatalogueRepository;

@Service
public class UserCatalogueService extends BaseService implements UserCatalogueServiceInterface {

    @Autowired
    private UserCatalogueRepository userCatalogueRepository;

    @Override
    public Page<UserCatalogue> paginate(Map<String, String[]> parameters) {

        int page = parameters.containsKey("page") ? Integer.parseInt(parameters.get("page")[0]) : 1;
        int perpage = parameters.containsKey("perpage") ? Integer.parseInt(parameters.get("perpage")[0]) : 20;
        String sortParam = parameters.containsKey("sort") ? parameters.get("sort")[0] : null;
        Sort sort = createSort(sortParam);

        Pageable pageable = PageRequest.of(page - 1, perpage, sort);

        return userCatalogueRepository.findAll(pageable);

    }

    @Override
    @Transactional
    public UserCatalogue create(StoreRequest request) {

        try {

            UserCatalogue payload = UserCatalogue.builder()
                .name(request.getName())
                .publish(request.getPublish())
                .build();

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

        UserCatalogue payload = userCatalogue.toBuilder()
            .name(request.getName())
            .publish(request.getPublish())
            .build();

        return userCatalogueRepository.save(payload);
    }
    
}
