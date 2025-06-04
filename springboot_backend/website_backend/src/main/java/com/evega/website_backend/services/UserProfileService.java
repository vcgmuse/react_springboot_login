package com.evega.website_backend.services;

import com.evega.website_backend.dto.UserDTO;
//import com.evega.website_backend.dto.UserProfileDTO;
import com.evega.website_backend.models.User;
import com.evega.website_backend.models.UserProfile;
import com.evega.website_backend.repositories.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
//    private final UserProfileDTO userProfileDTO;

// UserProfileDTO needed in parameter insert
    @Autowired
    public UserProfileService(UserProfileRepository userProfileRepository ) {
        this.userProfileRepository = userProfileRepository;
//        this.userProfileDTO = userProfileDTO;
    }

    @Transactional
    public boolean createUserProfile(UserDTO userDTO) {
        try {
            //  Populate the profile.
            UserProfile userProfile = new UserProfile();
            
            userProfileRepository.save(userProfile);
            return true;
        } catch (Exception e) {
            e.printStackTrace(); // Log the error
            return false;
        }
    }

    public UserProfile getUserProfile(Long userId) {
        return userProfileRepository.findByUserId(userId);
    }
}