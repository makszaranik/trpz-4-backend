package com.example.demo.service.file;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.model.file.FileEntity;
import com.example.demo.model.file.FileEntity.FileType;
import com.example.demo.model.file.LinterFileEntity;
import com.example.demo.model.file.SolutionFileEntity;
import com.example.demo.model.file.TestsFileEntity;
import com.example.demo.repository.FileRepository;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileUtilService {

    private final GridFsTemplate gridFsTemplate;
    private final FileRepository fileRepository;

    @SneakyThrows
    public FileEntity uploadFile(MultipartFile file, FileType fileType, String ownerId) {
        String gridFsFileId = gridFsTemplate
                .store(file.getInputStream(), file.getOriginalFilename(), file.getContentType())
                .toString();

        FileEntity fileEntity = switch (fileType) {
            case SOLUTION -> SolutionFileEntity.builder()
                    .ownerId(ownerId)
                    .gridFSFileId(gridFsFileId)
                    .build();

            case TEST -> TestsFileEntity.builder()
                    .ownerId(ownerId)
                    .gridFSFileId(gridFsFileId)
                    .build();

            case LINTER -> LinterFileEntity.builder()
                    .ownerId(ownerId)
                    .gridFSFileId(gridFsFileId)
                    .build();
        };

        return fileRepository.save(fileEntity);
    }

    public GridFsResource getFileById(String fileId) {
        FileEntity file = fileRepository.findById(fileId)
                .orElseThrow(() -> {
                    String message = String.format("file with id %s not found", fileId);
                    return new ResourceNotFoundException(message);
                });
        String gridFsFileId = file.getGridFSFileId();
        GridFSFile gridFsFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(gridFsFileId)));
        return gridFsTemplate.getResource(gridFsFile);
    }
}
