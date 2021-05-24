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
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    @Mock
    Store 저장할_스토어;
    @Mock
    Store 저장된_스토어;

    int 스토어_카테고리_최대개수 = 5;

    List<StoreCategory> 모두_저장되어있는_카테고리_목록;
    List<StoreCategory> 일부만_저장되어있는_카테고리_목록;
    List<StoreCategory> 최대개수_초과하는_카테고리_목록;

    @BeforeEach
    void 스토어_초기화() {
        given(저장할_스토어.getBasic()).willReturn(mock(StoreBasic.class));
        given(저장할_스토어.getBasic().getName()).willReturn("테스트스토어");

        given(저장된_스토어.getBasic()).willReturn(mock(StoreBasic.class));
        given(저장된_스토어.getBasic().getName()).willReturn("테스트스토어");
    }

    @BeforeEach
    void 유효성_속성정보_초기화() {
        given(productValidationProperties.getMaxCategoryCnt()).willReturn(스토어_카테고리_최대개수);
    }

    @BeforeEach
    void 카테고리_초기화() {
        long 저장되어있는_카테고리ID = 1L;
        long 저장되어있는_카테고리ID_2 = 2L;
        long 저장되어있지_않은_카테고리ID = 999L;
        Category 카테고리1 = mock(Category.class);
        Category 카테고리2 = mock(Category.class);
        given(카테고리1.getId()).willReturn(저장되어있는_카테고리ID);
        given(카테고리2.getId()).willReturn(저장되어있는_카테고리ID_2);

        Set<Long> 모두_저장되어있는_카테고리ID_목록 = Set.of(저장되어있는_카테고리ID, 저장되어있는_카테고리ID_2);
        모두_저장되어있는_카테고리_목록 = 모두_저장되어있는_카테고리ID_목록.stream().map(StoreCategory::new).collect(Collectors.toList());
        given(categoryRepository.findAllById(모두_저장되어있는_카테고리ID_목록)).willReturn(List.of(카테고리1, 카테고리2));

        Set<Long> 일부만_저장되어있는_카테고리ID_목록 = Set.of(저장되어있는_카테고리ID, 저장되어있지_않은_카테고리ID);
        일부만_저장되어있는_카테고리_목록 = 일부만_저장되어있는_카테고리ID_목록.stream().map(StoreCategory::new).collect(Collectors.toList());
        given(categoryRepository.findAllById(일부만_저장되어있는_카테고리ID_목록)).willReturn(List.of(카테고리1));

        Set<Long> 최대개수_초과하는_카테고리ID_목록 = LongStream.rangeClosed(1, 스토어_카테고리_최대개수 + 1).boxed().collect(Collectors.toSet());
        최대개수_초과하는_카테고리_목록 = 최대개수_초과하는_카테고리ID_목록.stream().map(StoreCategory::new).collect(Collectors.toList());

    }

    @Test
    void 존재하지_않는_셀러는_등록유효성검사_실패() {
        // given
        존재하지_않는_셀러다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::등록을_검사하다);

        // then
        존재하는_셀러인지_검사를_수행했다();
        assertEquals(exception.getErrorCode(), ErrorCode.USER_NOT_FOUND);
    }

    @Test
    void 이미_존재하는_스토어명은_등록유효성검사_실패() {
        // given
        존재하는_셀러다();
        이미_등록된_스토어명이다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::등록을_검사하다);

        // then
        존재하는_셀러인지_검사를_수행했다();
        스토어명이_중복되는지_검사를_수행했다();
        assertEquals(exception.getErrorCode(), ErrorCode.DUPLICATE_STORE_NAME);
    }

    @Test
    void 카테고리_ID_개수가_최대_개수를_초과하여_등록유효성검사_실패() {
        // given
        존재하는_셀러다();
        중복되지않은_스토어명이다();
        최대개수_초과하는_카테고리목록이다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::등록을_검사하다);

        // then
        존재하는_셀러인지_검사를_수행했다();
        스토어명이_중복되는지_검사를_수행했다();
        카테고리아이디로_카테고리목록을_찾는로직을_수행하지_않았다();
        assertEquals(exception.getErrorCode(), ErrorCode.EXCEED_MAX_STORE_CATEGORY_CNT);
    }

    @Test
    void 카테고리ID_중_한개라도_존재하지_않으면_등록유효성검사_실패() {
        // given
        존재하는_셀러다();
        중복되지않은_스토어명이다();
        일부_존재하지않는_카테고리목록이다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::등록을_검사하다);

        // then
        존재하는_셀러인지_검사를_수행했다();
        스토어명이_중복되는지_검사를_수행했다();
        카테고리아이디로_카테고리목록을_찾는로직을_수행했다();
        assertEquals(exception.getErrorCode(), ErrorCode.CATEGORY_NOT_FOUND);
    }

    @Test
    void 모든_조건이_만족하여_등록유효성검사_성공() {
        // given
        존재하는_셀러다();
        중복되지않은_스토어명이다();
        모두_존재하는_카테고리목록이다();

        // when
        등록을_검사하다();

        // then
        존재하는_셀러인지_검사를_수행했다();
        스토어명이_중복되는지_검사를_수행했다();
        카테고리아이디로_카테고리목록을_찾는로직을_수행했다();
    }

    @Test
    void 스토어명_그대로_수정시_스토어명_유효성검사를_하지_않음() {
        // given
        스토어가_조회된다();

        // when
        수정을_검사하다();

        // then
        이름으로_스토어_조회를_수행하지_않았다();
    }

    @Test
    void 이미_존재하는_스토어명은_수정유효성검사_실패() {
        // given
        스토어가_조회된다();
        변경된_스토어명이_이미_등록된_스토어명이다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::수정을_검사하다);

        // then
        이름으로_스토어_조회를_수행했다();
        assertEquals(exception.getErrorCode(), ErrorCode.DUPLICATE_STORE_NAME);
    }

    @Test
    void 기존에_없던_스토어명은_스토어명_중복_에러가_발생하지_않음() {
        // given
        스토어가_조회된다();
        기존에_등록되어있지_않았던_유니크한_스토어명이다("정말 맛없는 가게");

        // when
        수정을_검사하다();

        // then
        이름으로_스토어_조회를_수행했다();
    }

    @Test
    void 카테고리_ID_개수가_최대개수를_초과하여_수정유효성검사_실패() {
        // given
        스토어가_조회된다();
        최대개수_초과하는_카테고리목록이다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::수정을_검사하다);

        // then
        이름으로_스토어_조회를_수행하지_않았다();
        카테고리아이디로_카테고리목록을_찾는로직을_수행하지_않았다();
        assertEquals(exception.getErrorCode(), ErrorCode.EXCEED_MAX_STORE_CATEGORY_CNT);
    }

    @Test
    void 카테고리ID_중_한개라도_존재하지_않으면_수정유효성검사_실패() {
        // given
        스토어가_조회된다();
        일부_존재하지않는_카테고리목록이다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::수정을_검사하다);

        // then
        이름으로_스토어_조회를_수행하지_않았다();
        카테고리아이디로_카테고리목록을_찾는로직을_수행했다();
        assertEquals(exception.getErrorCode(), ErrorCode.CATEGORY_NOT_FOUND);
    }

    @Test
    void 모든_조건이_만족하여_수정_유효성검사_성공() {
        // given
        스토어가_조회된다();
        모두_존재하는_카테고리목록이다();

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

    private void 이미_등록된_스토어명이다() {
        given(storeRepository.existsByBasicName(anyString())).willReturn(true);
    }

    private void 중복되지않은_스토어명이다() {
        given(storeRepository.existsByBasicName(anyString())).willReturn(false);
    }

    private void 일부_존재하지않는_카테고리목록이다() {
        given(저장할_스토어.getCategories()).willReturn(일부만_저장되어있는_카테고리_목록);
    }

    private void 모두_존재하는_카테고리목록이다() {
        given(저장할_스토어.getCategories()).willReturn(모두_저장되어있는_카테고리_목록);
    }

    private void 최대개수_초과하는_카테고리목록이다() {
        given(저장할_스토어.getCategories()).willReturn(최대개수_초과하는_카테고리_목록);
    }

    private void 스토어가_조회된다() {
        given(storeRepository.findById(1L)).willReturn(Optional.ofNullable(저장된_스토어));
    }

    private void 변경된_스토어명이_이미_등록된_스토어명이다() {
        given(저장할_스토어.getBasic().getName()).willReturn("변경!");
        given(storeRepository.existsByBasicName(anyString())).willReturn(true);
    }

    private void 기존에_등록되어있지_않았던_유니크한_스토어명이다(String uniqueName) {
        given(저장할_스토어.getBasic().getName()).willReturn(uniqueName);
    }

    /**
     * when
     */

    public void 등록을_검사하다() {
        storeValidator.validate(저장할_스토어);
    }

    private void 수정을_검사하다() {
        storeValidator.validate(저장된_스토어, 저장할_스토어);
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

}