package com.by.repository;

import com.by.model.Employee;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface EmployeeRepository extends ReactiveCassandraRepository<Employee, Integer> {
    @Query("select * from emp where emp_id = ?0")
    Mono<Employee> findByEmpId(Integer emp_id);

}
