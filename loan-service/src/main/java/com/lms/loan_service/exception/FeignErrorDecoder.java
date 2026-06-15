package com.lms.loan_service.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {

        int status = response.status();

        return switch (status) {

            // ================= 400 BAD REQUEST =================
            case 400 -> new IllegalArgumentException(
                    "Bad request sent to remote service. Method: " + methodKey
            );

            // ================= 401 UNAUTHORIZED =================
            case 401 -> new SecurityException(
                    "Unauthorized access to remote service. Method: " + methodKey
            );

            // ================= 403 FORBIDDEN =================
            case 403 -> new SecurityException(
                    "Access denied by remote service. Method: " + methodKey
            );

            // ================= 404 NOT FOUND =================
            case 404 -> new EntityNotFoundException(
                    "Requested resource not found in remote service. Method: " + methodKey
            );

            // ================= 409 CONFLICT =================
            case 409 -> new IllegalStateException(
                    "Conflict occurred in remote service. Method: " + methodKey
            );

            // ================= 422 UNPROCESSABLE ENTITY =================
            case 422 -> new IllegalArgumentException(
                    "Validation failed in remote service. Method: " + methodKey
            );

            // ================= 429 TOO MANY REQUESTS =================
            case 429 -> new RuntimeException(
                    "Too many requests to remote service. Try again later."
            );


            // ================= DEFAULT =================
            default -> new RuntimeException(
                    "Unexpected error from remote service. Status: "
                            + status + ", Method: " + methodKey
            );
        };
    }
}