package com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice;

import com.whatsub.honeybread.core.domain.storedeliveryprice.StoreDeliveryPrice;
import com.whatsub.honeybread.core.domain.storedeliveryprice.StoreDeliveryPriceRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice.dto.StoreDeliveryPriceRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = StoreDeliveryPriceService.class)
@RequiredArgsConstructor
class StoreDeliveryPriceServiceTest {

    final StoreDeliveryPriceService service;

    @MockBean
    StoreDeliveryPriceRepository repository;

    @Mock
    StoreDeliveryPrice mockEntity;

    @Test
    void 주소별_배달금액_등록() {
        //given
        StoreDeliveryPriceRequest request = 주소별_배달금액_등록_요청_생성(0L, "서울시 강남구 수서동");
        주소별_배달금액이_중복되지_않음();

        //when
        service.create(request);

        //then
        주소별_배달금액이_등록되어야함();
        주소별_배달금액_중복을_체크해야함();
    }

    @Test
    void 주소별_배달금액_등록시_중복이라면_에러() {
        //given
        StoreDeliveryPriceRequest request = 주소별_배달금액_등록_요청_생성(0L, "서울시 강남구 수서동");
        주소별_배달금액이_중복됨();

        //when
        HoneyBreadException honeyBreadException = assertThrows(HoneyBreadException.class, () -> service.create(request));

        //then
        주소별_배달금액이_등록되지않아야함();
        주소별_배달금액_중복을_체크해야함();
        예상된_에러_발생(ErrorCode.STORE_DELIVERY_PRICE_NOT_FOUND, honeyBreadException);
    }

    private void 예상된_에러_발생(final ErrorCode storeDeliveryPriceNotFound, final HoneyBreadException honeyBreadException) {
        assertEquals(storeDeliveryPriceNotFound, honeyBreadException.getErrorCode());
    }

    private void 주소별_배달금액_중복을_체크해야함() {
        then(repository).should().existsByStoreIdAndSearchableDeliveryAddress(anyLong(), anyString());
    }

    private void 주소별_배달금액이_등록되어야함() {
        then(repository).should().save(any(StoreDeliveryPrice.class));
    }

    private void 주소별_배달금액이_등록되지않아야함() {
        then(repository).should(never()).save(any(StoreDeliveryPrice.class));
    }

    private void 주소별_배달금액이_중복되지_않음() {
        given(repository.existsByStoreIdAndSearchableDeliveryAddress(anyLong(), anyString())).willReturn(false);
    }

    private void 주소별_배달금액이_중복됨() {
        given(repository.existsByStoreIdAndSearchableDeliveryAddress(anyLong(), anyString())).willReturn(true);
    }

    private StoreDeliveryPriceRequest 주소별_배달금액_등록_요청_생성(final long storeId, final String address) {
        StoreDeliveryPriceRequest mockRequest = mock(StoreDeliveryPriceRequest.class);
        given(mockRequest.getStoreId()).willReturn(storeId);
        given(mockRequest.getSearchableDeliveryAddress()).willReturn(address);
        given(mockRequest.toEntity()).willReturn(mockEntity);
        return mockRequest;
    }

}