package com.whatsub.honeybread.mgmtadmin.domain.store;

import com.whatsub.honeybread.core.domain.category.Category;
import com.whatsub.honeybread.core.domain.category.CategoryRepository;
import com.whatsub.honeybread.core.domain.store.Store;
import com.whatsub.honeybread.core.domain.store.StoreBasic;
import com.whatsub.honeybread.core.domain.store.StoreRepository;
import com.whatsub.honeybread.core.domain.user.UserRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.store.dto.StoreBasicRequest;
import com.whatsub.honeybread.mgmtadmin.domain.store.dto.StoreCreateRequest;
import com.whatsub.honeybread.mgmtadmin.domain.store.dto.StoreUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = StoreService.class)
@RequiredArgsConstructor
public class StoreServiceTest {

    final StoreService storeService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    StoreRepository storeRepository;

    @MockBean
    CategoryRepository categoryRepository;

    @Mock
    StoreCreateRequest 스토어_등록요청;
    @Mock
    StoreUpdateRequest 스토어_수정요청;

    @Mock
    Store 스토어;

    Long 카테고리_ID_1 = 1L;
    Category 카테고리_1;

    Long 카테고리_ID_2 = 2L;
    Category 카테고리_2;

    List<Long> 카테고리_ID_목록 = List.of(카테고리_ID_1, 카테고리_ID_2);

    Long 스토어아이디 = 1000L;
    String 스토어명 = "테스트스토어";

    @BeforeEach
    void 스토어_등록요청_초기화() {
        카테고리_1 = mock(Category.class);
        given(카테고리_1.getId()).willReturn(카테고리_ID_1);

        카테고리_2 = mock(Category.class);
        given(카테고리_2.getId()).willReturn(카테고리_ID_2);

        given(스토어_등록요청.getBasic()).willReturn(mock(StoreBasicRequest.class));
        given(스토어_등록요청.getBasic().getName()).willReturn(스토어명);
        given(스토어_등록요청.getCategoryIds()).willReturn(카테고리_ID_목록);
    }

    @BeforeEach
    void 스토어_수정요청_초기화() {
        카테고리_ID_1 = 1L;
        카테고리_1 = mock(Category.class);
        given(카테고리_1.getId()).willReturn(카테고리_ID_1);

        카테고리_ID_2 = 2L;
        카테고리_2 = mock(Category.class);
        given(카테고리_2.getId()).willReturn(카테고리_ID_2);

        given(스토어_수정요청.getBasic()).willReturn(mock(StoreBasicRequest.class));
        given(스토어_수정요청.getBasic().getName()).willReturn(스토어명);
        given(스토어_수정요청.getCategoryIds()).willReturn(카테고리_ID_목록);
    }

    @BeforeEach
    void 스토어저장_초기화() {
        given(스토어.getId()).willReturn(스토어아이디);
        given(스토어.getBasic()).willReturn(mock(StoreBasic.class));
        given(스토어.getBasic().getName()).willReturn(스토어명);
        given(storeRepository.save(any())).willReturn(스토어);
    }

    @Test
    void 존재하지_않는_셀러는_등록_실패() {
        // given
        존재하지_않는_셀러다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::스토어를_등록한다);

