package com.whatsub.honeybread.core.domain.menu.history;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MenuHistoryRepository extends MongoRepository<MenuHistory, String> {
}
