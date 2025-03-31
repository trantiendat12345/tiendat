package com.example.tiendat.modules.users.mappers;

import org.mapstruct.Mapper;

import com.example.tiendat.mappers.BaseMapper;
import com.example.tiendat.modules.users.entities.UserCatalogue;
import com.example.tiendat.modules.users.resources.UserCatalogueResource;
import com.example.tiendat.modules.users.requests.UserCatalogue.StoreRequest;
import com.example.tiendat.modules.users.requests.UserCatalogue.UpdateRequest;

@Mapper(componentModel = "spring")
public interface UserCatalogueMapper extends BaseMapper<UserCatalogue ,UserCatalogueResource, StoreRequest, UpdateRequest> {

    // UserCatalogueResource tResource(UserCatalogue entity);

    // List<UserCatalogueResource> tList(List<UserCatalogue> entities);

    // default Page<UserCatalogueResource> toResourcePage(Page<UserCatalogue> page) {
    //     return page.map(this::tResource);
    // }

}
