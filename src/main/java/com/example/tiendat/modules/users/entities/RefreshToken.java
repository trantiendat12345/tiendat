package com.example.tiendat.modules.users.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;

import java.time.LocalDateTime; // thoi gian hien tai

import lombok.Data; // tu dong tao getter va setter
import lombok.NoArgsConstructor; // tu dong tao ham khoi tao khong tham so
import lombok.AllArgsConstructor; // tu dong tao ham khoi tao co tham so


@NoArgsConstructor // tao ham khoi tao khong tham so
@AllArgsConstructor // tao ham khoi tao co tham so
@Data
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="refresh_token", nullable = false, unique = true)
    private String refreshToken;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public void setToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return refreshToken;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    public void setUser(User user) {
        this.user = user;
    }
    
    public User getUser() {
        return user;
    }

}
