package com.chung.receiptsmanager.service.security;

import com.chung.receiptsmanager.entity.UserEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentAuthenticationService {

    public UserEntity getCurrentlyAuthenticatedUserEntity() {
        return (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
