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

    private Notice notice;

    public Long getNoticeId() {
        return notice.getNoticeId();
    }

    public String getTitle() {
        return notice.getTitle();
    }

    public String getContent() {
        return notice.getContent();
    }

    public String getAuthor() {
        return notice.getAuthor();
    }

    public String getUserId() {
        return notice.getUserId();
    }

    public LocalDateTime getStartDate() {
        return notice.getStartDate();
    }

    public LocalDateTime getEndDate() {
        return notice.getEndDate();
    }

    public int getViews() {
        return notice.getViews();
    }

    public List<FileListDTO> getFiles() {
        return notice.getFiles().stream()
                .map(file -> new FileListDTO(file.getFileId(), file.getFileName()))
                .toList();
    }
}
