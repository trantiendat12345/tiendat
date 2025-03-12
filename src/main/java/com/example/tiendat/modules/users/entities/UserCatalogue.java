package com.example.tiendat.modules.users.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Column;
import java.time.LocalDateTime;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;

import lombok.Data; // tu dong tao getter va setter
import lombok.NoArgsConstructor; // tu dong tao ham khoi tao khong tham so
import lombok.AllArgsConstructor; // tu dong tao ham khoi tao co tham so
import lombok.Builder;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "user_catalogues")
public class UserCatalogue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "publish", nullable = false, columnDefinition = "TINYINT")
    private Integer publish;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist //goi den ham nay truoc khi insert du lieu vao database
    protected void onCreated() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate //goi den ham nay truoc khi update du lieu vao database
    protected void onUpdated() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
}
