package com.ourdressingtable.exception;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("methodArgumentNotValidException", ex);
        List<ErrorResponse> errorResponses = new ArrayList<>();
        for(FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            // TODO: 추후 필드 별 예외 처리 추가

//            if(fieldError.getField().equals("email")) {
//                errorResponses.add(new ErrorResponse(ErrorCode.INVALID_EMAIL_FORMAT.getHttpStatus(), ErrorCode.BAD_REQUEST.getCode(), ErrorCode.BAD_REQUEST.getMessage()));
//            }
            errorResponses.add(new ErrorResponse(ErrorCode.BAD_REQUEST.getHttpStatus(), ErrorCode.BAD_REQUEST.getCode(), fieldError.getDefaultMessage()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponses);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        log.error("runtimeException", ex);
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INTERNAL_SEVER_ERROR.getHttpStatus(), ErrorCode.INTERNAL_SEVER_ERROR.getCode(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }



}
