package com.example.tiendat.databases.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void run(String... args) throws Exception {

        if (isTableEmpty()) {

            String password = passwordEncoder.encode("password");
            entityManager.createNativeQuery("INSERT INTO users (name, email, password, user_catalogue_id, phone) VALUES (?, ?, ?, ?, ?)")
                    .setParameter(1, "Tien Dat")
                    .setParameter(2, "trantiendat@gmail.com")
                    .setParameter(3, password)
                    .setParameter(4, 1)
                    .setParameter(5, "1203012301203")
                    .executeUpdate();

            System.out.println("password: " + password);
        } 

    }

    private boolean isTableEmpty() {
        Long count = (Long) entityManager.createQuery("SELECT COUNT(id) FROM User").getSingleResult();
        return count == 0;
    }
    
}
