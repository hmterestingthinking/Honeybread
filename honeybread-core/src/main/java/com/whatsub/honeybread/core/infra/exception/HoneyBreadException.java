package com.whatsub.honeybread.core.infra.exception;

import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import lombok.Getter;

@Getter
public class HoneyBreadException extends RuntimeException {
    private final ErrorCode errorCode;

    public HoneyBreadException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
