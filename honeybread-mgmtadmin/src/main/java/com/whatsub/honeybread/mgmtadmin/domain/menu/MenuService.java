package com.whatsub.honeybread.mgmtadmin.domain.menu;

import com.whatsub.honeybread.core.domain.menu.Menu;
import com.whatsub.honeybread.core.domain.menu.repository.MenuRepository;
import com.whatsub.honeybread.core.domain.menu.validator.MenuValidator;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
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

    @Transactional
    public void update(final Long id, final MenuRequest request) {
        Menu menu = findMenu(id);
        menu.update(request.toEntity());
        validator.validate(menu);
    }

    private Menu findMenu(final Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new HoneyBreadException(ErrorCode.MENU_NOT_FOUND));
    }
}
