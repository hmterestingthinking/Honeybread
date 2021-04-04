package com.whatsub.honeybread.mgmtadmin.domain.recentaddress;

import com.whatsub.honeybread.core.domain.recentaddress.RecentDeliveryAddress;
import com.whatsub.honeybread.core.domain.recentaddress.RecentDeliveryAddressRepository;
import com.whatsub.honeybread.mgmtadmin.domain.recentaddress.dto.RecentDeliveryAddressServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RecentDeliveryAddressService {

    private final RecentDeliveryAddressRepository repository;

    @Transactional
    public void createIfAbsent(RecentDeliveryAddressServiceRequest request) {
        RecentDeliveryAddress recentDeliveryAddress = repository.findByUserIdAndDeliveryAddressOrStateNameAddress(request.getUserId(), request.getDeliveryAddress(), request.getStateNameAddress())
            .orElseGet(() -> repository.save(request.toRecentDeliveryAddress()));
        recentDeliveryAddress.updateUsedAt();
    }
}
