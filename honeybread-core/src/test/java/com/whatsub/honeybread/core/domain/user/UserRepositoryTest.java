package com.whatsub.honeybread.core.domain.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DataJpaTest
@RequiredArgsConstructor
class UserRepositoryTest {

    final UserRepository userRepository;

    @Test
    public void 중복되는_이메일이_있는지_확인() {
        //given
        final User user = createUser();
        userRepository.save(user);

        //when
        boolean result = userRepository.existsByEmail(user.getEmail());

        //then
        assertTrue(result);
    }

    @Test
    public void 이메일로_찾기() {
        //given
        final User user = createUser();
        userRepository.save(user);

        //when
        Optional<User> findUser = userRepository.findByEmail(user.getEmail());

        //then
        assertTrue(findUser.isPresent());
        assertEquals(user, findUser.get());
    }

    private User createUser() {
        return User.createUser("test@honeybread.com",
                "testpasswd",
                "010-0000-0000",
                true,
                true);
    }

}