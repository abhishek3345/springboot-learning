package com.learning.demo.controller;

import com.learning.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/consumer")
public class ConsumerController {
        @Autowired
        UserRepository userRepository;

        public ConsumerController(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        @GetMapping("/profile")
        public String getProfile() {
            return "Consumer profile data";
        }

        @PutMapping("/profile")
        public String updateProfile() {
            return "Consumer profile updated";
        }
}

