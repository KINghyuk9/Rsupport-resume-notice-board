package com.example.rsupport.noticeboard.entity;

import com.example.rsupport.noticeboard.dto.NoticeCreateRequestDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
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
    private String postPw;
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "noticeBoard", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("noticeBoard")
    private List<FileTable> files;

    private Notice(NoticeCreateRequestDTO request, PasswordEncoder encoder){
        this.title = request.getTitle();
        this.content = request.getContent();
        this.author = request.getAuthor();
        this.postPw = encoder.encode(request.getPostPw());
        this.startDate = request.getStartDate();
        this.endDate = request.getEndDate();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static Notice from(NoticeCreateRequestDTO dto, PasswordEncoder encoder){
        return new Notice(dto, encoder);
    }
    public void setFiles(List<FileTable> files) {
        this.files = files;
        for (FileTable file : files) {
            file.setNoticeBoard(this);
        }
    }
}
