package com.example.rsupport.noticeboard.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileSaveResultDTO {
    private String fileId;
    private String fileName;
    private String filePath;
}