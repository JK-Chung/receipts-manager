package com.chung.receiptsmanager.controller;

import com.chung.receiptsmanager.dto.CreateUserDto;
import com.chung.receiptsmanager.dto.UserDto;
import com.chung.receiptsmanager.service.security.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService registerUserService;

    @Autowired
    public AuthenticationController(AuthenticationService registerUserService) {
        this.registerUserService = registerUserService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto registerUser(@Valid @RequestBody final CreateUserDto dto) {
        return registerUserService.createUser(dto);
    }

}