package com.whatsub.honeybread.core.domain.recentaddress;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecentDeliveryAddressRepository extends JpaRepository<RecentDeliveryAddress, Long> {
    Optional<RecentDeliveryAddress> findByUserIdAndDeliveryAddressOrStateNameAddress(Long userId, String deliveryAddress, String stateNameAddress);
}
