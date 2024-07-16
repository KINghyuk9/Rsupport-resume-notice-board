package com.example.rsupport.noticeboard.repository;

import com.example.rsupport.noticeboard.entity.FileTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FileTableRepositoryTest {
    @Autowired
    private FileTableRepository fileTableRepository;

    private FileTable fileTable;

    @BeforeEach
    void setUp() {
        fileTableRepository.deleteAll();

        fileTable = FileTable.builder()
                .fileName("test.txt")
                .filePath("/path/to/test.txt")
                .build();

        fileTableRepository.save(fileTable);
    }

    @Test
    void testSaveAndFindById() {
        Optional<FileTable> found = fileTableRepository.findById(fileTable.getFileId());

        assertThat(found).isPresent();
        assertThat(found.get().getFileName()).isEqualTo(fileTable.getFileName());
        assertThat(found.get().getFilePath()).isEqualTo(fileTable.getFilePath());
    }

    @Test
    void testDelete() {
        fileTableRepository.delete(fileTable);
        Optional<FileTable> found = fileTableRepository.findById(fileTable.getFileId());

        assertThat(found).isNotPresent();
    }
}