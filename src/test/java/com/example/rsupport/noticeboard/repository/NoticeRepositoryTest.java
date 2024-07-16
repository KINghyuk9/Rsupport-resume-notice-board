package com.example.rsupport.noticeboard.repository;

import com.example.rsupport.noticeboard.entity.Notice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NoticeRepositoryTest {
    @Autowired
    private NoticeRepository noticeRepository;

    @BeforeEach
    void setUp() {
        noticeRepository.deleteAll();

        Notice notice1 = new Notice();
        notice1.setTitle("Title1");
        notice1.setContent("Content1");
        notice1.setAuthor("Author1");
        notice1.setUserId("User1");
        notice1.setStartDate(LocalDateTime.now());
        notice1.setEndDate(LocalDateTime.now().plusDays(1));

        Notice notice2 = new Notice();
        notice2.setTitle("Title2");
        notice2.setContent("Content2");
        notice2.setAuthor("Author2");
        notice2.setUserId("User2");
        notice2.setStartDate(LocalDateTime.now());
        notice2.setEndDate(LocalDateTime.now().plusDays(1));

        noticeRepository.save(notice1);
        noticeRepository.save(notice2);
    }

    @Test
    void findAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Notice> noticePage = noticeRepository.findAll(pageable);

        assertThat(noticePage).isNotNull();
        assertThat(noticePage.getTotalElements()).isEqualTo(2);
        assertThat(noticePage.getContent()).extracting(Notice::getTitle).containsExactly("Title1", "Title2");
    }

    @Test
    void testGetNoticeDetail() {
        Notice notice = noticeRepository.findAll().get(0);
        Long noticeId = notice.getNoticeId();

        Notice foundNotice = noticeRepository.findById(noticeId).orElse(null);

        assertThat(foundNotice).isNotNull();
        assertThat(foundNotice.getTitle()).isEqualTo(notice.getTitle());
    }

    @Test
    void testSearchNoticesByTitle() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Notice> noticePage = noticeRepository.getNoticeSearch("title", "Title1", pageable);

        assertThat(noticePage).isNotNull();
        assertThat(noticePage.getTotalElements()).isEqualTo(1);
        assertThat(noticePage.getContent().get(0).getTitle()).isEqualTo("Title1");
    }

    @Test
    void testSearchNoticesByAuthor() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Notice> noticePage = noticeRepository.getNoticeSearch("author", "Author1", pageable);

        assertThat(noticePage).isNotNull();
        assertThat(noticePage.getTotalElements()).isEqualTo(1);
        assertThat(noticePage.getContent().get(0).getAuthor()).isEqualTo("Author1");
    }
}