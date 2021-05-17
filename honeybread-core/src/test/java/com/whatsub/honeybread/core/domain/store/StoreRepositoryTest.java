package com.whatsub.honeybread.core.domain.store;

import com.whatsub.honeybread.core.domain.store.dto.StoreSearch;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestConstructor;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DataJpaTest
@RequiredArgsConstructor
public class StoreRepositoryTest {

    final StoreRepository repository;

    Store 스토어A;
    Store 스토어B;

    int 페이지_사이즈 = 10;
    PageRequest pageRequest = PageRequest.of(0, 페이지_사이즈);

    @BeforeEach
    void 스토어_초기화() {
        스토어A = 다음과같은_이름의_스토어를_가져온다("스토어A");
        스토어B = 다음과같은_이름의_스토어를_가져온다("스토어B");
    }

    @Test
    void 스토어명_없는지_확인() {
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
    void UUID가_일치하는_스토어가_없다면_스토어는_조회되지_않는다() {
        // given
        String uuid = anyString();

        // when
        Optional<Store> store = repository.findByUuid(uuid);

        // then
        assertFalse(store.isPresent());
    }

    @Test
    void UUID가_일치하는_스토어를_조회한다() {
        // given
        String uuid = anyString();
        다음과같은_UUID의_스토어를_저장한다(uuid);

        // when
        Optional<Store> store = repository.findByUuid(uuid);

        // then
        assertTrue(store.isPresent());
        assertEquals(store.get().getUuid(), uuid);
    }

    @Test
    void 스토어_이름으로_스토어목록을_조회한다() {
        // given
        final int N = 7;
        String 허니브레드 = "허니브레드";
        다음과같은_이름으로_시작하는_스토어를_N개_저장한다(허니브레드, N);
        다음과같은_이름으로_시작하는_스토어를_N개_저장한다(anyString(), 10);
        int 조회되어야하는_데이터_개수 = Math.min(N, 페이지_사이즈);

        // when
        StoreSearch search = new StoreSearch(허니브레드, null, null);
        Page<Store> stores = repository.getStores(pageRequest, search);

        // then
        assertEquals(stores.getTotalElements(), N);
        assertEquals(stores.getContent().size(), 조회되어야하는_데이터_개수);
        assertTrue(stores.getContent().stream().allMatch(store -> store.getBasic().getName().startsWith(허니브레드)));
    }

    @Test
    void 셀러아이디로_스토어목록을_조회한다() {
        // given
        final int N = 7;
        long 셀러아이디 = 777L;
        다음과같은_아이디인_판매자의_스토어를_N개를_저장한다(셀러아이디, N);
        다음과같은_아이디인_판매자의_스토어를_N개를_저장한다(anyLong(), 10);
        int 조회되어야하는_데이터_개수 = Math.min(N, 페이지_사이즈);

        // when
        StoreSearch search = new StoreSearch(null, 셀러아이디, null);
        Page<Store> stores = repository.getStores(pageRequest, search);

        // then
        assertEquals(stores.getTotalElements(), N);
        assertEquals(stores.getContent().size(), 조회되어야하는_데이터_개수);
        assertTrue(stores.getContent().stream().allMatch(store -> store.getSellerId().equals(셀러아이디)));
    }

    @Test
    void 상태로_스토어목록을_조회한다() {
        // given
        final int N = 7;
        StoreStatus 상태 = StoreStatus.ACTIVATED;
        다음과같은_상태의_스토어를_N개를_저장한다(상태, N);
        다음과같은_상태의_스토어를_N개를_저장한다(any(), 10);
        int 조회되어야하는_데이터_개수 = Math.min(N, 페이지_사이즈);

        // when
        StoreSearch search = new StoreSearch(null, null, 상태);
        Page<Store> stores = repository.getStores(pageRequest, search);

        // then
        assertEquals(stores.getTotalElements(), N);
        assertEquals(stores.getContent().size(), 조회되어야하는_데이터_개수);
        assertTrue(stores.getContent().stream().allMatch(store -> store.getStatus() == 상태));
    }

    @Test
    void 스토어명_셀러아이디_상태로_스토어목록을_조회한다() {
        // given
        final int N = 7;
        long 셀러아이디 = 777L;
        String 스토어명 = "허니브레드";
        StoreStatus 상태 = StoreStatus.ACTIVATED;
        다음과같은_아이디인_판매자의_스토어를_N개를_저장한다(셀러아이디, 3);
        다음과같은_이름으로_시작하는_스토어를_N개_저장한다(스토어명, 3);
        다음과같은_상태의_스토어를_N개를_저장한다(상태, 3);
        다음과같은_정보의_스토어를_N개를_저장한다(스토어명, 셀러아이디, 상태, N);
        int 조회되어야하는_데이터_개수 = Math.min(N, 페이지_사이즈);

        // when
        StoreSearch search = new StoreSearch(스토어명, 셀러아이디, 상태);
        Page<Store> stores = repository.getStores(pageRequest, search);

        // then
        assertEquals(stores.getTotalElements(), N);
        assertEquals(stores.getContent().size(), 조회되어야하는_데이터_개수);
        assertTrue(stores.getContent().stream().allMatch(
                store -> store.getBasic().getName().equals(스토어명)
                        && store.getSellerId().equals(셀러아이디)
                        && store.getStatus() == 상태)
        );
    }

    /**
     * given
     */
    private void 다음과같은_이름으로_시작하는_스토어를_N개_저장한다(String 스토어명, int 저장할개수) {
        repository.saveAll(IntStream.range(0, 저장할개수)
                .mapToObj(value -> 다음과같은_이름의_스토어를_가져온다(스토어명 + value))
                .collect(Collectors.toList()));
    }

    private Store 다음과같은_이름의_스토어를_가져온다(String name) {
        return Store.builder()
                .sellerId(anyLong())
                .storeBasic(new StoreBasic(name,
                        anyString(),
                        anyString(),
                        new Address(anyString()),
                        new StoreAnnouncement(anyString(), anyString(), anyString()),
                        new BusinessHours(anyString(), anyString(), anyString()),
                        new BusinessLicense(anyString())
                ))
                .status(any())
                .bankAccount(new BankAccount(any(), anyString()))
                .categories(anyList())
                .payMethods(anyList())
                .build();
    }

    private void 다음과같은_아이디인_판매자의_스토어를_N개를_저장한다(long 셀러아이디, int 저장할개수) {
        repository.saveAll(IntStream.range(0, 저장할개수)
                .mapToObj(value -> 다음과같은_셀러의_스토어(셀러아이디))
                .collect(Collectors.toList()));
    }

    private Store 다음과같은_셀러의_스토어(long 셀러아이디) {
        return Store.builder()
                .sellerId(셀러아이디)
                .storeBasic(new StoreBasic(anyString(),
                        anyString(),
                        anyString(),
                        new Address(anyString()),
                        new StoreAnnouncement(anyString(), anyString(), anyString()),
                        new BusinessHours(anyString(), anyString(), anyString()),
                        new BusinessLicense(anyString())
                ))
                .status(any())
                .bankAccount(new BankAccount(any(), anyString()))
                .categories(anyList())
                .payMethods(anyList())
                .build();
    }

    private void 다음과같은_상태의_스토어를_N개를_저장한다(StoreStatus 상태, int 저장할개수) {
        repository.saveAll(IntStream.range(0, 저장할개수)
                .mapToObj(value -> 다음과같은_상태의_스토어(상태))
                .collect(Collectors.toList()));
    }

    private Store 다음과같은_상태의_스토어(StoreStatus 상태) {
        return Store.builder()
                .sellerId(anyLong())
                .storeBasic(new StoreBasic(anyString(),
                        anyString(),
                        anyString(),
                        new Address(anyString()),
                        new StoreAnnouncement(anyString(), anyString(), anyString()),
                        new BusinessHours(anyString(), anyString(), anyString()),
                        new BusinessLicense(anyString())
                ))
                .status(상태)
                .bankAccount(new BankAccount(any(), anyString()))
                .categories(anyList())
                .payMethods(anyList())
                .build();
    }

    private void 다음과같은_UUID의_스토어를_저장한다(String uuid) {
        Store 스토어 = Store.builder()
                .sellerId(anyLong())
                .storeBasic(new StoreBasic(anyString(),
                        anyString(),
                        anyString(),
                        new Address(anyString()),
                        new StoreAnnouncement(anyString(), anyString(), anyString()),
                        new BusinessHours(anyString(), anyString(), anyString()),
                        new BusinessLicense(anyString())
                ))
                .status(any())
                .bankAccount(new BankAccount(any(), anyString()))
                .categories(anyList())
                .payMethods(anyList())
                .build();
        스토어.updateUuid(uuid);
        repository.save(스토어);
    }

    private void 다음과같은_정보의_스토어를_N개를_저장한다(String 스토어명, long 셀러아이디, StoreStatus 상태, int 저장할개수) {
        repository.saveAll(IntStream.range(0, 저장할개수)
                .mapToObj(value -> 다음과같은_정보들의_스토어를_가져온다(스토어명, 셀러아이디, 상태))
                .collect(Collectors.toList()));
    }

    private Store 다음과같은_정보들의_스토어를_가져온다(String 스토어명, long 셀러아이디, StoreStatus 상태) {
        return Store.builder()
                .sellerId(셀러아이디)
                .storeBasic(new StoreBasic(스토어명,
                        anyString(),
                        anyString(),
                        new Address(anyString()),
                        new StoreAnnouncement(anyString(), anyString(), anyString()),
                        new BusinessHours(anyString(), anyString(), anyString()),
                        new BusinessLicense(anyString())
                ))
                .status(상태)
                .bankAccount(new BankAccount(any(), anyString()))
                .categories(anyList())
                .payMethods(anyList())
                .build();
    }
}
