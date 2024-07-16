package com.example.rsupport.noticeboard.repository;

import com.example.rsupport.noticeboard.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long>,  NoticeRepositoryCustom{
    Page<Notice> findAll(Pageable pageable);
}
