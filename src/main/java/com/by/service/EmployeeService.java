package com.by.service;

import com.by.kafka.EmployeeKafkaProducer;
import com.by.model.Employee;
import com.by.model.EmployeeInput;
import com.by.model.EmployeeSkill;
import com.by.model.Status;
import com.by.repository.EmployeeRepository;
import com.by.repository.EmployeeSkillRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Objects;

@Service
@Slf4j
public class EmployeeService implements IEmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    EmployeeSkillRepository employeeSkillRepository;

    @Autowired
    EmployeeKafkaProducer employeeKafkaProducer;

    @Value("${kafka.employee.topic}")
    String appTopic;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public Mono<EmployeeInput> createEmployee(EmployeeInput employeeInput) {
        Mono<EmployeeInput> response = employeeRepository.findByEmpId(employeeInput.getEmp_id())
                .flatMap(emp -> {
                    employeeInput.setStatus(Status.EXISTS.toString());
                    return Mono.just(employeeInput);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    employeeInput.setStatus(Status.CREATED.toString());
                    return employeeRepository
                            .save(new Employee(employeeInput.getEmp_id(),
                                    employeeInput.getEmp_name(),
                                    employeeInput.getEmp_city(),
                                    employeeInput.getEmp_phone()))
                            .then(employeeSkillRepository.save(
                                    new EmployeeSkill(employeeInput.getEmp_id()
                                            , employeeInput.getJava_exp()
                                            , employeeInput.getSpring_exp())))
                            .thenReturn(employeeInput).log();
                }))
                .doOnError(e -> log.error("Error while fetching employee records {}",e.getMessage()));

        response
                .subscribeOn(Schedulers.newBoundedElastic(3, 10, "producer-" + appTopic))
                .subscribe(employeeInput1 -> {
                    try {
                        employeeKafkaProducer.produce(String.valueOf(employeeInput1.getEmp_id()), objectMapper.writeValueAsString(employeeInput1), appTopic);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
        return response;

    }


}
