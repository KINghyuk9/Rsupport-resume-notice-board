package com.example.rsupport.noticeboard.entity;

import com.example.rsupport.noticeboard.dto.request.NoticeCreateRequestDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "notice_board")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private String author;
    @Column(nullable = false)
    private String userId;
    private int views;
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedDate;

    @OneToMany(mappedBy = "noticeBoard", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("noticeBoard")
    @ToString.Exclude
    private List<FileTable> files = new ArrayList<>();

    private Notice(NoticeCreateRequestDTO request){
        this.title = request.getTitle();
        this.content = request.getContent();
        this.author = request.getAuthor();
        this.userId = request.getUserId();
        this.startDate = request.getStartDate();
        this.endDate = request.getEndDate();
        this.createDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
        this.views = 0;
    }

    public static Notice from(NoticeCreateRequestDTO dto){
        return new Notice(dto);
    }
    public void setFiles(List<FileTable> files) {
        this.files = files;
        for (FileTable file : files) {
            file.setNoticeBoard(this);
        }
    }

    public void incrementViews(){
        this.views++;
    }

}
