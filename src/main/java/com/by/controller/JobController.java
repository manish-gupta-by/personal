package com.by.controller;

import com.by.model.EmployeeDTO;
import com.by.model.Job;
import com.by.model.JobDTO;
import com.by.service.IJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class JobController {

    @Autowired
    IJobService jobService;

    @PostMapping("/createJobProfile")
    public ResponseEntity<Mono<JobDTO>> createJobProfile(@RequestBody JobDTO jobDto){
        return ResponseEntity.ok(jobService.createJobProfile(jobDto));
    }

    @GetMapping("/findEmpForJobID")
    public ResponseEntity<Flux<EmployeeDTO>> findEmpForJobID (@RequestBody Map<String, Integer> req ){
        int jobId = req.get("job_id");
        return ResponseEntity.ok(jobService.getEmployees(jobId));
    }
}
