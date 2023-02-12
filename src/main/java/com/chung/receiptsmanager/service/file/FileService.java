package com.chung.receiptsmanager.service.file;

import com.chung.receiptsmanager.dto.FileDto;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public interface FileService {
    FileDto storeFileAndMetadata(final InputStream toStoreInputStream, final String userProvidedFilename) throws IOException;
}
