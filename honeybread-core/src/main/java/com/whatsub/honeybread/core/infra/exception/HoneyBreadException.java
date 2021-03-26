package com.whatsub.honeybread.core.infra.exception;

import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HoneyBreadException extends RuntimeException {
    private final ErrorCode errorCode;
}
