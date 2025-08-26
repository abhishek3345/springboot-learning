package com.example.mycoolapp.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class FunRestController {
    //expose "/" that return "Hello Wrold"

    @GetMapping("/")
    public String sayHello(){
        return "Hello World!" ;
    }

    @GetMapping("/workout")
    public String workout(){
        return "Go ahead and hit 20 kg" ;
    }

    @GetMapping("/fortune")
    public String fortune(){
        return "You are Lucky Today!";
    }
}
