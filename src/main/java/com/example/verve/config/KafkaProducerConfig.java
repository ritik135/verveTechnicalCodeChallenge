package com.example.verve.config;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class KafkaProducerConfig {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerConfig(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // Generic method to send a message to any Kafka topic
    public <T> void sendMessage(String topic, T message) {
        kafkaTemplate.send(topic, message);
    }
}
