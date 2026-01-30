package com.learning.demo.controller;

import com.learning.demo.model.User;
import com.learning.demo.repository.UserRepository;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    UserRepository userRepository;

    //constructor injection
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User existUser = user.get();
            return ResponseEntity.status(HttpStatus.OK).body(existUser);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User id not found");
        }
    }

    @PostMapping
    public User saveUser(@RequestBody User user) {
        userRepository.save(user);
        return user;
    }

    @GetMapping
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@RequestBody User user, @PathVariable int id) {
        Optional<User> opUser = userRepository.findById(id);
        if (opUser.isPresent()) {

            opUser.get().setName(user.getName());
            opUser.get().setEmail(user.getEmail());
            userRepository.save(opUser.get());
            return ResponseEntity.status(HttpStatus.OK).body(opUser.get());
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> pathUser(@PathVariable int id, @RequestBody String email){
        Optional<User> opUser = userRepository.findById(id);
        if(opUser.isPresent()){
            opUser.get().setEmail(email);
            userRepository.save(opUser.get());
            return ResponseEntity.ok(opUser.get());
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable int id) {
        Optional<User> opUser = userRepository.findById(id);
        if (opUser.isPresent()) {
            User existingUser = opUser.get();
            userRepository.delete(existingUser);
            return "User deleted";
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }

    }
}
