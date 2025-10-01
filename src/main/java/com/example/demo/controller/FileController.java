package com.example.demo.controller;


import com.example.demo.model.file.FileEntity;
import com.example.demo.model.file.FileEntity.FileType;
import com.example.demo.service.FileUtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("files")
public class FileController {

    private final FileUtilService fileUtilService;

    @PostMapping("upload")
    @ResponseStatus(HttpStatus.CREATED)
    public FileEntity uploadFile(@RequestParam("file") MultipartFile file, FileType fileType) {
        return fileUtilService.uploadFile(file, fileType, "ownerId");
    }

}
