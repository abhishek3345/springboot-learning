package com.learning.demo.controller;

import com.learning.demo.model.User;
import com.learning.demo.repository.UserRepository;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.web.bind.annotation.*;

@RestController
public class DemoController {



    @GetMapping("/wel")
    public String welcome(){
        return "welcome to learning";
    }
    @GetMapping("/welcome/{rol}")
    public String welcomeStudent(@PathVariable int rol){
        return "welcome student " + rol ;
    }
    @PostMapping("/greet")
    public String greet(@RequestParam String city){
        return "welcome from " + city ;
    }

    @GetMapping("/user")
    public User user(){
        return new User(1,"Jetha", "jetha@gm.com");
    }
    @PostMapping("/user")
    public User user(@RequestParam User user){
        return user;
    }
}
