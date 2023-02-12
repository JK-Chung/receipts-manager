package com.chung.receiptsmanager.unitTests.service;

import com.chung.receiptsmanager.ReceiptsManagerApplication;
import com.chung.receiptsmanager.dto.CreateUserDto;
import com.chung.receiptsmanager.dto.UserDto;
import com.chung.receiptsmanager.entity.UserEntity;
import com.chung.receiptsmanager.repository.user.UserRepository;
import com.chung.receiptsmanager.service.security.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { ReceiptsManagerApplication.class })
class AuthenticationServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private Converter<CreateUserDto, UserEntity> createNewUserDtoUserEntityMapper;

    @MockBean
    private Converter<UserEntity, UserDto> userEntityUserDtoMapper;

    @Test()
    void exceptionWhenLoadingUnknownUser() {
        final AuthenticationService underTest = new AuthenticationService(userRepository,
                createNewUserDtoUserEntityMapper, userEntityUserDtoMapper);

        doReturn(Optional.empty()).when(userRepository).findByUsername(anyString());
        assertThrows(UsernameNotFoundException.class,
                () -> underTest.loadUserByUsername("non-existent-username"));
    }

}