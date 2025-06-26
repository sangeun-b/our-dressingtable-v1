package com.ourdressingtable.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 회원 관련
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "M001", "이미 가입된 이메일입니다."),
    INVALID_MEMBER(HttpStatus.BAD_REQUEST, "M002", "유효하지 않은 사용자입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M003", "사용자를 찾을 수 없습니다."),
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "M004", "유효하지 않은 이메일 형식입니다."),
    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "M005", "비밀번호가 일치하지 않습니다."),
    INVALID_PASSWORD_FORMAT(HttpStatus.BAD_REQUEST, "M006",
            "비밀번호는 최소 8자 이상, 알파벳 대/소문자, 숫자, 특수문자를 포함해야 합니다."),
    MEMBER_NOT_ACTIVE(HttpStatus.BAD_REQUEST, "M007", "활동 불가 회원입니다."),
    ALREADY_WITHDRAW_OR_BLOCKED(HttpStatus.BAD_REQUEST, "M008", "이미 탈퇴 또는 차단된 회원입니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "M009", "이미 사용중인 닉네임입니다."),
    WITHDRAWN_MEMBER_RESTRICTED(HttpStatus.FORBIDDEN, "M010", "탈퇴한 회원은 일정 기간 내 재가입할 수 없습니다."),
    WITHDRAWN_BLOCK_MEMBER_RESTRICTED(HttpStatus.FORBIDDEN, "M11", "차단 당한 회원은 재가입 할 수 없습니다."),
    MEMBER_EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "M12", "가입된 이메일이 존재하지 않거나 확인할 수 없습니다."),

    // 인증/인가 관련
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A001", "로그인이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "A002", "접근 권한이 없습니다."),
    LOGIN_FAIL(HttpStatus.UNAUTHORIZED, "A003", "이메일 또는 비밀번호가 일치하지 않습니다."),
    EMAIL_NOT_VERIFIED(HttpStatus.BAD_REQUEST, "A004", "이메일 인증이 완료되지 않았습니다."),
    EXPIRED_VERIFICATION_CDOE(HttpStatus.BAD_REQUEST, "A005", "만료된 인증 코드입니다."),
    INVALID_VERIFICATION_CDOE(HttpStatus.BAD_REQUEST, "A006", "잘못된 인증 코드입니다."),
    HASHING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "A007", "토큰 해싱에 실패했습니다."),

    // TOO_MANY_REQUEST
    TOO_MANY_LOGIN_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "T001", "잠시 후 로그인 시도해주십시오."),
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "T002", "요청이 너무 많습니다. 잠시 후 다시 시도해주세요."),

    // BAD_REQUEST
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "B001", "잘못된 접근입니다."),

    // INTERNAL_SEVER_ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S001", "서버 에러입니다."),

    // 커뮤니티 관련
    COMMUNITY_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "C001", "카테고리를 찾을 수 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "C002", "게시글을 찾을 수 없습니다."),
    NO_PERMISSION_TO_EDIT(HttpStatus.FORBIDDEN, "C003", "게시글에 권한이 없습니다."),
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "C004", "좋아요 기록이 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "C005", "댓글을 찾을 수 없습니다."),
    INVALID_COMMENT_INPUT(HttpStatus.BAD_REQUEST, "C006", "댓글 내용을 입력해주세요."),

    // 화장대 관련
    DRESSING_TABLE_NOT_FOUND(HttpStatus.NOT_FOUND, "D001", "화장대를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}