package com.example.demo.model.file;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@SuperBuilder
@Document("files")
@TypeAlias("linter")
@EqualsAndHashCode(callSuper = true)
public class LinterFileEntity extends FileEntity {
}
