package com.lms.lms_common.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRollbackEvent {
    private Long bookId;
    private Long studentId;
    private String reason;
    private LocalDateTime failedAt;
}
