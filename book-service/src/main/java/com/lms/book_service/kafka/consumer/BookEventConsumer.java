package com.lms.book_service.kafka.consumer;

import com.lms.book_service.kafka.constants.KafkaGroup;
import com.lms.book_service.kafka.constants.KafkaTopic;
import com.lms.book_service.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.lms.lms_common.dto.kafka.BookRollbackEvent;

@Component
@RequiredArgsConstructor

public class BookEventConsumer {

    private final BookService bookService;

    @KafkaListener(topics = KafkaTopic.BOOK_ROLLBACK,groupId = KafkaGroup.BOOK_SERVICE_GROUP)
    public void bookRollbackConsumer(BookRollbackEvent event){
        try {
            bookService.returnBook(event.getBookId());
//            log.info("Successfully rolled back book: {}", event.getBookId());
        } catch (Exception e) {
//            log.error("Failed to process rollback for book {}: {}",
//                    event.getBookId(), e.getMessage());
            throw e;
        }
    }
}
