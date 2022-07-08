package com.by.service;

import com.by.model.EmployeeDTO;
import com.by.model.EmployeeSkill;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IEmployeeService {

    public Mono<EmployeeDTO> createEmployee(EmployeeDTO employee);
    public Flux<EmployeeDTO> getEmployees(EmployeeSkill employeeSkill);
}
