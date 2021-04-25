package com.whatsub.honeybread.mgmtadmin.domain.store;

import com.whatsub.honeybread.core.domain.category.Category;
import com.whatsub.honeybread.core.domain.category.CategoryRepository;
import com.whatsub.honeybread.core.domain.store.Store;
import com.whatsub.honeybread.core.domain.store.StoreBasic;
import com.whatsub.honeybread.core.domain.store.StoreRepository;
import com.whatsub.honeybread.core.domain.store.validator.ProductValidationProperties;
import com.whatsub.honeybread.core.domain.store.validator.StoreValidator;
import com.whatsub.honeybread.core.domain.user.UserRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.store.dto.StoreBasicRequest;
import com.whatsub.honeybread.mgmtadmin.domain.store.dto.StoreCreateRequest;
import com.whatsub.honeybread.mgmtadmin.domain.store.dto.StoreUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
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

    @MockBean
    ProductValidationProperties productValidationProperties;

    @MockBean
    StoreValidator storeValidator;

    StoreCreateRequest 스토어_등록요청;

    Long 스토어_아이디 = 1000L;
    String 스토어명 = "등록할스토어";
    Store 스토어;

    StoreUpdateRequest 스토어_수정요청;

    long 저장된_스토어_아이디 = 1L;
    String 저장된_스토어명 = "스토어명";
    Store 저장된_스토어;

    int 카테고리_아이디_등록_최대개수 = 5;
    Set<Long> 카테고리_아이디_목록_최대개수_초과_요청;
    Set<Long> 카테고리_아이디_목록_비정상_요청;
    Set<Long> 카테고리_아이디_목록_정상_요청;
    List<Category> 카테고리_아이디_목록_비정상_요청에_대한_조회결과;
    List<Category> 카테고리_아이디_목록_정상_요청에_대한_조회결과;

    @BeforeEach
    void 스토어_등록_초기화() {
        스토어 = mock(Store.class);
        given(스토어.getId()).willReturn(스토어_아이디);
        given(스토어.getBasic()).willReturn(mock(StoreBasic.class));
        given(스토어.getBasic().getName()).willReturn(스토어명);

        스토어_등록요청 = mock(StoreCreateRequest.class);
        given(스토어_등록요청.getBasic()).willReturn(mock(StoreBasicRequest.class));
        given(스토어_등록요청.getBasic().getName()).willReturn(스토어명);
        given(스토어_등록요청.toEntity()).willReturn(스토어);
    }

    @BeforeEach
    void 스토어_수정_초기화() {
        저장된_스토어 = mock(Store.class);
        given(저장된_스토어.getBasic()).willReturn(mock(StoreBasic.class));
        given(저장된_스토어.getBasic().getName()).willReturn(저장된_스토어명);

        스토어_수정요청 = mock(StoreUpdateRequest.class);
        given(스토어_수정요청.getBasic()).willReturn(mock(StoreBasicRequest.class));
        given(스토어_수정요청.getBasic().getName()).willReturn(저장된_스토어명);
        given(스토어_수정요청.toEntity()).willReturn(스토어);
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

        카테고리_아이디_목록_최대개수_초과_요청 = LongStream.rangeClosed(1, 카테고리_아이디_등록_최대개수 + 1).boxed().collect(Collectors.toSet());
        카테고리_아이디_목록_비정상_요청 = Set.of(저장되어있는_카테고리_1_아이디, 저장되어있지_않은_카테고리_아이디);
        카테고리_아이디_목록_정상_요청 = Set.of(저장되어있는_카테고리_1_아이디, 저장되어있는_카테고리_2_아이디);
        카테고리_아이디_목록_비정상_요청에_대한_조회결과 = List.of(저장되어있는_카테고리_1);
        카테고리_아이디_목록_정상_요청에_대한_조회결과 = List.of(저장되어있는_카테고리_1, 저장되어있는_카테고리_2);
    }

    @Test
    void 존재하지_않는_셀러는_등록_실패() {
        // given
        유효성_검사_결과는_존재하지_않는_셀러이다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::등록한다);

        // then
        등록_유효성검사를_수행했다();
        이런_에러가_떨어졌다(exception, ErrorCode.USER_NOT_FOUND);
    }

    @Test
    void 이미_존재하는_스토어명은_등록_실패() {
        // given
        등록_유효성_검사_결과는_중복된_스토어명이다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::등록한다);

        // then
        등록_유효성검사를_수행했다();
        이런_에러가_떨어졌다(exception, ErrorCode.DUPLICATE_STORE_NAME);
    }

    @Test
    void 카테고리_ID_개수가_5개를_초과하여_등록_실패() {
        // given
        등록_유효성_검사_결과는_카테고리_개수_초과이다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::등록한다);

        // then
        등록_유효성검사를_수행했다();
        이런_에러가_떨어졌다(exception, ErrorCode.EXCEED_MAX_STORE_CATEGORY_CNT);
    }

    @Test
    void 카테고리ID가_하나라도_존재하지_않으면_등록_실패() {
        // given
        등록_유효성_검사_결과는_존재하지_않는_카테고리이다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::등록한다);

        // then
        등록_유효성검사를_수행했다();
        이런_에러가_떨어졌다(exception, ErrorCode.CATEGORY_NOT_FOUND);
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
        해당_스토어는_존재하지_않는다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::수정한다);

        // then
        이런_에러가_떨어졌다(exception, ErrorCode.STORE_NOT_FOUND);
    }

    @Test
    void 다른_스토어명으로_수정시_실패() {
        // given
        저장된_스토어가_조회된다();
        수정_유효성_검사_결과는_중복된_스토어명이다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::수정한다);

        // then
        수정_유효성검사를_수행했다();
        이런_에러가_떨어졌다(exception, ErrorCode.DUPLICATE_STORE_NAME);
    }

    @Test
    void 카테고리_ID_개수가_5개를_초과하여_수정_실패() {
        // given
        저장된_스토어가_조회된다();
        수정_유효성_검사_결과는_카테고리_개수_초과이다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::수정한다);

        // then
        수정_유효성검사를_수행했다();
        이런_에러가_떨어졌다(exception, ErrorCode.EXCEED_MAX_STORE_CATEGORY_CNT);
    }

    @Test
    void 하나라도_카테고리_ID가_존재하지_않아_수정_실패() {
        // given
        저장된_스토어가_조회된다();
        수정_유효성_검사_결과는_존재하지_않는_카테고리이다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::수정한다);

        // then
        수정_유효성검사를_수행했다();
        이런_에러가_떨어졌다(exception, ErrorCode.CATEGORY_NOT_FOUND);
    }

    @Test
    void 모든_카테고리_ID가_존재하여_수정_성공() {
        // given
        저장된_스토어가_조회된다();
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
        given(storeRepository.save(스토어_등록요청.toEntity())).willReturn(스토어);
    }

    private void 유효성_검사_결과는_존재하지_않는_셀러이다() {
        willThrow(new HoneyBreadException(ErrorCode.USER_NOT_FOUND))
                .given(storeValidator).validate(스토어);
    }

    private void 등록_유효성_검사_결과는_중복된_스토어명이다() {
        willThrow(new HoneyBreadException(ErrorCode.DUPLICATE_STORE_NAME))
                .given(storeValidator).validate(스토어);
    }

    private void 등록_유효성_검사_결과는_존재하지_않는_카테고리이다() {
        willThrow(new HoneyBreadException(ErrorCode.CATEGORY_NOT_FOUND))
                .given(storeValidator).validate(스토어);
    }

    private void 등록_유효성_검사_결과는_카테고리_개수_초과이다() {
        willThrow(new HoneyBreadException(ErrorCode.EXCEED_MAX_STORE_CATEGORY_CNT))
                .given(storeValidator).validate(스토어);
    }

    private void 유효한_등록_요청이다() {
        willDoNothing()
                .given(storeValidator).validate(스토어);
    }

    private void 스토어를_수정하면_엔티티가_반환된다() {
        given(storeRepository.save(스토어_수정요청.toEntity())).willReturn(스토어);
    }

    private void 해당_스토어는_존재하지_않는다() {
        given(storeRepository.findById(anyLong())).willReturn(Optional.empty());
    }

    private void 저장된_스토어가_조회된다() {
        given(storeRepository.findById(저장된_스토어_아이디)).willReturn(Optional.ofNullable(저장된_스토어));
    }

    private void 수정_유효성_검사_결과는_중복된_스토어명이다() {
        willThrow(new HoneyBreadException(ErrorCode.DUPLICATE_STORE_NAME))
                .given(storeValidator).validate(저장된_스토어, 스토어);
    }

    private void 수정_유효성_검사_결과는_존재하지_않는_카테고리이다() {
        willThrow(new HoneyBreadException(ErrorCode.CATEGORY_NOT_FOUND))
                .given(storeValidator).validate(저장된_스토어, 스토어);
    }

    private void 수정_유효성_검사_결과는_카테고리_개수_초과이다() {
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
        storeService.update(저장된_스토어_아이디, 스토어_수정요청);
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

    private void 이런_에러가_떨어졌다(HoneyBreadException exception, ErrorCode errorCode) {
        assertEquals(exception.getErrorCode(), errorCode);
    }

}
