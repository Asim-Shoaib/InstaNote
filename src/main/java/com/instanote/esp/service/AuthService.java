package com.instanote.esp.service;

import com.instanote.esp.repository.UserRepository;
import com.instanote.esp.requests.JwtResponse;
import com.instanote.esp.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.instanote.utils.JwtUtil;

import java.util.Optional;


@Service
public class AuthService {
    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String registerUser(User user){
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
           throw new RuntimeException("User already exists");
        }

        user.setPassword(user.getPassword());
        userRepository.save(user);

        return "User registered successfully!";
    }

    public JwtResponse loginUser(String username, String password) throws RuntimeException {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid email or password");
        }

        User user = userOpt.get();

        if (!password.equals(user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return new JwtResponse(JwtUtil.generateToken(user.getUsername()));  
    }

}