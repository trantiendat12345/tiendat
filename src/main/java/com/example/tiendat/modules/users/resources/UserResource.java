package com.example.tiendat.modules.users.resources;

public class UserResource {

    private final Long id;
    private final String email;

    public UserResource(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }
    
}
