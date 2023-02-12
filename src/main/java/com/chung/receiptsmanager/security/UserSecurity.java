package com.chung.receiptsmanager.security;

import com.chung.receiptsmanager.entity.UserEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

/**
 * Decorator pattern over the JPA entity UserEntity. This class is compatible with Spring-Security's UserDetailsService
 */
public class UserSecurity implements UserDetails {

    private final UserEntity userEntity;

    public static UserSecurity from(UserEntity userEntity) {
        return new UserSecurity(userEntity);
    }

    private UserSecurity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public UserEntity getUnderlyingUserEntity() {
        final UserEntity copy = new UserEntity();
        BeanUtils.copyProperties(this.userEntity, copy);
        return copy;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // FIXME
        return Set.of(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return userEntity.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return userEntity.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return userEntity.isAccountNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return userEntity.isEnabled();
    }
}
