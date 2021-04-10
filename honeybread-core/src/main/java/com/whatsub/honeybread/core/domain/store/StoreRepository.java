package com.whatsub.honeybread.core.domain.store;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom {

    boolean existsByBasicName(final String name);

    Optional<Store> findAllByUuid(final String uuid);

}