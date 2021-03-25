package com.whatsub.honeybread.mgmtadmin.domain.user.dto;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserRequestTest {

    static ValidatorFactory validatorFactory;
    static Validator validator;

    @BeforeAll
    static void beforeAll() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void afterAll() {
        validatorFactory.close();
    }

    @ParameterizedTest
    @ValueSource(strings = {"qwer1234!", "qwer1234!2", "!qwer1234"})
    void 비밀번호_정규식_성공(String password) {
        final UserRequest userRequest =
                new UserRequest("test@honeybread.com", password, "010-9999-9999", false, false);

        Set<ConstraintViolation<UserRequest>> validate = validator.validate(userRequest);
        assertTrue(validate.isEmpty());
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"qwer1234", "qwer1234!5678910", "!qwer@#$%^", "qwerasdf"})
    void 비밀번호_정규식_실패(String password) {
        final UserRequest userRequest =
                new UserRequest("test@honeybread.com", password, "010-9999-9999", false, false);

        Set<ConstraintViolation<UserRequest>> validate = validator.validate(userRequest);
        assertEquals(1, validate.size());
    }

    @ParameterizedTest
    @ValueSource(strings = {"010-0000-0000", "010-1234-1234"})
    void 휴대폰번호_정규식_성공(String phoneNumber) {
        final UserRequest userRequest =
                new UserRequest("test@honeybread.com", "qwer1234!", phoneNumber, false, false);

        Set<ConstraintViolation<UserRequest>> validate = validator.validate(userRequest);
        assertTrue(validate.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"010-0000-000", "010-12342-1234"})
    void 휴대폰번호_정규식_실패(String phoneNumber) {
        final UserRequest userRequest =
                new UserRequest("test@honeybread.com", "qwer1234!", phoneNumber, false, false);

        Set<ConstraintViolation<UserRequest>> validate = validator.validate(userRequest);
        assertEquals(1, validate.size());
    }

}