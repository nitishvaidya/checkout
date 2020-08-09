package com.system.checkout.service;

import java.util.List;
import java.util.UUID;

import com.system.checkout.exception.CustomException;
import com.system.checkout.model.DealDTO;
import com.system.checkout.model.DiscountDTO;
import com.system.checkout.model.ProductDTO;

public interface StoreOwnerService {

	UUID create(ProductDTO productDTO) throws CustomException;

	List<ProductDTO> getAll();

	void update(UUID id, ProductDTO productDTO) throws CustomException;

	void delete(UUID id) throws CustomException;

	UUID create(UUID productId, DiscountDTO discountDTO) throws CustomException;

	List<DiscountDTO> getAllDiscounts(UUID productId);

	UUID create(UUID productId, DealDTO dealDTO) throws CustomException;

	List<DealDTO> getAllDeals(UUID productId);

}
