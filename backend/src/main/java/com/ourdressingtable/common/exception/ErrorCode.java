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

    // 인증/인가 관련
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A001", "로그인이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "A002", "접근 권한이 없습니다."),

    // BAD_REQUEST
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "B001", "잘못된 접근입니다."),

    // INTERNAL_SEVER_ERROR
    INTERNAL_SEVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S001", "서버 에러입니다."),

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