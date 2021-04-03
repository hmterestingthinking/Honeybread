package com.whatsub.honeybread.core.domain.store;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom {

    boolean existsByBasicName(final String name);

    boolean existsByIdNotAndBasicName(final long storeId, final String name);

}