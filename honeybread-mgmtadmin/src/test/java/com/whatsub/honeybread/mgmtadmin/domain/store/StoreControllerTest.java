package com.whatsub.honeybread.mgmtadmin.domain.store;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsub.honeybread.core.domain.model.TimePeriod;
import com.whatsub.honeybread.core.domain.store.BankType;
import com.whatsub.honeybread.core.domain.store.OperationStatus;
import com.whatsub.honeybread.core.domain.store.PayType;
import com.whatsub.honeybread.core.domain.store.StoreStatus;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.store.dto.AddressRequest;
import com.whatsub.honeybread.mgmtadmin.domain.store.dto.BankAccountRequest;
import com.whatsub.honeybread.mgmtadmin.domain.store.dto.BusinessHoursRequest;
import com.whatsub.honeybread.mgmtadmin.domain.store.dto.BusinessLicenseRequest;
import com.whatsub.honeybread.mgmtadmin.domain.store.dto.StoreAnnouncementRequest;
import com.whatsub.honeybread.mgmtadmin.domain.store.dto.StoreBasicRequest;
import com.whatsub.honeybread.mgmtadmin.domain.store.dto.StoreCreateRequest;
import com.whatsub.honeybread.mgmtadmin.domain.store.dto.StoreOperationRequest;
import com.whatsub.honeybread.mgmtadmin.domain.store.dto.StoreUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@WebMvcTest(StoreController.class)
@RequiredArgsConstructor
public class StoreControllerTest {

    static final String BASE_URL = "/stores";

    final MockMvc mockMvc;

    final ObjectMapper mapper;

    @MockBean
    StoreService service;

    @MockBean
    StoreCreateRequest 스토어_등록요청;
    @MockBean
    StoreUpdateRequest 스토어_수정요청;
    @MockBean
    StoreBasicRequest 기본정보_요청;
    @MockBean
    AddressRequest 주소정보_요청;
    @MockBean
    StoreAnnouncementRequest 안내정보_요청;
    @MockBean
    BusinessHoursRequest 운영시간정보_요청;
    @MockBean
    BusinessLicenseRequest 사업자등록증정보_요청;
    @MockBean
    BankAccountRequest 계좌정보_요청;
    @MockBean
    StoreOperationRequest 운영정보_요청;

    @BeforeEach
    void 등록수정_공통요청_초기화() {
        given(기본정보_요청.getAddress()).willReturn(주소정보_요청);
        given(기본정보_요청.getStoreIntroduce()).willReturn(안내정보_요청);
        given(기본정보_요청.getOperationTime()).willReturn(운영시간정보_요청);
        given(기본정보_요청.getBusinessLicense()).willReturn(사업자등록증정보_요청);
    }

    @BeforeEach
    void 등록요청_초기화() {
        given(스토어_등록요청.getBasic()).willReturn(기본정보_요청);
        given(스토어_등록요청.getBankAccount()).willReturn(계좌정보_요청);
    }

    @BeforeEach
    void 수정요청_초기화() {
        given(스토어_수정요청.getBasic()).willReturn(기본정보_요청);
        given(스토어_수정요청.getBankAccount()).willReturn(계좌정보_요청);
        given(스토어_수정요청.getOperation()).willReturn(운영정보_요청);
    }

    /**
     * 테스트
     */

