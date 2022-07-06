package com.by.kafka;

import com.by.model.Employee;
import com.by.model.EmployeeInput;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

import java.util.Objects;

@Slf4j
@Component
public class EmployeeKafkaConsumer {

    @Autowired
    KafkaConsumerConfig kafkaConsumerConfig;

    @Autowired
    EmployeeKafkaProducer employeeKafkaProducer;

    @Value("${kafka.dlq.topic}")
    String dlqTopic;

    @Value("${kafka.valid.employee.topic}")
    String empTopic;

    @Autowired
    ObjectMapper objectMapper;

    @EventListener({ApplicationReadyEvent.class})
    public void consume() {
        Scheduler scheduler = Schedulers.newBoundedElastic(3, 10, "consumer-" + kafkaConsumerConfig.getEmpTopic());
        Flux<ReceiverRecord<String, String>> receiverRecord = KafkaReceiver
                .create(kafkaConsumerConfig.receiverOptions()).receive();


        receiverRecord
                .groupBy(m -> m.receiverOffset().topicPartition())
                .flatMap(groupedFluxOnPartition -> {
                    return groupedFluxOnPartition.subscribeOn(scheduler)
                            .flatMap(record -> {
                                log.info("received record: {}", record);
                                try {
                                    employeeKafkaProducer.produce(record.key(), record.value(), validateRecord(record.value()) ? empTopic : dlqTopic);
                                } catch (JsonProcessingException e) {
                                    throw new RuntimeException(e);
                                }
                                return Mono.just(record);
                            });
                })
                .subscribe();

    }

    private boolean validateRecord(String value) throws JsonProcessingException {

        EmployeeInput employeeInput = objectMapper.readValue(value, EmployeeInput.class);
        if (Objects.isNull(employeeInput.getEmp_name())) {
            return false;
        }
        if (Objects.isNull(employeeInput.getEmp_city())) {
            return false;
        }
        if (Objects.isNull(employeeInput.getEmp_phone())) {
            return false;
        }
        if (Objects.isNull(employeeInput.getJava_exp())) {
            return false;
        }
        if (Objects.isNull(employeeInput.getSpring_exp())) {
            return false;
        }
        return true;
    }

}
