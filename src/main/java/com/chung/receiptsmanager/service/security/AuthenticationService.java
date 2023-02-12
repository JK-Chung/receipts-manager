package com.chung.receiptsmanager.service.security;

import com.chung.receiptsmanager.dto.CreateUserDto;
import com.chung.receiptsmanager.dto.UserDto;
import com.chung.receiptsmanager.entity.UserEntity;
import com.chung.receiptsmanager.exceptions.responseStatusException.user.UserAlreadyExistsException;
import com.chung.receiptsmanager.repository.user.UserRepository;
import com.chung.receiptsmanager.security.UserSecurity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Slf4j
@Service
public class AuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;
    private final Converter<CreateUserDto, UserEntity> createUserDtoToUserEntityMapper;
    private final Converter<UserEntity, UserDto> userEntityUserDtoMapper;

    @Autowired
    public AuthenticationService(UserRepository userRepository,
                                 Converter<CreateUserDto, UserEntity> createUserDtoToUserEntityMapper,
                                 Converter<UserEntity, UserDto> userEntityUserDtoMapper) {
        this.userRepository = userRepository;
        this.createUserDtoToUserEntityMapper = createUserDtoToUserEntityMapper;
        this.userEntityUserDtoMapper = userEntityUserDtoMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .map(UserSecurity::from)
                .orElseThrow(() -> new UsernameNotFoundException("could not find username " + username));
    }

    @Transactional
    public UserDto createUser(@Valid final CreateUserDto createUserDto) throws UserAlreadyExistsException {
        log.info("creating new user with username {} and email address {}", createUserDto.getUsername(), createUserDto.getEmailAddress());
        final UserEntity userEntity = createUserDtoToUserEntityMapper.convert(createUserDto);

        if(userRepository.doesUserAlreadyExist(userEntity)) {
            log.info("attempted to create user but this user already existed. New user was not created. Username: {} Email Address: {}",
                    userEntity.getUsername(),
                    userEntity.getEmailAddress());
            throw new UserAlreadyExistsException();
        }

        final UserEntity newlyCreatedUser = userRepository.save(userEntity);
        log.info("saved new user to repository with username {} and email address {}", newlyCreatedUser.getUsername(), newlyCreatedUser.getEmailAddress());
        return userEntityUserDtoMapper.convert(newlyCreatedUser);
    }

}
