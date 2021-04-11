package com.whatsub.honeybread.core.infra.errors;

import lombok.Value;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Value
public class ErrorField {
    String field;
    String value;
    String reason;

    static List<ErrorField> of(Errors errors) {
        if (errors == null) {
            return Collections.emptyList();
        }
        return errors.getFieldErrors()
            .stream()
            .map(fieldError -> new ErrorField(
                fieldError.getField(),
                getOrEmpty(fieldError.getRejectedValue()),
                getOrEmpty(fieldError.getDefaultMessage())
            ))
            .collect(Collectors.toList());
    }

    private static String getOrEmpty(Object target) {
        if (Objects.isNull(target)) {
            return StringUtils.EMPTY;
        }
        return target.toString();
    }
}
