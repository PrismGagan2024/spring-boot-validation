package com.example.demo2.controller;

import com.example.demo2.dto.RegisterUserDTO;
import com.example.demo2.dto.ResponseDTO;
import com.example.demo2.entity.User;
import com.example.demo2.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO<?>> register(@RequestBody @Valid RegisterUserDTO registerUserDTO, BindingResult result) {
        if (result.hasErrors()) {
            // Create a map to store the field errors
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

            // Wrap errors in a ResponseDTO with an appropriate status and message
            ResponseDTO<Map<String, String>> errorResponse = new ResponseDTO<>();
            errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            errorResponse.setMessage("Validation Failed");
            errorResponse.setData(errors);
            errorResponse.setToken(null);

            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        ResponseDTO<User> response = userService.register(registerUserDTO);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
