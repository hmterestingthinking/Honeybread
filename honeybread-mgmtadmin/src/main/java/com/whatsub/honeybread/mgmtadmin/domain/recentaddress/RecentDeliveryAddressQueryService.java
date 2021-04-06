package com.whatsub.honeybread.mgmtadmin.domain.recentaddress;

import com.whatsub.honeybread.core.domain.recentaddress.RecentDeliveryAddressRepository;
import com.whatsub.honeybread.mgmtadmin.domain.recentaddress.dto.RecentDeliveryAddressResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RecentDeliveryAddressQueryService {

    private final RecentDeliveryAddressRepository repository;

    public List<RecentDeliveryAddressResponse> getRecentDeliveryAddressesByUserId(Long id) {
        return repository.findAllByUserId(id).stream()
            .map(RecentDeliveryAddressResponse::of)
            .collect(Collectors.toList());
    }
}
