package com.example.cruddemo.dao;

import com.example.cruddemo.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;


// @RepositoryRestResource(path = "workers")
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    // no code needed...
}
