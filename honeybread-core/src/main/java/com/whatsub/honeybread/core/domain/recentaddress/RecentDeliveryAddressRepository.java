package com.whatsub.honeybread.core.domain.recentaddress;

import com.whatsub.honeybread.core.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecentDeliveryAddressRepository extends JpaRepository<RecentDeliveryAddress, Long> {
    Optional<RecentDeliveryAddress> findByUserAndDeliveryAddressOrStateNameAddress(User user, String deliveryAddress, String stateNameAddress);
}
