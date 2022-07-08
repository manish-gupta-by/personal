package com.by.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDTO {

    Integer job_id;

    String job_name;

    Double java_exp;

    Double spring_exp;

    String status;

    public JobDTO(Job jobDb, Status status) {
        this.job_id = jobDb.getJobId();
        this.job_name = jobDb.getJobName();
        this.java_exp = jobDb.getJavaExp();
        this.spring_exp = jobDb.getSpringExp();
        this.status = status.toString();
    }
}
