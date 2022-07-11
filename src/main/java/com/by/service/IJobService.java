package com.by.service;

import com.by.model.EmployeeDTO;
import com.by.model.Job;
import com.by.model.JobDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IJobService {

    public Mono<JobDTO> createJobProfile(JobDTO job);

    Flux<EmployeeDTO> getEmployees(int jobId);

    Mono<JobDTO> getJobProfile(Integer job_id);
}
