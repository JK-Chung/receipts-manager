package com.chung.receiptsmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(name = "PK_id")
    @GeneratedValue
    private UUID id;

    @NotBlank
    @Length(min = 1, max = 100)
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    @Length(min = 1, max = 50)
    private String firstName;

    @NotBlank
    @Length(min = 1, max = 50)
    private String lastName;

    @NotBlank
    @Length(min = 1, max = 50)
    private String emailAddress;

    private boolean isEnabled;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;

}
