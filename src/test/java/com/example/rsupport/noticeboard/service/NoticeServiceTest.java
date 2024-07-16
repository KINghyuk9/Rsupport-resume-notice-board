package com.example.rsupport.noticeboard.service;

import com.example.rsupport.noticeboard.dto.common.FileSaveResultDTO;
import com.example.rsupport.noticeboard.dto.request.NoticeCreateRequestDTO;
import com.example.rsupport.noticeboard.dto.request.NoticeUpdateRequestDTO;
import com.example.rsupport.noticeboard.dto.response.NoticeCreateResponseDTO;
import com.example.rsupport.noticeboard.dto.response.NoticeDetailResponseDTO;
import com.example.rsupport.noticeboard.dto.response.NoticeResponseDTO;
import com.example.rsupport.noticeboard.dto.response.NoticeUpdateResponseDTO;
import com.example.rsupport.noticeboard.entity.Notice;
import com.example.rsupport.noticeboard.repository.FileTableRepository;
import com.example.rsupport.noticeboard.repository.NoticeRepository;
import com.example.rsupport.noticeboard.util.FileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NoticeServiceTest {

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private FileTableRepository fileTableRepository;

    @Mock
    private FileManager fileManager;

    @InjectMocks
    private NoticeService noticeService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("공지사항 등록 테스트 - 파일 없음")
    @Test
    void createNotice(){
        //given
        NoticeCreateRequestDTO requestDTO = NoticeCreateRequestDTO.builder()
                .title("TestTitle")
                .content("TestContent")
                .userId("TestUserId")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .build();

        Notice notice = Notice.from(requestDTO);
        when(noticeRepository.save(any(Notice.class))).thenReturn(notice);

        //when
        NoticeCreateResponseDTO responseDTO = noticeService.createNotice(requestDTO, null);

        //then
        assertNoticeEquals(notice, responseDTO);

    }

    @DisplayName("공지사항 등록 테스트 - 파일 있음")
    @Test
    void createNoticeWithFile() throws Exception {
        // given
        NoticeCreateRequestDTO requestDTO = NoticeCreateRequestDTO.builder()
                .title("TestTitle")
                .content("TestContent")
                .userId("TestUserId")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .build();

        Notice notice = Notice.from(requestDTO);
        when(noticeRepository.save(any(Notice.class))).thenReturn(notice);

        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test data".getBytes());
        when(fileManager.saveFile(any(MultipartFile[].class))).thenReturn(Arrays.asList(FileSaveResultDTO.builder()
                .fileName(file.getOriginalFilename())
                .filePath("testPath")
                .build()));

        // when
        NoticeCreateResponseDTO responseDTO = noticeService.createNotice(requestDTO, new MultipartFile[]{file});

        // then
        assertEquals(notice.getUserId(), responseDTO.getUserId());
        assertEquals(notice.getTitle(), responseDTO.getTitle());
        assertEquals(notice.getContent(), responseDTO.getContent());
        assertEquals(notice.getStartDate(), responseDTO.getStartDate());
        assertEquals(notice.getEndDate(), responseDTO.getEndDate());
    }

    @DisplayName("공지사항 삭제 테스트")
    @Test
    void deleteNotice() {
        // given
        Long noticeId = 1L;
        String userId = "TestUserId";

        Notice notice = new Notice();
        notice.setNoticeId(noticeId);
        notice.setUserId(userId);

        when(noticeRepository.findById(noticeId)).
                thenReturn(Optional.of(notice));

        // when
        noticeService.deleteNotice(noticeId, userId);

        // then
        verify(noticeRepository, times(1)).delete(notice);
    }

    @DisplayName("공지사항 상세 조회 테스트")
    @Test
    void getNoticeDetail() {
        // given
        Long noticeId = 1L;
        Notice notice = new Notice();
        notice.setNoticeId(noticeId);
        notice.setTitle("TestTitle");
        notice.setContent("TestContent");
        notice.setAuthor("TestAuthor");
        notice.setUserId("TestUserId");
        notice.setStartDate(LocalDateTime.now());
        notice.setEndDate(LocalDateTime.now().plusDays(1));

        when(noticeRepository.findById(anyLong())).thenReturn(Optional.of(notice));

        // when
        NoticeDetailResponseDTO responseDTO = noticeService.getNoticeDetail(noticeId);

        // then
        assertNoticeEquals(notice, responseDTO);
    }

    @Test
    @DisplayName("공지사항 검색 테스트")
    void getNoticeSearch() {
        /// given
        Pageable pageable = PageRequest.of(0, 10);
        List<Notice> noticeList = Arrays.asList(
                createTestNotice(1L, "Title1", "Content1", "Author1"),
                createTestNotice(2L, "Title2", "Content2", "Author2")
        );
        Page<Notice> noticePage = new PageImpl<>(noticeList, pageable, noticeList.size());

        when(noticeRepository.getNoticeSearch(anyString(), anyString(), any(Pageable.class))).thenReturn(noticePage);

        // when
        Page<NoticeResponseDTO> result = noticeService.getNoticeSearch("title", "Title1", pageable);

        // then
        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.getTotalElements(), "Total elements should match");
        assertEquals("Title1", result.getContent().get(0).getTitle(), "Title should match");
    }

    @Test
    @DisplayName("공지사항 수정 테스트 - 파일 없음")
    void updateNotice() throws Exception{
        // given
        Long noticeId = 1L;
        NoticeUpdateRequestDTO requestDTO = NoticeUpdateRequestDTO.builder()
                .noticeId(noticeId)
                .title("UpdatedTitle")
                .content("UpdatedContent")
                .userId("TestUserId")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .build();

        MultipartFile[] files = {
                new MockMultipartFile("file1", "test1.txt", "text/plain", "test data".getBytes()),
                new MockMultipartFile("file2", "test2.txt", "text/plain", "test data".getBytes())
        };

        Notice notice = new Notice();
        notice.setNoticeId(noticeId);
        notice.setTitle("OriginalTitle");
        notice.setContent("OriginalContent");
        notice.setUserId("TestUserId");
        notice.setStartDate(LocalDateTime.now());
        notice.setEndDate(LocalDateTime.now().plusDays(1));

        when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(notice));

        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test data".getBytes());
        when(fileManager.saveFile(any(MultipartFile[].class))).thenReturn(Arrays.asList(FileSaveResultDTO.builder()
                .fileName(file.getOriginalFilename())
                .filePath("testPath")
                .build()));

        // when
        NoticeUpdateResponseDTO responseDTO = noticeService.updateNotice(requestDTO, files);

        //then
        assertNoticeEquals(notice, responseDTO);
    }

    private void assertNoticeEquals(Notice expected, Object actual) {
        if (actual instanceof NoticeCreateResponseDTO) {
            NoticeCreateResponseDTO actualDTO = (NoticeCreateResponseDTO) actual;
            assertEquals(expected.getNoticeId(), actualDTO.getNoticeId(), "NoticeId should match");
            assertEquals(expected.getTitle(), actualDTO.getTitle(), "Title should match");
            assertEquals(expected.getContent(), actualDTO.getContent(), "Content should match");
            assertEquals(expected.getAuthor(), actualDTO.getAuthor(), "Author should match");
            assertEquals(expected.getUserId(), actualDTO.getUserId(), "UserId should match");
            assertEquals(expected.getStartDate(), actualDTO.getStartDate(), "StartDate should match");
            assertEquals(expected.getEndDate(), actualDTO.getEndDate(), "EndDate should match");
        } else if (actual instanceof NoticeDetailResponseDTO) {
            NoticeDetailResponseDTO actualDTO = (NoticeDetailResponseDTO) actual;
            assertEquals(expected.getNoticeId(), actualDTO.getNoticeId(), "NoticeId should match");
            assertEquals(expected.getTitle(), actualDTO.getTitle(), "Title should match");
            assertEquals(expected.getContent(), actualDTO.getContent(), "Content should match");
            assertEquals(expected.getAuthor(), actualDTO.getAuthor(), "Author should match");
            assertEquals(expected.getUserId(), actualDTO.getUserId(), "UserId should match");
            assertEquals(expected.getStartDate(), actualDTO.getStartDate(), "StartDate should match");
            assertEquals(expected.getEndDate(), actualDTO.getEndDate(), "EndDate should match");
        } else if (actual instanceof NoticeUpdateResponseDTO) {
            NoticeUpdateResponseDTO actualDTO = (NoticeUpdateResponseDTO) actual;
            assertEquals(expected.getNoticeId(), actualDTO.getNoticeId(), "NoticeId should match");
            assertEquals(expected.getTitle(), actualDTO.getTitle(), "Title should match");
            assertEquals(expected.getContent(), actualDTO.getContent(), "Content should match");
            assertEquals(expected.getAuthor(), actualDTO.getAuthor(), "Author should match");
            assertEquals(expected.getStartDate(), actualDTO.getStartDate(), "StartDate should match");
            assertEquals(expected.getEndDate(), actualDTO.getEndDate(), "EndDate should match");
            assertEquals(expected.getViews(), actualDTO.getViews(), "Views should match");
        } else {
            throw new IllegalArgumentException("Unexpected type: " + actual.getClass().getName());
        }
    }

    private Notice createTestNotice(Long noticeId, String title, String content, String author) {
        Notice notice = new Notice();
        notice.setNoticeId(noticeId);
        notice.setTitle(title);
        notice.setContent(content);
        notice.setAuthor(author);
        notice.setUserId("TestUserId");
        notice.setStartDate(LocalDateTime.now());
        notice.setEndDate(LocalDateTime.now().plusDays(1));
        return notice;
    }
}