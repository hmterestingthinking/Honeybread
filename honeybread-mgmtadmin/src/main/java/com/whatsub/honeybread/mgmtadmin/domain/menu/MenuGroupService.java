package com.whatsub.honeybread.mgmtadmin.domain.menu;

import com.whatsub.honeybread.core.domain.menu.MenuGroup;
import com.whatsub.honeybread.core.domain.menu.repository.MenuGroupRepository;
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
}
