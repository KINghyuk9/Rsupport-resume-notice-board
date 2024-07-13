package com.example.rsupport.noticeboard.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "file_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;
    @Column(nullable = false)
    private String fileName;
    @Column(nullable = false)
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "notice_id")
    @JsonIgnoreProperties("files")
    private Notice noticeBoard;

    public FileTable(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public void setNoticeBoard(Notice noticeBoard) {
        this.noticeBoard = noticeBoard;
    }
}