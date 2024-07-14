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

    public static NoticeDetailResponseDTO of(Notice notice, List<FileListDTO> files) {
        return new NoticeDetailResponseDTO(
                notice.getNoticeId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getAuthor(),
                notice.getCreateDate(),
                notice.getUpdatedDate(),
                notice.getStartDate(),
                notice.getEndDate(),
                notice.getViews(),
                files);
    }
}
