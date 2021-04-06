package com.whatsub.honeybread.mgmtadmin.domain.recentaddress;

import com.whatsub.honeybread.core.domain.recentaddress.RecentDeliveryAddress;
import com.whatsub.honeybread.core.domain.recentaddress.RecentDeliveryAddressRepository;
import com.whatsub.honeybread.mgmtadmin.domain.recentaddress.dto.RecentDeliveryAddressResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = RecentDeliveryAddressQueryService.class)
@RequiredArgsConstructor
class RecentDeliveryAddressQueryServiceTest {

    final RecentDeliveryAddressQueryService service;

    @MockBean
    RecentDeliveryAddressRepository repository;

    @Mock
    List<RecentDeliveryAddress> mockList;

    @Test
    void UserId로_최근배달주소들_검색() {
        //given
        int expectSize = 10;
        사이즈만큼_최근배달주소들_검색(expectSize);

        //when
        List<RecentDeliveryAddressResponse> recentDeliveryAddresses =
            service.getRecentDeliveryAddressesByUserId(1L);

        //then
        UserId로_최근배달주소들_검색되어야함();
        assertEquals(expectSize, recentDeliveryAddresses.size());
    }

    private void 사이즈만큼_최근배달주소들_검색(int expectSize) {
        List<RecentDeliveryAddress> recentDeliveryAddresses = 사이즈만큼_최근배달주소_생성(expectSize);
        given(mockList.stream()).willReturn(Stream.of(recentDeliveryAddresses.toArray(RecentDeliveryAddress[]::new)));
        given(mockList.size()).willReturn(expectSize);
        given(repository.findAllByUserId(anyLong()))
            .willReturn(mockList);
    }

    private void UserId로_최근배달주소들_검색되어야함() {
        then(repository).should().findAllByUserId(anyLong());
    }

    private List<RecentDeliveryAddress> 사이즈만큼_최근배달주소_생성(int i) {
        return IntStream.range(0, i)
            .mapToObj((v) -> RecentDeliveryAddress.builder()
                .userId(1L)
                .deliveryAddress("서울시 강남구 수서동 500 301동 404호 " + v)
                .searchableDeliveryAddress("서울시 강남구 수서동")
                .stateNameAddress("서울시 강남구 광평로101길 200 301호 404호" + v)
                .zipCode("99999")
                .usedAt(LocalDateTime.now())
                .build())
            .collect(Collectors.toList());
    }
}