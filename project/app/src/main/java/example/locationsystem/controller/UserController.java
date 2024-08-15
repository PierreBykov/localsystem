package com.example.locationsystem.controller;

import com.example.locationsystem.model.User;
import com.example.locationsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestParam String name, @RequestParam String email) {
        User user = userService.registerUser(name, email);
        return ResponseEntity.ok(user);
    }
}
