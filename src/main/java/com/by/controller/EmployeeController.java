package com.by.controller;

import com.by.model.EmployeeDTO;
import com.by.model.EmployeeSkill;
import com.by.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class EmployeeController {

    @Autowired
    IEmployeeService employeeService;
    @PostMapping("/createEmployee")
    public ResponseEntity<Mono<EmployeeDTO>> createEmployee(@RequestBody EmployeeDTO employeeDTO){
        Mono<EmployeeDTO> response = employeeService.createEmployee(employeeDTO).log();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/findEmpSkillset")
    public ResponseEntity<Flux<EmployeeDTO>> getEmployees(@RequestBody EmployeeSkill employeeSkill){

        return ResponseEntity.ok(employeeService.getEmployees(employeeSkill));
    }

}
