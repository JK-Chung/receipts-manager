package com.chung.receiptsmanager.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class CreateUserDto {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String emailAddress;
    @NotBlank
    private String city;


}