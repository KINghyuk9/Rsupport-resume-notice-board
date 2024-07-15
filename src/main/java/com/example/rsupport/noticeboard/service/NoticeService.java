package com.example.rsupport.noticeboard.service;

import com.example.rsupport.noticeboard.exception.InvalidPasswordException;
import com.example.rsupport.noticeboard.exception.NoticeNotFoundException;
import com.example.rsupport.noticeboard.util.FileManager;
import com.example.rsupport.noticeboard.dto.common.FileListDTO;
import com.example.rsupport.noticeboard.dto.common.FileSaveResultDTO;
import com.example.rsupport.noticeboard.dto.request.NoticeCreateRequestDTO;
import com.example.rsupport.noticeboard.dto.request.NoticeDeleteRequestDTO;
import com.example.rsupport.noticeboard.dto.request.NoticeUpdateRequestDTO;
import com.example.rsupport.noticeboard.dto.response.NoticeDetailResponseDTO;
import com.example.rsupport.noticeboard.dto.response.NoticeResponseDTO;
import com.example.rsupport.noticeboard.entity.FileTable;
import com.example.rsupport.noticeboard.entity.Notice;
import com.example.rsupport.noticeboard.repository.FileTableRepository;
import com.example.rsupport.noticeboard.repository.NoticeRepository;
import com.example.rsupport.noticeboard.specification.NoticeSpecification;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoticeService {

    private static final Logger logger = LoggerFactory.getLogger(NoticeService.class);

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
                        .map(file -> new FileTable(file.getFileName(), file.getFilePath(), createNotice))
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
                .orElseThrow(() -> new NoticeNotFoundException("공지사항을 찾을 수 없습니다."));
        passwordCheck(notice, dto.getPostPw());

        List<FileTable> fileTables = notice.getFiles();
        if (fileTables != null && !fileTables.isEmpty()) {
            fileTableRepository.deleteAll(fileTables);
            deleteExistingFiles(notice);
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
                .orElseThrow(() -> new NoticeNotFoundException("공지사항을 찾을 수 없습니다."));
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

    @Transactional
    public Notice updateNotice(NoticeUpdateRequestDTO dto) {
        try {
            Notice notice = noticeRepository.findById(dto.getNoticeId())
                    .orElseThrow(() -> new NoticeNotFoundException("공지사항을 찾을 수 없습니다."));

            passwordCheck(notice, dto.getPostPw());

            if ("Y".equals(dto.getFileChangeYn())) {
                deleteExistingFiles(notice);
            }

            updateNoticeDetails(dto, notice);

            if (dto.getFiles() != null && dto.getFiles().length > 0) {
                addNewFiles(dto, notice);
            }

            Notice updatedNotice = noticeRepository.save(notice);
            logger.info("게시글 정보 업데이트 성공. ID: {}", updatedNotice.getNoticeId());

            return updatedNotice;

        } catch (IOException e) {
            logger.error("파일 처리 중 문제가 발생하였습니다.", e);
            throw new IllegalArgumentException("파일 처리 중 문제가 발생하였습니다.", e);
        }
    }

    private void passwordCheck(Notice notice, String postPw) {
        if (!encoder.matches(postPw, notice.getPostPw())) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }
    }

    private void deleteExistingFiles(Notice notice) {
        List<FileTable> existingFiles = notice.getFiles();
        fileTableRepository.deleteAll(existingFiles);
        logger.info("기존 파일 내역 DB 삭제 성공. 삭제된 파일 수: {}", existingFiles.size());

        List<String> existingFilePaths = existingFiles.stream()
                .map(file -> file.getFilePath() + '/' + file.getFileName())
                .collect(Collectors.toList());
        fileManager.deleteFiles(existingFilePaths);
        logger.info("기존 파일 삭제 성공");

        notice.setFiles(new ArrayList<>());
    }

    private void updateNoticeDetails(NoticeUpdateRequestDTO dto, Notice notice) {
        dto.updateNotice(notice);
    }

    private void addNewFiles(NoticeUpdateRequestDTO dto, Notice notice) throws IOException {
        try {
            List<FileSaveResultDTO> savedFiles = fileManager.saveFile(dto.getFiles());
            List<FileTable> addedFiles = savedFiles.stream()
                    .map(fileDto -> new FileTable(fileDto.getFileName(), fileDto.getFilePath(), notice))
                    .collect(Collectors.toList());
            fileTableRepository.saveAll(addedFiles);
            logger.info("새 파일 정보를 DB에 저장 성공. 저장된 파일 수: {}", addedFiles.size());
            notice.setFiles(addedFiles);
        }catch (Exception e){
            logger.error("파일 처리 중 문제가 발생하였습니다.", e);
            throw new IllegalArgumentException("파일 처리 중 문제가 발생하였습니다.", e);
        }
    }
}