package com.chung.receiptsmanager.entity;

import com.chung.receiptsmanager.entity.attributeConverters.CurrencyConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "receiptable_items")
public class ReceiptableItemsEntity {

    @Id
    @Column(name = "PK_id")
    @GeneratedValue
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "FK_owner_id", referencedColumnName = "PK_id", nullable = false)
    private UserEntity owner;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "FK_receiptable_items_collections_id", referencedColumnName = "PK_id", nullable = false)
    private ReceiptableItemsCollection collectionOwner;

    @NotBlank
    private String itemName;

    /**
     * Nullable.
     */
    private String description;

    @NotNull
    private LocalDateTime invoiceDatetime;

    @NotNull
    @Min(value = 0L, message = "Monetary amount must be positive")
    private Integer monetaryAmountInMinorUnits;

    @NotNull
    @Convert(converter = CurrencyConverter.class)
    private Currency iso4217CurrencyCode;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "FK_image_file_id", referencedColumnName = "PK_id", nullable = false)
    private FileEntity imageFile;
}
