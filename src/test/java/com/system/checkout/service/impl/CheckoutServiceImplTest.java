package com.system.checkout.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.system.checkout.entity.BasketEntity;
import com.system.checkout.entity.DealEntity;
import com.system.checkout.entity.DiscountEntity;
import com.system.checkout.entity.ProductEntity;
import com.system.checkout.exception.CustomException;
import com.system.checkout.mocks.MockData;
import com.system.checkout.model.BasketDTO;
import com.system.checkout.model.DealCheckoutDTO;
import com.system.checkout.model.ProductCheckoutDTO;
import com.system.checkout.repository.BasketRepository;
import com.system.checkout.repository.DealRepository;
import com.system.checkout.repository.DiscountRepository;
import com.system.checkout.repository.ProductRepository;
import com.system.checkout.type.CheckoutStatus;

public class CheckoutServiceImplTest {

	@Mock
	private ProductRepository productRepository;

	@Mock
	private DiscountRepository discountRepository;

	@Mock
	private DealRepository dealRepository;

	@Mock
	private BasketRepository basketRepository;

	@InjectMocks
	private CheckoutServiceImpl checkoutService;

	private MockData mockData = new MockData();

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldAddProductToBasket() throws CustomException {
		Mockito.when(basketRepository.findByStatus(CheckoutStatus.BASKET)).thenReturn(Optional.empty());
		Mockito.when(productRepository.save(Mockito.any())).thenReturn(mockData.getProductEntity());
		Mockito.when(basketRepository.save(Mockito.any())).thenReturn(mockData.getBasketEntity());
		Mockito.when(productRepository.findById(mockData.PRODUCT_ID))
				.thenReturn(Optional.of(mockData.getProductEntity()));
		ArgumentCaptor<ProductEntity> productCaptor = ArgumentCaptor.forClass(ProductEntity.class);
		ArgumentCaptor<BasketEntity> basketCaptor = ArgumentCaptor.forClass(BasketEntity.class);
		checkoutService.update(mockData.PRODUCT_ID, mockData.productCheckoutDTO());
		Mockito.verify(productRepository, Mockito.times(1)).save(productCaptor.capture());
		Mockito.verify(basketRepository, Mockito.times(1)).save(basketCaptor.capture());

		ProductEntity productEntity = productCaptor.getValue();
		Assertions.assertEquals(productEntity.getQuantity(), 9);

		BasketEntity basketEntity = basketCaptor.getValue();
		Assertions.assertEquals(basketEntity.getProducts().get(mockData.PRODUCT_ID), 1);
	}

	@Test
	public void shouldUpdateProductToBasket() throws CustomException {
		Mockito.when(basketRepository.findByStatus(CheckoutStatus.BASKET))
				.thenReturn(Optional.of(mockData.getBasketEntity()));
		Mockito.when(productRepository.save(Mockito.any())).thenReturn(mockData.getProductEntity());
		Mockito.when(basketRepository.save(Mockito.any())).thenReturn(mockData.getBasketEntity());
		Mockito.when(productRepository.findById(mockData.PRODUCT_ID))
				.thenReturn(Optional.of(mockData.getProductEntity()));
		ArgumentCaptor<ProductEntity> productCaptor = ArgumentCaptor.forClass(ProductEntity.class);
		ArgumentCaptor<BasketEntity> basketCaptor = ArgumentCaptor.forClass(BasketEntity.class);
		checkoutService.update(mockData.PRODUCT_ID, mockData.productCheckoutDTO());
		Mockito.verify(productRepository, Mockito.times(1)).save(productCaptor.capture());
		Mockito.verify(basketRepository, Mockito.times(1)).save(basketCaptor.capture());

		ProductEntity productEntity = productCaptor.getValue();
		Assertions.assertEquals(productEntity.getQuantity(), 14);

		BasketEntity basketEntity = basketCaptor.getValue();
		Assertions.assertEquals(basketEntity.getProducts().get(mockData.PRODUCT_ID), 1);
	}

	@Test
	public void shouldFailToAddInvalidProductToBasket() throws CustomException {
		Mockito.when(basketRepository.findByStatus(CheckoutStatus.BASKET))
				.thenReturn(Optional.of(mockData.getBasketEntity()));
		Mockito.when(productRepository.findById(mockData.PRODUCT_ID)).thenReturn(Optional.empty());
		ArgumentCaptor<ProductEntity> productCaptor = ArgumentCaptor.forClass(ProductEntity.class);
		ArgumentCaptor<BasketEntity> basketCaptor = ArgumentCaptor.forClass(BasketEntity.class);
		Assertions.assertThrows(CustomException.class, () -> {
			checkoutService.update(mockData.PRODUCT_ID, mockData.productCheckoutDTO());
		});
		Mockito.verify(productRepository, Mockito.times(0)).save(productCaptor.capture());
		Mockito.verify(basketRepository, Mockito.times(0)).save(basketCaptor.capture());
	}

