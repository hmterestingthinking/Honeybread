package com.whatsub.honeybread.core.domain.menu.repository;

import com.whatsub.honeybread.core.domain.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
