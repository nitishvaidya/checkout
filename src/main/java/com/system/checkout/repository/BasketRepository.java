package com.system.checkout.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.system.checkout.entity.BasketEntity;
import com.system.checkout.type.CheckoutStatus;

@Repository
public interface BasketRepository extends JpaRepository<BasketEntity, UUID> {
	Optional<BasketEntity> findByStatus(CheckoutStatus status);
}
