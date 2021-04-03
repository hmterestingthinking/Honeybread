package com.whatsub.honeybread.core.domain.store;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DataJpaTest
@RequiredArgsConstructor
public class StoreRepositoryTest {

    final StoreRepository repository;

    Store 스토어;

    @BeforeEach
    void 스토어_초기화() {
        스토어 = Store.createStore(
                anyLong(),
                new StoreBasic(anyString(),
                        anyString(),
                        anyString(),
                        new Address(anyString()),
                        new StoreAnnouncement(anyString(), anyString(), anyString()),
                        new BusinessHours(anyString(), anyString(), anyString()),
                        new BusinessLicense(anyString())
                ),
                new BankAccount(any(), anyString()),
                anyList(),
                anyList()
        );
    }

    @Test
    void 스토어명_없는지_확인() {
        //given

        //when
        boolean result = repository.existsByBasicName(스토어.getBasic().getName());

        //then
        assertFalse(result);
    }

    @Test
    void 스토어명_있는지_확인() {
        //given
        repository.save(스토어);

        //when
        boolean result = repository.existsByBasicName(스토어.getBasic().getName());

        //then
        assertTrue(result);
    }

}
