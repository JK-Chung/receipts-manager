package com.chung.receiptsmanager.repository.file;

import com.chung.receiptsmanager.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileRepository extends JpaRepository<FileEntity, UUID> {
}
