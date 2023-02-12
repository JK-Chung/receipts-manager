package com.chung.receiptsmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "receiptable_items_collections")
public class ReceiptableItemsCollection {

    @Id
    @Column(name = "PK_id")
    @GeneratedValue
    private String id;

    @NotBlank
    private String name;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "FK_owner_id", referencedColumnName = "PK_id", nullable = false)
    private UserEntity owner;

    @NotNull
    @OneToMany(mappedBy = "collectionOwner")
    private List<ReceiptableItemsEntity> receiptableItems;
}
