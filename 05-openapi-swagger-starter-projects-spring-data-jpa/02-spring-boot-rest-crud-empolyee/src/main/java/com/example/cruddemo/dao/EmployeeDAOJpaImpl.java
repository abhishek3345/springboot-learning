package com.example.cruddemo.dao;

import com.example.cruddemo.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeDAOJpaImpl implements EmployeeDAO{

    //define the field for entityManager
    private EntityManager entityManager;

    //set up constructor injection
    @Autowired
    public EmployeeDAOJpaImpl(EntityManager theEntityManager){
        entityManager = theEntityManager;
    }
    @Override
    public List<Employee> findAll() {

        //create a query
        TypedQuery<Employee> theQuery = entityManager.createQuery("From Employee", Employee.class);

        //execute a query and get result list
        List<Employee> employees = theQuery.getResultList();

        //return the results
        return employees;
    }

    @Override
    public Employee findById(int theId) {
        //get employee
        Employee theEmployee = entityManager.find(Employee.class,theId);

        //return employee
        return theEmployee;
    }

    @Override
    public Employee save(Employee theEmployee) {
        //save employee
        Employee dbEmployee = entityManager.merge(theEmployee);

        //return the dbEmployee (have updated Id)
        return  dbEmployee;
    }

    @Override
     public void deleteById(int theId) {
        //get the employee by Id
        Employee theEmployee = entityManager.find(Employee.class, theId);

        //delete the found employee
        entityManager.remove(theEmployee);

    }
}
