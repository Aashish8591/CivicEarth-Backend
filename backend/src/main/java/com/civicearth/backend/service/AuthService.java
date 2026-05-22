package com.civicearth.backend.service;

import com.civicearth.backend.dto.LoginRequest;
import com.civicearth.backend.dto.RegisterRequest;
import com.civicearth.backend.model.User;
import com.civicearth.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public User register(RegisterRequest req) {

        User user = new User();
        user.setFullName(req.fullName);
        user.setEmail(req.email);
        user.setPassword(passwordEncoder.encode(req.password));
        user.setRole(req.role != null ? req.role : "USER");

        if ("ADMIN".equals(req.role)) {
            if (!"ADMIN123".equals(req.adminCode)) {
                throw new RuntimeException("Invalid admin code");
            }
            user.setDepartment(req.department);
        }

        // 🔥 NEW ADDITIONS
        user.setLocation(req.location);
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    public User login(LoginRequest req) {



        User user = userRepository.findByEmail(req.email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(req.password, user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        return user;
    }
}