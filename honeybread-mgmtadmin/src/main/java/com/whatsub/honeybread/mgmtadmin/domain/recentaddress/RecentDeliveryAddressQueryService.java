package com.whatsub.honeybread.mgmtadmin.domain.recentaddress;

import com.whatsub.honeybread.core.domain.recentaddress.RecentDeliveryAddress;
import com.whatsub.honeybread.core.domain.recentaddress.RecentDeliveryAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RecentDeliveryAddressQueryService {

    private final RecentDeliveryAddressRepository repository;

    public List<RecentDeliveryAddress> getRecentDeliveryAddressesByUserId(Long id) {
        return repository.findAllByUserId(id);
    }

}
