package com.evega.website_backend.services;
import com.evega.website_backend.dto.UserDTO;
//import com.evega.website_backend.dto.UserProfileDTO;
import com.evega.website_backend.models.User;
import com.evega.website_backend.models.UserProfile;
import com.evega.website_backend.repositories.UserProfileRepository;
import com.evega.website_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserProfileRepository userProfileRepository) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
    }
//
//    public UserProfileDTO getUserProfile(Long id) {
//        Optional<UserProfile> userProfileOptional = userProfileRepository.findById(id);
//        if (userProfileOptional.isPresent()) {
//            UserProfile userProfile = userProfileOptional.get();
//            User user = userProfile.getUser();
//            UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getEmail()); // Create the DTO
//            return new UserProfileDTO(userProfile, userDTO); // You'll need to create this DTO
//        }
//        return null;
//    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByUserId(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElse(null);
    }
}