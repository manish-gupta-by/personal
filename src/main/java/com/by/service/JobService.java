package com.by.service;

import com.by.exceptions.JobIdNotFoundException;
import com.by.exceptions.NoEmployeeWithSkillFound;
import com.by.model.*;
import com.by.repository.JobRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.metrics.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class JobService implements IJobService{
    @Autowired
    JobRepository jobRepository;

    @Autowired
    WebClient webClient;

    @Override
    public Mono<JobDTO> createJobProfile(JobDTO jobDto) {
        return jobRepository.findByJobId(jobDto.getJob_id())
                .map(jobDb -> new JobDTO(jobDb, Status.EXISTS))
                .switchIfEmpty(jobRepository.save(new Job(jobDto)).map(jobDb -> new JobDTO(jobDb, Status.CREATED)))
                .doOnError(error -> log.error("Error while saving job profile {}", error.getMessage()));
        }

    @Override
    public Flux<EmployeeDTO> getEmployees(int jobId) {
        return jobRepository.findByJobId(jobId)
                .switchIfEmpty(Mono.error(new JobIdNotFoundException("No job exists")))
                .map(job -> new EmployeeSkill(job))
                .flatMapMany(employeeSkill ->
                        webClient
                        .method(HttpMethod.GET)
                        .uri("/findEmpSkillset")
                        .body(Mono.just(employeeSkill), EmployeeSkill.class)
                        .retrieve()
                        .bodyToFlux(EmployeeDTO.class))
                .switchIfEmpty(Mono.error(new NoEmployeeWithSkillFound("No employee with skills found")));
    }

}
