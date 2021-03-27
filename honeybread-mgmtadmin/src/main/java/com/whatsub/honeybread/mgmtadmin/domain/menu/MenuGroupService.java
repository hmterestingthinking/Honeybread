package com.whatsub.honeybread.mgmtadmin.domain.menu;

import com.whatsub.honeybread.core.domain.menu.MenuGroup;
import com.whatsub.honeybread.core.domain.menu.repository.MenuGroupRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.menu.dto.MenuGroupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuGroupService {
    private final MenuGroupRepository repository;

    @Transactional
    public Long create(MenuGroupRequest request) {
        MenuGroup menuGroup = request.toEntity();
        return repository.save(menuGroup).getId();
    }

    @Transactional
    public void update(final Long id, final MenuGroupRequest request) {
        MenuGroup menuGroup = findMenuGroup(id);
        menuGroup.update(request.toEntity());
    }

    private MenuGroup findMenuGroup(final Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new HoneyBreadException(ErrorCode.MENU_GROUP_NOT_FOUND));
    }
}
