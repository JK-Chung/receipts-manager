package com.chung.receiptsmanager.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public enum SecurityRole {
    ADMIN(new SimpleGrantedAuthority("ADMIN")),
    USER;

    private final Collection<GrantedAuthority> authorities;

    SecurityRole(final GrantedAuthority...authorities) {
        this.authorities = Collections.unmodifiableCollection(Arrays.asList(authorities));
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }
}