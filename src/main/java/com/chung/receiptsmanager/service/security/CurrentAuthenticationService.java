package com.chung.receiptsmanager.service.security;

import com.chung.receiptsmanager.entity.UserEntity;
import com.chung.receiptsmanager.security.UserSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentAuthenticationService {

    public UserEntity getCurrentlyAuthenticatedUserEntity() {
        final Object authenticationPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(authenticationPrincipal instanceof String s) {
            throw new IllegalStateException(
                    "Appears that request is unauthenticated because authentication principle is the String " + s
            );
        }

        return ((UserSecurity) authenticationPrincipal).getUnderlyingUserEntity();
    }

}
