package com.example.rsupport.noticeboard.common;

import com.example.rsupport.noticeboard.dto.common.FileSaveResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileManager {

    private final String fileSavePath;

    @Autowired
    public FileManager(@Value("${file.save.path}") String fileSavePath) {
        this.fileSavePath = fileSavePath;
    }

    public List<FileSaveResultDTO> saveFile(MultipartFile[] files) throws Exception {

        List<FileSaveResultDTO> resultList = new ArrayList<>(files.length);
        for (MultipartFile file : files) {

            String fileName = file.getOriginalFilename();
            File tmp = new File(fileSavePath + '/' + fileName);
            file.transferTo(tmp);

            resultList.add(FileSaveResultDTO.builder()
                    .fileName(fileName)
                    .filePath(fileSavePath)
                    .build());
        }
        return resultList;
    }
}