package com.example.cruddemo.dao;

import com.example.cruddemo.entity.Student;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class StudentDAOImpl implements StudentDAO{

    //define the field for entity Manager
    private EntityManager entityManager ;

    //defind entity manager using constructor injection
    @Autowired
    public StudentDAOImpl(EntityManager entityManager){
        this.entityManager = entityManager;

    }


    //create
    @Override
    @Transactional
    public void save(Student student) {
        entityManager.persist(student);
    }

    //read
    @Override
    public Student findById(Integer id) {
        return entityManager.find(Student.class, id);

    }
}
