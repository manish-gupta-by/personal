package com.by.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeDTO {

    Integer emp_id;
    String emp_name;
    String emp_city;
    String emp_phone;
    Double java_exp;
    Double spring_exp;

    public EmployeeDTO(Employee employee, EmployeeSkill employeeSkill) {
        this.emp_id = employee.getEmp_id();
        this.emp_name = employee.getName();
        this.emp_city = employee.getCity();
        this.emp_phone = employee.getPhone();
        this.java_exp = employeeSkill.getJavaExp();
        this.spring_exp = employeeSkill.getSpringExp();
    }

    @Transient
    String status;
}
