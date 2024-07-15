package com.example.rsupport.noticeboard.dto.request;

import com.example.rsupport.noticeboard.entity.Notice;
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
    private Long noticeId;
    private String title;
    private String content;
    private String author;
    private String postPw;
    private String fileChangeYn;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
    private MultipartFile[] files;

    public void updateNotice(Notice notice) {
        notice.setTitle(this.title);
        notice.setContent(this.content);
        notice.setStartDate(this.startDate);
        notice.setEndDate(this.endDate);
        notice.setUpdatedDate(LocalDateTime.now());
    }
}
