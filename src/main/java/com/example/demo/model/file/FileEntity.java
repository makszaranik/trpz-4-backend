package com.example.demo.model.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@Document("files")
@NoArgsConstructor
@AllArgsConstructor
public abstract class FileEntity {

    @Id
    private String id;
    private String ownerId;
    private String gridFSFileId;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

    public enum FileType {
        LINTER,
        SOLUTION,
        TEST
    }
}

