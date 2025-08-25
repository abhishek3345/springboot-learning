package com.example.cruddemo;

import com.example.cruddemo.dao.StudentDAO;
import com.example.cruddemo.entity.Student;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CruddemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CruddemoApplication.class, args);
	}

    @Bean
    public CommandLineRunner commandLineRunner(StudentDAO studentDAO){
        return runner -> {
           // createStudent(studentDAO);

           // createMultimpleStudents(studentDAO);
            readStudent(studentDAO);
        };
    }

    private void readStudent(StudentDAO studentDAO) {
        //create a student object
        Student tmpStudent = new Student("Tappu", "Gada","tapu@gm.com");

        //save the student object
        studentDAO.save(tmpStudent);

        //get the id of saved student
        int theId = tmpStudent.getId();

        //Get the student by the id
        Student myStudent = studentDAO.findById(theId);

        //Display the retrived student
        System.out.println("Found the student: " + myStudent);
    }

    private void createMultimpleStudents(StudentDAO studentDAO) {
        //create multiple students
        Student tempstudent1 = new Student("Rohan", "Wengdu","rweng@gm.com");
        Student tempstudent2 = new Student("Jetha", "Gada","jetha@gm.com");
        Student tempstudent3 = new Student("Popat", "Patra","popat@gm.com");

        //save the student objects
        studentDAO.save(tempstudent1);
        studentDAO.save(tempstudent2);
        studentDAO.save(tempstudent3);
    }

    private void createStudent(StudentDAO studentDAO) {
        //create the student obj
        Student tempstudent = new Student("Rohan", "Wengdu","rweng@gm.com");

        //save the student obj
        studentDAO.save(tempstudent);

        //display id of the saved student
        System.out.println("Saved student. Generated id: " + tempstudent.getId());
    }
}
