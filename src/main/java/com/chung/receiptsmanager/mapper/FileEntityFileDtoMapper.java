package com.chung.receiptsmanager.mapper;

import com.chung.receiptsmanager.dto.FileDto;
import com.chung.receiptsmanager.entity.FileEntity;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FileEntityFileDtoMapper implements Converter<FileEntity, FileDto> {

    private final ModelMapper modelMapper = new ModelMapper();

    public FileEntityFileDtoMapper() {
        this.modelMapper.createTypeMap(FileEntity.class, FileDto.class);
    }

    @Override
    public FileDto convert(FileEntity source) {
        return this.modelMapper.map(source, FileDto.class);
    }

}
