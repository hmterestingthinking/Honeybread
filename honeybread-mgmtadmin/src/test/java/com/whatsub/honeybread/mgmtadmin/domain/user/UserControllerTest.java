package com.whatsub.honeybread.mgmtadmin.domain.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsub.honeybread.mgmtadmin.domain.user.dto.UserRequest;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    public void 유저_등록하기() throws Exception {
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
    public void 유저_등록시_유효성검사에_실패한다면_에러() throws Exception {
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

}