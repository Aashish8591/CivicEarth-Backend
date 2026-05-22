package com.civicearth.backend.controller;

import com.civicearth.backend.dto.LoginRequest;
import com.civicearth.backend.dto.RegisterRequest;
import com.civicearth.backend.model.User;
import com.civicearth.backend.service.AuthService;
import com.civicearth.backend.config.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    // 🔥 REGISTER
    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest req) {
        return authService.register(req);
    }

    // 🔐 LOGIN WITH JWT
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequest req) {

        // ✅ validate user
        User user = authService.login(req);

        // ✅ generate JWT token
        String token = jwtUtil.generateToken(user.getId(), user.getRole());

        // ✅ create user map (safe, allows null)
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("fullName", user.getFullName());
        userMap.put("email", user.getEmail());
        userMap.put("role", user.getRole());
        userMap.put("profilePic", user.getProfilePic());
        userMap.put("department", user.getDepartment());

        // ✅ create response map
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", user.getRole());
        response.put("user", userMap);

        return response;
    }
}