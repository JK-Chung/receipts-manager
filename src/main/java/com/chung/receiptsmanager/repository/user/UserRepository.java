package com.chung.receiptsmanager.repository.user;

import com.chung.receiptsmanager.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByUsernameOrEmailAddress(String username, String emailAddress);

    default boolean doesUserAlreadyExist(UserEntity userEntity) {
        return findByUsernameOrEmailAddress(
                userEntity.getUsername(),
                userEntity.getEmailAddress()
        ).isPresent();
    }
}
