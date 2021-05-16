package com.whatsub.honeybread.core.infra.errors;

import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.core.infra.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> validation(final ValidationException e) {
        log.error("Validation Exception occur", e);
        return ResponseEntity
            .status(ErrorCode.VALIDATION_ERROR.getStatus())
            .body(ErrorResponse.of(ErrorCode.VALIDATION_ERROR, e.getErrors()));
    }

    @ExceptionHandler(HoneyBreadException.class)
    public ResponseEntity<ErrorResponse> honeyBread(final HoneyBreadException e) {
        log.error("HoneyBread Exception occur", e);
        return ResponseEntity
            .status(e.getErrorCode().getStatus())
            .body(ErrorResponse.of(e.getErrorCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(final Exception e) {
        log.error("Not Defined or Not Handled Exception occur", e);
        return ResponseEntity
            .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
            .body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}
