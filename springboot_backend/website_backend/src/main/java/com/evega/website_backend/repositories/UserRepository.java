package com.evega.website_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.evega.website_backend.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}