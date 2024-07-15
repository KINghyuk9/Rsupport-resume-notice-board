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
}
