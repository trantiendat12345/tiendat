package com.example.tiendat.modules.users.services.interfaces;

import com.example.tiendat.modules.users.entities.UserCatalogue;
import com.example.tiendat.modules.users.requests.UserCatalogue.StoreRequest;
import com.example.tiendat.modules.users.requests.UserCatalogue.UpdateRequest;

public interface UserCatalogueServiceInterface {

    UserCatalogue create(StoreRequest request);
    UserCatalogue update(Long id, UpdateRequest request);
    
}
