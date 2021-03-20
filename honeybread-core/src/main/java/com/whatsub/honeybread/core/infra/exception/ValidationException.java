package com.whatsub.honeybread.core.infra.exception;

import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import lombok.Getter;
import org.springframework.validation.Errors;

@Getter
public class ValidationException extends HoneyBreadException {
    private final Errors errors;

    public ValidationException(Errors errors) {
        super(ErrorCode.VALIDATION_ERROR);
        this.errors = errors;
    }
}
