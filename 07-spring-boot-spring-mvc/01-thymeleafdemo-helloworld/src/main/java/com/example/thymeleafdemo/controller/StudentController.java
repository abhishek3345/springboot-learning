package com.example.thymeleafdemo.controller;

import com.example.thymeleafdemo.model.Student;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class StudentController {

    @Value("${countries}")
    List<String>Country;

    @Value("${languages}")
    List<String>Languages;

    @Value("${os}")
    List<String>OS;

    @GetMapping("/showStudentForm")
    public String shwoFrom(Model theModel){
        //create a student object
        Student theStudent = new Student();

        //add student object to the model
        theModel.addAttribute("student",theStudent);
        //add countries list to the model from application.properties
        theModel.addAttribute("countries",Country);

        //add favLang list to the model from application.properties
        theModel.addAttribute("languages",Languages);

        //add favOS list to the model from application.properties
        theModel.addAttribute("os",OS);
        return "student-form" ;
    }

    @PostMapping("/processStudentForm")
    public String processForm(@ModelAttribute("student") Student theStudent){
        //log the input data on console
        System.out.println("theStudent: " + theStudent.getFirstName() + " " + theStudent.getLastName());
        return "student-confirmation";
    }
}
