package com.whatsub.honeybread.mgmtadmin.domain.userstorefavorite;

import com.whatsub.honeybread.core.domain.store.StoreRepository;
import com.whatsub.honeybread.core.domain.userstorefavorite.UserStoreFavorite;
import com.whatsub.honeybread.core.domain.userstorefavorite.UserStoreFavoriteRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = UserStoreFavoriteService.class)
@RequiredArgsConstructor
class UserStoreFavoriteServiceTest {

    final UserStoreFavoriteService service;

    @MockBean
    UserStoreFavoriteRepository repository;

    @MockBean
    StoreRepository storeRepository;

    @Test
    void 존재하지_않는_스토어면_등록_실패() {
        // given
        존재하지_않는_스토어();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::찜_등록);

        // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.STORE_NOT_FOUND);
    }

    @Test
    void 존재하는_스토어_그리고_찜한적_없으면_등록_성공() {
        // given
        존재하는_스토어();
        찜한적_없음();
        given(repository.save(any(UserStoreFavorite.class))).willReturn(mock(UserStoreFavorite.class));

        // when
        찜_등록();

        // then
        verify(repository).existsByUserIdAndStoreId(anyLong(), anyLong());
        verify(repository).save(any(UserStoreFavorite.class));
    }

    @Test
    void 존재하는_스토어지만_이미_찜했기에_등록_실패() {
        // given
        존재하는_스토어();
        찜했음();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::찜_등록);

        // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_USER_STORE_FAVORITE);
    }

    @Test
    void 존재하지_않는_스토어면_찜삭제_실패() {
        // given
        존재하지_않는_스토어();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::찜_삭제);

        // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.STORE_NOT_FOUND);
    }

    @Test
    void 찜한적_없으면_찜삭제_실패() {
        // given
        존재하는_스토어();
        찜한적_없음();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::찜_삭제);

        // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_STORE_FAVORITE_NOT_FOUND);
    }

    @Test
    void 찜한적_있으면_찜삭제_성공() {
        // given
        존재하는_스토어();
        찜했음();

        // when
        찜_삭제();

        // then
        verify(repository).deleteByUserIdAndStoreId(anyLong(), anyLong());
    }

    private void 존재하지_않는_스토어() {
        given(storeRepository.existsById(anyLong())).willReturn(false);
    }

    private void 존재하는_스토어() {
        given(storeRepository.existsById(anyLong())).willReturn(true);
    }

    private void 찜한적_없음() {
        given(repository.existsByUserIdAndStoreId(anyLong(), anyLong())).willReturn(false);
    }

    private void 찜했음() {
        given(repository.existsByUserIdAndStoreId(anyLong(), anyLong())).willReturn(true);
    }

    private void 찜_등록() {
        service.create(1L, 1L);
    }

    private void 찜_삭제() {
        service.delete(1L, 1L);
    }

}