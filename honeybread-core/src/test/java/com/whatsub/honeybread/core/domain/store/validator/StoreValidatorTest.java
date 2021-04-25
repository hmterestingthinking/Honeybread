package com.whatsub.honeybread.core.domain.store.validator;

import com.whatsub.honeybread.core.domain.category.Category;
import com.whatsub.honeybread.core.domain.category.CategoryRepository;
import com.whatsub.honeybread.core.domain.store.Store;
import com.whatsub.honeybread.core.domain.store.StoreBasic;
import com.whatsub.honeybread.core.domain.store.StoreCategory;
import com.whatsub.honeybread.core.domain.store.StoreRepository;
import com.whatsub.honeybread.core.domain.user.UserRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = StoreValidator.class)
@RequiredArgsConstructor
class StoreValidatorTest {
    final StoreValidator storeValidator;

    @MockBean
    UserRepository userRepository;

    @MockBean
    StoreRepository storeRepository;

    @MockBean
    CategoryRepository categoryRepository;

    @MockBean
    ProductValidationProperties productValidationProperties;

    Store 스토어;
    Store 저장된_스토어;
    String 저장된_스토어명 = "스토어명";
    String 저장되어있는_다른_스토어명 = "다른 스토어명";

    int 카테고리_아이디_등록_최대개수 = 5;

    List<StoreCategory> 카테고리_정상_목록;
    List<Category> 카테고리_정상_요청에_대한_조회결과;

    List<StoreCategory> 카테고리_비정상_목록;
    List<Category> 카테고리_비정상_요청에_대한_조회결과;

    List<StoreCategory> 카테고리_최대개수_초과_목록;

    @BeforeEach
    void 스토어_초기화() {
        스토어 = mock(Store.class);
        given(스토어.getBasic()).willReturn(mock(StoreBasic.class));
        given(스토어.getBasic().getName()).willReturn(저장된_스토어명);

        저장된_스토어 = mock(Store.class);
        given(저장된_스토어.getBasic()).willReturn(mock(StoreBasic.class));
        given(저장된_스토어.getBasic().getName()).willReturn(저장된_스토어명);
    }

    @BeforeEach
    void 카테고리_초기화() {
        given(productValidationProperties.getMaxCategoryCnt()).willReturn(카테고리_아이디_등록_최대개수);

        long 저장되어있는_카테고리_1_아이디 = 1L;
        long 저장되어있는_카테고리_2_아이디 = 2L;
        long 저장되어있지_않은_카테고리_아이디 = 999999L;

        Category 저장되어있는_카테고리_1 = mock(Category.class);
        Category 저장되어있는_카테고리_2 = mock(Category.class);
        given(저장되어있는_카테고리_1.getId()).willReturn(저장되어있는_카테고리_1_아이디);
        given(저장되어있는_카테고리_2.getId()).willReturn(저장되어있는_카테고리_2_아이디);

        카테고리_정상_목록 = List.of(new StoreCategory(저장되어있는_카테고리_1_아이디), new StoreCategory(저장되어있는_카테고리_2_아이디));
        카테고리_정상_요청에_대한_조회결과 = List.of(저장되어있는_카테고리_1, 저장되어있는_카테고리_2);

        카테고리_비정상_목록 = List.of(new StoreCategory(저장되어있는_카테고리_1_아이디), new StoreCategory(저장되어있지_않은_카테고리_아이디));
        카테고리_비정상_요청에_대한_조회결과 = List.of(저장되어있는_카테고리_1);

        카테고리_최대개수_초과_목록 = LongStream.rangeClosed(1, 카테고리_아이디_등록_최대개수 + 1).mapToObj(StoreCategory::new).collect(Collectors.toList());
    }

