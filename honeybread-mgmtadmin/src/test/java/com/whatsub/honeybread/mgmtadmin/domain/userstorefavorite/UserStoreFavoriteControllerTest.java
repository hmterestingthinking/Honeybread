package com.whatsub.honeybread.mgmtadmin.domain.userstorefavorite;

import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserStoreFavoriteController.class)
class UserStoreFavoriteControllerTest {
    static final String BASE_URL = "/stores/favorites";
    static final String CREATE_URL = BASE_URL + "/" + 1L;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserStoreFavoriteService userStoreFavoriteService;

    @Test
    void 해당_스토어도_존재하고_찜한적도_없다면_등록_성공() throws Exception {
        given(userStoreFavoriteService.create(anyLong(), anyLong())).willReturn(Long.MIN_VALUE);
        ResultActions response = mockMvc.perform(post(CREATE_URL)).andDo(print());

        verify(userStoreFavoriteService).create(anyLong(), anyLong());
        response.andExpect(status().isCreated());
    }

    @Test
    void 해당_스토어도_존재하지_않다면_등록_실패() throws Exception {
        given(userStoreFavoriteService.create(anyLong(), anyLong())).willAnswer(mock -> {
            throw new HoneyBreadException(ErrorCode.STORE_NOT_FOUND);
        });

        // when
        ResultActions response = mockMvc.perform(post(CREATE_URL)).andDo(print());

        // then
        verify(userStoreFavoriteService).create(anyLong(), anyLong());
        response.andExpect(status().isNotFound());
    }

    @Test
    void 해당_스토어도_존재하는데_이미_찜했다면_등록_실패() throws Exception {
        given(userStoreFavoriteService.create(anyLong(), anyLong())).willAnswer(mock -> {
            throw new HoneyBreadException(ErrorCode.DUPLICATE_USER_STORE_FAVORITE);
        });

        // when
        ResultActions response = mockMvc.perform(post(CREATE_URL)).andDo(print());

        // then
        verify(userStoreFavoriteService).create(anyLong(), anyLong());
        response.andExpect(status().isConflict());
    }

}