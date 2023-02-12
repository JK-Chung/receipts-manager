package com.chung.receiptsmanager.service.file;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

public interface FileService {
    UUID saveFileAndMetadata(final Path toSave) throws IOException;
}
