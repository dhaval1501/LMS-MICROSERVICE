package com.lms.loan_service.kafka.producer;

import com.lms.lms_common.dto.kafka.BookRollbackEvent;
import com.lms.loan_service.kafka.constants.KafkaTopic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class LoanEventProducer {

    private final KafkaProducerService kafkaProducerService;

    public void publishBookRollbackEvent(Long bookId, Long studentId, String reason) {
        BookRollbackEvent event = new BookRollbackEvent(
                bookId,
                studentId,
                reason,
                LocalDateTime.now()
        );

        kafkaProducerService.send(KafkaTopic.BOOK_ROLLBACK, event);
//        log.warn("Published rollback event for book {} due to: {}", bookId, reason);
    }
}
