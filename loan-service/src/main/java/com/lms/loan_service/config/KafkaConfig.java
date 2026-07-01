package com.lms.loan_service.config;

import com.lms.loan_service.kafka.constants.KafkaTopic;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic getBookRollbackTopic() {
        return TopicBuilder
                .name(KafkaTopic.BOOK_ROLLBACK)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
