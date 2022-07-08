package com.by.repository;

import com.by.model.Job;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface JobRepository extends ReactiveCassandraRepository<Job, Integer> {

    Mono<Job> findByJobId(Integer jobId);
}
