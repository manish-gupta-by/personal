package com.by.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("job")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Job {

    @PrimaryKeyColumn(
            name = "jobId",
            ordinal = 0,
            type = PrimaryKeyType.PARTITIONED
    )
    Integer jobId;

    @Column("job_name")
    String jobName;

    @PrimaryKeyColumn(
            name = "java_exp",
            ordinal = 1,
            type = PrimaryKeyType.CLUSTERED
    )
    @Column("java_exp")
    Double javaExp;

    @PrimaryKeyColumn(
            name = "spring_exp",
            ordinal = 0,
            type = PrimaryKeyType.CLUSTERED
    )
    @Column("spring_exp")
    Double springExp;

    public Job(JobDTO jobDto) {
        this.jobId = jobDto.getJob_id();
        this.jobName = jobDto.getJob_name();
        this.javaExp = jobDto.getJava_exp();
        this.springExp = jobDto.getSpring_exp();
    }
}
