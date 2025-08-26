package com.example.cruddemo.dao;

import com.example.cruddemo.entity.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class StudentDAOImpl implements StudentDAO{
    //define field for entity manager
    private EntityManager entityManager;

    //inject entity manager using constructor injection
    @Autowired
    public StudentDAOImpl(EntityManager entityManager ){
        this.entityManager = entityManager;
    }
    //implement the save method
    @Override
    @Transactional
    public void save(Student theStudent) {
        entityManager.persist(theStudent);
    }

    @Override
    public Student findById(Integer id) {
        return entityManager.find(Student.class, id);

    }

    @Override
    public List<Student> findAll() {
        //create the query
        TypedQuery<Student> theQuery = entityManager.createQuery("FROM Student", Student.class);

        //return query results
        return theQuery.getResultList();
    }

    @Override
    public List<Student> findByLastName(String theLastName) {
        //create the query
        TypedQuery<Student> theQuery = entityManager.createQuery("FROM Student WHERE lastname=:theData", Student.class);

        //set the query parameters
        theQuery.setParameter("theData", theLastName);



        //return the query results
        return theQuery.getResultList();
    }

    @Override
    @Transactional
    public void update(Student theStudent) {
        //update the student
        entityManager.merge(theStudent);

    }

    @Override
    @Transactional
    public int updateLastName(String theLastName) {
        //return the num of rows updates

        Query theQuery = entityManager.createQuery("UPDATE Student SET lastname=:theData WHERE firstname='Babu'");
        theQuery.setParameter("theData", theLastName);
        return theQuery.executeUpdate();
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        //retrieve the student by id
        Student theStudent = entityManager.find(Student.class, id);

        //delete the student
        entityManager.remove(theStudent);
    }

    @Override
    @Transactional
    public int deleteAll() {
        int numRowsDeleted = entityManager.createQuery("DELETE FROM Student").executeUpdate();
        return numRowsDeleted;
    }
}
