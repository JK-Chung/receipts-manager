package com.chung.receiptsmanager.service.file;

import com.chung.receiptsmanager.service.file.helper.VolatileLocalFileStorage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class FileStorageConfig {

    @Bean
    @ConditionalOnProperty(prefix = "application", name = "fileStorage", havingValue = "local_hard_drive_tmp")
    public FileStorageService fileStorageService() throws IOException {
        return new VolatileLocalFileStorage("file-storage-service");
    }

    @Bean
    public VolatileLocalFileStorage volatileLocalFileStorage() throws IOException {
        return new VolatileLocalFileStorage("temp-file-storage");
    }

}
