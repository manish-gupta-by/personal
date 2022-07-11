package com.by.controller;

import com.by.model.EmployeeDTO;
import com.by.model.JobDTO;
import com.by.service.IJobService;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
public class JobController {

    @Autowired
    IJobService jobService;

    @PostMapping("/createJobProfile")
    public ResponseEntity<Mono<JobDTO>> createJobProfile(@RequestBody JobDTO jobDto){
        return ResponseEntity.ok(jobService.createJobProfile(jobDto));
    }

    @GetMapping("/getJobProfileFromCache")
    public ResponseEntity<Mono<JobDTO>> getJobProfileFromCache(@RequestBody HashMap<String, Integer> map){
        return ResponseEntity.ok(jobService.getJobProfile(map.get("job_id")));
    }

    @GetMapping("/findEmpForJobID")
    public ResponseEntity<Flux<EmployeeDTO>> findEmpForJobID (@RequestBody Map<String, Integer> req ){
        int jobId = req.get("job_id");
        return ResponseEntity.ok(jobService.getEmployees(jobId));
    }

}
