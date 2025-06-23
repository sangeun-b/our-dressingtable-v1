package com.ourdressingtable.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class OurDressingTableException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String code;

    public OurDressingTableException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.httpStatus = errorCode.getHttpStatus();
        this.code = errorCode.getCode();
    }

    public OurDressingTableException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.httpStatus = errorCode.getHttpStatus();
        this.code = errorCode.getCode();
    }

    public OurDressingTableException(ErrorCode errorCode, String msg) {
        super(msg);
        this.httpStatus = errorCode.getHttpStatus();
        this.code = errorCode.getCode();
    }
}
