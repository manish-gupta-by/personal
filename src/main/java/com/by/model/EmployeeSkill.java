package com.by.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("emp_skill")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeSkill {

    @PrimaryKeyColumn(
            name = "emp_id",
            ordinal = 0,
            type = PrimaryKeyType.PARTITIONED
    )
    private Integer emp_id;

    @PrimaryKeyColumn(
            name = "java_exp",
            ordinal = 1,
            type = PrimaryKeyType.CLUSTERED
    )
    @JsonProperty("java_exp")
    @Column("java_exp")
    private Double javaExp;

    @PrimaryKeyColumn(
            name = "spring_exp",
            ordinal = 2,
            type = PrimaryKeyType.CLUSTERED
    )
    @JsonProperty("spring_exp")
    @Column("spring_exp")
    private Double springExp;

    public EmployeeSkill(Job job) {
        this.javaExp = job.getJavaExp();
        this.springExp = job.getSpringExp();
    }
}
