package com.example.cruddemo.rest;

import com.example.cruddemo.dao.EmployeeDAO;
import com.example.cruddemo.entity.Employee;
import com.example.cruddemo.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.management.ObjectName;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeRestController {

    private EmployeeService employeeService;
    private ObjectMapper objectMapper ;

    //quick and dirty: inject employee dao
    @Autowired
    public EmployeeRestController(EmployeeService theEmployeeService, ObjectMapper theObjectMapper){
        employeeService = theEmployeeService;
        objectMapper = theObjectMapper;
    }

    //expose "/employees" and return a list of employees
    @GetMapping("/employees")
    public List<Employee> findAll(){
        return employeeService.findAll();
    }

    //add mapping for GET /employee/{employeeId}

    @GetMapping("/employees/{employeeId}")
    public Employee getEmployee(@PathVariable int employeeId){
        Employee theEmployee = employeeService.findById(employeeId);
        if(theEmployee == null){
            throw new RuntimeException("Emplyee id not found -" + employeeId);
        }
        return theEmployee;
    }

    // add mapping for POST /employees -add new employee
    @PostMapping("/employees")
    public Employee addEmployee(@RequestBody Employee theEmployee){
        //also Just in case they pass an id in JSON -> set id to 0
        //this is to force a save of new item... instead of update
        theEmployee.setId(0);
        Employee dbEmployee = employeeService.save(theEmployee);

        return dbEmployee;
    }
    // add mapping for PUT /employees - update the employee
    @PutMapping("/employees")
    public Employee updateEmployee(@RequestBody Employee theEmployee){
        Employee dbEmployee = employeeService.save(theEmployee);
        return dbEmployee;
    }

    // add mapping for Patch /employees/{employeeId} - patch employee -> partial update
    @PatchMapping("/employees/{employeeId}")
    public Employee patchEmploye(@PathVariable int employeeId, @RequestBody HashMap<String, Object> patchPayload){
        Employee tempEmployee = employeeService.findById(employeeId);

        if(tempEmployee == null){
            throw new RuntimeException("Employee not found- " + employeeId );
        }

        if(patchPayload.containsKey("id")){
            throw new RuntimeException("Employee id not allowed in request body- " + employeeId);
        }

        Employee patchEmployee = apply(patchPayload,tempEmployee);

        Employee dbEmployee = employeeService.save(patchEmployee);

        return dbEmployee;

    }

    private Employee apply(HashMap<String, Object> patchPayload, Employee tempEmployee) {
        //convert emp object to JSON object Node
        ObjectNode employeeNode = objectMapper.convertValue(tempEmployee, ObjectNode.class);

        //convert patch payload map to JSON object node
        ObjectNode patchNode = objectMapper.convertValue(patchPayload,ObjectNode.class);

        // Merge the patch updates into the employee node
        employeeNode.setAll(patchNode);
        return objectMapper.convertValue(employeeNode,Employee.class);
    }

    @DeleteMapping("/employees/{employeeId}")
    private String deleteEmployee(@PathVariable int employeeId){
        Employee tempEmployee = employeeService.findById(employeeId);

        if(tempEmployee == null) {
            throw new RuntimeException("Employee id is not found - " + employeeId);
        }


        employeeService.deleteById(employeeId);

        return "Deleted employee id - " + employeeId ;
    }



}
