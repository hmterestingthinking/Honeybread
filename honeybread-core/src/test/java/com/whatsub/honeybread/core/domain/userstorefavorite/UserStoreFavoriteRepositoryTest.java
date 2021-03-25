package com.whatsub.honeybread.core.domain.userstorefavorite;

import com.whatsub.honeybread.core.domain.store.Store;
import com.whatsub.honeybread.core.domain.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DataJpaTest
@RequiredArgsConstructor
class UserStoreFavoriteRepositoryTest {

    private final long 유저아이디_1 = 1L;
    private final long 유저아이디_2 = 2L;
    private final long 스토어아이디_1 = 1L;
    private final long 스토어아이디_2 = 2L;

    private final StoreRepository storeRepository;
    private final UserStoreFavoriteRepository repository;

    @Test
    void 유저별로_저장된_찜정보만_조회_성공() {
        // given
        final int size = 10;

        final List<Store> 저장된_스토어 = getStores(size);
        storeRepository.saveAll(저장된_스토어);

        long 유저A_아이디 = 1L;
        int 유저A_추가할_찜_개수 = 2;
        long 유저B_아이디 = 2L;
        int 유저B_추가할_찜_개수 = 3;
        final List<UserStoreFavorite> 유저A_저장된_찜정보 = getUserStoreFavorites(유저A_아이디, 유저A_추가할_찜_개수);
        final List<UserStoreFavorite> 유저B_저장된_찜정보 = getUserStoreFavorites(유저B_아이디, 유저B_추가할_찜_개수);
        repository.saveAll(유저A_저장된_찜정보);
        repository.saveAll(유저B_저장된_찜정보);

        // when
        Page<Store> 유저A_조회결과 = repository.getStoresByUserId(PageRequest.of(0, size), 유저A_아이디);
        Page<Store> 유저B_조회결과 = repository.getStoresByUserId(PageRequest.of(0, size), 유저B_아이디);

        // then
        AssertionsForClassTypes.assertThat(유저A_조회결과.getTotalElements()).isEqualTo(유저A_추가할_찜_개수);
        AssertionsForClassTypes.assertThat(유저A_조회결과.getContent().get(0).getId()).isEqualTo(유저A_아이디);
        AssertionsForClassTypes.assertThat(유저B_조회결과.getTotalElements()).isEqualTo(유저B_추가할_찜_개수);
        AssertionsForClassTypes.assertThat(유저B_조회결과.getContent().get(0).getId()).isEqualTo(유저B_아이디);
    }

    private List<Store> getStores(final int size) {
        return IntStream.range(0, size)
                .mapToObj(value -> Store.newStore())
                .collect(Collectors.toList());
    }

    private List<UserStoreFavorite> getUserStoreFavorites(Long userId, int storeIdSize) {
        return LongStream.range(0, storeIdSize)
                .mapToObj(value -> new UserStoreFavorite(userId, value + 1))
                .collect(Collectors.toList());
    }

    @Test
    void 유저아이디와_스토어아이디가_둘다_일치해야_조회_성공() {
        // given
        repository.save(new UserStoreFavorite(유저아이디_1, 스토어아이디_1));

        // when
        boolean 찜_존재_여부 = repository.existsByUserIdAndStoreId(유저아이디_1, 스토어아이디_1);

        // then
        assertThat(찜_존재_여부).isTrue();
    }

    @Test
    void 찜정보를_등록한적_없으면_조회되지_않음() {
        // given

        // when
        boolean 찜_존재_여부 = repository.existsByUserIdAndStoreId(유저아이디_1, 스토어아이디_1);

        // then
        assertThat(찜_존재_여부).isFalse();
    }

    @Test
    void 스토어아이디가_다르면_유저의_스토어_찜정보가_조회되지_않음() {
        // given
        repository.save(new UserStoreFavorite(유저아이디_1, 스토어아이디_1));

        // when
        boolean 찜_존재_여부 = repository.existsByUserIdAndStoreId(유저아이디_1, 스토어아이디_2);

        // then
        assertThat(찜_존재_여부).isFalse();
    }

    @Test
    void 유저아이디가_다르면_유저의_스토어_찜정보가_조회되지_않음() {
        // given
        repository.save(new UserStoreFavorite(유저아이디_1, 스토어아이디_1));

        // when
        boolean 찜_존재_여부 = repository.existsByUserIdAndStoreId(유저아이디_2, 스토어아이디_1);

        // then
        assertThat(찜_존재_여부).isFalse();
    }

    @Test
    void 유저아이디와_스토어아이디가_다르면_유저의_스토어_찜정보가_조회되지_않음() {
        // given
        repository.save(new UserStoreFavorite(유저아이디_1, 스토어아이디_1));

        // when
        boolean 찜_존재_여부 = repository.existsByUserIdAndStoreId(유저아이디_2, 스토어아이디_2);

        // then
        assertThat(찜_존재_여부).isFalse();
    }

}