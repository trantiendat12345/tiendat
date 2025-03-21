package com.example.tiendat.modules.users.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

import com.example.tiendat.modules.users.entities.UserCatalogue;
import com.example.tiendat.modules.users.requests.UserCatalogue.StoreRequest;
import com.example.tiendat.modules.users.requests.UserCatalogue.UpdateRequest;
import com.example.tiendat.resources.ApiResource;
import com.example.tiendat.modules.users.services.interfaces.UserCatalogueServiceInterface;
import com.example.tiendat.modules.users.resources.UserCatalogueResource;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Validated
@RestController
@RequestMapping("/api/v1")
public class UserCatalogueController { // quan li danh sach nguoi dung

    private static final Logger logger = LoggerFactory.getLogger(UserCatalogueController.class);

    private final UserCatalogueServiceInterface userCatalogueService;

    public UserCatalogueController(UserCatalogueServiceInterface userCatalogueService) {
        this.userCatalogueService = userCatalogueService;
    }

    @GetMapping("user_catalogues/all")
    public ResponseEntity<?> list(HttpServletRequest request) {

        Map<String, String[]> parameters = request.getParameterMap();
        List<UserCatalogue> userCatalogues = userCatalogueService.getAll(parameters);

        List<UserCatalogueResource> userCatalogueResource = userCatalogues.stream().map(userCatalogue -> UserCatalogueResource.builder()
            .id(userCatalogue.getId())
            .name(userCatalogue.getName())
            .publish(userCatalogue.getPublish())
            .build()).collect(Collectors.toList());
        
        ApiResource<List<UserCatalogueResource>> response = ApiResource.ok(userCatalogueResource, "SUCESS");

        logger.info("method list running ...");

        return ResponseEntity.ok(response);
    }
    

    @GetMapping("user_catalogues")
    public ResponseEntity<?> pagination(HttpServletRequest request) {

        Map<String, String[]> parameters = request.getParameterMap();

        Page<UserCatalogue> userCatalogues = userCatalogueService.paginate(parameters);

        Page<UserCatalogueResource> userCataloguesResource = userCatalogues.map(userCatalogue -> UserCatalogueResource.builder()
            .id(userCatalogue.getId())
            .name(userCatalogue.getName())
            .publish(userCatalogue.getPublish())
            .build());

        ApiResource<Page<UserCatalogueResource>> response = ApiResource.ok(userCataloguesResource, "SUCESS");

        logger.info("method getUserCatalogues running ...");

        return ResponseEntity.ok(response);
    }
    

    @PostMapping("user_catalogues")
    public ResponseEntity<?> store(@Valid @RequestBody StoreRequest request) {
        
        UserCatalogue userCatalogue = userCatalogueService.create(request);

        UserCatalogueResource userCatalogueResource = UserCatalogueResource.builder()
            .id(userCatalogue.getId())
            .name(userCatalogue.getName())
            .publish(userCatalogue.getPublish())
            .build();

        ApiResource<UserCatalogueResource> response = ApiResource.ok(userCatalogueResource, "SUCESS");

        logger.info("method store running ...");

        return ResponseEntity.ok(response);
    }

    @PutMapping("user_catalogues/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UpdateRequest request) {

        logger.info("method update running ...");

        try {
            UserCatalogue userCatalogue = userCatalogueService.update(id, request);
            UserCatalogueResource userCatalogueResource = UserCatalogueResource.builder()
                .id(userCatalogue.getId())
                .name(userCatalogue.getName())
                .publish(userCatalogue.getPublish())
                .build();

            ApiResource<UserCatalogueResource> response = ApiResource.ok(userCatalogueResource, "SUCESS");

            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResource.error("NOT FOUND", e.getMessage(), HttpStatus.BAD_REQUEST));

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResource.error("INTERNAL_SERVER_ERROR", "CO LOI XAY RA", HttpStatus.INTERNAL_SERVER_ERROR));

        }

    }
    
    
}
