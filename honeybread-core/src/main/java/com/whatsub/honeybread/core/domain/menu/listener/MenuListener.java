package com.whatsub.honeybread.core.domain.menu.listener;

import com.whatsub.honeybread.core.domain.menu.Menu;
import com.whatsub.honeybread.core.domain.menu.history.MenuHistory;
import com.whatsub.honeybread.core.domain.menu.history.MenuHistoryConverter;
import com.whatsub.honeybread.core.domain.menu.history.MenuHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

@Slf4j
public class MenuListener {

    @Autowired
    private MenuHistoryRepository historyRepository;

    @Autowired
    private MenuHistoryConverter converter;

    @PostPersist
    @PostUpdate
    private void afterPersist(Menu e) {
        MenuHistory history = converter.convert(e);
        historyRepository.save(history);
    }

}
