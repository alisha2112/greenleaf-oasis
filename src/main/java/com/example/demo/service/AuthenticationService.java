package com.example.demo.service;


import com.example.demo.dto.category.AuthenticationResponse;
import com.example.demo.dto.category.RegisterRequest;

public interface AuthenticationService {
    public AuthenticationResponse register(RegisterRequest request);
}
