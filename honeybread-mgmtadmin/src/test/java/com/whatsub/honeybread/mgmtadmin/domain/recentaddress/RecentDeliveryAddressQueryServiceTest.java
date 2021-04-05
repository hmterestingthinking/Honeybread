package com.whatsub.honeybread.mgmtadmin.domain.recentaddress;

import com.whatsub.honeybread.core.domain.recentaddress.RecentDeliveryAddress;
import com.whatsub.honeybread.core.domain.recentaddress.RecentDeliveryAddressRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
        List<RecentDeliveryAddress> recentDeliveryAddresses =
            service.getRecentDeliveryAddressesByUserId(1L);

        //then
        UserId로_최근배달주소들_검색되어야함();
        assertEquals(expectSize, recentDeliveryAddresses.size());
    }

    private void 사이즈만큼_최근배달주소들_검색(int expectSize) {
        given(mockList.size()).willReturn(expectSize);
        given(repository.findAllByUserId(anyLong()))
            .willReturn(mockList);
    }

    private void UserId로_최근배달주소들_검색되어야함() {
        then(repository).should().findAllByUserId(anyLong());
    }
}