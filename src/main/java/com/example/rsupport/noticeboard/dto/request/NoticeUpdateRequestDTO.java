package com.example.rsupport.noticeboard.dto.request;

import com.example.rsupport.noticeboard.entity.Notice;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeUpdateRequestDTO {
    @NotNull
    private Long noticeId;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotBlank
    private String author;
    @NotBlank
    private String userId;
    @NotBlank
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @NotBlank
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    public void updateNotice(Notice notice) {
        notice.setTitle(this.title);
        notice.setContent(this.content);
        notice.setStartDate(this.startDate);
        notice.setEndDate(this.endDate);
        notice.setUpdatedDate(LocalDateTime.now());
    }
}
