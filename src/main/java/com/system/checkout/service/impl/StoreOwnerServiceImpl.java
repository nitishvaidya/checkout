package com.system.checkout.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.system.checkout.entity.DealEntity;
import com.system.checkout.entity.DiscountEntity;
import com.system.checkout.entity.ProductEntity;
import com.system.checkout.exception.CustomException;
import com.system.checkout.model.DealDTO;
import com.system.checkout.model.DiscountDTO;
import com.system.checkout.model.ProductDTO;
import com.system.checkout.repository.DealRepository;
import com.system.checkout.repository.DiscountRepository;
import com.system.checkout.repository.ProductRepository;
import com.system.checkout.service.StoreOwnerService;
import com.system.checkout.validator.impl.DealValidator;
import com.system.checkout.validator.impl.DiscountValidator;
import com.system.checkout.validator.impl.ProductValidator;

@Service
public class StoreOwnerServiceImpl implements StoreOwnerService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private DiscountRepository discountRepository;

	@Autowired
	private DealRepository dealRepository;

	@Autowired
	private ProductValidator productValidator;

	@Autowired
	private DiscountValidator discountValidator;

	@Autowired
	private DealValidator dealValidator;

	@Override
	public UUID create(ProductDTO productDTO) throws CustomException {
		productValidator.validate(productDTO);
		ensureProductIsNew(productDTO.getName());
		ProductEntity productEntity = productRepository.save(getFrom(productDTO));
		return productEntity.getId();
	}

	@Override
	public List<ProductDTO> getAll() {
		List<ProductEntity> productEntities = productRepository.findAll();
		return productEntities.stream().map(productEntity -> getDTOFrom(productEntity)).collect(Collectors.toList());
	}

	@Override
	public void update(UUID id, ProductDTO productDTO) throws CustomException {
		productValidator.validate(productDTO);
		Optional<ProductEntity> optionalEntity = productRepository.findById(id);
		if (optionalEntity.isPresent()) {
			ProductEntity productEntity = optionalEntity.get();
			updateDetails(productDTO, productEntity);
			productRepository.save(productEntity);
		} else {
			throw new CustomException("Invalid product id");
		}

	}

	@Override
	public void delete(UUID id) throws CustomException {
		Optional<ProductEntity> optionalEntity = productRepository.findById(id);
		if (optionalEntity.isPresent()) {
			ProductEntity productEntity = optionalEntity.get();
			productRepository.delete(productEntity);
		} else {
			throw new CustomException("Invalid product id");
		}
	}

	private ProductEntity getFrom(ProductDTO productDTO) {
		ProductEntity productEntity = new ProductEntity();
		productEntity.setId(UUID.randomUUID());
		productEntity.setName(productDTO.getName().toLowerCase());
		updateDetails(productDTO, productEntity);
		return productEntity;

	}

	private void updateDetails(ProductDTO productDTO, ProductEntity productEntity) {
		productEntity.setDescription(productDTO.getDescription());
		productEntity.setPrice(productDTO.getPrice());
		productEntity.setQuantity(productDTO.getQuantity());
		productEntity.setLastUpdatedAt(LocalDateTime.now());
	}

	private ProductDTO getDTOFrom(ProductEntity productEntity) {
		ProductDTO productDTO = new ProductDTO();
		productDTO.setName(productEntity.getName());
		productDTO.setId(productEntity.getId());
		productDTO.setDescription(productEntity.getDescription());
		productDTO.setPrice(productEntity.getPrice());
		productDTO.setQuantity(productEntity.getQuantity());
		return productDTO;
	}

	@Override
	public UUID create(UUID productId, DiscountDTO discountDTO) throws CustomException {
		discountValidator.validate(discountDTO);
		validateIfProductExists(productId);
		DiscountEntity discountEntity = discountRepository.save(getFrom(productId, discountDTO));
		return discountEntity.getId();
	}

	@Override
	public List<DiscountDTO> getAllDiscounts(UUID productId) {
		List<DiscountEntity> discountEntities = discountRepository.findByProductIdAndApplicableTillAfter(productId,
				LocalDateTime.now());
		return discountEntities.stream().map(discount -> {
			return getDTOFrom(discount);
		}).collect(Collectors.toList());
	}

	private DiscountDTO getDTOFrom(DiscountEntity discount) {
		DiscountDTO discountDTO = new DiscountDTO();
		discountDTO.setId(discount.getId());
		discountDTO.setDescription(discount.getDescription());
		discountDTO.setBuyQuantity(discount.getBuyQuantity());
		discountDTO.setPercentage(discount.getPercentage());
		discountDTO.setApplicableTill(discount.getApplicableTill());
		return discountDTO;
	}

	private void ensureProductIsNew(String name) throws CustomException {
		Optional<ProductEntity> optionalEntity = productRepository.findByName(name.toLowerCase());
		if (optionalEntity.isPresent()) {
			throw new CustomException("Product already exists");
		}

	}

	private DiscountEntity getFrom(UUID productId, DiscountDTO discountDTO) {
		DiscountEntity discountEntity = new DiscountEntity();
		discountEntity.setId(UUID.randomUUID());
		discountEntity.setDescription(discountDTO.getDescription());
		discountEntity.setApplicableTill(discountDTO.getApplicableTill());
		discountEntity.setBuyQuantity(discountDTO.getBuyQuantity());
		discountEntity.setLastUpdatedAt(LocalDateTime.now());
		discountEntity.setPercentage(discountDTO.getPercentage());
		discountEntity.setProductId(productId);
		return discountEntity;
	}

	private void validateIfProductExists(UUID productId) throws CustomException {
		if (!productRepository.findById(productId).isPresent()) {
			throw new CustomException("Product for this discount does not exist");
		}
	}

	@Override
	public UUID create(UUID productId, DealDTO dealDTO) throws CustomException {
		dealValidator.validate(dealDTO);
		validateIfProductExists(productId);
		ensureFreeProductsAreValid(productId, dealDTO.getFreeProducts());
		DealEntity dealEntity = dealRepository.save(getFrom(productId, dealDTO));
		return dealEntity.getId();
	}

	@Override
	public List<DealDTO> getAllDeals(UUID productId) {
		List<DealEntity> dealEntities = dealRepository.findByProductIdAndApplicableTillAfter(productId,
				LocalDateTime.now());
		return dealEntities.stream().map(deal -> {
			return getDTOFrom(deal);
		}).collect(Collectors.toList());
	}

	private DealDTO getDTOFrom(DealEntity deal) {
		DealDTO dealDTO = new DealDTO();
		dealDTO.setId(deal.getId());
		dealDTO.setDescription(deal.getDescription());
		dealDTO.setBuyQuantity(deal.getBuyQuantity());
		dealDTO.setFreeProducts(deal.getProducts());
		dealDTO.setApplicableTill(deal.getApplicableTill());
		return dealDTO;
	}

	private void ensureFreeProductsAreValid(UUID productId, Map<UUID, Integer> freeProducts) throws CustomException {
		if (freeProducts.isEmpty()) {
			throw new CustomException("Free products must be greater than zero");
		}
		for (Map.Entry<UUID, Integer> entry : freeProducts.entrySet()) {
			validateIfProductExists(entry.getKey());
			if (entry.getValue() <= 0) {
				throw new CustomException("Free products must be greater than zero");
			}
		}

	}

	private DealEntity getFrom(UUID productId, DealDTO dealDTO) {
		DealEntity dealEntity = new DealEntity();
		dealEntity.setId(UUID.randomUUID());
		dealEntity.setDescription(dealDTO.getDescription());
		dealEntity.setApplicableTill(dealDTO.getApplicableTill());
		dealEntity.setBuyQuantity(dealDTO.getBuyQuantity());
		dealEntity.setLastUpdatedAt(LocalDateTime.now());
		dealEntity.setProducts(dealDTO.getFreeProducts());
		dealEntity.setProductId(productId);
		return dealEntity;
	}

}