    @Test
    void 셀러아이디를_입력하지_않아_등록_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_셀러아이디로_요청했다(null);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 기본정보를_입력하지_않으면_등록_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_기본정보로_등록을_요청했다(null);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 스토어명을_입력하지_않아_등록_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_스토어명으로_요청했다(null);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            "세글자",
            "누가스토어명을30글자이상입력하래스토어명30글자라라고얘기했는데!!!!!"
    })
    void 스토어명을_5글자미만_30글자초과로_입력하면_등록_실패(String 스토어명) throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_스토어명으로_요청했다(스토어명);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 전화변호를_입력하지_않아_등록_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_전화번호로_요청했다(null);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            "012-123-123",
            "012-1234-12345",
            "012-12345-1234"
    })
    void 전화번호_형식이_맞지않아_등록_실패(String 전화번호) throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_전화번호로_요청했다(전화번호);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 이미지URL을_입력하지_않아_등록_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_이미지URL로_요청했다(null);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            "얘는이미지URL이아니지",
    })
    void 이미지URL이_형식에_맞지않아_등록_실패(String 이미지URL) throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_이미지URL로_요청했다(이미지URL);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 주소정보를_입력하지_않으면_등록_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_주소정보로_요청했다(null);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 주소를_입력하지_않아_등록_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_상세주소로_요청했다(null);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            "세글자",
            "솔직히 아무리 시골에 있다고 하더라도 인간적으로 100자 넘으면 그건 개미집 주소 아니냐" +
                    "주소를 어떻게 100글자를 채우지 더 이상 쓸게 없는데" +
                    "내 일기나 써야지" +
                    "오늘은 허리가 아파서 늦게 일어났다" +
                    "100글자가 넘었다"
    })
    void 주소가_형식에_맞지않아_등록_실패(String 주소) throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_상세주소로_요청했다(주소);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 안내정보를_입력하지_않으면_등록_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_안내정보로_요청했다(null);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 소개글을_입력하지_않아_등록_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_소개글로_요청했다(null);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            "세글자",
            "이 편지는 영국에서 최초로 시작되어 일년에 한 바퀴 돌면서 받는 사람에게 행운을 주었고 지금은 당신에게로 옮겨진 이 편지는 4일 안에 당신 곁을 떠나야 합니다." +
                    "이 편지를 포함해서 7통을 행운이 필요한 사람에게 보내 주셔야 합니다." +
                    "복사를 해도 좋습니다. 혹 미신이라 하실지 모르지만 사실입니다." +
                    "영국에서 HGXWCH이라는 사람은 1930년에 이 편지를 받았습니다." +
                    "그는 비서에게 복사해서 보내라고 했습니다." +
                    "며칠 뒤에 복권이 당첨되어 20억을 받았습니다." +
                    "어떤 이는 이 편지를 받았으나 96시간 이내 자신의 손에서 떠나야 한다는 사실을 잊었습니다." +
                    "그는 곧 사직되었습니다." +
                    "나중에야 이 사실을 알고 7통의 편지를 보냈는데 다시 좋은 직장을 얻었습니다." +
                    "미국의 케네디 대통령은 이 편지를 받았지만 그냥 버렸습니다. 결국 9일 후 그는 암살 당했습니다." +
                    "기억해 주세요. 이 편지를 보내면 7년의 행운이 있을 것이고 그렇지 않으면 3년의 불행이 있을 것입니다." +
                    "그리고 이 편지를 버리거나 낙서를 해서는 절대로 안됩니다." +
                    "7통입니다." +
                    "이 편지를 받은 사람은 행운이 깃들 것입니다." +
                    "힘들겠지만 좋은게 좋다고 생각하세요. "
    })
    void 소개글을_50글자미만_300글자초과로_입력하면_등록_실패(String 소개글) throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_소개글로_요청했다(소개글);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 안내글을_입력하지_않아_등록_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_안내글로_요청했다(null);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            "세글자",
            "안내글이에요." +
                    "이제부터 300글자 초과를 만들어볼거에요" +
                    "이 편지는 영국에서 최초로 시작되어 일년에 한 바퀴 돌면서 받는 사람에게 행운을 주었고 지금은 당신에게로 옮겨진 이 편지는 4일 안에 당신 곁을 떠나야 합니다." +
                    "이 편지를 포함해서 7통을 행운이 필요한 사람에게 보내 주셔야 합니다." +
                    "복사를 해도 좋습니다. 혹 미신이라 하실지 모르지만 사실입니다." +
                    "영국에서 HGXWCH이라는 사람은 1930년에 이 편지를 받았습니다." +
                    "그는 비서에게 복사해서 보내라고 했습니다." +
                    "며칠 뒤에 복권이 당첨되어 20억을 받았습니다." +
                    "어떤 이는 이 편지를 받았으나 96시간 이내 자신의 손에서 떠나야 한다는 사실을 잊었습니다." +
                    "그는 곧 사직되었습니다." +
                    "나중에야 이 사실을 알고 7통의 편지를 보냈는데 다시 좋은 직장을 얻었습니다." +
                    "미국의 케네디 대통령은 이 편지를 받았지만 그냥 버렸습니다. 결국 9일 후 그는 암살 당했습니다." +
                    "기억해 주세요. 이 편지를 보내면 7년의 행운이 있을 것이고 그렇지 않으면 3년의 불행이 있을 것입니다." +
                    "그리고 이 편지를 버리거나 낙서를 해서는 절대로 안됩니다." +
                    "7통입니다." +
                    "이 편지를 받은 사람은 행운이 깃들 것입니다." +
                    "힘들겠지만 좋은게 좋다고 생각하세요. " +
                    "이 편지는 영국에서 최초로 시작되어 일년에 한 바퀴 돌면서 받는 사람에게 행운을 주었고 지금은 당신에게로 옮겨진 이 편지는 4일 안에 당신 곁을 떠나야 합니다." +
                    "이 편지를 포함해서 7통을 행운이 필요한 사람에게 보내 주셔야 합니다." +
                    "복사를 해도 좋습니다. 혹 미신이라 하실지 모르지만 사실입니다." +
                    "영국에서 HGXWCH이라는 사람은 1930년에 이 편지를 받았습니다." +
                    "그는 비서에게 복사해서 보내라고 했습니다." +
                    "며칠 뒤에 복권이 당첨되어 20억을 받았습니다." +
                    "어떤 이는 이 편지를 받았으나 96시간 이내 자신의 손에서 떠나야 한다는 사실을 잊었습니다." +
                    "그는 곧 사직되었습니다." +
                    "나중에야 이 사실을 알고 7통의 편지를 보냈는데 다시 좋은 직장을 얻었습니다." +
                    "미국의 케네디 대통령은 이 편지를 받았지만 그냥 버렸습니다. 결국 9일 후 그는 암살 당했습니다." +
                    "기억해 주세요. 이 편지를 보내면 7년의 행운이 있을 것이고 그렇지 않으면 3년의 불행이 있을 것입니다." +
                    "그리고 이 편지를 버리거나 낙서를 해서는 절대로 안됩니다." +
                    "7통입니다." +
                    "이 편지를 받은 사람은 행운이 깃들 것입니다." +
                    "힘들겠지만 좋은게 좋다고 생각하세요. "
    })
    void 안내글을_200글자미만_1000글자초과로_입력하면_등록_실패(String 안내글) throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_안내글로_요청했다(안내글);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 원산지를_입력하지_않아_등록_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_원산지로_요청했다(null);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            "세글자",
            "원산지정보에요." +
                    "이제부터 500글자 초과를 만들어볼거에요" +
                    "이 편지는 영국에서 최초로 시작되어 일년에 한 바퀴 돌면서 받는 사람에게 행운을 주었고 지금은 당신에게로 옮겨진 이 편지는 4일 안에 당신 곁을 떠나야 합니다." +
                    "이 편지를 포함해서 7통을 행운이 필요한 사람에게 보내 주셔야 합니다." +
                    "복사를 해도 좋습니다. 혹 미신이라 하실지 모르지만 사실입니다." +
                    "영국에서 HGXWCH이라는 사람은 1930년에 이 편지를 받았습니다." +
                    "그는 비서에게 복사해서 보내라고 했습니다." +
                    "며칠 뒤에 복권이 당첨되어 20억을 받았습니다." +
                    "어떤 이는 이 편지를 받았으나 96시간 이내 자신의 손에서 떠나야 한다는 사실을 잊었습니다." +
                    "그는 곧 사직되었습니다." +
                    "나중에야 이 사실을 알고 7통의 편지를 보냈는데 다시 좋은 직장을 얻었습니다." +
                    "미국의 케네디 대통령은 이 편지를 받았지만 그냥 버렸습니다. 결국 9일 후 그는 암살 당했습니다." +
                    "기억해 주세요. 이 편지를 보내면 7년의 행운이 있을 것이고 그렇지 않으면 3년의 불행이 있을 것입니다." +
                    "그리고 이 편지를 버리거나 낙서를 해서는 절대로 안됩니다." +
                    "7통입니다." +
                    "이 편지를 받은 사람은 행운이 깃들 것입니다." +
                    "힘들겠지만 좋은게 좋다고 생각하세요. "
    })
    void 원산지를_200글자미만_1000글자초과로_입력하면_등록_실패(String 원산지) throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_원산지로_요청했다(원산지);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 운영시간정보를_입력하지_않으면_등록_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_운영시간정보로_요청했다(null);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 운영시간을_입력하지_않아_등록_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_운영시간으로_요청했다(null);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            "운영시간정보에요." +
                    "사실 이 정보는 길게 입력할 이유가 없어요." +
                    "100글자 이상되는 게 이상하죠. 안그래요?" +
                    "그냥 간단하게 연중무휴.. 평일 8-21 이런식으로 입력하면 되는 정보에요" +
                    "이 이상 입력하면 TMI스런 정보가 들어가지 않았나 싶네요" +
                    "자기 생일이라도 써놨나?"
    })
    void 운영시간을_100글자초과로_입력하면_등록_실패(String 운영시간) throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_운영시간으로_요청했다(운영시간);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "휴무일 정보에요." +
                    "사실 이 정보는 길게 입력할 이유가 없어요." +
                    "100글자 이상되는 게 이상하죠. 안그래요?" +
                    "그냥 간단하게 연중무휴.. 평일 8-21 이런식으로 입력하면 되는 정보에요" +
                    "이 이상 입력하면 TMI스런 정보가 들어가지 않았나 싶네요" +
                    "자기 생일이라도 써놨나?"
    })
    void 휴무일을_100글자초과로_입력하면_등록_실패(String 휴무일) throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_휴무일로_요청했다(휴무일);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "휴무일 정보에요." +
                    "사실 이 정보는 길게 입력할 이유가 없어요." +
                    "100글자 이상되는 게 이상하죠. 안그래요?" +
                    "그냥 간단하게 연중무휴.. 평일 8-21 이런식으로 입력하면 되는 정보에요" +
                    "이 이상 입력하면 TMI스런 정보가 들어가지 않았나 싶네요" +
                    "자기 생일이라도 써놨나?"
    })
    void 휴게시간을_100글자초과로_입력하면_등록_실패(String 휴게시간) throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_휴게시간으로_요청했다(휴게시간);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 사업자등록정보를_입력하지_않으면_등록_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_사업자등록정보로_요청했다(null);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 사업자등록번호를_입력하지_않아_등록_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_사업자등록번호로_요청했다(null);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            "012123",
            "0123456789",
            "01-23-45678",
            "012-3-45678",
            "012-34-567",
    })
    void 사업자등록번호_형식이_맞지않아_등록_실패(String 사업자등록번호) throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_사업자등록번호로_요청했다(사업자등록번호);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 계좌정보를_입력하지_않으면_등록_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_계좌정보로_등록을_요청했다(null);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 은행정보를_입력하지_않아_등록_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_은행으로_요청했다(null);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 계좌번호를_입력하지_않아_등록_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_계좌번호_요청했다(null);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 카테고리목록을_입력하지_않아_등록_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_카테고리목록으로_등록을_요청했다(null);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 카테고리목록을_empty하게_요청하면_등록_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_카테고리목록으로_등록을_요청했다(Collections.emptySet());

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 결제방식목록을_입력하지_않아_등록_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_결제방식목록으로_등록을_요청했다(null);

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 결제방식목록을_empty하게_요청하면_등록_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        다음과같은_결제방식목록으로_등록을_요청했다(Collections.emptySet());

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 셀러가_존재하지_않으면_등록_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        등록을_시도했더니_없는_판매자_정보이다();

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되었다();
        요청의_응답은_NOT_FOUND(result);
    }

    @Test
    void 스토어명이_겹치면_등록_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        등록을_시도했더니_겹치는_스토어명이다();

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되었다();
        요청의_응답은_CONFLICT(result);
    }

    @Test
    void 카테고리가_존재하지_않으면_등록_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        등록을_시도했더니_없는_카테고리_정보이다();

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되었다();
        요청의_응답은_NOT_FOUND(result);
    }

    @Test
    void 모두_정상적으로_요청하면_등록_성공() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_등록요청이다();
        정상적으로_스토어가_등록되었다();

        // when
        ResultActions result = 등록_요청하다(스토어_등록요청);

        // then
        등록요청_서비스가_수행되었다();
        요청의_응답은_CREATED(result);
    }

    @Test
    void 운영정보를_입력하지_않으면_수정_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_운영정보로_요청했다(null);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 운영상태를_입력하지_않으면_수정_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_운영상태로_요청했다(null);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 운영기간을_입력하지_않으면_수정_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_운영기간으로_요청했다(null);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 상태를_입력하지_않으면_수정_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_상태정보로_요청했다(null);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 기본정보를_입력하지_않으면_수정_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_기본정보로_수정을_요청했다(null);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 스토어명을_입력하지_않아_수정_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_스토어명으로_요청했다(null);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            "세글자",
            "누가스토어명을30글자이상입력하래스토어명30글자라라고얘기했는데!!!!!"
    })
    void 스토어명을_5글자미만_30글자초과로_입력하면_수정_실패(String 스토어명) throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_스토어명으로_요청했다(스토어명);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 전화변호를_입력하지_않아_수정_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_전화번호로_요청했다(null);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            "012-123-123",
            "012-1234-12345",
            "012-12345-1234"
    })
    void 전화번호_형식이_맞지않아_수정_실패(String 전화번호) throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_전화번호로_요청했다(전화번호);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 이미지URL을_입력하지_않아_수정_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_이미지URL로_요청했다(null);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            "얘는이미지URL이아니지",
    })
    void 이미지URL이_형식에_맞지않아_수정_실패(String 이미지URL) throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_이미지URL로_요청했다(이미지URL);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 주소정보를_입력하지_않으면_수정_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_주소정보로_요청했다(null);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 상세주소를_입력하지_않아_수정_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_상세주소로_요청했다(null);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            "세글자",
            "솔직히 아무리 시골에 있다고 하더라도 인간적으로 100자 넘으면 그건 개미집 주소 아니냐" +
                    "주소를 어떻게 100글자를 채우지 더 이상 쓸게 없는데" +
                    "내 일기나 써야지" +
                    "오늘은 허리가 아파서 늦게 일어났다" +
                    "100글자가 넘었다"
    })
    void 상세주소가_형식에_맞지않아_수정_실패(String 주소) throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_상세주소로_요청했다(주소);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 안내정보를_입력하지_않으면_수정_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_안내정보로_요청했다(null);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }
    @Test
    void 소개글을_입력하지_않아_수정_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_소개글로_요청했다(null);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            "세글자",
            "이 편지는 영국에서 최초로 시작되어 일년에 한 바퀴 돌면서 받는 사람에게 행운을 주었고 지금은 당신에게로 옮겨진 이 편지는 4일 안에 당신 곁을 떠나야 합니다." +
                    "이 편지를 포함해서 7통을 행운이 필요한 사람에게 보내 주셔야 합니다." +
                    "복사를 해도 좋습니다. 혹 미신이라 하실지 모르지만 사실입니다." +
                    "영국에서 HGXWCH이라는 사람은 1930년에 이 편지를 받았습니다." +
                    "그는 비서에게 복사해서 보내라고 했습니다." +
                    "며칠 뒤에 복권이 당첨되어 20억을 받았습니다." +
                    "어떤 이는 이 편지를 받았으나 96시간 이내 자신의 손에서 떠나야 한다는 사실을 잊었습니다." +
                    "그는 곧 사직되었습니다." +
                    "나중에야 이 사실을 알고 7통의 편지를 보냈는데 다시 좋은 직장을 얻었습니다." +
                    "미국의 케네디 대통령은 이 편지를 받았지만 그냥 버렸습니다. 결국 9일 후 그는 암살 당했습니다." +
                    "기억해 주세요. 이 편지를 보내면 7년의 행운이 있을 것이고 그렇지 않으면 3년의 불행이 있을 것입니다." +
                    "그리고 이 편지를 버리거나 낙서를 해서는 절대로 안됩니다." +
                    "7통입니다." +
                    "이 편지를 받은 사람은 행운이 깃들 것입니다." +
                    "힘들겠지만 좋은게 좋다고 생각하세요. "
    })
    void 소개글을_50글자미만_300글자초과로_입력하면_수정_실패(String 소개글) throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_소개글로_요청했다(소개글);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 안내글을_입력하지_않아_수정_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_안내글로_요청했다(null);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            "세글자",
            "안내글이에요." +
                    "이제부터 300글자 초과를 만들어볼거에요" +
                    "이 편지는 영국에서 최초로 시작되어 일년에 한 바퀴 돌면서 받는 사람에게 행운을 주었고 지금은 당신에게로 옮겨진 이 편지는 4일 안에 당신 곁을 떠나야 합니다." +
                    "이 편지를 포함해서 7통을 행운이 필요한 사람에게 보내 주셔야 합니다." +
                    "복사를 해도 좋습니다. 혹 미신이라 하실지 모르지만 사실입니다." +
                    "영국에서 HGXWCH이라는 사람은 1930년에 이 편지를 받았습니다." +
                    "그는 비서에게 복사해서 보내라고 했습니다." +
                    "며칠 뒤에 복권이 당첨되어 20억을 받았습니다." +
                    "어떤 이는 이 편지를 받았으나 96시간 이내 자신의 손에서 떠나야 한다는 사실을 잊었습니다." +
                    "그는 곧 사직되었습니다." +
                    "나중에야 이 사실을 알고 7통의 편지를 보냈는데 다시 좋은 직장을 얻었습니다." +
                    "미국의 케네디 대통령은 이 편지를 받았지만 그냥 버렸습니다. 결국 9일 후 그는 암살 당했습니다." +
                    "기억해 주세요. 이 편지를 보내면 7년의 행운이 있을 것이고 그렇지 않으면 3년의 불행이 있을 것입니다." +
                    "그리고 이 편지를 버리거나 낙서를 해서는 절대로 안됩니다." +
                    "7통입니다." +
                    "이 편지를 받은 사람은 행운이 깃들 것입니다." +
                    "힘들겠지만 좋은게 좋다고 생각하세요. " +
                    "이 편지는 영국에서 최초로 시작되어 일년에 한 바퀴 돌면서 받는 사람에게 행운을 주었고 지금은 당신에게로 옮겨진 이 편지는 4일 안에 당신 곁을 떠나야 합니다." +
                    "이 편지를 포함해서 7통을 행운이 필요한 사람에게 보내 주셔야 합니다." +
                    "복사를 해도 좋습니다. 혹 미신이라 하실지 모르지만 사실입니다." +
                    "영국에서 HGXWCH이라는 사람은 1930년에 이 편지를 받았습니다." +
                    "그는 비서에게 복사해서 보내라고 했습니다." +
                    "며칠 뒤에 복권이 당첨되어 20억을 받았습니다." +
                    "어떤 이는 이 편지를 받았으나 96시간 이내 자신의 손에서 떠나야 한다는 사실을 잊었습니다." +
                    "그는 곧 사직되었습니다." +
                    "나중에야 이 사실을 알고 7통의 편지를 보냈는데 다시 좋은 직장을 얻었습니다." +
                    "미국의 케네디 대통령은 이 편지를 받았지만 그냥 버렸습니다. 결국 9일 후 그는 암살 당했습니다." +
                    "기억해 주세요. 이 편지를 보내면 7년의 행운이 있을 것이고 그렇지 않으면 3년의 불행이 있을 것입니다." +
                    "그리고 이 편지를 버리거나 낙서를 해서는 절대로 안됩니다." +
                    "7통입니다." +
                    "이 편지를 받은 사람은 행운이 깃들 것입니다." +
                    "힘들겠지만 좋은게 좋다고 생각하세요. "
    })
    void 안내글을_200글자미만_1000글자초과로_입력하면_수정_실패(String 안내글) throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_안내글로_요청했다(안내글);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 원산지를_입력하지_않아_수정_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_원산지로_요청했다(null);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            "세글자",
            "원산지정보에요." +
                    "이제부터 500글자 초과를 만들어볼거에요" +
                    "이 편지는 영국에서 최초로 시작되어 일년에 한 바퀴 돌면서 받는 사람에게 행운을 주었고 지금은 당신에게로 옮겨진 이 편지는 4일 안에 당신 곁을 떠나야 합니다." +
                    "이 편지를 포함해서 7통을 행운이 필요한 사람에게 보내 주셔야 합니다." +
                    "복사를 해도 좋습니다. 혹 미신이라 하실지 모르지만 사실입니다." +
                    "영국에서 HGXWCH이라는 사람은 1930년에 이 편지를 받았습니다." +
                    "그는 비서에게 복사해서 보내라고 했습니다." +
                    "며칠 뒤에 복권이 당첨되어 20억을 받았습니다." +
                    "어떤 이는 이 편지를 받았으나 96시간 이내 자신의 손에서 떠나야 한다는 사실을 잊었습니다." +
                    "그는 곧 사직되었습니다." +
                    "나중에야 이 사실을 알고 7통의 편지를 보냈는데 다시 좋은 직장을 얻었습니다." +
                    "미국의 케네디 대통령은 이 편지를 받았지만 그냥 버렸습니다. 결국 9일 후 그는 암살 당했습니다." +
                    "기억해 주세요. 이 편지를 보내면 7년의 행운이 있을 것이고 그렇지 않으면 3년의 불행이 있을 것입니다." +
                    "그리고 이 편지를 버리거나 낙서를 해서는 절대로 안됩니다." +
                    "7통입니다." +
                    "이 편지를 받은 사람은 행운이 깃들 것입니다." +
                    "힘들겠지만 좋은게 좋다고 생각하세요. "
    })
    void 원산지를_200글자미만_1000글자초과로_입력하면_수정_실패(String 원산지) throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_원산지로_요청했다(원산지);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 운영시간정보를_입력하지_않으면_수정_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_운영시간정보로_요청했다(null);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 운영시간을_입력하지_않아_수정_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_운영시간으로_요청했다(null);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            "운영시간정보에요." +
                    "사실 이 정보는 길게 입력할 이유가 없어요." +
                    "100글자 이상되는 게 이상하죠. 안그래요?" +
                    "그냥 간단하게 연중무휴.. 평일 8-21 이런식으로 입력하면 되는 정보에요" +
                    "이 이상 입력하면 TMI스런 정보가 들어가지 않았나 싶네요" +
                    "자기 생일이라도 써놨나?"
    })
    void 운영시간을_100글자초과로_입력하면_수정_실패(String 운영시간) throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_운영시간으로_요청했다(운영시간);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "휴무일 정보에요." +
                    "사실 이 정보는 길게 입력할 이유가 없어요." +
                    "100글자 이상되는 게 이상하죠. 안그래요?" +
                    "그냥 간단하게 연중무휴.. 평일 8-21 이런식으로 입력하면 되는 정보에요" +
                    "이 이상 입력하면 TMI스런 정보가 들어가지 않았나 싶네요" +
                    "자기 생일이라도 써놨나?"
    })
    void 휴무일을_100글자초과로_입력하면_수정_실패(String 휴무일) throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_휴무일로_요청했다(휴무일);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "휴무일 정보에요." +
                    "사실 이 정보는 길게 입력할 이유가 없어요." +
                    "100글자 이상되는 게 이상하죠. 안그래요?" +
                    "그냥 간단하게 연중무휴.. 평일 8-21 이런식으로 입력하면 되는 정보에요" +
                    "이 이상 입력하면 TMI스런 정보가 들어가지 않았나 싶네요" +
                    "자기 생일이라도 써놨나?"
    })
    void 휴게시간을_100글자초과로_입력하면_수정_실패(String 휴게시간) throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_휴게시간으로_요청했다(휴게시간);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 사업자등록정보를_입력하지_않으면_수정_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_사업자등록정보로_요청했다(null);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 사업자등록번호를_입력하지_않아_수정_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_사업자등록번호로_요청했다(null);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            "012123",
            "0123456789",
            "01-23-45678",
            "012-3-45678",
            "012-34-567",
    })
    void 사업자등록번호_형식이_맞지않아_수정_실패(String 사업자등록번호) throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_사업자등록번호로_요청했다(사업자등록번호);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 계좌정보를_입력하지_않으면_수정_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_계좌정보로_수정을_요청했다(null);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 은행정보를_입력하지_않아_수정_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_은행으로_요청했다(null);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 계좌정보를_입력하지_않아_수정_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        given(스토어_수정요청.getBankAccount()).willReturn(null);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 계좌번호를_입력하지_않아_수정_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_계좌번호_요청했다(null);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 카테고리목록을_입력하지_않아_수정_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_카테고리목록으로_수정을_요청했다(null);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 카테고리목록을_empty하게_요청하면_수정_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_카테고리목록으로_수정을_요청했다(Collections.emptySet());

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 결제방식목록을_입력하지_않아_수정_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_결제방식목록으로_수정을_요청했다(null);

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 결제방식목록을_empty하게_요청하면_수정_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        다음과같은_결제방식목록으로_수정을_요청했다(Collections.emptySet());

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되지_않았다();
        요청의_응답은_BAD_REQUEST(result);
    }

    @Test
    void 셀러가_존재하지_않으면_수정_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        수정을_시도했더니_없는_스토어_정보이다();

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되었다();
        요청의_응답은_NOT_FOUND(result);
    }

    @Test
    void 스토어명이_겹치면_수정_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        수정을_시도했더니_겹치는_스토어명이다();

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되었다();
        요청의_응답은_CONFLICT(result);
    }

    @Test
    void 카테고리가_존재하지_않으면_수정_실패() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();
        수정을_시도했더니_없는_카테고리_정보이다();

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되었다();
        요청의_응답은_NOT_FOUND(result);
    }

    @Test
    void 모두_정상적으로_요청하면_수정_성공() throws Exception {
        모든_프로퍼티가_정상적으로_입력된_수정요청이다();

        // when
        ResultActions result = 수정_요청하다(스토어_수정요청);

        // then
        수정요청_서비스가_수행되었다();
        요청의_응답은_OK(result);
    }

    /**
     * given
     */

    private void 다음과같은_기본정보로_등록을_요청했다(StoreBasicRequest 기본정보_요청) {
        given(스토어_등록요청.getBasic()).willReturn(기본정보_요청);
    }

    private void 다음과같은_기본정보로_수정을_요청했다(StoreBasicRequest 기본정보_요청) {
        given(스토어_수정요청.getBasic()).willReturn(기본정보_요청);
    }

    private void 다음과같은_주소정보로_요청했다(AddressRequest 주소정보_요청) {
        given(기본정보_요청.getAddress()).willReturn(주소정보_요청);
    }

    private void 다음과같은_안내정보로_요청했다(StoreAnnouncementRequest 안내정보_요청) {
        given(기본정보_요청.getStoreIntroduce()).willReturn(안내정보_요청);
    }

    private void 다음과같은_운영시간정보로_요청했다(BusinessHoursRequest 운영시간정보_요청) {
        given(기본정보_요청.getOperationTime()).willReturn(운영시간정보_요청);
    }

    private void 다음과같은_사업자등록정보로_요청했다(BusinessLicenseRequest 사업자등록정보_요청) {
        given(기본정보_요청.getBusinessLicense()).willReturn(사업자등록정보_요청);
    }

    private void 다음과같은_계좌정보로_등록을_요청했다(BankAccountRequest 계좌정보_요청) {
        given(스토어_등록요청.getBankAccount()).willReturn(계좌정보_요청);
    }

    private void 다음과같은_계좌정보로_수정을_요청했다(BankAccountRequest 계좌정보_요청) {
        given(스토어_수정요청.getBankAccount()).willReturn(계좌정보_요청);
    }

    private void 다음과같은_셀러아이디로_요청했다(Long 셀러아이디) {
        given(스토어_등록요청.getSellerId()).willReturn(셀러아이디);
    }

    private void 다음과같은_스토어명으로_요청했다(String 스토어명) {
        given(기본정보_요청.getName()).willReturn(스토어명);
    }

    private void 다음과같은_전화번호로_요청했다(String 전화번호) {
        given(기본정보_요청.getTel()).willReturn(전화번호);
    }

    private void 다음과같은_이미지URL로_요청했다(String 이미지URL) {
        given(기본정보_요청.getImageUrl()).willReturn(이미지URL);
    }

    private void 다음과같은_상세주소로_요청했다(String 주소) {
        given(주소정보_요청.getDetailAddress()).willReturn(주소);
    }

    private void 다음과같은_소개글로_요청했다(String 소개글) {
        given(안내정보_요청.getIntroduce()).willReturn(소개글);
    }

    private void 다음과같은_안내글로_요청했다(String 안내글) {
        given(안내정보_요청.getInformation()).willReturn(안내글);
    }

    private void 다음과같은_원산지로_요청했다(String 안내글) {
        given(안내정보_요청.getOriginCountry()).willReturn(안내글);
    }

    private void 다음과같은_운영시간으로_요청했다(String 운영시간) {
        given(운영시간정보_요청.getBusinessHour()).willReturn(운영시간);
    }

    private void 다음과같은_휴무일로_요청했다(String 휴무일) {
        given(운영시간정보_요청.getHoliday()).willReturn(휴무일);
    }

    private void 다음과같은_휴게시간으로_요청했다(String 휴게시간) {
        given(운영시간정보_요청.getBreakTime()).willReturn(휴게시간);
    }

    private void 다음과같은_사업자등록번호로_요청했다(String 사업자등록번호) {
        given(사업자등록증정보_요청.getBusinessLicenseNumber()).willReturn(사업자등록번호);
    }

    private void 다음과같은_은행으로_요청했다(BankType 은행) {
        given(계좌정보_요청.getBankType()).willReturn(은행);
    }

    private void 다음과같은_계좌번호_요청했다(String 계좌번호) {
        given(계좌정보_요청.getAccountNumber()).willReturn(계좌번호);
    }

    private void 다음과같은_카테고리목록으로_등록을_요청했다(Set<Long> 카테고리아이디_목록) {
        given(스토어_등록요청.getCategoryIds()).willReturn(카테고리아이디_목록);
    }

    private void 다음과같은_결제방식목록으로_등록을_요청했다(Set<PayType> 결제방식아이디_목록) {
        given(스토어_등록요청.getPayMethods()).willReturn(결제방식아이디_목록);
    }

    private void 다음과같은_카테고리목록으로_수정을_요청했다(Set<Long> 카테고리아이디_목록) {
        given(스토어_수정요청.getCategoryIds()).willReturn(카테고리아이디_목록);
    }

    private void 다음과같은_결제방식목록으로_수정을_요청했다(Set<PayType> 결제방식아이디_목록) {
        given(스토어_수정요청.getPayMethods()).willReturn(결제방식아이디_목록);
    }

    private void 다음과같은_운영정보로_요청했다(StoreOperationRequest 운영정보) {
        given(스토어_수정요청.getOperation()).willReturn(운영정보);
    }

    private void 다음과같은_운영상태로_요청했다(OperationStatus 상태) {
        given(운영정보_요청.getStatus()).willReturn(상태);
    }

    private void 다음과같은_운영기간으로_요청했다(TimePeriod 기간) {
        given(운영정보_요청.getPeriod()).willReturn(기간);
    }

    private void 다음과같은_상태정보로_요청했다(StoreStatus 상태) {
        given(스토어_수정요청.getStatus()).willReturn(상태);
    }

    private void 모든_프로퍼티가_정상적으로_입력된_등록수정_요청이다() {
        다음과같은_스토어명으로_요청했다("테스트스토어");
        다음과같은_전화번호로_요청했다("010-1234-1234");
        다음과같은_이미지URL로_요청했다("https://avatars.githubusercontent.com/u/55195022?s=200&v=4");
        다음과같은_상세주소로_요청했다("경기도 성남시 분당구 판교동 1111번지 유스페이스 a동");
        다음과같은_소개글로_요청했다("우리집은 닭발과 곱창을 같이 팔아요. " +
                "두마리 토끼를 어떻게 잡냐라는 분이 계신데 호식이도 두마리 치킨을 하는데 저라고 못하겠어요?");
        다음과같은_안내글로_요청했다("우리 가게가 성남에서 가장 많이 팔리는 곱창집이 되었어요~" +
                "짝짝짝짝작짝짝짝짝작짝짝짝짝작 모두 박수박수~~ 대박대박~~" +
                "이정도면 대통령상도 받아야할 각인것같은데.. 뭐 암튼 우리만한 곱창집이 어딨겠습니까" +
                "개발자가 안내글을 최소 200자를 원해서 이렇게 작성을 하고 있는데 솔직히 즁요한 내용은 1도 없어요" +
                "테스트코드 짜는 것도 나름 일이네요 에혀" +
                "심심하니까 엘라스틱서치에 대한 얘기좀 해볼게요" +
                "엘라스틱서치는 역색인의 구조를 가지고 있죠" +
                "또 분산형이기 때문에 그래서 검색에 아주 특화되어있는 엔진 및 시스템이에요" +
                "엘라스틱서치 쓰기 전에는 검색을 대체 어떻게 했을까요?" +
                "정말 느려터졌을텐데.. 하여간 검색을 위해 정말 꼭 필요한 시스템인것 같아요" +
                "설마 아직도 200글자가 안넘었겠어요?");
        다음과같은_원산지로_요청했다("소고기 : 국내산, 돼지고기 : 국내산, 김치 : 중국산");
        다음과같은_운영시간으로_요청했다("연중무휴");
        다음과같은_운영시간으로_요청했다("주말");
        다음과같은_휴게시간으로_요청했다("4-5pm");
        다음과같은_사업자등록번호로_요청했다("012-34-56789");
        다음과같은_은행으로_요청했다(BankType.KB);
        다음과같은_계좌번호_요청했다("000000-00-000000");
    }

    private void 모든_프로퍼티가_정상적으로_입력된_등록요청이다() {
        모든_프로퍼티가_정상적으로_입력된_등록수정_요청이다();

        다음과같은_셀러아이디로_요청했다(1L);
        다음과같은_카테고리목록으로_등록을_요청했다(Set.of(1L));
        다음과같은_결제방식목록으로_등록을_요청했다(Set.of(PayType.ACCOUNT_TRANSFER));
    }

    private void 모든_프로퍼티가_정상적으로_입력된_수정요청이다() {
        모든_프로퍼티가_정상적으로_입력된_등록수정_요청이다();

        다음과같은_운영상태로_요청했다(OperationStatus.OPERATING);
        다음과같은_운영기간으로_요청했다(TimePeriod.of(LocalDateTime.now(), LocalDateTime.now()));
        다음과같은_상태정보로_요청했다(StoreStatus.ACTIVATED);
        다음과같은_카테고리목록으로_수정을_요청했다(Set.of(1L));
        다음과같은_결제방식목록으로_수정을_요청했다(Set.of(PayType.ACCOUNT_TRANSFER));
    }

    /**
     * when
     */
    private ResultActions 등록_요청하다(StoreCreateRequest 스토어_등록요청) throws Exception {
        return mockMvc.perform(
                post(BASE_URL)
                        .content(mapper.writeValueAsString(스토어_등록요청))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());
    }

    private ResultActions 수정_요청하다(StoreUpdateRequest 스토어_수정요청) throws Exception {
        return mockMvc.perform(
                put(BASE_URL + "/" + 1)
                        .content(mapper.writeValueAsString(스토어_수정요청))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());
    }

    /**
     * then
     */

    private void 등록요청_서비스가_수행되지_않았다() {
        then(service).should(never()).create(any());
    }

    private void 등록요청_서비스가_수행되었다() {
        then(service).should().create(any());
    }

    private void 정상적으로_스토어가_등록되었다() {
        given(service.create(스토어_등록요청)).willReturn(1L);
    }

    private void 등록을_시도했더니_없는_판매자_정보이다() {
        doAnswer(m -> {
            throw new HoneyBreadException(ErrorCode.USER_NOT_FOUND);
        }).when(service).create(any());
    }

    private void 등록을_시도했더니_겹치는_스토어명이다() {
        doAnswer(m -> {
            throw new HoneyBreadException(ErrorCode.DUPLICATE_STORE_NAME);
        }).when(service).create(any());
    }

    private void 등록을_시도했더니_없는_카테고리_정보이다() {
        doAnswer(m -> {
            throw new HoneyBreadException(ErrorCode.CATEGORY_NOT_FOUND);
        }).when(service).create(any());
    }

    private void 수정요청_서비스가_수행되었다() {
        then(service).should().update(anyLong(), any());
    }

    private void 수정요청_서비스가_수행되지_않았다() {
        then(service).should(never()).update(anyLong(), any());
    }

    private void 수정을_시도했더니_없는_스토어_정보이다() {
        doAnswer(m -> {
            throw new HoneyBreadException(ErrorCode.STORE_NOT_FOUND);
        }).when(service).update(anyLong(), any());
    }

    private void 수정을_시도했더니_겹치는_스토어명이다() {
        doAnswer(m -> {
            throw new HoneyBreadException(ErrorCode.DUPLICATE_STORE_NAME);
        }).when(service).update(anyLong(), any());
    }

    private void 수정을_시도했더니_없는_카테고리_정보이다() {
        doAnswer(m -> {
            throw new HoneyBreadException(ErrorCode.CATEGORY_NOT_FOUND);
        }).when(service).update(anyLong(), any());
    }

    private void 요청의_응답은_BAD_REQUEST(ResultActions result) throws Exception {
        result.andExpect(status().isBadRequest());
    }

    private void 요청의_응답은_OK(ResultActions result) throws Exception {
        result.andExpect(status().isOk());
    }

    private void 요청의_응답은_CREATED(ResultActions result) throws Exception {
        result.andExpect(status().isCreated());
    }

    private void 요청의_응답은_NOT_FOUND(ResultActions result) throws Exception {
        result.andExpect(status().isNotFound());
    }

    private void 요청의_응답은_CONFLICT(ResultActions result) throws Exception {
        result.andExpect(status().isConflict());
    }

}
