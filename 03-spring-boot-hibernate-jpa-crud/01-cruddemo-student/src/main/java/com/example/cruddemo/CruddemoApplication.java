package com.example.cruddemo;

import com.example.cruddemo.dao.StudentDAO;
import com.example.cruddemo.entity.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class CruddemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CruddemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(StudentDAO studentDAO){
		return runner->{
			//createStudent(studentDAO);
			createMultipleStudents(studentDAO);
			//readStudent(studentDAO);
			//queryForStudents(studentDAO);
			//queryForStudentsByLastName(studentDAO);
			//updateStudent(studentDAO);
			//updateLastName(studentDAO);
			//deleteStudent(studentDAO);
			//deleteAllStudent(studentDAO);
		};
	}

	private void deleteAllStudent(StudentDAO studentDAO) {
		System.out.println("Deleting all student");
		int numRowsDelete = studentDAO.deleteAll();
		System.out.println("Rows deleted: "+numRowsDelete);
	}

	private void deleteStudent(StudentDAO studentDAO) {
		int studentId = 2;
		System.out.println("Deleting student id: " + studentId);
		studentDAO.delete(studentId);
	}

	private void updateLastName(StudentDAO studentDAO) {
		int numRowsUpdated = studentDAO.updateLastName("Buffy");
		System.out.println("Rows update: " + numRowsUpdated);
	}

	private void updateStudent(StudentDAO studentDAO) {
		//retrieve student by id
		int studentId = 1;
		Student myStudent = studentDAO.findById(studentId);

		//change the name of student
		myStudent.setFirstname("Babu");

		//update the student
		studentDAO.update(myStudent);

		//display the updated student
		System.out.println("Update Student: " + myStudent);

	}

	private void queryForStudentsByLastName(StudentDAO studentDAO) {
		//get the list of students
		List<Student> theStudents = studentDAO.findByLastName("Gada");

		//Display the list
		for(Student tempStudent : theStudents){
			System.out.println(tempStudent);
		}

	}

	private void queryForStudents(StudentDAO studentDAO) {
		//get the list of students
		List<Student>theStudents = studentDAO.findAll();

		//display the list
		for(Student tempStudent : theStudents ){
			System.out.println(tempStudent);
		}
	}

	private void readStudent(StudentDAO studentDAO) {
		//create a student object
		Student tempStudent = new Student("Jetha","Gada", "jetha@gm.com");

		//save the student
		studentDAO.save(tempStudent);

		//get the id of saved student
		int theId = tempStudent.getId();

		//retrieve the student by id
		Student myStudent = studentDAO.findById(theId);

		//display the retrieved student
		System.out.println("Found the student: " + myStudent);

	}

	private void createMultipleStudents(StudentDAO studentDAO) {
		//create multiple student objects
		Student tempStudent1 = new Student("Ramu", "Destro", "ramu@gm.com");
		Student tempStudent2 = new Student("Raju", "Oracle", "raju@gm.com");
		Student tempStudent3 = new Student("Sham", "Donald", "sham@gm.com");

		//save the student objects
		studentDAO.save(tempStudent1);
		studentDAO.save(tempStudent2);
		studentDAO.save(tempStudent3);
		System.out.println("Saved multiple students");

	}

	private void createStudent(StudentDAO studentDAO) {
		//make the object for student
		Student tempStudent = new Student("Ramu", "Destro", "ramu@gm.com");

		//save the object
		studentDAO.save(tempStudent);

		//display the id of saved object
		System.out.println("Saved student. Generated id: " + tempStudent.getId());
	}

}
