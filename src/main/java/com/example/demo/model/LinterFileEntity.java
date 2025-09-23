package com.example.demo.model;

import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Builder
@Document("files")
@NoArgsConstructor
@AllArgsConstructor
@TypeAlias("linter")
@EqualsAndHashCode(callSuper = true)
public class LinterFileEntity extends FileEntity {
}
