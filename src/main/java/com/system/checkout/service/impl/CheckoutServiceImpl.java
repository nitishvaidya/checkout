package com.system.checkout.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.system.checkout.entity.BasketEntity;
import com.system.checkout.entity.DealEntity;
import com.system.checkout.entity.DiscountEntity;
import com.system.checkout.entity.ProductEntity;
import com.system.checkout.exception.CustomException;
import com.system.checkout.model.BasketDTO;
import com.system.checkout.model.DealCheckoutDTO;
import com.system.checkout.model.ProductCheckoutDTO;
import com.system.checkout.repository.BasketRepository;
import com.system.checkout.repository.DealRepository;
import com.system.checkout.repository.DiscountRepository;
import com.system.checkout.repository.ProductRepository;
import com.system.checkout.service.CheckoutService;
import com.system.checkout.type.CheckoutStatus;
import com.system.checkout.util.DealComparator;
import com.system.checkout.util.DiscountComparator;

@Service
public class CheckoutServiceImpl implements CheckoutService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private DiscountRepository discountRepository;

	@Autowired
	private DealRepository dealRepository;

	@Autowired
	private BasketRepository basketRepository;

	@Override
	public BasketDTO get() throws CustomException {
		Optional<BasketEntity> optionalEntity = basketRepository.findByStatus(CheckoutStatus.BASKET);
		if (optionalEntity.isPresent()) {
			return getDTOFrom(optionalEntity.get());
		}
		throw new CustomException("No active basket exists. Add product to basket to generate basket automatically");
	}

	private BasketDTO getDTOFrom(BasketEntity basketEntity) throws CustomException {
		BasketDTO basketDTO = new BasketDTO();
		basketDTO.setProducts(getProducts(basketEntity.getProducts()));
		return basketDTO;
	}

	private List<ProductCheckoutDTO> getProducts(Map<UUID, Integer> map) throws CustomException {
		List<ProductCheckoutDTO> productCheckoutDTOs = new ArrayList<ProductCheckoutDTO>();
		for (Map.Entry<UUID, Integer> entry : map.entrySet()) {
			Optional<ProductEntity> optional = productRepository.findById(entry.getKey());
			if (optional.isPresent()) {
				ProductCheckoutDTO productCheckoutDTO = getDTOFrom(entry.getValue(), optional.get().getName());
				productCheckoutDTOs.add(productCheckoutDTO);
			} else {
				throw new CustomException("No product found product  " + entry.getKey() + " present in for basket");
			}
		}
		return productCheckoutDTOs;
	}

	private ProductCheckoutDTO getDTOFrom(Integer quantity, String name) {
		ProductCheckoutDTO productCheckoutDTO = new ProductCheckoutDTO();
		productCheckoutDTO.setName(name);
		productCheckoutDTO.setQuantity(quantity);
		return productCheckoutDTO;
	}

	@Override
	public void update(UUID productId, ProductCheckoutDTO productCheckoutDTO) throws CustomException {
		BasketEntity basketEntity = null;
		Optional<BasketEntity> optional = basketRepository.findByStatus(CheckoutStatus.BASKET);
		if (optional.isPresent()) {
			basketEntity = optional.get();
		} else {
			basketEntity = createBasket();
		}
		addOrUpdateProduct(productId, productCheckoutDTO, basketEntity);
	}

	@Transactional
	private void addOrUpdateProduct(UUID productId, ProductCheckoutDTO productCheckoutDTO, BasketEntity basketEntity)
			throws CustomException {
		ProductEntity productEntity = getProductIfCanBeAdded(productId, productCheckoutDTO);
		Integer existingQuantity = basketEntity.getProducts().get(productId);
		if (existingQuantity == null) {
			existingQuantity = 0;
		}
		basketEntity.getProducts().put(productId, productCheckoutDTO.getQuantity());
		basketEntity.setLastUpdatedAt(LocalDateTime.now());
		basketRepository.save(basketEntity);

		productEntity.setQuantity(productEntity.getQuantity() + existingQuantity - productCheckoutDTO.getQuantity());
		productRepository.save(productEntity);

	}

	private ProductEntity getProductIfCanBeAdded(UUID productId, ProductCheckoutDTO productCheckoutDTO)
			throws CustomException {
		if (productCheckoutDTO.getQuantity() <= 0) {
			throw new CustomException("Need to provide atleast 1 quantity for this product");
		}
		Optional<ProductEntity> optional = productRepository.findById(productId);
		if (!optional.isPresent()) {
			throw new CustomException("This product does not exist");
		}
		if (productCheckoutDTO.getQuantity() > optional.get().getQuantity()) {
			throw new CustomException(
					"Only " + optional.get().getQuantity() + "quantity left for product " + optional.get().getName());
		}
		return optional.get();

	}

	private BasketEntity createBasket() {
		BasketEntity basketEntity = new BasketEntity();
		basketEntity.setId(UUID.randomUUID());
		basketEntity.setStatus(CheckoutStatus.BASKET);
		basketEntity.setProducts(new HashMap<>());
		basketEntity.setDeals(new ArrayList<>());
		basketEntity.setDiscounts(new ArrayList<>());
		return basketEntity;
	}

	@Override
	public void delete(UUID productId) throws CustomException {
		Optional<BasketEntity> optional = basketRepository.findByStatus(CheckoutStatus.BASKET);
		if (optional.isPresent()) {
			BasketEntity basketEntity = optional.get();
			removeProduct(productId, basketEntity);
		} else {
			throw new CustomException("Basked is either already checkout or not created");
		}

	}

	@Transactional
	private void removeProduct(UUID productId, BasketEntity basketEntity) {
		Integer quantity = basketEntity.getProducts().remove(productId);
		Optional<ProductEntity> optional = productRepository.findById(productId);
		if (optional.isPresent()) {
			ProductEntity productEntity = optional.get();
			productEntity.setQuantity(productEntity.getQuantity() + quantity);
			productRepository.save(productEntity);
		}
		basketEntity.setLastUpdatedAt(LocalDateTime.now());
		basketRepository.save(basketEntity);
	}

	@Override
	public BasketDTO checkout() throws CustomException {
		Optional<BasketEntity> optional = basketRepository.findByStatus(CheckoutStatus.BASKET);
		if (optional.isPresent()) {
			BasketEntity basketEntity = optional.get();
			return process(basketEntity);
		} else {
			throw new CustomException("Basked is either already checkout or not created");
		}
	}

	@Transactional
	private BasketDTO process(BasketEntity basketEntity) throws CustomException {
		for (Map.Entry<UUID, Integer> entry : basketEntity.getProducts().entrySet()) {
			Optional<ProductEntity> optional = productRepository.findById(entry.getKey());
			if (optional.isPresent()) {
				ProductEntity productEntity = optional.get();
				Integer quantity = entry.getValue();
				applyDiscountTo(basketEntity, productEntity.getId(), optional.get().getPrice(), quantity);
				applyDealsTo(basketEntity, productEntity.getId(), quantity);
			} else {
				throw new CustomException("No product found during checkout for id: " + entry.getKey());
			}
		}
		return getSummary(basketEntity);
	}

	private BasketDTO getSummary(BasketEntity basketEntity) throws CustomException {
		BasketDTO basketDTO = new BasketDTO();
		basketEntity.setSummary(basketDTO);
		getProducts(basketEntity);
		getDiscountSummary(basketEntity);
		getDealsSummary(basketEntity);
		basketDTO.setTotalPrice(basketEntity.getTotalPrice());
		basketEntity.setStatus(CheckoutStatus.CHECKOUT);
		basketRepository.save(basketEntity);
		return basketDTO;
	}

	private void getDealsSummary(BasketEntity basketEntity) throws CustomException {
		ArrayList<DealCheckoutDTO> deals = new ArrayList<>();
		for (UUID id : basketEntity.getDeals()) {
			DealEntity dealEntity = dealRepository.getOne(id);
			DealCheckoutDTO dealCheckoutDTO = new DealCheckoutDTO();
			dealCheckoutDTO.setName(dealEntity.getDescription());
			dealCheckoutDTO.setProducts(getProducts(dealEntity.getProducts()));
			deals.add(dealCheckoutDTO);
		}
		basketEntity.getSummary().setDeals(deals);
	}

	private void getDiscountSummary(BasketEntity basketEntity) {
		ArrayList<String> discounts = new ArrayList<>();
		for (UUID id : basketEntity.getDiscounts()) {
			DiscountEntity discountEntity = discountRepository.getOne(id);
			discounts.add(discountEntity.getDescription());
		}
		basketEntity.getSummary().setDiscounts(discounts);

	}

	private void getProducts(BasketEntity basketEntity) throws CustomException {
		basketEntity.getSummary().setProducts(getProducts(basketEntity.getProducts()));

	}

	private void applyDealsTo(BasketEntity basketEntity, UUID productId, Integer quantity) {
		List<DealEntity> dealEntities = getApplicableDealsForProduct(productId);
		DealEntity dealEntity = null;
		try {
			dealEntity = getMostApplicableDeal(quantity, dealEntities);
			basketEntity.getDeals().add(dealEntity.getId());
			basketEntity.setFreeProducts(new HashMap<>(dealEntity.getProducts()));
		} catch (CustomException e) {
			System.out.println(e.getMessage());
		}

	}

	private DealEntity getMostApplicableDeal(Integer quantity, List<DealEntity> dealEntities) throws CustomException {
		Collections.sort(dealEntities, new DealComparator());
		System.out.println(dealEntities);
		for (DealEntity dealEntity : dealEntities) {
			if (quantity >= dealEntity.getBuyQuantity() && allProductsPresent(dealEntity.getProducts())) {
				return dealEntity;
			}
		}
		throw new CustomException("No valid deal found");

	}

	private boolean allProductsPresent(Map<UUID, Integer> products) {
		for (Map.Entry<UUID, Integer> product : products.entrySet()) {
			Optional<ProductEntity> optional = productRepository.findByIdAndQuantityGreaterThanEqual(product.getKey(),
					product.getValue());
			if (!optional.isPresent()) {
				return false;
			}

			ProductEntity productEntity = optional.get();
			productEntity.setQuantity(productEntity.getQuantity() - product.getValue());
			productRepository.save(productEntity);
		}
		return true;
	}

	private void applyDiscountTo(BasketEntity basketEntity, UUID productId, BigDecimal price, Integer quantity) {
		List<DiscountEntity> discountEntities = getApplicableDiscountsForProduct(productId);
		DiscountEntity discountEntity = null;
		try {
			discountEntity = getMostApplicableDiscount(discountEntities, quantity);
			basketEntity.getDiscounts().add(discountEntity.getId());
		} catch (CustomException e) {
			System.out.println(e.getMessage());
		}
		BigDecimal totalPrice = calculatePrice(price, quantity, discountEntity);
		basketEntity.setTotalPrice(totalPrice);

	}

	private BigDecimal calculatePrice(BigDecimal price, Integer quantity, DiscountEntity discountEntity) {
		if (discountEntity == null) {
			return getPriceFor(price, quantity);
		}
		BigDecimal discountedPrice = getDiscountedPrice(price, discountEntity.getPercentage());
		return getPriceFor(discountedPrice, discountEntity.getBuyQuantity())
				.add(getPriceFor(price, quantity - discountEntity.getBuyQuantity()));
	}

	private BigDecimal getPriceFor(BigDecimal price, Integer quantity) {
		return roundOff(price.multiply(BigDecimal.valueOf(quantity)));
	}

	private BigDecimal getDiscountedPrice(BigDecimal price, Integer percentage) {
		return roundOff(price.subtract(price.multiply(BigDecimal.valueOf((float)percentage / 100))));
	}

	private BigDecimal roundOff(BigDecimal value) {
		return value.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}

	private DiscountEntity getMostApplicableDiscount(List<DiscountEntity> discountEntities, Integer quantity)
			throws CustomException {
		Collections.sort(discountEntities, new DiscountComparator());
		System.out.println(discountEntities);
		for (DiscountEntity discountEntity : discountEntities) {
			if (quantity >= discountEntity.getBuyQuantity()) {
				return discountEntity;
			}
		}
		throw new CustomException("No valid discount found");

	}

	private List<DiscountEntity> getApplicableDiscountsForProduct(UUID productId) {
		return discountRepository.findByProductIdAndApplicableTillAfter(productId, LocalDateTime.now());
	}

	private List<DealEntity> getApplicableDealsForProduct(UUID productId) {
		return dealRepository.findByProductIdAndApplicableTillAfter(productId, LocalDateTime.now());
	}

}
