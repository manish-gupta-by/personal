package com.by.kafka;

import com.by.config.KafkaConsumerConfig;
import com.by.model.EmployeeDTO;
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
        Flux<ReceiverRecord<Integer , String>> receiverRecord = KafkaReceiver
                .<Integer , String>create(kafkaConsumerConfig.receiverOptions()).receive();

        receiverRecord
                .groupBy(m -> m.receiverOffset().topicPartition())
                .flatMap(groupedFluxOnPartition -> {
                    return groupedFluxOnPartition.subscribeOn(scheduler)
                            .flatMap(record -> {
                                log.info("received record: {}", record);

                                try {
                                    employeeKafkaProducer.produce(record.key(), record.value(), validateRecord(record.value()) ? empTopic : dlqTopic);
                                } catch (JsonProcessingException e) {
                                    return Mono.error(e);
                                } catch(Exception e){
                                    return Mono.error(e);
                                }
                                return Mono.just(record);
                            });
                })
                .doOnError(e -> log.error("error while consuming from kafka topic {}, {}", kafkaConsumerConfig.getEmpTopic(), e.getMessage()))
                .subscribe();

    }

    private boolean validateRecord(String value) throws JsonProcessingException {

        EmployeeDTO employeeDTO = objectMapper.readValue(value, EmployeeDTO.class);
        if (Objects.isNull(employeeDTO.getEmp_name())) {
            return false;
        }
        if (Objects.isNull(employeeDTO.getEmp_city())) {
            return false;
        }
        if (Objects.isNull(employeeDTO.getEmp_phone())) {
            return false;
        }
        if (Objects.isNull(employeeDTO.getJava_exp())) {
            return false;
        }
        if (Objects.isNull(employeeDTO.getSpring_exp())) {
            return false;
        }
        return true;
    }

}
