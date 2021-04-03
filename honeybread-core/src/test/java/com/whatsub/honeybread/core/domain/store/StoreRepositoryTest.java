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

    Store 스토어A;
    Store 스토어B;

    @BeforeEach
    void 스토어_초기화() {
        스토어A = 스토어_객체_만들기("스토어A");
        스토어B = 스토어_객체_만들기("스토어B");
    }

    @Test
    void 스토어명_없는지_확인() {
        //given

        //when
        boolean result = repository.existsByBasicName(스토어A.getBasic().getName());

        //then
        assertFalse(result);
    }

    @Test
    void 스토어명_있는지_확인() {
        //given
        repository.save(스토어A);

        //when
        boolean result = repository.existsByBasicName(스토어A.getBasic().getName());

        //then
        assertTrue(result);
    }

    @Test
    void 다른_스토어명으로_스토어명_중복검사() {
        //given
        repository.save(스토어A);
        repository.save(스토어B);

        //when
        boolean result = repository.existsByIdNotAndBasicName(스토어A.getId(), 스토어B.getBasic().getName());

        //then
        assertTrue(result);
    }

    @Test
    void 자신의_스토어명으로_스토어명_중복검사() {
        //given
        repository.save(스토어A);
        repository.save(스토어B);

        //when
        boolean result = repository.existsByIdNotAndBasicName(스토어A.getId(), 스토어A.getBasic().getName());

        //then
        assertFalse(result);
    }

    @Test
    void 아예_없던_스토어명으로_스토어명_중복검사() {
        //given
        repository.save(스토어A);
        repository.save(스토어B);

        //when
        boolean result = repository.existsByIdNotAndBasicName(스토어A.getId(), anyString());

        //then
        assertFalse(result);
    }

    private Store 스토어_객체_만들기(String name) {
        return Store.createStore(
                anyLong(),
                new StoreBasic(name,
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

}
