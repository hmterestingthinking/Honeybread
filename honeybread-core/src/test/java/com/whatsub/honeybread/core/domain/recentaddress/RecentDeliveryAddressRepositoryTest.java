package com.whatsub.honeybread.core.domain.recentaddress;

import com.whatsub.honeybread.core.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DataJpaTest
@RequiredArgsConstructor
class RecentDeliveryAddressRepositoryTest {

    final RecentDeliveryAddressRepository repository;
    final UserRepository userRepository;

    @Test
    void 배달주소로_최근배달주소검색() {
        //given
        final long userId = 1L;
        final String deliveryAddress = "서울시 강남구 수서동 500 301동 404호";

        final RecentDeliveryAddress recentDeliveryAddress = RecentDeliveryAddress.builder()
            .userId(userId)
            .deliveryAddress(deliveryAddress)
            .searchableDeliveryAddress("서울시 강남구 수서동")
            .stateNameAddress("서울시 강남구 광평로101길 200 301호 404호")
            .zipCode("99999")
            .usedAt(LocalDateTime.now())
            .build();

        repository.save(recentDeliveryAddress);

        //when
        Optional<RecentDeliveryAddress> findRecentDeliveryAddress =
            repository.findByUserIdAndDeliveryAddressOrStateNameAddress(userId, deliveryAddress, "");

        //then
        assertTrue(findRecentDeliveryAddress.isPresent());
        assertEquals(recentDeliveryAddress, findRecentDeliveryAddress.get());
    }

    @Test
    void 도로명주소로로_최근배달주소검색() {
        //given
        final long userId = 1L;
        final String stateNameAddress = "서울시 강남구 광평로101길 200 301호 404호";

        final RecentDeliveryAddress recentDeliveryAddress = RecentDeliveryAddress.builder()
            .userId(userId)
            .deliveryAddress("서울시 강남구 수서동 500 301동 404호")
            .searchableDeliveryAddress("서울시 강남구 수서동")
            .stateNameAddress(stateNameAddress)
            .zipCode("99999")
            .usedAt(LocalDateTime.now())
            .build();

        repository.save(recentDeliveryAddress);

        //when
        Optional<RecentDeliveryAddress> findRecentDeliveryAddress
            = repository.findByUserIdAndDeliveryAddressOrStateNameAddress(userId, "", stateNameAddress);

        //then
        assertTrue(findRecentDeliveryAddress.isPresent());
        assertEquals(recentDeliveryAddress, findRecentDeliveryAddress.get());
    }

    @Test
    void UserId로_최근배달주소들_검색() {
        //given
        final long userId = 1L;
        사이즈만큼_최근배달주소_등록(10, userId);

        //when
        List<RecentDeliveryAddress> recentDeliveryAddresses = repository.findAllByUserId(userId);

        //then
        assertEquals(10, recentDeliveryAddresses.size());
    }

    private void 사이즈만큼_최근배달주소_등록(int i, long userId) {
        IntStream.range(0, i)
            .mapToObj((v) -> RecentDeliveryAddress.builder()
                .userId(userId)
                .deliveryAddress("서울시 강남구 수서동 500 301동 404호 " + v)
                .searchableDeliveryAddress("서울시 강남구 수서동")
                .stateNameAddress("서울시 강남구 광평로101길 200 301호 404호" + v)
                .zipCode("99999")
                .usedAt(LocalDateTime.now())
                .build())
            .forEach(repository::save);
    }
}