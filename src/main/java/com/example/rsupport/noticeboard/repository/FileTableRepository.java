package com.example.rsupport.noticeboard.repository;

import com.example.rsupport.noticeboard.entity.FileTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileTableRepository extends JpaRepository<FileTable, String> {
}
