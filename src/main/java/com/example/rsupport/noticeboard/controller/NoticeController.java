package com.example.rsupport.noticeboard.controller;

import com.example.rsupport.noticeboard.common.ApiResult;
import com.example.rsupport.noticeboard.dto.request.NoticeCreateRequestDTO;
import com.example.rsupport.noticeboard.dto.request.NoticeUpdateRequestDTO;
import com.example.rsupport.noticeboard.dto.response.NoticeCreateResponseDTO;
import com.example.rsupport.noticeboard.dto.response.NoticeDetailResponseDTO;
import com.example.rsupport.noticeboard.dto.response.NoticeResponseDTO;
import com.example.rsupport.noticeboard.dto.response.NoticeUpdateResponseDTO;
import com.example.rsupport.noticeboard.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/board/notice")
public class NoticeController {
    private final NoticeService noticeService;

    @Autowired
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "공지사항 등록", description = "공지사항 등록")
    public ResponseEntity<ApiResult> createNotice(
            @RequestPart(value = "files", required = false) MultipartFile[] files,
            @Valid @RequestPart NoticeCreateRequestDTO noticeCreateRequestDTO) {
        NoticeCreateResponseDTO responseDTO = noticeService.createNotice(noticeCreateRequestDTO, files);
        return ApiResult.ok(responseDTO);
    }


    @DeleteMapping("/delete/{noticeId}/{userId}")
    @Operation(summary = "공지사항 삭제", description = "공지사항 삭제")
    public ResponseEntity<ApiResult> deleteNotice(
            @PathVariable("noticeId") Long id,
            @PathVariable String userId) {
        noticeService.deleteNotice(id, userId);
        return ApiResult.ok("공지사항이 삭제되었습니다.");
    }

    @GetMapping("/search-notice-detail/{noticeId}")
    @Operation(summary = "공지사항 상세 조회", description = "공지사항 상세 조회")
    public ResponseEntity<ApiResult> getNoticeDetail(
            @PathVariable("noticeId") Long id) {
        NoticeDetailResponseDTO notice = noticeService.getNoticeDetail(id);
        return ApiResult.ok(notice);
    }

    @GetMapping("/search-with-condition")
    @Operation(summary = "공지사항 조건 검색", description = "공지사항 [제목, 내용, 작성자] 기준 검색")
    public ResponseEntity<ApiResult> getNoticeSearch(
            @RequestParam String searchType,
            @RequestParam String keyword,
            Pageable pageable) {
        Page<NoticeResponseDTO> noticeList = noticeService.getNoticeSearch(searchType, keyword, pageable);
        return ApiResult.ok(noticeList);
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "공지사항 수정", description = "공지사항 수정")
    public ResponseEntity<ApiResult> updateNotice(
            @RequestPart(value = "files", required = false)MultipartFile[] files,
            @Valid @RequestPart NoticeUpdateRequestDTO noticeUpdateRequestDTO){
        NoticeUpdateResponseDTO responseDTO = noticeService.updateNotice(noticeUpdateRequestDTO, files);
        return ApiResult.ok(responseDTO);
    }

}