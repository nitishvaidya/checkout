package com.system.checkout.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.system.checkout.entity.DiscountEntity;
@Repository
public interface DiscountRepository extends JpaRepository<DiscountEntity, UUID> {
	
	List<DiscountEntity> findByProductIdAndApplicableTillAfter(UUID productId, LocalDateTime applicableTill);
}
