package com.evega.website_backend.controllers;

import com.evega.website_backend.models.UserProfile;
import com.evega.website_backend.services.UserProfileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile") //  Base path for profile-related endpoints
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable Long userId) {
        UserProfile userProfile = userProfileService.getUserProfile(userId);
        if (userProfile != null) {
            return ResponseEntity.ok(userProfile);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
