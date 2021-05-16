package com.whatsub.honeybread.mgmtadmin.domain.advertisement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsub.honeybread.core.domain.advertisement.Advertisement;
import com.whatsub.honeybread.core.domain.advertisement.AdvertisementType;
import com.whatsub.honeybread.core.domain.advertisement.dto.AdvertisementSearch;
import com.whatsub.honeybread.core.domain.advertisement.repository.AdvertisementRepository;
import com.whatsub.honeybread.core.domain.model.TimePeriod;
import com.whatsub.honeybread.mgmtadmin.domain.advertisement.dto.AdvertisementResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@WebMvcTest(AdvertisementController.class)
@RequiredArgsConstructor
class AdvertisementControllerTest {
    static final String BASE_URL = "/advertisements";

    final MockMvc mockMvc;
    final ObjectMapper mapper;

    @MockBean
    AdvertisementQueryService queryService;

    @Test
    void 광고_목록_조회_성공() throws Exception {
        // given
        final int size = 10;
        광고목록_조회시_성공한다(size);

        // when
        final ResultActions result = mockMvc.perform(
            get(BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isNotEmpty())
            .andExpect(jsonPath("$.content.length()").value(size))
            .andExpect(jsonPath("$.content[0].id").exists())
            .andExpect(jsonPath("$.content[0].type").exists())
            .andExpect(jsonPath("$.content[0].maximumStoreCounts").exists())
            .andExpect(jsonPath("$.content[0].advertisedStores").exists())
            .andExpect(jsonPath("$.content[0].period").exists())
        ;
    }

    /**
     * Given
     */
    private void 광고목록_조회시_성공한다(final int size) {
        final List<AdvertisementResponse> response = 광고목록_생성(size);
        given(queryService.getAdvertisements(any(Pageable.class), any(AdvertisementSearch.class)))
            .willReturn(new PageImpl<>(response, PageRequest.of(0, size), response.size()));
    }

    /**
     * When
     */

    /**
     * Then
     */

    /**
     * Helper
     */
    private List<AdvertisementResponse> 광고목록_생성(final int size) {
        return LongStream.range(0, size)
            .mapToObj(this::광고생성)
            .collect(Collectors.toList());
    }

    private AdvertisementResponse 광고생성(final long id) {
        return AdvertisementResponse.builder()
            .id(id)
            .type(AdvertisementType.OPEN_LIST)
            .maximumStoreCounts(50)
            .period(   TimePeriod.of(
                LocalDateTime.of(2021, 4, 16, 12, 0, 0),
                LocalDateTime.of(2021, 5, 16, 12, 0, 0)
            ))
            .advertisedStores(Collections.emptyList())
            .build();
    }
}