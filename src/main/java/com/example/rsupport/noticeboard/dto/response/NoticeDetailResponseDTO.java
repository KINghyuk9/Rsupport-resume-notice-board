package com.example.rsupport.noticeboard.dto.response;

import com.example.rsupport.noticeboard.dto.common.FileListDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NoticeDetailResponseDTO {

    private Long noticeId;
    private String title;
    private String content;
    private String author;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
    private int views;
    private List<FileListDTO> files;

    public NoticeDetailResponseDTO(Long noticeId,String title, String content, String author, LocalDateTime createdDate, LocalDateTime updatedDate, LocalDateTime startDate, LocalDateTime endDate, int views, List<FileListDTO> files) {
        this.noticeId = noticeId;
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.views = views;
        this.files = files;
    }

    public static NoticeDetailResponseDTO of(Long noticeId, String title, String content, String author, LocalDateTime createdDate,LocalDateTime updatedDate, LocalDateTime startDate, LocalDateTime endDate, int views, List<FileListDTO> files) {
        return new NoticeDetailResponseDTO(noticeId, title, content, author, createdDate, updatedDate, startDate, endDate, views, files);
    }
}
