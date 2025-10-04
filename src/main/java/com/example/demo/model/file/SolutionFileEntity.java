package com.example.demo.model.file;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@SuperBuilder
@Document("files")
@NoArgsConstructor
@TypeAlias("solution")
@EqualsAndHashCode(callSuper = true)
public class SolutionFileEntity extends FileEntity {
}
