package com.example.tiendat.modules.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tiendat.modules.users.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
}
