package com.whatsub.honeybread.mgmtadmin.domain.advertisement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsub.honeybread.core.domain.advertisement.repository.AdvertisementRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;


@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@WebMvcTest(AdvertisementBidController.class)
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

        // when

        // then
    }
}