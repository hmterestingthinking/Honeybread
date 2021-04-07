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
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = StoreService.class)
@RequiredArgsConstructor
public class StoreServiceCreateTest {

    final StoreService storeService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    StoreRepository storeRepository;

    @MockBean
    CategoryRepository categoryRepository;

    StoreCreateRequest 스토어_등록요청;
    Long 스토어아이디 = 1000L;
    String 스토어명 = "테스트스토어";
    Store 스토어;

    Set<Long> 카테고리_아이디_목록_비정상_요청;
    Set<Long> 카테고리_아이디_목록_정상_요청;
    List<Category> 카테고리_아이디_목록_비정상_요청에_대한_조회결과;
    List<Category> 카테고리_아이디_목록_정상_요청에_대한_조회결과;

    @BeforeEach
    void 스토어_등록요청_초기화() {
        스토어_등록요청 = mock(StoreCreateRequest.class);
        given(스토어_등록요청.getBasic()).willReturn(mock(StoreBasicRequest.class));
        given(스토어_등록요청.getBasic().getName()).willReturn(스토어명);

        스토어 = mock(Store.class);
        given(스토어.getId()).willReturn(스토어아이디);
        given(스토어.getBasic()).willReturn(mock(StoreBasic.class));
        given(스토어.getBasic().getName()).willReturn(스토어명);
        given(storeRepository.save(any())).willReturn(스토어);
    }

    @BeforeEach
    void 카테고리_셋업() {
        long 저장되어있는_카테고리_1_아이디 = 1L;
        long 저장되어있는_카테고리_2_아이디 = 2L;
        long 저장되어있지_않은_카테고리_아이디 = 999999L;

        Category 저장되어있는_카테고리_1 = mock(Category.class);
        Category 저장되어있는_카테고리_2 = mock(Category.class);
        given(저장되어있는_카테고리_1.getId()).willReturn(저장되어있는_카테고리_1_아이디);
        given(저장되어있는_카테고리_2.getId()).willReturn(저장되어있는_카테고리_2_아이디);

        카테고리_아이디_목록_비정상_요청 = Set.of(저장되어있는_카테고리_1_아이디, 저장되어있지_않은_카테고리_아이디);
        카테고리_아이디_목록_정상_요청 = Set.of(저장되어있는_카테고리_1_아이디, 저장되어있는_카테고리_2_아이디);
        카테고리_아이디_목록_비정상_요청에_대한_조회결과 = List.of(저장되어있는_카테고리_1);
        카테고리_아이디_목록_정상_요청에_대한_조회결과 = List.of(저장되어있는_카테고리_1, 저장되어있는_카테고리_2);
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
        일부_존재하지않는_카테고리아이디로_등록요청을_한다();
        일부_존재하지않는_카테고리아이디로_카테고리_조회시_저장된카테고리만_조회된다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::스토어를_등록한다);

        // then
        존재하는_셀러인지_검사를_수행했다();
        스토어명이_중복되는지_검사를_수행했다();
        카테고리아이디로_카테고리목록을_찾는로직을_수행했다();
        then(exception.getErrorCode()).equals(ErrorCode.CATEGORY_NOT_FOUND);
    }

    @Test
    void 모든_조건이_만족하여_등록_성공() {
        // given
        존재하는_셀러다();
        중복되지않은_스토어명이다();
        모두_존재하는_카테고리아이디로_등록요청을_한다();
        모두_존재하는_카테고리아이디로_카테고리_조회시_요청한_모든카테고리가_조회된다();

        // when
        스토어를_등록한다();

        // then
        존재하는_셀러인지_검사를_수행했다();
        스토어명이_중복되는지_검사를_수행했다();
        카테고리아이디로_카테고리목록을_찾는로직을_수행했다();
        스토어가_등록됐다();
    }

    /**
     * given
     */

    private void 존재하지_않는_셀러다() {
        given(userRepository.existsById(anyLong())).willReturn(false);
    }

    private void 존재하는_셀러다() {
        given(userRepository.existsById(anyLong())).willReturn(true);
    }

    private void 중복되지않은_스토어명이다() {
        given(storeRepository.existsByBasicName(anyString())).willReturn(false);
    }

    private void 중복된_스토어명이다() {
        given(storeRepository.existsByBasicName(스토어_등록요청.getBasic().getName())).willReturn(true);
    }

    private void 일부_존재하지않는_카테고리아이디로_등록요청을_한다() {
        given(스토어_등록요청.getCategoryIds()).willReturn(카테고리_아이디_목록_비정상_요청);
    }

    private void 일부_존재하지않는_카테고리아이디로_카테고리_조회시_저장된카테고리만_조회된다() {
        given(categoryRepository.findAllById(카테고리_아이디_목록_비정상_요청)).willReturn(카테고리_아이디_목록_비정상_요청에_대한_조회결과);
    }

    private void 모두_존재하는_카테고리아이디로_등록요청을_한다() {
        given(스토어_등록요청.getCategoryIds()).willReturn(카테고리_아이디_목록_정상_요청);
    }

    private void 모두_존재하는_카테고리아이디로_카테고리_조회시_요청한_모든카테고리가_조회된다() {
        given(categoryRepository.findAllById(카테고리_아이디_목록_정상_요청)).willReturn(카테고리_아이디_목록_정상_요청에_대한_조회결과);
    }

    /**
     * when
     */

    public void 스토어를_등록한다() {
        storeService.create(스토어_등록요청);
    }

    /**
     * then
     */

    private void 존재하는_셀러인지_검사를_수행했다() {
        then(userRepository).should().existsById(anyLong());
    }

    private void 스토어명이_중복되는지_검사를_수행했다() {
        then(storeRepository).should().existsByBasicName(anyString());
    }

    private void 카테고리아이디로_카테고리목록을_찾는로직을_수행했다() {
        then(categoryRepository).should().findAllById(anyCollection());
    }

    private void 스토어가_등록됐다() {
        then(storeRepository).should().save(any());
    }

}
