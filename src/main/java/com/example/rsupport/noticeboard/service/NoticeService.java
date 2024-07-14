package com.example.rsupport.noticeboard.service;

import com.example.rsupport.noticeboard.common.FileManager;
import com.example.rsupport.noticeboard.dto.common.FileListDTO;
import com.example.rsupport.noticeboard.dto.common.FileSaveResultDTO;
import com.example.rsupport.noticeboard.dto.request.NoticeCreateRequestDTO;
import com.example.rsupport.noticeboard.dto.request.NoticeDeleteRequestDTO;
import com.example.rsupport.noticeboard.dto.response.NoticeDetailResponseDTO;
import com.example.rsupport.noticeboard.dto.response.NoticeResponseDTO;
import com.example.rsupport.noticeboard.entity.FileTable;
import com.example.rsupport.noticeboard.entity.Notice;
import com.example.rsupport.noticeboard.repository.FileTableRepository;
import com.example.rsupport.noticeboard.repository.NoticeRepository;
import com.example.rsupport.noticeboard.specification.NoticeSpecification;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final PasswordEncoder encoder;
    private final FileManager fileManager;
    private final FileTableRepository fileTableRepository;

    @Autowired
    public NoticeService(NoticeRepository noticeRepository, PasswordEncoder encoder, FileManager fileManager, FileTableRepository fileTableRepository) {
        this.noticeRepository = noticeRepository;
        this.encoder = encoder;
        this.fileManager = fileManager;
        this.fileTableRepository = fileTableRepository;
    }

    @Transactional
    public void createNotice(NoticeCreateRequestDTO dto) {
        try {
            Notice createNotice = Notice.from(dto, encoder);

            if (dto.getFiles() != null && dto.getFiles().length > 0) {
                List<FileSaveResultDTO> savedFiles = fileManager.saveFile(dto.getFiles());

                List<FileTable> fileTables = savedFiles.stream()
                        .map(file -> new FileTable(file.getFileName(), file.getFilePath()))
                        .toList();

                createNotice.setFiles(fileTables);
            }

            noticeRepository.save(createNotice);
        } catch (Exception e) {
            throw new IllegalArgumentException("공지사항 등록 중 문제가 발생하였습니다.");
        }
    }

    @Transactional
    public void deleteNotice(NoticeDeleteRequestDTO dto) {
        Notice notice = noticeRepository.findById(dto.getNoticeId())
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다. ID: " + dto.getNoticeId()));

        if (!notice.getAuthor().equals(dto.getAuthor())) {
            throw new IllegalArgumentException("작성자가 일치하지 않습니다.");
        }

        if (!encoder.matches(dto.getPostPw(), notice.getPostPw())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        List<FileTable> fileTables = notice.getFiles();
        if (fileTables != null && !fileTables.isEmpty()) {
            fileTableRepository.deleteAll(fileTables);
        }

        noticeRepository.delete(notice);
    }

    @Transactional
    public Page<NoticeResponseDTO> getNoticeList(Pageable pageable){
        return noticeRepository.findAll(pageable)
                .map(NoticeResponseDTO::fromNotice);
    }

    @Transactional
    public Page<NoticeResponseDTO> getNoticeSearch(String searchType, String keyword, Pageable pageable) {
        Specification<Notice> spec = NoticeSpecification.search(searchType, keyword);

        return noticeRepository.findAll(spec, pageable)
                .map(NoticeResponseDTO::fromNotice);
    }

    @Transactional
    public NoticeDetailResponseDTO getNoticeDetail(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다. ID: " + noticeId));
        notice.incrementViews();
        noticeRepository.save(notice);

        List<FileTable> fileTables = fileTableRepository.findByNoticeBoard(notice);
        List<FileListDTO> files = fileTables.stream()
                .map(file -> new FileListDTO(file.getFileId(), file.getFileName()))
                .toList();

        return NoticeDetailResponseDTO.of(
                notice, files
        );
    }
}