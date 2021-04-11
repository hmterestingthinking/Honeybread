package com.whatsub.honeybread.mgmtadmin.domain.recentaddress;

import com.whatsub.honeybread.core.domain.recentaddress.RecentDeliveryAddress;
import com.whatsub.honeybread.core.domain.recentaddress.RecentDeliveryAddressRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.recentaddress.dto.RecentDeliveryAddressRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RecentDeliveryAddressService {

    @Value("${user.recent-delivery-address.size}")
    private int recentDeliveryAddressSize;

    private final RecentDeliveryAddressRepository repository;

    @Transactional
    public void createIfAbsent(RecentDeliveryAddressRequest request) {
        RecentDeliveryAddress recentDeliveryAddress =
            repository.findByUserIdAndDeliveryAddressOrStateNameAddress(request.getUserId(),
                                                                        request.getDeliveryAddress(),
                                                                        request.getStateNameAddress())
                    .orElseGet(() -> {
                        deleteEldestIfSizeGreaterThanOrEqual(repository.findAllByUserId(request.getUserId()));
                        return repository.save(request.toRecentDeliveryAddress());
                    });
        recentDeliveryAddress.updateUsedAt(LocalDateTime.now());
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(findById(id));
    }

    private RecentDeliveryAddress findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new HoneyBreadException(ErrorCode.RECENT_DELIVERY_ADDRESS_NOT_FOUND));
    }

    private void deleteEldestIfSizeGreaterThanOrEqual(List<RecentDeliveryAddress> list) {
        if(list.size() >= recentDeliveryAddressSize) {
            list.stream()
                .sorted(Comparator.comparing(RecentDeliveryAddress::getUsedAt))
                .collect(Collectors.toList());
            repository.delete(list.get(0));
        }
    }
}
