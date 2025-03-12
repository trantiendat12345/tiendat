package com.example.tiendat.modules.users.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
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