        // then
        존재하는_셀러인지_검사를_수행했다();
        then(exception.getErrorCode()).equals(ErrorCode.USER_NOT_FOUND);
    }

    @Test
    void 이미_존재하는_스토어명은_등록_실패() {
        // given
        존재하는_셀러다();
        중복된_스토어명이다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::스토어를_등록한다);

        // then
        존재하는_셀러인지_검사를_수행했다();
        스토어명이_중복되는지_검사를_수행했다();
        then(exception.getErrorCode()).equals(ErrorCode.DUPLICATE_STORE_NAME);
    }

    @Test
    void 카테고리ID가_하나라도_존재하지_않으면_등록_실패() {
        // given
        존재하는_셀러다();
        중복되지않은_스토어명이다();
        모든_카테고리_ID가_존재하는_것은_아니다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::스토어를_등록한다);

        // then
        존재하는_셀러인지_검사를_수행했다();
        스토어명이_중복되는지_검사를_수행했다();
        카테고리가_존재하는지_검사를_수행했다();
        then(exception.getErrorCode()).equals(ErrorCode.CATEGORY_NOT_FOUND);
    }

    @Test
    void 모든_조건이_만족하여_등록_성공() {
        // given
        존재하는_셀러다();
        중복되지않은_스토어명이다();
        모든_카테고리_ID가_존재한다();

        // when
        스토어를_등록한다();

        // then
        존재하는_셀러인지_검사를_수행했다();
        스토어명이_중복되는지_검사를_수행했다();
        카테고리가_존재하는지_검사를_수행했다();
        스토어가_등록됐다();
    }

    @Test
    void 존재하지_않는_스토어는_수정_실패() {
        // given
        존재하지_않는_스토어다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::스토어를_수정한다);

        // then
        스토어가_존재하는지_검사를_수행했다();
        then(exception.getErrorCode()).equals(ErrorCode.STORE_NOT_FOUND);
    }

    @Test
    void 중복된_스토어명은_수정_실패() {
        // given
        존재하는_스토어다();
        해당스토어명을_제외했을때_중복된_스토어명이다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::스토어를_수정한다);

        // then
        스토어가_존재하는지_검사를_수행했다();
        해당스토어명을_제외하고_스토어명이_중복되는지_검사를_수행했다();
        then(exception.getErrorCode()).equals(ErrorCode.DUPLICATE_STORE_NAME);
    }

    @Test
    void 카테고리ID가_하나라도_존재하지_않으면_수정_실패() {
        // given
        존재하는_스토어다();
        해당스토어명을_제외했을때도_중복되지_않은_스토어명이다();
        모든_카테고리_ID가_존재하는_것은_아니다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::스토어를_수정한다);

        // then
        스토어가_존재하는지_검사를_수행했다();
        해당스토어명을_제외하고_스토어명이_중복되는지_검사를_수행했다();
        카테고리가_존재하는지_검사를_수행했다();
        then(exception.getErrorCode()).equals(ErrorCode.CATEGORY_NOT_FOUND);
    }

    @Test
    void 모든_조건이_만족하여_수정_성공() {
        // given
        존재하는_스토어다();
        해당스토어명을_제외했을때도_중복되지_않은_스토어명이다();
        모든_카테고리_ID가_존재한다();

        // when
        스토어를_수정한다();

        // then
        스토어가_존재하는지_검사를_수행했다();
        해당스토어명을_제외하고_스토어명이_중복되는지_검사를_수행했다();
        카테고리가_존재하는지_검사를_수행했다();
    }

    /**
     * given
     */

    private void 존재하지_않는_스토어다() {
        given(storeRepository.existsById(스토어아이디)).willReturn(false);
    }

    private void 존재하는_스토어다() {
        given(storeRepository.existsById(스토어아이디)).willReturn(true);
        given(storeRepository.findById(스토어아이디)).willReturn(Optional.of(스토어));
    }

    private void 존재하지_않는_셀러다() {
        given(userRepository.existsById(anyLong())).willReturn(false);
    }

    private void 존재하는_셀러다() {
        given(userRepository.existsById(anyLong())).willReturn(true);
    }

    private void 중복되지않은_스토어명이다() {
        given(storeRepository.existsByBasicName(anyString())).willReturn(false);
    }

    private void 해당스토어명을_제외했을때_중복된_스토어명이다() {
        given(storeRepository.existsByIdNotAndBasicName(스토어아이디, 스토어_등록요청.getBasic().getName())).willReturn(true);
    }

    private void 해당스토어명을_제외했을때도_중복되지_않은_스토어명이다() {
        given(storeRepository.existsByIdNotAndBasicName(스토어아이디, 스토어_등록요청.getBasic().getName())).willReturn(false);
    }

    private void 중복된_스토어명이다() {
        given(storeRepository.existsByBasicName(스토어_등록요청.getBasic().getName())).willReturn(true);
    }

    private void 모든_카테고리_ID가_존재한다() {
        given(categoryRepository.findAllById(카테고리_ID_목록)).willReturn(List.of(카테고리_1, 카테고리_2));
    }

    private void 모든_카테고리_ID가_존재하는_것은_아니다() {
        given(categoryRepository.findAllById(카테고리_ID_목록)).willReturn(List.of(카테고리_1));
    }

    /**
     * when
     */

    public void 스토어를_등록한다() {
        storeService.create(스토어_등록요청);
    }

    public void 스토어를_수정한다() {
        storeService.update(스토어아이디, 스토어_수정요청);
    }

    /**
     * then
     */

    private void 스토어가_존재하는지_검사를_수행했다() {
        then(storeRepository).should().existsById(anyLong());
    }

    private void 존재하는_셀러인지_검사를_수행했다() {
        then(userRepository).should().existsById(anyLong());
    }

    private void 스토어명이_중복되는지_검사를_수행했다() {
        then(storeRepository).should().existsByBasicName(anyString());
    }

    private void 해당스토어명을_제외하고_스토어명이_중복되는지_검사를_수행했다() {
        then(storeRepository).should().existsByIdNotAndBasicName(anyLong(), anyString());
    }

    private void 카테고리가_존재하는지_검사를_수행했다() {
        then(categoryRepository).should().findAllById(카테고리_ID_목록);
    }

    private void 스토어가_등록됐다() {
        then(storeRepository).should().save(any());
    }

}
