package com.example.thymeleafdemo.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    //need a controller method to readfrom data and
    //add data to the model

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
