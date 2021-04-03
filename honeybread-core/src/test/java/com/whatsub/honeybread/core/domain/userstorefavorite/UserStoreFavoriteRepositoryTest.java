package com.whatsub.honeybread.core.domain.userstorefavorite;

import com.whatsub.honeybread.core.domain.store.Address;
import com.whatsub.honeybread.core.domain.store.BankAccount;
import com.whatsub.honeybread.core.domain.store.BusinessHours;
import com.whatsub.honeybread.core.domain.store.BusinessLicense;
import com.whatsub.honeybread.core.domain.store.Store;
import com.whatsub.honeybread.core.domain.store.StoreAnnouncement;
import com.whatsub.honeybread.core.domain.store.StoreBasic;
import com.whatsub.honeybread.core.domain.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;

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
        final int 저장할_스토어_전체_개수 = 10;
        final List<Store> 저장된_스토어_목록 = 사이즈만큼_스토어_저장하기(저장할_스토어_전체_개수);
        storeRepository.saveAll(저장된_스토어_목록);

        Long 첫번째_스토어 = 저장된_스토어_목록.get(0).getId();
        Long 두번째_스토어 = 저장된_스토어_목록.get(1).getId();
        Long 세번째_스토어 = 저장된_스토어_목록.get(2).getId();
        Long 네번째_스토어 = 저장된_스토어_목록.get(3).getId();
        Long 다섯번째_스토어 = 저장된_스토어_목록.get(4).getId();

        long 유저A_아이디 = 1L;
        long 유저B_아이디 = 2L;

        UserStoreFavorite 유저A의_첫번째_스토어_찜정보 = new UserStoreFavorite(유저A_아이디, 첫번째_스토어);
        UserStoreFavorite 유저A의_두번째_스토어_찜정보 = new UserStoreFavorite(유저A_아이디, 두번째_스토어);
        repository.save(유저A의_첫번째_스토어_찜정보);
        repository.save(유저A의_두번째_스토어_찜정보);
        UserStoreFavorite 유저B의_두번째_스토어_찜정보 = new UserStoreFavorite(유저B_아이디, 두번째_스토어);
        UserStoreFavorite 유저B의_세번째_스토어_찜정보 = new UserStoreFavorite(유저B_아이디, 세번째_스토어);
        UserStoreFavorite 유저B의_네번째_스토어_찜정보 = new UserStoreFavorite(유저B_아이디, 네번째_스토어);
        repository.save(유저B의_두번째_스토어_찜정보);
        repository.save(유저B의_세번째_스토어_찜정보);
        repository.save(유저B의_네번째_스토어_찜정보);

        // when
        List<UserStoreFavorite> 유저A_조회된_찜목록 = repository.findAllByUserId(유저A_아이디);
        List<UserStoreFavorite> 유저B_조회된_찜목록 = repository.findAllByUserId(유저B_아이디);

        // then
        assertThat(유저A_조회된_찜목록.size()).isEqualTo(2);
        assertThat(유저A_조회된_찜목록.get(0)).isEqualTo(유저A의_첫번째_스토어_찜정보);
        assertThat(유저A_조회된_찜목록.get(1)).isEqualTo(유저A의_두번째_스토어_찜정보);

        assertThat(유저B_조회된_찜목록.size()).isEqualTo(3);
        assertThat(유저B_조회된_찜목록.get(0)).isEqualTo(유저B의_두번째_스토어_찜정보);
        assertThat(유저B_조회된_찜목록.get(1)).isEqualTo(유저B의_세번째_스토어_찜정보);
        assertThat(유저B_조회된_찜목록.get(2)).isEqualTo(유저B의_네번째_스토어_찜정보);
    }

    private List<Store> 사이즈만큼_스토어_저장하기(final int size) {
        return IntStream.range(0, size)
                .mapToObj(this::스토어_가져오기)
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

    Store 스토어_가져오기(int value) {
        return Store.createStore(
                anyLong(),
                new StoreBasic(anyString(),
                        anyString(),
                        anyString(),
                        new Address(anyString()),
                        new StoreAnnouncement(anyString(), anyString(), anyString()),
                        new BusinessHours(anyString(), anyString(), anyString()),
                        new BusinessLicense(anyString())
                ),
                new BankAccount(any(), anyString()),
                anyList(),
                anyList()
        );
    }

}