package com.whatsub.honeybread.mgmtadmin.domain.menu;

import com.whatsub.honeybread.core.domain.menu.Menu;
import com.whatsub.honeybread.core.domain.menu.repository.MenuRepository;
import com.whatsub.honeybread.core.domain.menu.validator.MenuValidator;
import com.whatsub.honeybread.mgmtadmin.domain.menu.dto.MenuRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository repository;
    private final MenuValidator validator;

    @Transactional
    public Long create(final MenuRequest request) {
        Menu menu = request.toEntity();
        validator.validate(menu);
        return repository.save(menu).getId();
    }
}
