package com.example.rsupport.noticeboard.controller;

import com.example.rsupport.noticeboard.common.ApiResult;
import com.example.rsupport.noticeboard.dto.NoticeCreateRequestDTO;
import com.example.rsupport.noticeboard.dto.NoticeDeleteRequestDTO;
import com.example.rsupport.noticeboard.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board/notice")
public class NoticeController {
    private final NoticeService noticeService;

    @Autowired
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @PostMapping("/create")
    @Operation(summary = "공지사항 등록", description = "공지사항 등록")
    public ResponseEntity<ApiResult> createNotice(
            @ModelAttribute NoticeCreateRequestDTO noticeCreateRequestDTO) {
        try {
            noticeService.createNotice(noticeCreateRequestDTO);
            return ApiResult.ok("공지사항이 등록되었습니다.");
        } catch (IllegalArgumentException e) {
            return ApiResult.error(e.getMessage(), "E1001", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    @Operation(summary = "공지사항 삭제", description = "공지사항 삭제")
    public ResponseEntity<ApiResult> deleteNotice(
            @RequestBody NoticeDeleteRequestDTO noticeDeleteRequestDTO) {
        try {
            noticeService.deleteNotice(noticeDeleteRequestDTO);
            return ApiResult.ok("공지사항이 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            return ApiResult.error(e.getMessage(), "E1002", HttpStatus.BAD_REQUEST);
        }
    }
}