	@Test
	public void shouldFailToAddProductWithInsufficientInventoryToBasket() throws CustomException {
		Mockito.when(basketRepository.findByStatus(CheckoutStatus.BASKET))
				.thenReturn(Optional.of(mockData.getBasketEntity()));

		ProductEntity productEntity = mockData.getProductEntity();
		productEntity.setQuantity(0);

		Mockito.when(productRepository.findById(mockData.PRODUCT_ID)).thenReturn(Optional.of(productEntity));
		ArgumentCaptor<ProductEntity> productCaptor = ArgumentCaptor.forClass(ProductEntity.class);
		ArgumentCaptor<BasketEntity> basketCaptor = ArgumentCaptor.forClass(BasketEntity.class);
		Assertions.assertThrows(CustomException.class, () -> {
			checkoutService.update(mockData.PRODUCT_ID, mockData.productCheckoutDTO());
		});
		Mockito.verify(productRepository, Mockito.times(0)).save(productCaptor.capture());
		Mockito.verify(basketRepository, Mockito.times(0)).save(basketCaptor.capture());
	}

	@Test
	public void shouldFailToAddProductWithZeroQuantityToBasket() throws CustomException {
		Mockito.when(basketRepository.findByStatus(CheckoutStatus.BASKET))
				.thenReturn(Optional.of(mockData.getBasketEntity()));

		ProductCheckoutDTO productCheckoutDTO = mockData.productCheckoutDTO();
		productCheckoutDTO.setQuantity(0);

		ArgumentCaptor<ProductEntity> productCaptor = ArgumentCaptor.forClass(ProductEntity.class);
		ArgumentCaptor<BasketEntity> basketCaptor = ArgumentCaptor.forClass(BasketEntity.class);
		Assertions.assertThrows(CustomException.class, () -> {
			checkoutService.update(mockData.PRODUCT_ID, productCheckoutDTO);
		});
		Mockito.verify(productRepository, Mockito.times(0)).save(productCaptor.capture());
		Mockito.verify(basketRepository, Mockito.times(0)).save(basketCaptor.capture());
	}

	@Test
	public void shouldRemoveProductFromBasket() throws CustomException {
		Mockito.when(basketRepository.findByStatus(CheckoutStatus.BASKET))
				.thenReturn(Optional.of(mockData.getBasketEntity()));
		Mockito.when(productRepository.save(Mockito.any())).thenReturn(mockData.getProductEntity());
		Mockito.when(basketRepository.save(Mockito.any())).thenReturn(mockData.getBasketEntity());
		Mockito.when(productRepository.findById(mockData.PRODUCT_ID))
				.thenReturn(Optional.of(mockData.getProductEntity()));
		ArgumentCaptor<ProductEntity> productCaptor = ArgumentCaptor.forClass(ProductEntity.class);
		ArgumentCaptor<BasketEntity> basketCaptor = ArgumentCaptor.forClass(BasketEntity.class);
		checkoutService.delete(mockData.PRODUCT_ID);
		Mockito.verify(productRepository, Mockito.times(1)).save(productCaptor.capture());
		Mockito.verify(basketRepository, Mockito.times(1)).save(basketCaptor.capture());

		ProductEntity productEntity = productCaptor.getValue();
		Assertions.assertEquals(productEntity.getQuantity(), 15);

		BasketEntity basketEntity = basketCaptor.getValue();
		Assertions.assertNull(basketEntity.getProducts().get(mockData.PRODUCT_ID));
	}

	@Test
	public void shouldFailToGetBasketIfNoProductIsAddedOrRecentlyCheckedOut() {
		Mockito.when(basketRepository.findByStatus(CheckoutStatus.BASKET)).thenReturn(Optional.empty());
		Assertions.assertThrows(CustomException.class, () -> {
			checkoutService.get();
		});
	}

	@Test
	public void shouldGetBasketWithProductsIfNotCheckedOut() throws CustomException {
		Mockito.when(basketRepository.findByStatus(CheckoutStatus.BASKET))
				.thenReturn(Optional.of(mockData.getBasketEntity()));
		Mockito.when(productRepository.findById(mockData.PRODUCT_ID))
				.thenReturn(Optional.of(mockData.getProductEntity()));
		BasketDTO basketDTO = checkoutService.get();
		Assertions.assertEquals(1, basketDTO.getProducts().size());
		Assertions.assertEquals(5, basketDTO.getProducts().get(0).getQuantity());
		Assertions.assertEquals(5, basketDTO.getProducts().get(0).getQuantity());
		Assertions.assertEquals(mockData.PRODUCT_NAME, basketDTO.getProducts().get(0).getName());

	}

