package com.system.checkout.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.system.checkout.entity.DealEntity;
@Repository
public interface DealRepository extends JpaRepository<DealEntity, UUID> {

	List<DealEntity> findByProductIdAndApplicableTillAfter(UUID productId, LocalDateTime applicableTillAfter);
}
