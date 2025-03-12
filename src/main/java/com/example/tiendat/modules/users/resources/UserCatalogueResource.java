package com.example.tiendat.modules.users.resources;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;
// import lombok.RequiredArgsConstructor;

@Data
@Builder
// @RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCatalogueResource {

    private final Long id;
    private final String name;
    private final Integer publish;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public Integer getPublish() {
        return publish;
    }
    
}
