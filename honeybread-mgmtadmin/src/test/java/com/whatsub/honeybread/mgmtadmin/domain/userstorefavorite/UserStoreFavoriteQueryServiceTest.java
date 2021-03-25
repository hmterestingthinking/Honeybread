package com.whatsub.honeybread.mgmtadmin.domain.userstorefavorite;

import com.whatsub.honeybread.core.domain.store.Store;
import com.whatsub.honeybread.core.domain.store.dto.StoreResponse;
import com.whatsub.honeybread.core.domain.userstorefavorite.UserStoreFavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = UserStoreFavoriteQueryService.class)
@RequiredArgsConstructor
class UserStoreFavoriteQueryServiceTest {

    final UserStoreFavoriteQueryService queryService;

    @MockBean
    UserStoreFavoriteRepository repository;

    @Test
    void 목록_조회() {
        // given
        final int size = 10;
        final List<Store> 스토어_목록 = 스토어_목록(size);
        final List<Store> 조회될_스토어_목록 = 스토어_목록.subList(1, 2);

        PageRequest pageRequest = PageRequest.of(0, size);

        given(repository.getStoresByUserId(any(Pageable.class), anyLong()))
                .willReturn(new PageImpl<>(조회될_스토어_목록, pageRequest, 조회될_스토어_목록.size()));

        // when
        Page<StoreResponse> 조회_결과 = queryService.getStoresByUserId(pageRequest, Long.MIN_VALUE);

        // then
        verify(repository).getStoresByUserId(any(Pageable.class), anyLong());

        assertThat(조회_결과.getTotalElements()).isEqualTo(1);
        assertThat(조회_결과.getContent().size()).isEqualTo(1);
    }

    private List<Store> 스토어_목록(int size) {
        return LongStream.range(0, size)
                .mapToObj(id -> {
                    Store mock = mock(Store.class);
                    given(mock.getId()).willReturn(id);
                    return mock;
                })
                .collect(Collectors.toList());
    }

}