package com.chung.receiptsmanager.mapper;

import com.chung.receiptsmanager.dto.UserDto;
import com.chung.receiptsmanager.entity.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserEntityUserDtoMapper implements Converter<UserEntity, UserDto> {

    private final ModelMapper modelMapper = new ModelMapper();

    public UserEntityUserDtoMapper() {
        this.modelMapper.createTypeMap(UserEntity.class, UserDto.class);
    }

    @Override
    public UserDto convert(UserEntity userEntity) {
        return this.modelMapper.map(userEntity, UserDto.class);
    }

}
