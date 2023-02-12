package com.chung.receiptsmanager.controller;

import com.chung.receiptsmanager.dto.FileDto;
import com.chung.receiptsmanager.service.file.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/image")
public class ImageFileController {

    private final FileService fileService;

    public ImageFileController(FileService fileService) {
        this.fileService = fileService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public FileDto uploadImageFile(@RequestPart final MultipartFile imageFile) throws IOException {
        try (final InputStream is = new BufferedInputStream(imageFile.getInputStream())) {
            return fileService.storeFileAndMetadata(is, imageFile.getResource().getFilename());
        }
    }
}
