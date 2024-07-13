package com.example.rsupport.noticeboard.dto;

import lombok.Data;

@Data
public class NoticeDeleteRequestDTO {
    private Long noticeId;
    private String author;
    private String postPw;
}
