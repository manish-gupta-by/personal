package com.by.repository;

import com.by.model.EmployeeSkill;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface EmployeeSkillRepository extends ReactiveCassandraRepository<EmployeeSkill, Integer> {
    @Query("select * from emp_skill where emp_id = ?0 ALLOW FILTERING")
    Mono<EmployeeSkill> findOneByEmpId(Integer id);
}
