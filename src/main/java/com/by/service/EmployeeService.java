package com.by.service;

import com.by.kafka.EmployeeKafkaProducer;
import com.by.model.Employee;
import com.by.model.EmployeeDTO;
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
    public Mono<EmployeeDTO> createEmployee(EmployeeDTO employeeDTO) {
        Mono<EmployeeDTO> response = employeeRepository.findByEmpId(employeeDTO.getEmp_id())
                .flatMap(emp -> {
                    employeeDTO.setStatus(Status.EXISTS.toString());
                    return Mono.just(employeeDTO);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    employeeDTO.setStatus(Status.CREATED.toString());
                    return employeeRepository
                            .save(new Employee(employeeDTO.getEmp_id(),
                                    employeeDTO.getEmp_name(),
                                    employeeDTO.getEmp_city(),
                                    employeeDTO.getEmp_phone()))
                            .then(employeeSkillRepository.save(
                                    new EmployeeSkill(employeeDTO.getEmp_id()
                                            , employeeDTO.getJava_exp()
                                            , employeeDTO.getSpring_exp())))
                            .thenReturn(employeeDTO).log();
                }))
                .doOnError(e -> log.error("Error while fetching employee records {}",e.getMessage()));

        response
                .subscribeOn(Schedulers.newBoundedElastic(3, 10, "producer-" + appTopic))
                .subscribe(employeeInput1 -> {
                    try {
                        employeeKafkaProducer.produce(employeeInput1.getEmp_id(), objectMapper.writeValueAsString(employeeInput1), appTopic);
                    } catch (JsonProcessingException e) {
                        log.error("error while converting json to employeeInput object {}", e.getMessage());
                    }
                });
        return response;

    }

    @Override
    public Flux<EmployeeDTO> getEmployees(EmployeeSkill employeeSkill) {

        Flux<EmployeeSkill> employeeSkills = employeeSkillRepository.findAll().log();
        if(Objects.nonNull(employeeSkill.getJavaExp())){
            employeeSkills = employeeSkills.filter(e -> {
                return e.getJavaExp().equals(employeeSkill.getJavaExp());
            });
        }
        if(Objects.nonNull(employeeSkill.getSpringExp())){
            employeeSkills = employeeSkills.filter(e -> e.getSpringExp().equals(employeeSkill.getSpringExp()));
        }
        employeeSkills = employeeSkills.sort((e1, e2) -> e1.getEmp_id() - e2.getEmp_id());


        Flux<Employee> employeesWithSkills = employeeSkills
                .map(employeeSkill1 -> employeeSkill1.getEmp_id())
                .collectList()
                .flatMapMany(list -> employeeRepository.findAllByEmpIds(list))
                .sort((e1, e2) -> e1.getEmp_id() - e2.getEmp_id());
        return employeesWithSkills.zipWith(employeeSkills)
                .flatMap(tuple -> Flux.just(new EmployeeDTO(tuple.getT1(), tuple.getT2()))).log();
    }


}
