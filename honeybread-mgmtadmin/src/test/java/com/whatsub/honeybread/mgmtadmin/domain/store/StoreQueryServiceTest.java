package com.whatsub.honeybread.mgmtadmin.domain.store;

import com.whatsub.honeybread.core.domain.store.Address;
import com.whatsub.honeybread.core.domain.store.BankAccount;
import com.whatsub.honeybread.core.domain.store.BusinessHours;
import com.whatsub.honeybread.core.domain.store.BusinessLicense;
import com.whatsub.honeybread.core.domain.store.Store;
import com.whatsub.honeybread.core.domain.store.StoreAnnouncement;
import com.whatsub.honeybread.core.domain.store.StoreBasic;
import com.whatsub.honeybread.core.domain.store.StoreOperation;
import com.whatsub.honeybread.core.domain.store.StoreRepository;
import com.whatsub.honeybread.core.domain.store.StoreStatus;
import com.whatsub.honeybread.core.domain.store.dto.StoreSearch;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.store.dto.StoreResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@SpringBootTest(classes = StoreQueryService.class)
public class StoreQueryServiceTest {

    final StoreQueryService queryService;

    @MockBean
    StoreRepository storeRepository;


    @Test
    void 스토어가_조회되면_스토어정보를_반환한다() {
        // given
        long 스토어아이디 = 1L;
        아이디가_다음과같은_스토어가_조회된다(스토어아이디);
        // when
        StoreResponse 응답 = queryService.getStoreByStoreId(스토어아이디);

        // then
        then(storeRepository).should().findById(스토어아이디);
        assertEquals(응답.getStoreId(), 스토어아이디);
    }

    @Test
    void 스토어가_조회되지_않으면_에러가_발생한다() {
        // given
        Long 스토어아이디 = anyLong();

        // when
        HoneyBreadException exception = assertThrows(HoneyBreadException.class, () -> queryService.getStoreByStoreId(스토어아이디));

        // then
        then(storeRepository).should().findById(스토어아이디);
        assertEquals(exception.getErrorCode(), ErrorCode.STORE_NOT_FOUND);
    }

    @Test
    void 스토어_목록조회() {
        // given
        final int size = 10;
        size만큼_스토어들이_조회된다(size);

        // when
        Page<StoreResponse> response = queryService.getStores(mock(Pageable.class), mock(StoreSearch.class));

        // then
        then(storeRepository).should().getStores(any(Pageable.class), any(StoreSearch.class));
        then(response.getTotalElements()).equals(size);
        then(response.getContent().size()).equals(size);
        assertEquals(response.getTotalElements(), size);
        assertEquals(response.getContent().size(), size);
    }

    private void size만큼_스토어들이_조회된다(int size) {
        final List<Store> 스토어들 = IntStream.range(0, size).boxed()
                .map(this::다음과같은_아이디의_스토어)
                .collect(Collectors.toList());
        final PageRequest 페이지 = PageRequest.of(0, size);

        given(storeRepository.getStores(any(), any()))
                .willReturn(new PageImpl<>(스토어들, 페이지, size));
    }

    private void 아이디가_다음과같은_스토어가_조회된다(long 스토어아이디) {
        Store 스토어 = 다음과같은_아이디의_스토어(스토어아이디);
        given(storeRepository.findById(스토어아이디)).willReturn(Optional.ofNullable(스토어));
    }

    private Store 다음과같은_아이디의_스토어(long 스토어아이디) {
        Store 스토어 = mock(Store.class);
        given(스토어.getId()).willReturn(스토어아이디);
        given(스토어.getSellerId()).willReturn(1L);
        given(스토어.getPayMethods()).willReturn(Lists.emptyList());
        given(스토어.getBasic()).willReturn(mock(StoreBasic.class));
        given(스토어.getBasic().getName()).willReturn(Strings.EMPTY);
        given(스토어.getBasic().getTel()).willReturn(Strings.EMPTY);
        given(스토어.getBasic().getImageUrl()).willReturn(Strings.EMPTY);
        given(스토어.getBasic().getAddress()).willReturn(mock(Address.class));
        given(스토어.getBasic().getStoreAnnouncement()).willReturn(mock(StoreAnnouncement.class));
        given(스토어.getBasic().getOperationTime()).willReturn(mock(BusinessHours.class));
        given(스토어.getBasic().getBusinessLicense()).willReturn(mock(BusinessLicense.class));
        given(스토어.getBankAccount()).willReturn(mock(BankAccount.class));
        given(스토어.getStatus()).willReturn(StoreStatus.WAITING);
        given(스토어.getCategories()).willReturn(Lists.emptyList());
        given(스토어.getOperation()).willReturn(StoreOperation.createClosedOperation());
        return 스토어;
    }

}
