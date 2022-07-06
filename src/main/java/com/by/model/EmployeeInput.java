package com.by.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Component;

import javax.sound.midi.Soundbank;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeInput {

    Integer emp_id;
    String emp_name;
    String emp_city;
    String emp_phone;
    Double java_exp;
    Double spring_exp;
    @Transient
    String status;
}
