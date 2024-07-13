package com.example.rsupport.noticeboard.common;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
public class ApiResult {

    private boolean success;
    private String message;
    private String code;
    private Object data;

    private ApiResult(boolean success, String message, String code, Object data) {
        this.success = success;
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public static ResponseEntity<ApiResult> ok(Object data) {
        return ResponseEntity.ok(new ApiResult(true, "success", "200", data));
    }

    public static ResponseEntity<ApiResult> error(String message, String code, HttpStatus status) {
        return ResponseEntity.status(status).body(new ApiResult(false, message, code, null));
    }
}
