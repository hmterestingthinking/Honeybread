package com.whatsub.honeybread.mgmtadmin.domain.recentaddress;

import com.whatsub.honeybread.core.domain.recentaddress.RecentDeliveryAddress;
import com.whatsub.honeybread.core.domain.recentaddress.RecentDeliveryAddressRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.recentaddress.dto.RecentDeliveryAddressServiceRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = RecentDeliveryAddressService.class)
@RequiredArgsConstructor
class RecentDeliveryAddressServiceTest {

    final RecentDeliveryAddressService service;

    @MockBean
    RecentDeliveryAddressRepository repository;

    @Mock
    RecentDeliveryAddressServiceRequest mockRequest;

    @Mock
    RecentDeliveryAddress mockEntity;

    @Test
    void 주소검색시_등록된_주소가_아니라면_주소_등록() {
        //given
        주소_요청();
        도로명주소_또는_지번주소로_검색_실패();
        등록된_주소가_10개_이하();
        주소_등록();

        //when
        service.createIfAbsent(mockRequest);

        //then
        도로명주소_또는_지번주소로_검색되어야함();
        주소_등록이_실행되어야함();
        주소_사용시간이_업데이트되어야함();
        주소가_삭제되지_않아야함();
        주소개수가_카운팅되어야함();
    }

    @Test
    void 주소검색시_등록된_주소라면_주소_사용시간_업데이트() {
        //given
        주소_요청();
        도로명주소_또는_지번주소로_검색_성공();

        //when
        service.createIfAbsent(mockRequest);

        //then
        도로명주소_또는_지번주소로_검색되어야함();
        주소_등록이_실행되지_않아야함();
        주소_사용시간이_업데이트되어야함();
        주소가_삭제되지_않아야함();
    }

    @Test
    @DisplayName("주소가 등록될때 해당 User의 주소가 10개 이상이라면 사용시간이 가장 오래된 최근배달주소를 삭제")
    void 주소검색시_가장_오래된_주소_삭제하기() {
        //given
        주소_요청();
        도로명주소_또는_지번주소로_검색_실패();
        등록된_주소가_10개_이상();
        가장오래된_주소_1개_검색();
        주소_등록();

        //when
        service.createIfAbsent(mockRequest);

        //then
        도로명주소_또는_지번주소로_검색되어야함();
        주소_등록이_실행되어야함();
        주소_사용시간이_업데이트되어야함();
        가장_오래된_주소가_검색되어야함();
        주소가_삭제되어야함();
        주소개수가_카운팅되어야함();
    }

    @Test
    void 주소_삭제() {
        //given
        주소_ID_검색_성공();

        //when
        service.delete(anyLong());

        //then
        주소가_삭제되어야함();
    }

    @Test
    void 주소_삭제시_없을경우_에러() {
        //given
        주소_ID_검색_실패();

        //when
        HoneyBreadException honeyBreadException =
            assertThrows(HoneyBreadException.class, () -> service.delete(anyLong()));

        //then
        RECENT_DELIVERY_ADDRESS_NOT_FOUND_에러_발생(honeyBreadException);
    }

    @Test
    void 주소_ID_검색() {
        //given
        주소_ID_검색_성공();

        //when
        repository.findById(anyLong());

        //then
        주소가_ID로_검색되어야함();
    }

    @Test
    void 주소_ID_검색시_없을경우_에러() {
        //given
        주소_ID_검색_실패();

        //when
        HoneyBreadException honeyBreadException =
            assertThrows(HoneyBreadException.class, () -> service.delete(anyLong()));

        //then
        RECENT_DELIVERY_ADDRESS_NOT_FOUND_에러_발생(honeyBreadException);
    }

    private void 가장오래된_주소_1개_검색() {
        given(repository.findTop1ByUserIdOrderByUsedAtAsc(anyLong())).willReturn(Optional.of(mockEntity));
    }

    private void 등록된_주소가_10개_이상() {
        given(repository.countByUserId(anyLong())).willReturn(10);
    }

    private void 등록된_주소가_10개_이하() {
        given(repository.countByUserId(anyLong())).willReturn(9);
    }

    private void 주소_ID_검색_성공() {
        given(repository.findById(anyLong()))
            .willReturn(Optional.of(mockEntity));
    }

    private void 주소_ID_검색_실패() {
        given(repository.findById(anyLong()))
            .willReturn(Optional.empty());
    }

    private void 주소_요청() {
        given(mockRequest.getUserId()).willReturn(1L);
        given(mockRequest.getDeliveryAddress()).willReturn("서울시 강남구 수서동 500 301동 404호");
        given(mockRequest.getStateNameAddress()).willReturn("서울시 강남구 광평로101길 200 301동 404호");
        given(mockRequest.toRecentDeliveryAddress()).willReturn(mockEntity);
    }

    private void 도로명주소_또는_지번주소로_검색_성공() {
        given(repository.findByUserIdAndDeliveryAddressOrStateNameAddress(anyLong(), anyString(), anyString()))
            .willReturn(Optional.of(mockEntity));
    }

    private void 도로명주소_또는_지번주소로_검색_실패() {
        given(repository.findByUserIdAndDeliveryAddressOrStateNameAddress(anyLong(), anyString(), anyString()))
            .willReturn(Optional.empty());
    }

    private void 주소_등록() {
        given(repository.save(any(RecentDeliveryAddress.class))).willReturn(mockEntity);
    }

    private void 도로명주소_또는_지번주소로_검색되어야함() {
        then(repository).should().findByUserIdAndDeliveryAddressOrStateNameAddress(anyLong(), anyString(), anyString());
    }

    private void 주소_등록이_실행되어야함() {
        then(repository).should().save(any(RecentDeliveryAddress.class));
    }

    private void 주소_사용시간이_업데이트되어야함() {
        then(mockEntity).should().updateUsedAt();
    }

    private void 주소_등록이_실행되지_않아야함() {
        then(repository).should(never()).save(any(RecentDeliveryAddress.class));
    }

    private void 주소가_삭제되어야함() {
        then(repository).should().delete(any(RecentDeliveryAddress.class));
    }

    private void 주소가_삭제되지_않아야함() {
        then(repository).should(never()).delete(any(RecentDeliveryAddress.class));
    }

    private void 주소가_ID로_검색되어야함() {
        then(repository).should().findById(anyLong());
    }

    private void 가장_오래된_주소가_검색되어야함() {
        then(repository).should().findTop1ByUserIdOrderByUsedAtAsc(anyLong());
    }

    private void 주소개수가_카운팅되어야함() {
        then(repository).should().countByUserId(anyLong());
    }

    private void RECENT_DELIVERY_ADDRESS_NOT_FOUND_에러_발생(HoneyBreadException honeyBreadException) {
        assertEquals(ErrorCode.RECENT_DELIVERY_ADDRESS_NOT_FOUND.getMessage(), honeyBreadException.getErrorCode().getMessage());
        assertEquals(ErrorCode.RECENT_DELIVERY_ADDRESS_NOT_FOUND.getCode(), honeyBreadException.getErrorCode().getCode());
        assertEquals(ErrorCode.RECENT_DELIVERY_ADDRESS_NOT_FOUND.getStatus(), honeyBreadException.getErrorCode().getStatus());
    }
}