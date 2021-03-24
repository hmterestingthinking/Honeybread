package com.whatsub.honeybread.mgmtadmin.domain.user;

import com.whatsub.honeybread.core.domain.user.User;
import com.whatsub.honeybread.core.domain.user.UserRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.user.dto.UserModifyRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestConstructor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = UserService.class)
@RequiredArgsConstructor
class UserServiceTest {

    final UserService userService;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    UserRepository userRepository;

    @Test
    public void 중복되는_이메일이없다면_등록_성공() {
        //given
        final User user = createUser();
        given(userRepository.existsByEmail(user.getEmail())).willReturn(false);
        final String expectEncodedPassword = "encodedPassword";
        given(passwordEncoder.encode(user.getPassword())).willReturn(expectEncodedPassword);

        //when
        userService.register(user);

        //then
        verify(userRepository).existsByEmail(anyString());
        verify(passwordEncoder).encode(anyString());
        verify(userRepository).save(user);
        assertEquals(expectEncodedPassword, user.getPassword());
    }

    @Test
    public void 중복되는_이메일이있다면_등록_실패() {
        //given
        final User user = createUser();
        given(userRepository.existsByEmail(user.getEmail())).willReturn(true);

        //when
        HoneyBreadException honeyBreadException =
                assertThrows(HoneyBreadException.class, () -> userService.register(user));

        //then
        verify(userRepository).existsByEmail(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(user);
        assertEquals(ErrorCode.DUPLICATE_USER_EMAIL, honeyBreadException.getErrorCode());
    }
    
    @Test
    public void 등록되지_않은_유저_검색시_에러() {
        //given
        final long id = 1L;
        given(userRepository.findById(id))
                .willThrow(new HoneyBreadException(ErrorCode.USER_NOT_FOUND));

        //when
        HoneyBreadException honeyBreadException =
                assertThrows(HoneyBreadException.class, () -> userService.findById(id));

        //then
        assertEquals(ErrorCode.USER_NOT_FOUND, honeyBreadException.getErrorCode());
    }

    @Test
    public void 유저_정보_수정() {
        //given
        final long id = 1L;
        final User user = createUser();
        final UserModifyRequest userModifyRequest =
                new UserModifyRequest("changedPassword", "010-9999-9999", false, false);
        final String expectEncodedPassword = "encodedPassword";

        given(userRepository.findById(id)).willReturn(Optional.of(user));
        given(passwordEncoder.encode(userModifyRequest.getPassword())).willReturn(expectEncodedPassword);

        //when
        userService.update(id, userModifyRequest);

        //then
        verify(userRepository).findById(id);
        verify(passwordEncoder).encode(anyString());
        assertEquals(userModifyRequest.getPhoneNumber(), user.getPhoneNumber());
        assertEquals(userModifyRequest.isMarketingAgreement(), user.isMarketingAgreement());
        assertEquals(userModifyRequest.isSmsAgreement(), user.isSmsAgreement());
        assertEquals(expectEncodedPassword, user.getPassword());

    }
    
    @Test
    public void 유저_삭제() {
        //given
        final long id = 1L;
        final User user = createUser();

        given(userRepository.findById(id)).willReturn(Optional.of(user));

        //when
        userService.delete(id);

        //then
        verify(userRepository).delete(user);
    }

    private User createUser() {
        return User.createUser("test@honeybread.com", "testpasswd", "010-0000-0000", true, true);
    }


}