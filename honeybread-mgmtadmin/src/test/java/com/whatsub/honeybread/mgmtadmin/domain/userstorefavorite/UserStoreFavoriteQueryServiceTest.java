package com.whatsub.honeybread.mgmtadmin.domain.userstorefavorite;

import com.whatsub.honeybread.core.domain.store.dto.StoreIdsResponse;
import com.whatsub.honeybread.core.domain.userstorefavorite.UserStoreFavorite;
import com.whatsub.honeybread.core.domain.userstorefavorite.UserStoreFavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
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
        final int 저장할_찜_사이즈 = 10;
        List<UserStoreFavorite> 찜_목록 = 찜_목록(저장할_찜_사이즈);
        given(repository.findByUserId(anyLong())).willReturn(찜_목록);

        // when
        StoreIdsResponse 조회_결과 = queryService.getStoresByUserId(1L);

        // then
        verify(repository).findByUserId(anyLong());
        assertThat(조회_결과.getStoreIds().size()).isEqualTo(저장할_찜_사이즈);
    }

    private List<UserStoreFavorite> 찜_목록(int size) {
        return LongStream.range(0, size)
                .mapToObj(id -> {
                    UserStoreFavorite mock = mock(UserStoreFavorite.class);
                    given(mock.getId()).willReturn(id);
                    return mock;
                })
                .collect(Collectors.toList());
    }

}