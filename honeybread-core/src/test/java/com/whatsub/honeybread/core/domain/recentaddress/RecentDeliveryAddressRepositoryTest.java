package com.whatsub.honeybread.core.domain.recentaddress;

import com.whatsub.honeybread.core.domain.user.User;
import com.whatsub.honeybread.core.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

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
        User user = createUser();
        userRepository.save(user);

        String deliveryAddress = "서울시 강남구 수서동 500 301동 404호";

        RecentDeliveryAddress recentDeliveryAddress = RecentDeliveryAddress.builder()
            .user(user)
            .deliveryAddress(deliveryAddress)
            .searchableDeliveryAddress("서울시 강남구 수서동")
            .stateNameAddress("서울시 강남구 광평로101길 200 301호 404호")
            .zipCode("99999")
            .usedAt(LocalDateTime.now())
            .build();

        repository.save(recentDeliveryAddress);

        //when
        Optional<RecentDeliveryAddress> findRecentDeliveryAddress =
            repository.findByUserAndDeliveryAddressOrStateNameAddress(user, deliveryAddress, "");

        //then
        assertTrue(findRecentDeliveryAddress.isPresent());
        assertEquals(recentDeliveryAddress, findRecentDeliveryAddress.get());
    }

    @Test
    void 도로명주소로로_최근배달주소검색() {
        //given
        User user = createUser();
        userRepository.save(user);

        String stateNameAddress = "서울시 강남구 광평로101길 200 301호 404호";

        RecentDeliveryAddress recentDeliveryAddress = RecentDeliveryAddress.builder()
            .user(user)
            .deliveryAddress("서울시 강남구 수서동 500 301동 404호")
            .searchableDeliveryAddress("서울시 강남구 수서동")
            .stateNameAddress(stateNameAddress)
            .zipCode("99999")
            .usedAt(LocalDateTime.now())
            .build();

        repository.save(recentDeliveryAddress);

        //when
        Optional<RecentDeliveryAddress> findRecentDeliveryAddress
            = repository.findByUserAndDeliveryAddressOrStateNameAddress(user, "", stateNameAddress);

        //then
        assertTrue(findRecentDeliveryAddress.isPresent());
        assertEquals(recentDeliveryAddress, findRecentDeliveryAddress.get());
    }

    private User createUser() {
        return User.createUser("test@honeybread.com",
            "testpasswd",
            "010-0000-0000",
            true,
            true);
    }
}