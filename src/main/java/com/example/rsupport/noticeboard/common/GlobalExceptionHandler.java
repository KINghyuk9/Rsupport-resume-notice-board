package com.example.rsupport.noticeboard.common;

import com.example.rsupport.noticeboard.exception.InvalidPasswordException;
import com.example.rsupport.noticeboard.exception.NoticeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoticeNotFoundException.class)
    public ResponseEntity<ApiResult> handleNoticeNotFoundException(NoticeNotFoundException e) {
        return ApiResult.error(e.getMessage(), "NOTICE_NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ApiResult> handleInvalidPasswordException(InvalidPasswordException e) {
        return ApiResult.error(e.getMessage(), "INVALID_PASSWORD", HttpStatus.BAD_REQUEST);
    }
}
