package com.civicearth.backend.dto;

public class RegisterRequest {

    public String fullName;
    public String email;
    public String password;
    public String location;

    // 🔥 ADD THESE
    public String role;
    public String department;
    public String adminCode;
}