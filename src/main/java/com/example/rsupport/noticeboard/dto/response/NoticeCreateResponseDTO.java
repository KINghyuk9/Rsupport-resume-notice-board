package com.example.rsupport.noticeboard.dto.response;

import com.example.rsupport.noticeboard.dto.common.FileListDTO;
import com.example.rsupport.noticeboard.entity.Notice;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeCreateResponseDTO {
    private Long noticeId;
    private String author;
    private String userId;
    private String title;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
    private int views;
    private List<FileListDTO> files;

    public NoticeCreateResponseDTO(Notice notice) {
        this.noticeId = notice.getNoticeId();
        this.author = notice.getAuthor();
        this.userId = notice.getUserId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.createdDate = notice.getCreateDate();
        this.startDate = notice.getStartDate();
        this.endDate = notice.getEndDate();
        this.views = notice.getViews();
        this.files = notice.getFiles().stream()
                .map(file -> new FileListDTO(file.getFileId(), file.getFileName()))
                .toList();
    }
}
