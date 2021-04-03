package com.whatsub.honeybread.core.infra.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "-10001", "Resource Not Found"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "-10002", "Bad Request"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "-10003", "Validation Error"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "-10004", "Internal Server Error"),
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "-10005", "Unauthenticated"),

    DUPLICATE_USER_EMAIL(HttpStatus.CONFLICT, "-11001", "Duplicate User Email"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "-11002", "User Not Found"),

    DUPLICATE_CATEGORY(HttpStatus.CONFLICT, "-20001", "Duplicate Category"),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "-20002", "Category NotFound"),

    DUPLICATE_USER_STORE_FAVORITE(HttpStatus.CONFLICT, "-21000", "Duplicate User Store Favorite"),
    USER_STORE_FAVORITE_NOT_FOUND(HttpStatus.NOT_FOUND, "-21001", "User Store Favorite Not Found"),

    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "-22000", "Store Not Found"),
    DUPLICATE_STORE_NAME(HttpStatus.CONFLICT, "-22001", "Duplicate Store Name")
    ;
    private final HttpStatus status;
    private final String code;
    private final String message;

}
