package com.example.rsupport.noticeboard.repository;

import com.example.rsupport.noticeboard.entity.FileTable;
import com.example.rsupport.noticeboard.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileTableRepository extends JpaRepository<FileTable, Long> {
    List<FileTable> findByNoticeBoard(Notice notice);
    void deleteAllByNoticeBoard(Notice notice);
}
