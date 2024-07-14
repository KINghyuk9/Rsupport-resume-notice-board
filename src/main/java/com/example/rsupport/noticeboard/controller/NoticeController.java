package com.example.rsupport.noticeboard.controller;

import com.example.rsupport.noticeboard.common.ApiResult;
import com.example.rsupport.noticeboard.dto.request.NoticeCreateRequestDTO;
import com.example.rsupport.noticeboard.dto.request.NoticeDeleteRequestDTO;
import com.example.rsupport.noticeboard.dto.response.NoticeDetailResponseDTO;
import com.example.rsupport.noticeboard.dto.response.NoticeResponseDTO;
import com.example.rsupport.noticeboard.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/search-notice-list")
    @Operation(summary = "공지사항 조회", description = "공지사항 조회")
    public ResponseEntity<ApiResult> getNoticeList(Pageable pageable) {
        try {
            Page<NoticeResponseDTO> noticeList = noticeService.getNoticeList(pageable);
            return ApiResult.ok(noticeList);
        } catch (IllegalArgumentException e) {
            return ApiResult.error(e.getMessage(), "E1003", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search-notice-detail/{id}")
    @Operation(summary = "공지사항 상세 조회", description = "공지사항 상세 조회")
    public ResponseEntity<ApiResult> getNoticeDetail(
            @PathVariable Long id) {
        try {
            NoticeDetailResponseDTO notice = noticeService.getNoticeDetail(id);
            return ApiResult.ok(notice);
        } catch (IllegalArgumentException e) {
            return ApiResult.error(e.getMessage(), "E1003", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search-with-condition")
    @Operation(summary = "공지사항 조건 검색", description = "공지사항 [제목, 내용, 작성자] 기준 검색")
    public ResponseEntity<ApiResult> getNoticeSearch(
            @RequestParam String searchType,
            @RequestParam String keyword,
            Pageable pageable) {
        try {
            Page<NoticeResponseDTO> noticeList = noticeService.getNoticeSearch(searchType, keyword, pageable);
            return ApiResult.ok(noticeList);
        } catch (IllegalArgumentException e) {
            return ApiResult.error(e.getMessage(), "E1003", HttpStatus.BAD_REQUEST);
        }
    }

}