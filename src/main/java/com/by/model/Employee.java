package com.by.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Table("emp")
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @PrimaryKeyColumn(
            name = "emp_id",
            ordinal = 0,
            type = PrimaryKeyType.PARTITIONED
    )
    @Id
    @Column(value = "emp_id")
    private Integer emp_id;

    @Column(value = "emp_name")
    String name;

    @Column(value = "emp_city")
    String city;

    @Column(value = "emp_phone")
    String phone;

}
