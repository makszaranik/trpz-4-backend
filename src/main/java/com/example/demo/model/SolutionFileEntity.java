package com.example.demo.model;

import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document("files")
@TypeAlias("solution")
@EqualsAndHashCode(callSuper = true)
public class SolutionFileEntity extends FileEntity {
}
