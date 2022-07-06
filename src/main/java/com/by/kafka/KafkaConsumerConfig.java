package com.by.kafka;

import jnr.ffi.annotations.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "kafka")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KafkaConsumerConfig {

    Map<String, Object> consumer;

    @Value("${kafka.valid.employee.topic}")
    String empTopic;

    @Value("${kafka.employee.topic}")
    String appTopic;

    private Map<String, Object> flatten(Map<String, Object> map) {
        Map<String, Object> result = new HashMap();
        map.forEach((key, value) -> {
            if (value instanceof Map) {
                Map<String, Object> nestedMap = this.flatten((Map)value);
                nestedMap.forEach((nestedKey, nestedValue) -> {
                    result.put(key + "." + nestedKey, nestedValue);
                });
            } else {
                result.put(key, value);
            }

        });
        return result;
    }
    public ReceiverOptions<Integer, String> receiverOptions(){
        if (this.consumer == null) {
            this.consumer = new HashMap();
        } else {
            this.consumer = this.flatten(this.consumer);
        }
        consumer.put("key.deserializer", JsonDeserializer.class.getName());
        consumer.put("value.deserializer", JsonDeserializer.class.getName());
        return ReceiverOptions.<Integer, String>create(consumer)
                .subscription(Collections.singleton(appTopic))
                .addAssignListener(p -> log.info("Group partition assigned: {}", p))
                .addAssignListener(p -> log.info("Group partition revoked: {}", p));
    }
}
