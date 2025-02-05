package com.example.tiendat.databases.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.example.tiendat.modules.users.entities.User;
import com.example.tiendat.modules.users.repositories.UserRepository;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseSeeder.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired //tu dong tao ra doi tuong
    private UserRepository userRepository;

    @Transactional
    @Override
    public void run(String... args) throws Exception {


        if (isTableEmpty()) {

            String password = passwordEncoder.encode("password");
            // entityManager.createNativeQuery("INSERT INTO users (name, email, password, user_catalogue_id, phone) VALUES (?, ?, ?, ?, ?)")
            //         .setParameter(1, "Tien Dat")
            //         .setParameter(2, "trantiendat@gmail.com")
            //         .setParameter(3, password)
            //         .setParameter(4, 1)
            //         .setParameter(5, "1203012301203")
            //         .executeUpdate();

            System.out.println("password: " + password);

            // User user = new User();
            // user.setName("Tien Dat");
            // user.setEmail("trantiendat@gmail.com");
            // user.setPassword(password);
            // user.setUserCatalogueId(1L);
            // user.setPhone("1203012301203");
            // entityManager.persist(user);

            // User user = new User();
            // user.setName("Tien Dat");
            // user.setEmail("trantiendat@gmail.com");
            // user.setPassword(password);
            // user.setUserCatalogueId(1L);
            // user.setPhone("1203012301203");
            // userRepository.save(user);

            User user = new User("Tien Dat", "trantiendat@gmail.com", password, 1L, "123434232342234");
            userRepository.save(user);
            LOGGER.info("Database seeded");

            User user2 = new User("Nhu Quynh", "nguyennhuquynh@gmail.com", password, 1L, "12342312312");
            userRepository.save(user2);
            LOGGER.info("Database seeded");
            
        } 

    }

    private boolean isTableEmpty() {
        Long count = (Long) entityManager.createQuery("SELECT COUNT(id) FROM User").getSingleResult();
        return count == 0;
    }
    
}