	@Test
	public void shouldCheckoutProductWithoutDealOrDiscount() throws CustomException {
		Mockito.when(basketRepository.findByStatus(CheckoutStatus.BASKET))
				.thenReturn(Optional.of(mockData.getBasketEntity()));
		Mockito.when(productRepository.findById(mockData.PRODUCT_ID))
				.thenReturn(Optional.of(mockData.getProductEntity()));
		Mockito.when(discountRepository.findByProductIdAndApplicableTillAfter(mockData.PRODUCT_ID, LocalDateTime.now()))
				.thenReturn(new ArrayList<DiscountEntity>());
		Mockito.when(dealRepository.findByProductIdAndApplicableTillAfter(mockData.PRODUCT_ID, LocalDateTime.now()))
				.thenReturn(new ArrayList<DealEntity>());
		Mockito.when(productRepository.findById(mockData.PRODUCT_ID))
				.thenReturn(Optional.of(mockData.getProductEntity()));
		Mockito.when(basketRepository.save(Mockito.any())).thenReturn(mockData.getBasketEntity());
		ArgumentCaptor<BasketEntity> basketCaptor = ArgumentCaptor.forClass(BasketEntity.class);

		BasketDTO basketDTO = checkoutService.checkout();
		Mockito.verify(basketRepository, Mockito.times(1)).save(basketCaptor.capture());
		Assertions.assertEquals(1, basketDTO.getProducts().size());
		Assertions.assertEquals(5, basketDTO.getProducts().get(0).getQuantity());
		Assertions.assertEquals(5, basketDTO.getProducts().get(0).getQuantity());
		Assertions.assertEquals(mockData.PRODUCT_NAME, basketDTO.getProducts().get(0).getName());
		Assertions.assertEquals(0, basketDTO.getDeals().size());
		Assertions.assertEquals(0, basketDTO.getDiscounts().size());
		Assertions.assertEquals("50.00", basketDTO.getTotalPrice().toString());
	}

	@Test
	public void shouldCheckoutProductWithDiscount() throws CustomException {
		Mockito.when(basketRepository.findByStatus(CheckoutStatus.BASKET))
				.thenReturn(Optional.of(mockData.getBasketEntity()));
		Mockito.when(productRepository.findById(mockData.PRODUCT_ID))
				.thenReturn(Optional.of(mockData.getProductEntity()));
		Mockito.when(discountRepository.findByProductIdAndApplicableTillAfter(Mockito.any(), Mockito.any()))
				.thenReturn(Arrays.asList(mockData.getDiscountEntity()));
		Mockito.when(dealRepository.findByProductIdAndApplicableTillAfter(mockData.PRODUCT_ID, LocalDateTime.now()))
				.thenReturn(new ArrayList<DealEntity>());
		Mockito.when(productRepository.findById(mockData.PRODUCT_ID))
				.thenReturn(Optional.of(mockData.getProductEntity()));
		Mockito.when(discountRepository.getOne(mockData.DISCOUNT_ID)).thenReturn(mockData.getDiscountEntity());
		Mockito.when(basketRepository.save(Mockito.any())).thenReturn(mockData.getBasketEntity());
		ArgumentCaptor<BasketEntity> basketCaptor = ArgumentCaptor.forClass(BasketEntity.class);

		BasketDTO basketDTO = checkoutService.checkout();
		Mockito.verify(basketRepository, Mockito.times(1)).save(basketCaptor.capture());
		Assertions.assertEquals(1, basketDTO.getProducts().size());
		Assertions.assertEquals(5, basketDTO.getProducts().get(0).getQuantity());
		Assertions.assertEquals(5, basketDTO.getProducts().get(0).getQuantity());
		Assertions.assertEquals(mockData.PRODUCT_NAME, basketDTO.getProducts().get(0).getName());
		Assertions.assertEquals(0, basketDTO.getDeals().size());
		Assertions.assertEquals(1, basketDTO.getDiscounts().size());
		Assertions.assertEquals("description", basketDTO.getDiscounts().get(0));
		Assertions.assertEquals("48.00", basketDTO.getTotalPrice().toString());
	}

