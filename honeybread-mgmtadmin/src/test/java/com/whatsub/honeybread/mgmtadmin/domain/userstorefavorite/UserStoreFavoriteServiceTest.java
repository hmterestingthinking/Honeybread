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
    void 스토어가_없으면_등록_실패() {
        // given
        해당_스토어는_없다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::찜_등록);

        // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.STORE_NOT_FOUND);
    }

    @Test
    void 유저가_스토어를_찜한적_없으면_등록_성공() {
        // given
        해당_스토어가_있다();
        찜한적_없다();
        given(repository.save(any(UserStoreFavorite.class))).willReturn(mock(UserStoreFavorite.class));

        // when
        찜_등록();

        // then
        verify(repository).existsByUserIdAndStoreId(anyLong(), anyLong());
        verify(repository).save(any(UserStoreFavorite.class));
    }

    @Test
    void 유저가_스토어를_찜한적_있으면_등록_실패() {
        // given
        해당_스토어가_있다();
        찜한적_있다();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, this::찜_등록);

        // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_USER_STORE_FAVORITE);
    }

    private void 찜한적_없다() {
        given(repository.existsByUserIdAndStoreId(anyLong(), anyLong())).willReturn(false);
    }

    private void 찜한적_있다() {
        given(repository.existsByUserIdAndStoreId(anyLong(), anyLong())).willReturn(true);
    }

    private void 해당_스토어는_없다() {
        given(storeRepository.existsById(anyLong())).willReturn(false);
    }

    private void 해당_스토어가_있다() {
        given(storeRepository.existsById(anyLong())).willReturn(true);
    }

    private void 찜_등록() {
        service.create(1L, 1L);
    }

}