package com.chung.receiptsmanager.mapper;

import com.chung.receiptsmanager.dto.CreateUserDto;
import com.chung.receiptsmanager.entity.UserEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CreateNewUserDtoUserEntityMapper implements Converter<CreateUserDto, UserEntity> {

    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper = new ModelMapper();

    public CreateNewUserDtoUserEntityMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        configureModelMapper(this.modelMapper);
    }

    @Override
    public UserEntity convert(CreateUserDto dto) {
        return modelMapper.map(dto, UserEntity.class);
    }

    private void configureModelMapper(ModelMapper toConfigure) {
        final org.modelmapper.Converter<String, String> passwordEncoderConverter = mappingContext
                -> passwordEncoder.encode(mappingContext.getSource());

        toConfigure.createTypeMap(CreateUserDto.class, UserEntity.class)
                .addMappings(new PropertyMap<>() {
                    @Override
                    protected void configure() {
                        skip().setId(null);
                        using(passwordEncoderConverter).map().setPassword(source.getPassword());
                        map().setEnabled(true);
                        map().setAccountNonExpired(true);
                        map().setAccountNonLocked(true);
                        map().setCredentialsNonExpired(true);
                    }
                });
    }
}
