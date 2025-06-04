package com.evega.website_backend.services;

import com.evega.website_backend.models.User;
import com.evega.website_backend.models.UserToken;
import com.evega.website_backend.repositories.UserRepository;
import com.evega.website_backend.repositories.UserTokenRepository;
import com.evega.website_backend.security.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService; // Inject JwtService

    @Autowired
    public AuthService(UserRepository userRepository, UserTokenRepository userTokenRepository, BCryptPasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.userTokenRepository = userTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public boolean registerUser(String username, String password, String email) {
        if (userRepository.findByUsername(username) != null) {
            return false;
        }
        String hashedPassword = passwordEncoder.encode(password);
        User newUser = new User(username, hashedPassword, email);
        userRepository.save(newUser);

        // Generate and save JWT
        String token = jwtService.createToken(newUser.getUsername()); // Use JwtService
        UserToken userToken = new UserToken(token, newUser,  "initial_device_id");
        userTokenRepository.save(userToken);
        return true;
    }

    @Transactional
    public User authenticateUser(String username, String password, String deviceId) {
        User user = userRepository.findByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return null; // Authentication failed
        }

        // 1. Find the existing non-invalidated token for this user and device (if any)
        Optional<UserToken> existingTokenOptional = userTokenRepository.findByUserIdAndDeviceIdAndInvalidatedAtIsNull(user.getId(), deviceId);

        // 2. Invalidate the existing token if it exists and hasn't been invalidated yet
        existingTokenOptional.ifPresent(existingToken -> {
            if (existingToken.getInvalidatedAt() == null) {
                existingToken.setInvalidatedAt(LocalDateTime.now());
                userTokenRepository.save(existingToken);
            }
            // If invalidatedAt is not null, we do nothing to it.
        });

        // 3. Generate a new JWT
        String newToken = jwtService.createToken(user.getUsername());

        // 4. Save the new token
        UserToken newUserToken = new UserToken(newToken, user, deviceId);
        userTokenRepository.save(newUserToken);

        return user;
    }
    
    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}