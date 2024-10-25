package com.example.demo2.service;

import com.example.demo2.dto.RegisterUserDTO;
import com.example.demo2.dto.ResponseDTO;
import com.example.demo2.entity.User;
import com.example.demo2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public ResponseDTO<User> register(RegisterUserDTO registerUserDTO) {
        Optional<User> userOpt = userRepository.findByUsername(registerUserDTO.getUsername());
        
        if(userOpt.isPresent()) {
            return new ResponseDTO<>(404, "User already present", null, null);
        }

        User newUser = new User();
        newUser.setName(registerUserDTO.getName());
        newUser.setEmail(registerUserDTO.getEmail());
        newUser.setUsername(registerUserDTO.getUsername());
        newUser.setPassword(registerUserDTO.getPassword());

        userRepository.save(newUser);

        return new ResponseDTO<>(201, "User registered successfully", null, null);
    }
}
