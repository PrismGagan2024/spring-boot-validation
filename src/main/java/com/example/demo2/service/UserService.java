package com.example.demo2.service;

import com.example.demo2.dto.LoginUserDTO;
import com.example.demo2.dto.RegisterUserDTO;
import com.example.demo2.dto.ResponseDTO;
import com.example.demo2.entity.User;
import com.example.demo2.repository.UserRepository;
import com.example.demo2.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public ResponseDTO<User> register(RegisterUserDTO registerUserDTO) {
        Optional<User> userOpt = userRepository.findByUsername(registerUserDTO.getUsername());

        if (userOpt.isPresent()) {
            return new ResponseDTO<>(404, "User already present", null, null);
        }

        User newUser = new User();
        newUser.setName(registerUserDTO.getName());
        newUser.setEmail(registerUserDTO.getEmail());
        newUser.setUsername(registerUserDTO.getUsername());

        String hashedPassword = passwordEncoder.encode(registerUserDTO.getPassword());
        newUser.setPassword(hashedPassword);

        userRepository.save(newUser);

        return new ResponseDTO<>(201, "User registered successfully", null, null);
    }

    public ResponseDTO<User> login(LoginUserDTO loginUserDTO) {
        Optional<User> userOpt = userRepository.findByUsername(loginUserDTO.getUsername());

        if (!userOpt.isPresent()) {
            return new ResponseDTO<>(400, "User not present", null, null);
        }

        User user = userOpt.get();

        boolean checkPassword = passwordEncoder.matches(loginUserDTO.getPassword(), user.getPassword());

        if (!checkPassword) {
            return new ResponseDTO<>(400, "Incorrect username or password", null, null);
        }

        // Generate JWT token
        String jwtToken = jwtUtil.generateToken(loginUserDTO.getUsername());

        return new ResponseDTO<>(200, "User login successfully", user, jwtToken);
    }

    public ResponseDTO<User> getUserById(String userId) {
        Optional<User> user = userRepository.findById(userId);

        if(!user.isPresent()) {
            return new ResponseDTO<>(404, "User not present", null, null);
        }

        return new ResponseDTO<>(200, "User retrieve successfully", user.get(), null);
    }
}