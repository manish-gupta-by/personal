package com.by.controller;

import com.by.model.Employee;
import com.by.model.EmployeeInput;
import com.by.repository.EmployeeRepository;
import com.by.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/createEmployee")
public class EmployeeController {

    @Autowired
    IEmployeeService employeeService;
    @PostMapping
    public Mono<EmployeeInput> createEmployee(@RequestBody EmployeeInput employeeInput){
        Mono<EmployeeInput> response = employeeService.createEmployee(employeeInput).log();

        return response;
    }

}
