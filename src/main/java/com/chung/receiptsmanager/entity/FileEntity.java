package com.chung.receiptsmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "files")
public class FileEntity {

    @Id
    @Column(name = "PK_id")
    @GeneratedValue
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "FK_owner_id", referencedColumnName = "PK_id", nullable = false)
    private UserEntity owner;

    @NotBlank
    private String userProvidedFileName;

    @NotBlank
    private String fileLocation;

}
