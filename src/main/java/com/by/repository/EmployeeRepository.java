package com.by.repository;

import com.by.model.Employee;
import com.by.model.EmployeeSkill;
import jnr.ffi.annotations.In;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface EmployeeRepository extends ReactiveCassandraRepository<Employee, Integer> {
    @Query("select * from emp where emp_id = ?0")
    Mono<Employee> findByEmpId(Integer emp_id);

    @Query("select * from emp where emp_id in ?0 ALLOW FILTERING")
    Flux<Employee> findAllByEmpIds(List<Integer> emp_id);
}
