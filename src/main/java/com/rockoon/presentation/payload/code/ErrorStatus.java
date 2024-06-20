package com.rockoon.presentation.payload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseCode{
    // 서버 오류
    _INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, 5000, "서버 에러, 관리자에게 문의 바랍니다."),
    _UNAUTHORIZED_LOGIN_DATA_RETRIEVAL_ERROR(INTERNAL_SERVER_ERROR, 5001, "서버 에러, 로그인이 필요없는 요청입니다."),
    _ASSIGNABLE_PARAMETER(BAD_REQUEST, 5002, "인증타입이 잘못되어 할당이 불가능합니다."),

    // 일반적인 요청 오류
    _BAD_REQUEST(BAD_REQUEST, 4000, "잘못된 요청입니다."),
    _UNAUTHORIZED(UNAUTHORIZED, 4001, "로그인이 필요합니다."),
    _FORBIDDEN(FORBIDDEN, 4002, "금지된 요청입니다."),

    //인증 관련 오류(4050 ~ 4099)
    AUTH_INVALID_TOKEN(BAD_REQUEST, 4050, "유효하지 않은 토큰입니다."),
    AUTH_INVALID_REFRESH_TOKEN(BAD_REQUEST,4051, "유효하지 않은 리프레시토큰입니다"),
    AUTH_TOKEN_HAS_EXPIRED(BAD_REQUEST, 4053, "토큰의 유효기간이 만료되었습니다."),
    AUTH_TOKEN_IS_UNSUPPORTED(BAD_REQUEST,4054, "서버에서 지원하는 형식의 토큰이 아닙니다."),
    AUTH_IS_NULL(BAD_REQUEST,4055, "토큰 값이 존재하지 않습니다.(null)"),
    //회원 관련 오류(4100 ~ 4149)
    MEMBER_NOT_FOUND(NOT_FOUND, 4100, "존재하지 않는 회원입니다."),

    //프로모션 관련 오류(4150 ~ 4199)
    PROMOTION_NOT_FOUND(NOT_FOUND, 4150, "존재하지 않는 회원입니다."),
    PROMOTION_ONLY_CAN_BE_TOUCHED_BY_WRITER(BAD_REQUEST, 4151, "작성자가 아닌 유저는 프로모션을 수정이 불가합니다."),

    //게스트 관련 오류(4200 ~ 4249)
    GUEST_NOT_FOUND(NOT_FOUND, 4200, "존재하지 않는 게스트입니다."),
    GUEST_ONLY_CAN_BE_TOUCHED_BY_CREATOR(BAD_REQUEST, 4201, "작성자가 아닌 유저는 게스트를 수정할 수 없습니다."),

    //티켓 관련 오류(4250 ~ 4300)
    TICKET_NOT_FOUND(NOT_FOUND, 4250, "존재하지 않는 티켓입니다.");

    private final HttpStatus httpStatus;
    private final Integer code;
    private final String message;


    @Override
    public Reason getReason() {
        return Reason.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public Reason getReasonHttpStatus() {
        return Reason.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
