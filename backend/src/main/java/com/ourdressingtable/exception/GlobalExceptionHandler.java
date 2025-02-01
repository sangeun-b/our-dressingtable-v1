package com.ourdressingtable.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OurDressingTableException.class)
    public ResponseEntity<ErrorResponse> handleOurDressingTableException(OurDressingTableException ex) {
        log.error("ourDressingTableException", ex);
        ErrorResponse errorResponse = new ErrorResponse(ex.getHttpStatus(), ex.getCode(), ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        log.error("runtimeException", ex);
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INTERNAL_SEVER_ERROR.getHttpStatus(), ErrorCode.INTERNAL_SEVER_ERROR.getCode(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }



}
