package com.ecommerce.domain.item.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.domain.item.entity.ItemEntity;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

    @Query("SELECT DISTINCT i FROM ItemEntity i " +
            "LEFT JOIN FETCH i.options io")
    List<ItemEntity> findAllByDeletedStatusFalse();

}
