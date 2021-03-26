package com.whatsub.honeybread.mgmtadmin.domain.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsub.honeybread.core.domain.user.User;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.user.dto.UserModifyRequest;
import com.whatsub.honeybread.mgmtadmin.domain.user.dto.UserRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@WebMvcTest(UserController.class)
@RequiredArgsConstructor
class UserControllerTest {

    static final String BASE_URL = "/users";
    final MockMvc mockMvc;
    final UserController userController;
    final ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @Test
    void 유저_등록() throws Exception {
        //given
        final UserRequest userRequest =
                new UserRequest("test@honeybread.com", "qwer1234!", "010-9999-9999", false, false);

        //when
        ResultActions resultActions = mockMvc.perform(
                post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andDo(print());

        //then
        verify(userService).register(any());
        resultActions.andExpect(status().isCreated());
    }

    @Test
    void 유저_등록시_유효성검사에_실패한다면_에러() throws Exception {
        //given
        final UserRequest userRequest =
                new UserRequest("test#honeybread.com", "changedPassword", "010-99-9999", false, false);

        //when
        ResultActions resultActions = mockMvc.perform(
                post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andDo(print());

        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(3)));
    }

    @Test
    void 유저_수정() throws Exception {
        //given
        final long id = 1L;
        final UserModifyRequest userModifyRequest =
                new UserModifyRequest("qwer1234!", "010-9999-9999", false, false);

        //when
        ResultActions resultActions = mockMvc.perform(
                put(BASE_URL + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userModifyRequest)))
                .andDo(print());

        //then
        verify(userService).update(id, userModifyRequest);
        resultActions.andExpect(status().isOk());
    }

    @Test
    void 유저_수정시_유효성검사에_실패한다면_에러() throws Exception {
        //given
        final long id = 1L;
        final UserModifyRequest userModifyRequest =
                new UserModifyRequest("qwerasdf", "010-999-9999", false, false);

        //when
        ResultActions resultActions = mockMvc.perform(
                put(BASE_URL + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userModifyRequest)))
                .andDo(print());

        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(2)));
    }
    
    @Test
    void 유저_삭제() throws Exception {
        //given
        final long id = 1L;

        //when
        ResultActions resultActions = mockMvc.perform(
                delete(BASE_URL + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        verify(userService).delete(id);
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    void 유저_삭제시_없을경우_에러() throws Exception {
        //given
        final long id = 1L;

        willThrow(new HoneyBreadException(ErrorCode.USER_NOT_FOUND))
                .given(userService).delete(id);

        //when
        ResultActions resultActions = mockMvc.perform(
                delete(BASE_URL + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        verify(userService).delete(id);
        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(ErrorCode.USER_NOT_FOUND.getMessage())));
    }

    @Test
    void 유저_조회() throws Exception {
        //given
        final long id = 1L;
        final User user = createUser();

        given(userService.findById(id)).willReturn(user);

        //when
        ResultActions resultActions = mockMvc.perform(
                get(BASE_URL + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        verify(userService).findById(id);
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.phoneNumber", is(user.getPhoneNumber())))
                .andExpect(jsonPath("$.smsAgreement", is(user.isSmsAgreement())))
                .andExpect(jsonPath("$.marketingAgreement", is(user.isMarketingAgreement())));
    }

    @Test
    void 유저_조회시_없을경우_에러() throws Exception {
        //given
        final long id = 1L;

        given(userService.findById(id)).willThrow(new HoneyBreadException(ErrorCode.USER_NOT_FOUND));

        //when
        ResultActions resultActions = mockMvc.perform(
                get(BASE_URL + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        verify(userService).findById(id);
        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(ErrorCode.USER_NOT_FOUND.getMessage())));
    }

    private User createUser() {
        return User.createUser("test@honeybread.com", "testpasswd", "010-0000-0000", true, true);
    }
}