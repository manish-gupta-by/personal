package com.by.service;

import com.by.model.Employee;
import com.by.model.EmployeeInput;
import reactor.core.publisher.Mono;

public interface IEmployeeService {

    public Mono<EmployeeInput> createEmployee(EmployeeInput employee);
}
