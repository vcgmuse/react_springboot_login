package com.evega.website_backend.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_tokens")
public class UserToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @Column(nullable = false)
    private String deviceId;

    @Column(nullable = true)
    private LocalDateTime invalidatedAt;

    // Constructors
    public UserToken() {
    }

    public UserToken(String token, User user, String deviceId) {
        this.token = token;
        this.user = user;
        this.createdAt = createdAt;
        this.deviceId = deviceId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public LocalDateTime getInvalidatedAt() {
        return invalidatedAt;
    }
    
    public void setInvalidatedAt(LocalDateTime localDateTime) {
        this.invalidatedAt = localDateTime;
    }
    
    @PrePersist
    protected void onCreate() {
    	this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

    }
    
    @PreUpdate
    protected void onUpdate() {
    	this.updatedAt = LocalDateTime.now();
    }
}