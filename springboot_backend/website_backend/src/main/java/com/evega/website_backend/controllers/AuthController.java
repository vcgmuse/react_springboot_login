package com.evega.website_backend.controllers;

import com.evega.website_backend.models.User;
import com.evega.website_backend.security.JwtService;
import com.evega.website_backend.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }
    
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        System.out.println("From Auth Controller - Register: " + user.getEmail());

        boolean authRegistered = authService.registerUser(user.getUsername(), user.getPassword(), user.getEmail());
        Map<String, Object> response = new HashMap<>();
        
        System.out.println(authRegistered);

        if (authRegistered) {
            // After successful registration, authenticate the user and generate a token
            User registeredUser = authService.loadUserByUsername(user.getUsername()); // Assuming you have a method to load a user by username
            System.out.println(registeredUser);
            if (registeredUser != null) {
                String token = jwtService.createToken(registeredUser.getUsername());
                response.put("token", token);
                response.put("userId", registeredUser.getId()); // Assuming your User entity has an getId() method
                response.put("message", "User registered and logged in successfully");
                return ResponseEntity.ok(response);
            } else {
                // This should ideally not happen if registration was successful,
                // but handle it just in case.
                response.put("auth", "User registered, but failed to retrieve user details.");
                return ResponseEntity.internalServerError().body(response);
            }
        } else {
            response.put("auth", "Username already taken");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user, @RequestHeader("Device-Id") String deviceId) {
    	User authenticatedUser = authService.authenticateUser(user.getUsername(), user.getPassword(), deviceId);
        if (authenticatedUser != null) {
            String token = jwtService.createToken(authenticatedUser.getUsername());
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            // TODO Keep am eye get id when this get id should be
            response.put("userId", authenticatedUser.getId());
            response.put("message", "Login successful");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        jwtService.invalidateToken(jwtToken); // Assuming you have this method in JwtService
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logout successful");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-auth")
    public ResponseEntity<String> checkAuth(@RequestHeader("Authorization") String token,
                                             @RequestHeader("Device-Id") String deviceId) {
        String jwtToken = token.replace("Bearer ", "");
        String username = jwtService.extractUsername(jwtToken);
        if (jwtService.validateToken(jwtToken, username, deviceId)) {
            return ResponseEntity.ok("User is authenticated");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
    }
}
