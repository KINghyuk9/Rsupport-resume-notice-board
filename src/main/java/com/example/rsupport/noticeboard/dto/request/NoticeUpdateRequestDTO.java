package com.example.rsupport.noticeboard.dto.request;

import com.example.rsupport.noticeboard.entity.Notice;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeUpdateRequestDTO {
    @NotNull(message = "게시글 번호가 식별되지 않습니다.")
    private Long noticeId;
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
    @NotBlank(message = "작성자 아이디를 입력해주세요.")
    private String userId;
    @NotNull(message = "시작일을 입력해주세요.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(type = "string", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @NotNull(message = "종료일을 입력해주세요.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(type = "string", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    public void updateNotice(Notice notice) {
        notice.setTitle(this.title);
        notice.setContent(this.content);
        notice.setStartDate(this.startDate);
        notice.setEndDate(this.endDate);
        notice.setUpdatedDate(LocalDateTime.now());
    }
}
