package com.example.tiendat.modules.users.resources;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;
// import lombok.RequiredArgsConstructor;

@Data
@Builder
// @RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResource {

    private final Long id;
    private final String email;
    private final String name;
    private final String phone;

    public UserResource(Long id, String email, String name, String phone) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
    
}
