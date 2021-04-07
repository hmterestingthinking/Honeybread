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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = StoreService.class)
@RequiredArgsConstructor
public class StoreServiceUpdateTest {

    final StoreService storeService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    StoreRepository storeRepository;

    @MockBean
    CategoryRepository categoryRepository;

    StoreUpdateRequest 스토어_수정요청;

    long 저장된_스토어_아이디 = 1L;
    String 저장된_스토어의_이름 = "저장된 스토어의 이름";
    Store 저장된_스토어;

    String 저장되어있는_다른_스토어명 = "다른 스토어명";

    Set<Long> 카테고리_아이디_목록_비정상_요청;
    Set<Long> 카테고리_아이디_목록_정상_요청;
    List<Category> 카테고리_아이디_목록_비정상_요청에_대한_조회결과;
    List<Category> 카테고리_아이디_목록_정상_요청에_대한_조회결과;

    @BeforeEach
    void 스토어_셋업() {
        저장된_스토어 = mock(Store.class);
        given(저장된_스토어.getBasic()).willReturn(mock(StoreBasic.class));
        given(저장된_스토어.getBasic().getName()).willReturn(저장된_스토어의_이름);

        스토어_수정요청 = mock(StoreUpdateRequest.class);

        given(스토어_수정요청.getBasic()).willReturn(mock(StoreBasicRequest.class));
        given(스토어_수정요청.getBasic().getName()).willReturn(저장된_스토어의_이름);
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
    void 스토어_존재하지_않으면_수정_실패() {
        // given
        해당_스토어는_존재하지_않는다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::수정한다);

        // then
        수정했더니_이런_에러가_떨어졌다(exception, ErrorCode.STORE_NOT_FOUND);
    }

    @Test
    void 다른_스토어명으로_수정시_실패() {
        // given
        저장된_스토어가_조회된다();
        다른_스토어가_사용중인_스토어명이다();
        다른_스토어의_이름으로_수정을_요청한다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::수정한다);

        // then
        스토어가_존재하는지_검사를_수행했다();
        이름으로_스토어_조회를_수행했다();
        수정했더니_이런_에러가_떨어졌다(exception, ErrorCode.DUPLICATE_STORE_NAME);
    }

    @Test
    void 스토어명_그대로_수정시_중복이라고_판단하지_않음() {
        // given
        저장된_스토어가_조회된다();

        // when
        수정한다();

        // then
        스토어가_존재하는지_검사를_수행했다();
        이름으로_스토어_조회를_수행하지_않았다();
    }

    @Test
    void 유니크한_이름으로_스토어명_수정시_중복이라고_판단하지_않음() {
        // given
        저장된_스토어가_조회된다();
        지금까지_이런_스토어명은_없었다("정말 맛없는 가게");

        // when
        수정한다();

        // then
        스토어가_존재하는지_검사를_수행했다();
        이름으로_스토어_조회를_수행했다();
    }

    @Test
    void 하나라도_카테고리_ID가_존재하지_않아_수정_실패() {
        // given
        저장된_스토어가_조회된다();
        일부_존재하지않는_카테고리아이디로_수정요청을_한다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::수정한다);

        // then
        스토어가_존재하는지_검사를_수행했다();
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
        수정한다();

        // then
        스토어가_존재하는지_검사를_수행했다();
        이름으로_스토어_조회를_수행하지_않았다();
        카테고리아이디로_카테고리목록을_찾는로직을_수행했다();
        then(저장된_스토어).should().update(스토어_수정요청.toEntity());
    }

    /**
     * given
     */

    private void 해당_스토어는_존재하지_않는다() {
        given(storeRepository.findById(anyLong())).willReturn(Optional.empty());
    }

    private void 저장된_스토어가_조회된다() {
        given(storeRepository.findById(저장된_스토어_아이디)).willReturn(Optional.ofNullable(저장된_스토어));
    }

    private void 다른_스토어의_이름으로_수정을_요청한다() {
        given(스토어_수정요청.getBasic().getName()).willReturn(저장되어있는_다른_스토어명);
    }

    private void 다른_스토어가_사용중인_스토어명이다() {
        given(storeRepository.existsByBasicName(저장되어있는_다른_스토어명)).willReturn(true);
    }

    private void 지금까지_이런_스토어명은_없었다(String uniqueName) {
        given(스토어_수정요청.getBasic().getName()).willReturn(uniqueName);
    }

    private void 일부_존재하지않는_카테고리아이디로_수정요청을_한다() {
        given(스토어_수정요청.getCategoryIds()).willReturn(카테고리_아이디_목록_비정상_요청);
        given(categoryRepository.findAllById(카테고리_아이디_목록_비정상_요청)).willReturn(카테고리_아이디_목록_비정상_요청에_대한_조회결과);
    }

    private void 모두_존재하는_카테고리아이디로_수정요청을_한다() {
        given(스토어_수정요청.getCategoryIds()).willReturn(카테고리_아이디_목록_정상_요청);
        given(categoryRepository.findAllById(카테고리_아이디_목록_정상_요청)).willReturn(카테고리_아이디_목록_정상_요청에_대한_조회결과);
    }

    /**
     * when
     */

    private void 수정한다() {
        storeService.update(저장된_스토어_아이디, 스토어_수정요청);
    }

    /**
     * then
     */

    private void 스토어가_존재하는지_검사를_수행했다() {
        then(storeRepository).should().findById(anyLong());
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

    private void 수정했더니_이런_에러가_떨어졌다(HoneyBreadException exception, ErrorCode storeNotFound) {
        then(exception.getErrorCode()).equals(storeNotFound);
    }
}
