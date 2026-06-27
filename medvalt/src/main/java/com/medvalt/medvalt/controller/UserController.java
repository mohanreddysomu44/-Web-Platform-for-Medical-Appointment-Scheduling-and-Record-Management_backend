package com.medvalt.medvalt.controller;

import com.medvalt.medvalt.entity.User;
import com.medvalt.medvalt.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

    // Optional: clear all users for testing
    @DeleteMapping("/clear")
    public String clearUsers() {
        userRepository.deleteAll();
        return "All users deleted!";
    }
}