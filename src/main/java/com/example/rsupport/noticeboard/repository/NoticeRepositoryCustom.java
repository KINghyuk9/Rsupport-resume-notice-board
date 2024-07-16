package com.example.rsupport.noticeboard.repository;

import com.example.rsupport.noticeboard.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface NoticeRepositoryCustom {
    Notice getNoticeDetail(Long id);
    Page<Notice> getNoticeSearch(String searchType, String keyword, Pageable pageable);
    void updateNotice(Notice notice);
}
