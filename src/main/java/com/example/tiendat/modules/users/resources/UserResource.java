package com.example.tiendat.modules.users.resources;

public class UserResource {

    private final Long id;
    private final String email;
    private final String name;

    public UserResource(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
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
    
}
