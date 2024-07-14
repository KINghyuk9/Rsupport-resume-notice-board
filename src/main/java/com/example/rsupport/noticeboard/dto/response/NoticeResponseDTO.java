package com.example.rsupport.noticeboard.dto.response;

import com.example.rsupport.noticeboard.entity.Notice;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeResponseDTO {

    private Long noticeId;
    private String title;
    private String content;
    private String author;
    private int views;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    public static NoticeResponseDTO fromNotice(Notice notice){
        return new NoticeResponseDTO(
                notice.getNoticeId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getAuthor(),
                notice.getViews(),
                notice.getCreateDate()
        );
    }
}