    @Test
    void 존재하지_않는_셀러는_등록_실패() {
        // given
        존재하지_않는_셀러다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::등록을_검사하다);

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
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::등록을_검사하다);

        // then
        존재하는_셀러인지_검사를_수행했다();
        스토어명이_중복되는지_검사를_수행했다();
        then(exception.getErrorCode()).equals(ErrorCode.DUPLICATE_STORE_NAME);
    }

    @Test
    void 카테고리_ID_개수가_5개를_초과하여_등록_실패() {
        // given
        존재하는_셀러다();
        중복되지않은_스토어명이다();
        최대개수_초과하는_카테고리목록을_등록요청을_한다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::등록을_검사하다);

        // then
        존재하는_셀러인지_검사를_수행했다();
        스토어명이_중복되는지_검사를_수행했다();
        카테고리아이디로_카테고리목록을_찾는로직을_수행하지_않았다();
        then(exception.getErrorCode()).equals(ErrorCode.EXCEED_MAX_STORE_CATEGORY_CNT);
    }

    @Test
    void 카테고리ID가_하나라도_존재하지_않으면_등록_실패() {
        // given
        존재하는_셀러다();
        중복되지않은_스토어명이다();
        일부_존재하지않는_카테고리아이디로_등록요청을_한다();
        일부_존재하지않는_카테고리아이디로_카테고리_조회시_저장된카테고리만_조회된다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::등록을_검사하다);

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

        // when
        등록을_검사하다();

        // then
        존재하는_셀러인지_검사를_수행했다();
        스토어명이_중복되는지_검사를_수행했다();
        카테고리아이디로_카테고리목록을_찾는로직을_수행했다();
    }

    @Test
    void 다른_스토어명으로_수정시_실패() {
        // given
        저장된_스토어가_조회된다();
        다른_스토어가_사용중인_스토어명이다();
        다른_스토어의_이름으로_수정을_요청한다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::수정을_검사하다);

        // then
        이름으로_스토어_조회를_수행했다();
        수정했더니_이런_에러가_떨어졌다(exception, ErrorCode.DUPLICATE_STORE_NAME);
    }

    @Test
    void 스토어명_그대로_수정시_중복이라고_판단하지_않음() {
        // given
        저장된_스토어가_조회된다();

        // when
        수정을_검사하다();

        // then
        이름으로_스토어_조회를_수행하지_않았다();
    }

    @Test
    void 유니크한_이름으로_스토어명_수정시_중복이라고_판단하지_않음() {
        // given
        저장된_스토어가_조회된다();
        지금까지_이런_스토어명은_없었다("정말 맛없는 가게");

        // when
        수정을_검사하다();

        // then
        이름으로_스토어_조회를_수행했다();
    }

    @Test
    void 카테고리_ID_개수가_5개를_초과하여_수정_실패() {
        // given
        저장된_스토어가_조회된다();
        최대개수_초과하는_카테고리목록을_수정요청을_한다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::수정을_검사하다);

        // then
        이름으로_스토어_조회를_수행하지_않았다();
        카테고리아이디로_카테고리목록을_찾는로직을_수행하지_않았다();
        수정했더니_이런_에러가_떨어졌다(exception, ErrorCode.EXCEED_MAX_STORE_CATEGORY_CNT);
    }

    @Test
    void 하나라도_카테고리_ID가_존재하지_않아_수정_실패() {
        // given
        저장된_스토어가_조회된다();
        일부_존재하지않는_카테고리아이디로_수정요청을_한다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::수정을_검사하다);

        // then
        이름으로_스토어_조회를_수행하지_않았다();
        카테고리아이디로_카테고리목록을_찾는로직을_수행했다();
        수정했더니_이런_에러가_떨어졌다(exception, ErrorCode.CATEGORY_NOT_FOUND);
    }

    @Test
    void 모든_카테고리_ID가_존재하여_수정_성공() {
        // given
        저장된_스토어가_조회된다();
        모두_존재하는_카테고리아이디로_수정요청을_한다();

        // when
        수정을_검사하다();

        // then
        이름으로_스토어_조회를_수행하지_않았다();
        카테고리아이디로_카테고리목록을_찾는로직을_수행했다();
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
        given(storeRepository.existsByBasicName(스토어.getBasic().getName())).willReturn(true);
    }

    private void 일부_존재하지않는_카테고리아이디로_등록요청을_한다() {
        given(스토어.getCategories()).willReturn(카테고리_비정상_목록);
    }

    private void 일부_존재하지않는_카테고리아이디로_카테고리_조회시_저장된카테고리만_조회된다() {
        given(categoryRepository.findAllById(카테고리_비정상_목록.stream().map(StoreCategory::getCategoryId).collect(Collectors.toSet())))
                .willReturn(카테고리_비정상_요청에_대한_조회결과);
    }

    private void 모두_존재하는_카테고리아이디로_등록요청을_한다() {
        given(스토어.getCategories()).willReturn(카테고리_정상_목록);
        given(categoryRepository.findAllById(카테고리_정상_목록.stream().map(StoreCategory::getCategoryId).collect(Collectors.toSet())))
                .willReturn(카테고리_정상_요청에_대한_조회결과);
    }

    private void 저장된_스토어가_조회된다() {
        given(storeRepository.findById(1L)).willReturn(Optional.ofNullable(저장된_스토어));
    }

    private void 다른_스토어의_이름으로_수정을_요청한다() {
        given(스토어.getBasic().getName()).willReturn(저장되어있는_다른_스토어명);
    }

    private void 다른_스토어가_사용중인_스토어명이다() {
        given(storeRepository.existsByBasicName(저장되어있는_다른_스토어명)).willReturn(true);
    }

    private void 지금까지_이런_스토어명은_없었다(String uniqueName) {
        given(스토어.getBasic().getName()).willReturn(uniqueName);
    }

    private void 일부_존재하지않는_카테고리아이디로_수정요청을_한다() {
        given(스토어.getCategories())
                .willReturn(카테고리_비정상_목록);
        given(categoryRepository.findAllById(카테고리_비정상_목록.stream().map(StoreCategory::getCategoryId).collect(Collectors.toSet())))
                .willReturn(카테고리_비정상_요청에_대한_조회결과);
    }

    private void 최대개수_초과하는_카테고리목록을_등록요청을_한다() {
        given(스토어.getCategories()).willReturn(카테고리_최대개수_초과_목록);
    }

    private void 최대개수_초과하는_카테고리목록을_수정요청을_한다() {
        given(스토어.getCategories()).willReturn(카테고리_최대개수_초과_목록);
    }

    private void 모두_존재하는_카테고리아이디로_수정요청을_한다() {
        given(스토어.getCategories())
                .willReturn(카테고리_정상_목록);
        given(categoryRepository.findAllById(카테고리_정상_목록.stream().map(StoreCategory::getCategoryId).collect(Collectors.toSet())))
                .willReturn(카테고리_정상_요청에_대한_조회결과);
    }

    /**
     * when
     */

    public void 등록을_검사하다() {
        storeValidator.validate(스토어);
    }

    private void 수정을_검사하다() {
        storeValidator.validate(저장된_스토어, 스토어);
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

    private void 이름으로_스토어_조회를_수행했다() {
        then(storeRepository).should().existsByBasicName(anyString());
    }

    private void 이름으로_스토어_조회를_수행하지_않았다() {
        then(storeRepository).should(never()).existsByBasicName(anyString());
    }

    private void 카테고리아이디로_카테고리목록을_찾는로직을_수행했다() {
        then(categoryRepository).should().findAllById(anyCollection());
    }

    private void 카테고리아이디로_카테고리목록을_찾는로직을_수행하지_않았다() {
        then(categoryRepository).should(never()).findAllById(anyCollection());
    }

    private void 수정했더니_이런_에러가_떨어졌다(HoneyBreadException exception, ErrorCode errorCode) {
        assertEquals(exception.getErrorCode(), errorCode);
    }
}