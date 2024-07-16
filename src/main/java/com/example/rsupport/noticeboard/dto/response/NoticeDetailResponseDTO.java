package com.example.rsupport.noticeboard.dto.response;

import com.example.rsupport.noticeboard.entity.Notice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDetailResponseDTO {

    private Notice notice;

}
