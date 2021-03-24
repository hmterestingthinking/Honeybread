package com.whatsub.honeybread.core.domain.userstorefavorite;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserStoreFavoriteRepositoryTest {

    private final long 유저아이디_1 = 1L;
    private final long 유저아이디_2 = 2L;
    private final long 스토어아이디_1 = 1L;
    private final long 스토어아이디_2 = 2L;

    @Autowired
    private UserStoreFavoriteRepository userStoreFavoriteRepository;

    @Test
    void 유저아이디와_스토어아이디가_둘다_일치해야_조회_성공() {
        // given
        userStoreFavoriteRepository.save(new UserStoreFavorite(유저아이디_1, 스토어아이디_1));

        // when
        boolean 찜_존재_여부 = userStoreFavoriteRepository.existsByUserIdAndStoreId(유저아이디_1, 스토어아이디_1);

        // then
        assertThat(찜_존재_여부).isTrue();
    }

    @Test
    void 찜정보를_등록한적_없으면_조회되지_않음() {
        // given

        // when
        boolean 찜_존재_여부 = userStoreFavoriteRepository.existsByUserIdAndStoreId(유저아이디_1, 스토어아이디_1);

        // then
        assertThat(찜_존재_여부).isFalse();
    }

    @Test
    void 스토어아이디가_다르면_유저의_스토어_찜정보가_조회되지_않음() {
        // given
        userStoreFavoriteRepository.save(new UserStoreFavorite(유저아이디_1, 스토어아이디_1));

        // when
        boolean 찜_존재_여부 = userStoreFavoriteRepository.existsByUserIdAndStoreId(유저아이디_1, 스토어아이디_2);

        // then
        assertThat(찜_존재_여부).isFalse();
    }

    @Test
    void 유저아이디가_다르면_유저의_스토어_찜정보가_조회되지_않음() {
        // given
        userStoreFavoriteRepository.save(new UserStoreFavorite(유저아이디_1, 스토어아이디_1));

        // when
        boolean 찜_존재_여부 = userStoreFavoriteRepository.existsByUserIdAndStoreId(유저아이디_2, 스토어아이디_1);

        // then
        assertThat(찜_존재_여부).isFalse();
    }

    @Test
    void 유저아이디와_스토어아이디가_다르면_유저의_스토어_찜정보가_조회되지_않음() {
        // given
        userStoreFavoriteRepository.save(new UserStoreFavorite(유저아이디_1, 스토어아이디_1));

        // when
        boolean 찜_존재_여부 = userStoreFavoriteRepository.existsByUserIdAndStoreId(유저아이디_2, 스토어아이디_2);

        // then
        assertThat(찜_존재_여부).isFalse();
    }

}