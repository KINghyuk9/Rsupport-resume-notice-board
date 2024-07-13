package com.example.rsupport.noticeboard.service;

import com.example.rsupport.noticeboard.common.FileManager;
import com.example.rsupport.noticeboard.dto.FileSaveResultDTO;
import com.example.rsupport.noticeboard.dto.NoticeCreateRequestDTO;
import com.example.rsupport.noticeboard.entity.FileTable;
import com.example.rsupport.noticeboard.entity.Notice;
import com.example.rsupport.noticeboard.repository.FileTableRepository;
import com.example.rsupport.noticeboard.repository.NoticeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final PasswordEncoder encoder;
    private final FileManager fileManager;

    @Autowired
    public NoticeService(NoticeRepository noticeRepository, PasswordEncoder encoder, FileManager fileManager) {
        this.noticeRepository = noticeRepository;
        this.encoder = encoder;
        this.fileManager = fileManager;
    }

    @Transactional
    public void createNotice(NoticeCreateRequestDTO dto) {
        try {
            Notice createNotice = Notice.from(dto, encoder);

            if (dto.getFiles() != null && dto.getFiles().length > 0) {
                List<FileSaveResultDTO> savedFiles = fileManager.saveFile(dto.getFiles());

                List<FileTable> fileTables = savedFiles.stream()
                        .map(file -> new FileTable(file.getFileName(), file.getFilePath()))
                        .collect(Collectors.toList());

                createNotice.setFiles(fileTables);
            }

            noticeRepository.save(createNotice);
        } catch (Exception e) {
            throw new IllegalArgumentException("공지사항 등록 중 문제가 발생하였습니다.");
        }
    }
}