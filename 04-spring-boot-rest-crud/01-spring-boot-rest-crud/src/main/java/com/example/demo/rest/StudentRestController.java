package com.example.demo.rest;

import com.example.demo.entity.Student;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentRestController {

    List<Student>theStudents;
    //define @PostConstruct to load the student data ... only once!
    @PostConstruct
    public void loadData(){
        theStudents = new ArrayList<>();

        theStudents.add(new Student("Jeth","Gada"));
        theStudents.add(new Student("Daya","Gad"));
        theStudents.add(new Student("Joe","Trib"));
    }

    //define the endpoints for "/student"
    @GetMapping("/students")
    public List<Student> getStudents(){
        return theStudents;
    }

    //define the endpoint for "/students/{studentId}" = return student at index
    @GetMapping("/students/{studentId}")
    public Student getStudent(@PathVariable int studentId){
        //studentId as index
        if(studentId >= theStudents.size() || studentId<0){
            throw new StudentNotFoundException("Student id not found - " + studentId);
        }
        return theStudents.get(studentId);
    }

    // Add an exception handler using @ExceptionHandler

}
