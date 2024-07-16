package com.example.rsupport.noticeboard.common;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

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

    public static ResponseEntity<ApiResult> ok(Page<?> page){
        Map<String, Object> result = new HashMap<>();
        result.put("content", page.getContent());
        result.put("totalPages", page.getTotalPages());
        result.put("totalElements", page.getTotalElements());
        result.put("size", page.getSize());
        result.put("number", page.getNumber());
        result.put("numberOfElements", page.getNumberOfElements());
        result.put("first", page.isFirst());
        result.put("last", page.isLast());
        result.put("empty", page.isEmpty());
        return ResponseEntity.ok(new ApiResult(true, "success", "200", result));
    }

    public static ResponseEntity<ApiResult> error(Object data, String code, HttpStatus status) {
        return ResponseEntity.status(status).body(new ApiResult(false, "error", code, data));
    }
}
