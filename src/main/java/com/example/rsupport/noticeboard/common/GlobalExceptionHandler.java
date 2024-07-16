package com.example.rsupport.noticeboard.common;

import com.example.rsupport.noticeboard.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoticeNotFoundException.class)
    public ResponseEntity<ApiResult> handleNoticeNotFoundException(NoticeNotFoundException e) {
        return ApiResult.error(e.getMessage(), "NOTICE_NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserIdMismatchException.class)
    public ResponseEntity<ApiResult> handleUserIdMismatchException(UserIdMismatchException e) {
        return ApiResult.error(e.getMessage(), "USER_ID_MISMATCH", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileSaveException.class)
    public ResponseEntity<ApiResult> handleFilerException(FileSaveException e) {
        return ApiResult.error(e.getMessage(), "FILER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoticeCreateException.class)
    public ResponseEntity<ApiResult> handleNoticeCreateException(NoticeCreateException e) {
        return ApiResult.error(e.getMessage(), "NOTICE_CREATE_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoticeDeleteException.class)
    public ResponseEntity<ApiResult> handleNoticeDeleteException(NoticeDeleteException e) {
        return ApiResult.error(e.getMessage(), "NOTICE_DELETE_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoticeDetailException.class)
    public ResponseEntity<ApiResult> handleNoticeDetailException(NoticeDetailException e) {
        return ApiResult.error(e.getMessage(), "NOTICE_DETAIL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoticeSearchException.class)
    public ResponseEntity<ApiResult> handleNoticeSearchException(NoticeSearchException e) {
        return ApiResult.error(e.getMessage(), "NOTICE_SEARCH_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