	@Test
	public void shouldCheckoutProductWithDeal() throws CustomException {
		Mockito.when(basketRepository.findByStatus(CheckoutStatus.BASKET))
				.thenReturn(Optional.of(mockData.getBasketEntity()));
		Mockito.when(productRepository.findById(mockData.PRODUCT_ID))
				.thenReturn(Optional.of(mockData.getProductEntity()));
		Mockito.when(discountRepository.findByProductIdAndApplicableTillAfter(Mockito.any(), Mockito.any()))
				.thenReturn(new ArrayList<>());
		Mockito.when(dealRepository.findByProductIdAndApplicableTillAfter(Mockito.any(), Mockito.any()))
				.thenReturn(Arrays.asList(mockData.getDealEntity()));
		Mockito.when(productRepository.findByIdAndQuantityGreaterThanEqual(mockData.PRODUCT_ID, 1))
				.thenReturn(Optional.of(mockData.getProductEntity()));
		Mockito.when(productRepository.save(Mockito.any())).thenReturn(mockData.getProductEntity());
		Mockito.when(basketRepository.save(Mockito.any())).thenReturn(mockData.getBasketEntity());
		ArgumentCaptor<ProductEntity> productCaptor = ArgumentCaptor.forClass(ProductEntity.class);
		ArgumentCaptor<BasketEntity> basketCaptor = ArgumentCaptor.forClass(BasketEntity.class);
		Mockito.when(dealRepository.getOne(mockData.DEAL_ID)).thenReturn(mockData.getDealEntity());
		BasketDTO basketDTO = checkoutService.checkout();

		Mockito.verify(basketRepository, Mockito.times(1)).save(basketCaptor.capture());
		Mockito.verify(productRepository, Mockito.times(1)).save(productCaptor.capture());

		ProductEntity productEntity = productCaptor.getValue();
		Assertions.assertEquals(9, productEntity.getQuantity());

		Assertions.assertEquals(1, basketDTO.getProducts().size());
		Assertions.assertEquals(5, basketDTO.getProducts().get(0).getQuantity());
		Assertions.assertEquals(5, basketDTO.getProducts().get(0).getQuantity());
		Assertions.assertEquals(mockData.PRODUCT_NAME, basketDTO.getProducts().get(0).getName());
		Assertions.assertEquals(1, basketDTO.getDeals().size());
		Assertions.assertEquals(0, basketDTO.getDiscounts().size());
		DealCheckoutDTO dealCheckoutDTO = basketDTO.getDeals().get(0);
		Assertions.assertEquals("description", dealCheckoutDTO.getName());
		Assertions.assertEquals(1, dealCheckoutDTO.getProducts().size());
		Assertions.assertEquals(mockData.PRODUCT_NAME, dealCheckoutDTO.getProducts().get(0).getName());
		Assertions.assertEquals(1, dealCheckoutDTO.getProducts().get(0).getQuantity());

		Assertions.assertEquals("50.00", basketDTO.getTotalPrice().toString());
	}

	@Test
	public void shouldCheckoutProductWithoutDealForInsufficientInventoryOfDealProduct() throws CustomException {
		Mockito.when(basketRepository.findByStatus(CheckoutStatus.BASKET))
				.thenReturn(Optional.of(mockData.getBasketEntity()));
		Mockito.when(productRepository.findById(mockData.PRODUCT_ID))
				.thenReturn(Optional.of(mockData.getProductEntity()));
		Mockito.when(discountRepository.findByProductIdAndApplicableTillAfter(Mockito.any(), Mockito.any()))
				.thenReturn(new ArrayList<>());
		Mockito.when(dealRepository.findByProductIdAndApplicableTillAfter(Mockito.any(), Mockito.any()))
				.thenReturn(Arrays.asList(mockData.getDealEntity()));
		Mockito.when(productRepository.findByIdAndQuantityGreaterThanEqual(mockData.PRODUCT_ID, 1))
				.thenReturn(Optional.empty());
		Mockito.when(productRepository.save(Mockito.any())).thenReturn(mockData.getProductEntity());
		Mockito.when(basketRepository.save(Mockito.any())).thenReturn(mockData.getBasketEntity());
		ArgumentCaptor<ProductEntity> productCaptor = ArgumentCaptor.forClass(ProductEntity.class);
		ArgumentCaptor<BasketEntity> basketCaptor = ArgumentCaptor.forClass(BasketEntity.class);
		Mockito.when(dealRepository.getOne(mockData.DEAL_ID)).thenReturn(mockData.getDealEntity());
		BasketDTO basketDTO = checkoutService.checkout();

		Mockito.verify(basketRepository, Mockito.times(1)).save(basketCaptor.capture());
		Mockito.verify(productRepository, Mockito.times(0)).save(productCaptor.capture());

		Assertions.assertEquals(1, basketDTO.getProducts().size());
		Assertions.assertEquals(5, basketDTO.getProducts().get(0).getQuantity());
		Assertions.assertEquals(5, basketDTO.getProducts().get(0).getQuantity());
		Assertions.assertEquals(mockData.PRODUCT_NAME, basketDTO.getProducts().get(0).getName());
		Assertions.assertEquals(0, basketDTO.getDeals().size());
		Assertions.assertEquals(0, basketDTO.getDiscounts().size());

		Assertions.assertEquals("50.00", basketDTO.getTotalPrice().toString());
	}
}
