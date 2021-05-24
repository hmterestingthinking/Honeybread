package com.whatsub.honeybread.mgmtadmin.domain.store;

import com.whatsub.honeybread.core.domain.store.Store;
import com.whatsub.honeybread.core.domain.store.StoreRepository;
import com.whatsub.honeybread.core.domain.store.validator.StoreValidator;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.store.dto.StoreCreateRequest;
import com.whatsub.honeybread.mgmtadmin.domain.store.dto.StoreUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = StoreService.class)
@RequiredArgsConstructor
public class StoreServiceTest {

    final StoreService storeService;

    @MockBean
    StoreRepository storeRepository;

    @MockBean
    StoreValidator storeValidator;

    @Mock
    Store 스토어;
    @Mock
    StoreCreateRequest 스토어_등록요청;
    @Mock
    StoreUpdateRequest 스토어_수정요청;
    @Mock
    Store 저장된_스토어;

    @BeforeEach
    void 스토어_초기화() {
        given(스토어_등록요청.toEntity()).willReturn(스토어);
        given(스토어_수정요청.toEntity()).willReturn(스토어);
    }

    @Test
    void 존재하지_않는_셀러는_등록_실패() {
        // given
        등록_유효성검사_결과는_존재하지_않는_셀러이다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::등록한다);

        // then
        등록_유효성검사를_수행했다();
        assertEquals(exception.getErrorCode(), ErrorCode.USER_NOT_FOUND);
    }

    @Test
    void 이미_존재하는_스토어명은_등록_실패() {
        // given
        등록_유효성검사_결과는_중복된_스토어명이다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::등록한다);

        // then
        등록_유효성검사를_수행했다();
        assertEquals(exception.getErrorCode(), ErrorCode.DUPLICATE_STORE_NAME);
    }

    @Test
    void 카테고리_ID_개수가_최대개수를_초과하여_등록_실패() {
        // given
        등록_유효성검사_결과는_카테고리_개수_초과이다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::등록한다);

        // then
        등록_유효성검사를_수행했다();
        assertEquals(exception.getErrorCode(), ErrorCode.EXCEED_MAX_STORE_CATEGORY_CNT);
    }

    @Test
    void 카테고리ID가_한개라도_존재하지_않으면_등록_실패() {
        // given
        등록_유효성검사_결과는_존재하지_않는_카테고리이다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::등록한다);

        // then
        등록_유효성검사를_수행했다();
        assertEquals(exception.getErrorCode(), ErrorCode.CATEGORY_NOT_FOUND);
    }

    @Test
    void 모든_조건이_만족하여_등록_성공() {
        // given
        유효한_등록_요청이다();
        스토어를_저장하면_엔티티가_반환된다();

        // when
        등록한다();

        // then
        등록_유효성검사를_수행했다();
        스토어가_등록됐다();
    }

    @Test
    void 스토어_존재하지_않으면_수정_실패() {
        // given
        스토어가_조회되지_않는다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::수정한다);

        // then
        assertEquals(exception.getErrorCode(), ErrorCode.STORE_NOT_FOUND);
    }

    @Test
    void 이미_존재하는_스토어명은_수정_실패() {
        // given
        스토어가_조회된다();
        수정_유효성검사_결과는_중복된_스토어명이다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::수정한다);

        // then
        수정_유효성검사를_수행했다();
        assertEquals(exception.getErrorCode(), ErrorCode.DUPLICATE_STORE_NAME);
    }

    @Test
    void 카테고리_ID_개수가_최대개수를_초과하여_수정_실패() {
        // given
        스토어가_조회된다();
        수정_유효성검사_결과는_카테고리_개수_초과이다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::수정한다);

        // then
        수정_유효성검사를_수행했다();
        assertEquals(exception.getErrorCode(), ErrorCode.EXCEED_MAX_STORE_CATEGORY_CNT);
    }

    @Test
    void 카테고리ID가_한개라도_존재하지_않으면_수정_실패() {
        // given
        스토어가_조회된다();
        수정_유효성검사_결과는_존재하지_않는_카테고리이다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::수정한다);

        // then
        수정_유효성검사를_수행했다();
        assertEquals(exception.getErrorCode(), ErrorCode.CATEGORY_NOT_FOUND);
    }

    @Test
    void 모든_카테고리_ID가_존재하여_수정_성공() {
        // given
        스토어가_조회된다();
        유효한_등록_요청이다();
        스토어를_수정하면_엔티티가_반환된다();

        // when
        수정한다();

        // then
        수정_유효성검사를_수행했다();
    }

    /**
     * given
     */

    private void 스토어를_저장하면_엔티티가_반환된다() {
        given(storeRepository.save(any())).willReturn(스토어);
    }

    private void 등록_유효성검사_결과는_존재하지_않는_셀러이다() {
        willThrow(new HoneyBreadException(ErrorCode.USER_NOT_FOUND))
                .given(storeValidator).validate(스토어);
    }

    private void 등록_유효성검사_결과는_중복된_스토어명이다() {
        willThrow(new HoneyBreadException(ErrorCode.DUPLICATE_STORE_NAME))
                .given(storeValidator).validate(스토어);
    }

    private void 등록_유효성검사_결과는_존재하지_않는_카테고리이다() {
        willThrow(new HoneyBreadException(ErrorCode.CATEGORY_NOT_FOUND))
                .given(storeValidator).validate(스토어);
    }

    private void 등록_유효성검사_결과는_카테고리_개수_초과이다() {
        willThrow(new HoneyBreadException(ErrorCode.EXCEED_MAX_STORE_CATEGORY_CNT))
                .given(storeValidator).validate(스토어);
    }

    private void 유효한_등록_요청이다() {
        willDoNothing()
                .given(storeValidator).validate(스토어);
    }

    private void 스토어를_수정하면_엔티티가_반환된다() {
        given(storeRepository.save(any())).willReturn(스토어);
    }

    private void 스토어가_조회되지_않는다() {
        given(storeRepository.findById(anyLong())).willReturn(Optional.empty());
    }

    private void 스토어가_조회된다() {
        given(storeRepository.findById(anyLong())).willReturn(Optional.ofNullable(저장된_스토어));
    }

    private void 수정_유효성검사_결과는_중복된_스토어명이다() {
        willThrow(new HoneyBreadException(ErrorCode.DUPLICATE_STORE_NAME))
                .given(storeValidator).validate(저장된_스토어, 스토어);
    }

    private void 수정_유효성검사_결과는_존재하지_않는_카테고리이다() {
        willThrow(new HoneyBreadException(ErrorCode.CATEGORY_NOT_FOUND))
                .given(storeValidator).validate(저장된_스토어, 스토어);
    }

    private void 수정_유효성검사_결과는_카테고리_개수_초과이다() {
        willThrow(new HoneyBreadException(ErrorCode.EXCEED_MAX_STORE_CATEGORY_CNT))
                .given(storeValidator).validate(저장된_스토어, 스토어);
    }

    /**
     * when
     */

    public void 등록한다() {
        storeService.create(스토어_등록요청);
    }

    private void 수정한다() {
        storeService.update(1L, 스토어_수정요청);
    }

    /**
     * then
     */

    private void 등록_유효성검사를_수행했다() {
        then(storeValidator).should().validate(any());
    }

    private void 수정_유효성검사를_수행했다() {
        then(storeValidator).should().validate(저장된_스토어, 스토어);
    }

    private void 스토어가_등록됐다() {
        then(storeRepository).should().save(any());
    }

}
