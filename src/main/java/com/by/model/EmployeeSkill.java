package com.by.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("emp_skill")
@Data
@AllArgsConstructor
public class EmployeeSkill {

    @PrimaryKeyColumn(
            name = "emp_id",
            ordinal = 0,
            type = PrimaryKeyType.PARTITIONED
    )
    @Id
    private Integer emp_id;

    @Column("java_exp")
    Double javaExp;

    @Column("spring_exp")
    Double springExp;

}
