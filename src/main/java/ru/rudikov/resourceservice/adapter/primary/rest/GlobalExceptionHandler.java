package ru.rudikov.resourceservice.adapter.primary.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<String>> handleException(Exception ex) {
        log.error("INTERNAL_SERVER_ERROR", ex);
        String body = createErrorResponse(ex);
        return Mono.just(ResponseEntity.status(INTERNAL_SERVER_ERROR).body(body));
    }

    private String createErrorResponse(Exception error) {
        LocalDateTime errorTime = LocalDateTime.now();
        String formattedErrorTime = errorTime.format(ISO_LOCAL_DATE_TIME);

        return "Error: " + error.getClass().getSimpleName() + "\n" +
                "Time: " + formattedErrorTime + "\n" +
                "Message: " + error.getMessage();
    }
}
