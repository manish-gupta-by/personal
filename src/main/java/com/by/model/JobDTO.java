package com.by.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDTO implements Serializable {

    Integer job_id;

    String job_name;

    Double java_exp;

    Double spring_exp;

    String status;

    public JobDTO(Job jobDb, String status) {
        this.job_id = jobDb.getJobId();
        this.job_name = jobDb.getJobName();
        this.java_exp = jobDb.getJavaExp();
        this.spring_exp = jobDb.getSpringExp();
        this.status = status;
    }
}
