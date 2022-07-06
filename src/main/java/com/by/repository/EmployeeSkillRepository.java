package com.by.repository;

import com.by.model.EmployeeSkill;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeSkillRepository extends ReactiveCassandraRepository<EmployeeSkill, Integer> {
}
