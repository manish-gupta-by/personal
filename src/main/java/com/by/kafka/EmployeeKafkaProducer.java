package com.by.kafka;

import com.by.model.EmployeeInput;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jnr.ffi.annotations.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import java.util.function.Consumer;

@Slf4j
@Component
public class EmployeeKafkaProducer {

    @Autowired
    KafkaProducerConfig kafkaProducerConfig;

    @Autowired
    KafkaSender<Integer, String> kafkaSender;

    @Autowired
    ObjectMapper objectMapper;

    public void produce(Integer key, String value, String topic) throws JsonProcessingException {

        kafkaSender.send(Mono.just(getRecordPublisher(topic, key, value)))
                .doOnError(e -> log.error("Error while sending record to kafka: {}", e.getMessage()))
                .subscribe();
    }

    private SenderRecord<Integer, String, Integer> getRecordPublisher(String topic, Integer key, String value) throws JsonProcessingException {
        return SenderRecord.create(
                new ProducerRecord<>(topic,
                        key,
                        value)
                , key);
    }
}
