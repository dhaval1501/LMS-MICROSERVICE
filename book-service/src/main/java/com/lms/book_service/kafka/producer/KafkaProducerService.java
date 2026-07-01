package com.lms.book_service.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    //    @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,


    public void send(String topic, Object event) {
        kafkaTemplate.send(topic, event);
    }

    public void send(String topic, String key, Object event) {
        kafkaTemplate.send(topic, key, event);
    }
}