package com.by.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "kafka")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KafkaProducerConfig {
    Map<String, Object> producer;

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

    @Bean
    public KafkaSender kafkaSender(){
        if (this.producer == null) {
            this.producer = new HashMap();
        } else {
            this.producer = this.flatten(this.producer);
        }
        producer.put("key.serializer", JsonSerializer.class.getName());
        producer.put("value.serializer", JsonSerializer.class.getName());
        return KafkaSender.create(SenderOptions.create(producer));
    }
}
