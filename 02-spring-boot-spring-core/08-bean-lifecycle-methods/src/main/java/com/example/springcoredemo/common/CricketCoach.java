package com.example.springcoredemo.common;
import ch.qos.logback.core.net.SyslogOutputStream;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class CricketCoach implements Coach{

    public CricketCoach(){
        System.out.println("In Constructor: " + getClass().getSimpleName());
    }

    //define our init method
    @PostConstruct
    public void doStartupStuff() {
        System.out.println("In doStartupStuff(): " + getClass().getSimpleName());
    }

    //defined out destroy method
    @PreDestroy
    public void doCleanupStuff(){
        System.out.println("In doCleanupStuff(): " + getClass().getSimpleName());
    }

    @Override
    public String getDailyWorkout() {
        return "Practice bowling for 15 mins!";
    }
}
