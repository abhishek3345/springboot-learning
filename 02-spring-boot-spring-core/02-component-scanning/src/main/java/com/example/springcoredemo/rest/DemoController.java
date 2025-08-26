package com.example.springcoredemo.rest;

import com.example.util.Coach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    //Define private field for the dependency
    private Coach myCoach;

    @Autowired
    public DemoController(Coach thecoach){
        myCoach = thecoach;
    }

    @GetMapping("/dailyworkout")
    public String getDailyWorkout(){
        return myCoach.getDailyWorkout();
    }
}
