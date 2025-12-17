package com.ecommerce.domain.item.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ITEM_OPTION")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemOptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "i_option_id")
    private Long optionId;

    @Column(name = "i_option_name")
    private String optionName;

    @Column(name = "i_option_quantity")
    private int optionQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Column(name = "deleted_status")
    private Boolean deletedStatus;

    @Column(name = "creation_user")
    private String creationUser;

    @Column(name = "modification_user")
    private String modificationUser;

    @PrePersist
    protected void onCreate() {
        if (createdDate == null) {
            createdDate = LocalDateTime.now();
        }
        if (deletedStatus == null) {
            deletedStatus = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedDate = LocalDateTime.now();
    }
}
