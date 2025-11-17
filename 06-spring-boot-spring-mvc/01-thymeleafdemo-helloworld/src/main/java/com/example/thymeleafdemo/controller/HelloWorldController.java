package com.example.thymeleafdemo.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloWorldController {
    //need a controller method to show initial HTML form
    @RequestMapping("/showForm")
    public String showForm(){
        return "helloworld-form";
    }

    //need a controller method to process the HTML form
    @RequestMapping("/processForm")
    public String processForm(){
        return "helloworld";
    }

    //need a controller method to read from data and
    //add data to the model

    @PostMapping("/processFormVersionThree")
    public String processFormVersionThree(@RequestParam("studentName") String theName, Model model){
        //with the annotation @RequestParam param from HTML form automatically read

        //convert data to all caps
       theName =  theName.toUpperCase();

        //create message
        String result = "Heyyy! Pal... " + theName ;

        //add message to model
        model.addAttribute("message",result);

        return "helloworld" ;
    }

    @RequestMapping("/processFormVersionTwo")
    public String letsShoutDude(HttpServletRequest request, Model model){
        //read request param from HTML form
        String theName = request.getParameter("studentName");

        //convert data to all caps
        theName =  theName.toUpperCase();

        //create message
        String result = "Heyyy! " + theName ;

        //add message to model
        model.addAttribute("message",result);

        return "helloworld" ;
    }
}
