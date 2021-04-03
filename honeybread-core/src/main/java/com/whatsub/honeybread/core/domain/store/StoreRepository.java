package com.whatsub.honeybread.core.domain.store;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom {

    boolean existsByBasicName(final String name);

}