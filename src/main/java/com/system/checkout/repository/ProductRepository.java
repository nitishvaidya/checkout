package com.system.checkout.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.system.checkout.entity.ProductEntity;
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

	Optional<ProductEntity> findByName(String name);

	Optional<ProductEntity> findByIdAndQuantityGreaterThanEqual(UUID key, Integer quantity);
}
