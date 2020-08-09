package com.system.checkout.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.system.checkout.entity.DealEntity;
import com.system.checkout.entity.DiscountEntity;
import com.system.checkout.entity.ProductEntity;
import com.system.checkout.exception.CustomException;
import com.system.checkout.mocks.MockData;
import com.system.checkout.model.DealDTO;
import com.system.checkout.model.DiscountDTO;
import com.system.checkout.model.ProductDTO;
import com.system.checkout.repository.DealRepository;
import com.system.checkout.repository.DiscountRepository;
import com.system.checkout.repository.ProductRepository;
import com.system.checkout.validator.impl.DealValidator;
import com.system.checkout.validator.impl.DiscountValidator;
import com.system.checkout.validator.impl.ProductValidator;

public class StoreOwnerServiceImplTest {

	@Mock
	private ProductRepository productRepository;

	@Mock
	private DiscountRepository discountRepository;

	@Mock
	private DealRepository dealRepository;

	@Mock
	private ProductValidator productValidator;

	@Mock
	private DiscountValidator discountValidator;

	@Mock
	private DealValidator dealValidator;

	@InjectMocks
	private StoreOwnerServiceImpl storeOwnerService;

	private MockData mockData = new MockData();

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldCreateProduct() throws CustomException {
		Mockito.doNothing().when(productValidator).validate(Mockito.any());
		Mockito.when(productRepository.save(Mockito.any())).thenReturn(mockData.getProductEntity());
		Mockito.when(productRepository.findByName(mockData.PRODUCT_NAME.toLowerCase())).thenReturn(Optional.empty());
		ArgumentCaptor<ProductEntity> argumentCaptor = ArgumentCaptor.forClass(ProductEntity.class);
		storeOwnerService.create(mockData.getProdctDTO());
		Mockito.verify(productRepository, Mockito.times(1)).save(argumentCaptor.capture());

		ProductEntity productEntity = argumentCaptor.getValue();

		Assertions.assertEquals("description", productEntity.getDescription());
		Assertions.assertEquals(mockData.PRODUCT_NAME, productEntity.getName());
		Assertions.assertEquals(BigDecimal.valueOf(10), productEntity.getPrice());
		Assertions.assertEquals(10, productEntity.getQuantity());
	}

	@Test
	public void shouldFailToCreateExistingProduct() throws CustomException {
		Mockito.doNothing().when(productValidator).validate(Mockito.any());
		Mockito.when(productRepository.save(Mockito.any())).thenReturn(mockData.getProductEntity());
		Mockito.when(productRepository.findByName(mockData.PRODUCT_NAME.toLowerCase()))
				.thenReturn(Optional.of(mockData.getProductEntity()));
		ArgumentCaptor<ProductEntity> argumentCaptor = ArgumentCaptor.forClass(ProductEntity.class);
		Assertions.assertThrows(CustomException.class, () -> {
			storeOwnerService.create(mockData.getProdctDTO());
		});
		Mockito.verify(productRepository, Mockito.times(0)).save(argumentCaptor.capture());
	}

	@Test
	public void shouldUpdateProduct() throws CustomException {
		Mockito.doNothing().when(productValidator).validate(Mockito.any());
		Mockito.when(productRepository.save(Mockito.any())).thenReturn(mockData.getProductEntity());
		Mockito.when(productRepository.findById(mockData.PRODUCT_ID))
				.thenReturn(Optional.of(mockData.getProductEntity()));
		ArgumentCaptor<ProductEntity> argumentCaptor = ArgumentCaptor.forClass(ProductEntity.class);
		storeOwnerService.update(mockData.PRODUCT_ID, mockData.getProdctDTO());
		Mockito.verify(productRepository, Mockito.times(1)).save(argumentCaptor.capture());

		ProductEntity productEntity = argumentCaptor.getValue();

		Assertions.assertEquals(mockData.PRODUCT_ID, productEntity.getId());
		Assertions.assertEquals("description", productEntity.getDescription());
		Assertions.assertEquals(mockData.PRODUCT_NAME, productEntity.getName());
		Assertions.assertEquals(BigDecimal.valueOf(10), productEntity.getPrice());
		Assertions.assertEquals(10, productEntity.getQuantity());

	}

	@Test
	public void shouldRemoveProduct() throws CustomException {
		Mockito.doNothing().when(productValidator).validate(Mockito.any());
		Mockito.when(productRepository.findById(mockData.PRODUCT_ID))
				.thenReturn(Optional.of(mockData.getProductEntity()));
		ArgumentCaptor<ProductEntity> argumentCaptor = ArgumentCaptor.forClass(ProductEntity.class);
		storeOwnerService.delete(mockData.PRODUCT_ID);
		Mockito.verify(productRepository, Mockito.times(1)).delete(argumentCaptor.capture());
	}

	@Test
	public void shouldGetAllProducts() throws CustomException {
		Mockito.when(productRepository.findAll()).thenReturn(Arrays.asList(mockData.getProductEntity()));
		List<ProductDTO> products = storeOwnerService.getAll();
		Assertions.assertEquals(1, products.size());
		ProductDTO productDTO = products.get(0);
		Assertions.assertEquals("desc", productDTO.getDescription());
		Assertions.assertEquals(mockData.PRODUCT_NAME, productDTO.getName());
		Assertions.assertEquals(BigDecimal.valueOf(10), productDTO.getPrice());
		Assertions.assertEquals(10, productDTO.getQuantity());

	}

	@Test
	public void shouldCreateDiscount() throws CustomException {
		Mockito.doNothing().when(discountValidator).validate(Mockito.any());
		Mockito.when(discountRepository.save(Mockito.any())).thenReturn(mockData.getDiscountEntity());
		Mockito.when(productRepository.findById(mockData.PRODUCT_ID))
				.thenReturn(Optional.of(mockData.getProductEntity()));
		ArgumentCaptor<DiscountEntity> argumentCaptor = ArgumentCaptor.forClass(DiscountEntity.class);
		storeOwnerService.create(mockData.PRODUCT_ID, mockData.getDiscountDTO());
		Mockito.verify(discountRepository, Mockito.times(1)).save(argumentCaptor.capture());

		DiscountEntity discountEntity = argumentCaptor.getValue();

		Assertions.assertEquals("description", discountEntity.getDescription());
		Assertions.assertEquals(mockData.PRODUCT_ID, discountEntity.getProductId());
		Assertions.assertEquals(2, discountEntity.getBuyQuantity());
		Assertions.assertEquals(10, discountEntity.getPercentage());

	}

	@Test
	public void shouldFailToCreateDiscountForInvalidProduct() throws CustomException {
		Mockito.doNothing().when(discountValidator).validate(Mockito.any());
		Mockito.when(discountRepository.save(Mockito.any())).thenReturn(mockData.getDiscountEntity());
		Mockito.when(productRepository.findById(mockData.PRODUCT_ID)).thenReturn(Optional.empty());
		ArgumentCaptor<DiscountEntity> argumentCaptor = ArgumentCaptor.forClass(DiscountEntity.class);
		Assertions.assertThrows(CustomException.class, () -> {
			storeOwnerService.create(mockData.PRODUCT_ID, mockData.getDiscountDTO());

		});
		Mockito.verify(discountRepository, Mockito.times(0)).save(argumentCaptor.capture());
	}

	@Test
	public void shouldGetAllActiveDiscounts() throws CustomException {
		Mockito.when(discountRepository.findByProductIdAndApplicableTillAfter(Mockito.any(), Mockito.any()))
				.thenReturn(Arrays.asList(mockData.getDiscountEntity()));
		List<DiscountDTO> discounts = storeOwnerService.getAllDiscounts(mockData.PRODUCT_ID);
		Assertions.assertEquals(1, discounts.size());
		DiscountDTO discountDTO = discounts.get(0);
		Assertions.assertEquals("description", discountDTO.getDescription());
		Assertions.assertEquals(10, discountDTO.getPercentage());
		Assertions.assertEquals(2, discountDTO.getBuyQuantity());
	}

	@Test
	public void shouldCreateDeal() throws CustomException {
		Mockito.doNothing().when(dealValidator).validate(Mockito.any());
		Mockito.when(dealRepository.save(Mockito.any())).thenReturn(mockData.getDealEntity());
		Mockito.when(productRepository.findById(mockData.PRODUCT_ID))
				.thenReturn(Optional.of(mockData.getProductEntity()));
		ArgumentCaptor<DealEntity> argumentCaptor = ArgumentCaptor.forClass(DealEntity.class);
		storeOwnerService.create(mockData.PRODUCT_ID, mockData.getDealDTO());
		Mockito.verify(dealRepository, Mockito.times(1)).save(argumentCaptor.capture());
		DealEntity dealEntity = argumentCaptor.getValue();
		Assertions.assertEquals("description", dealEntity.getDescription());
		Assertions.assertEquals(mockData.PRODUCT_ID, dealEntity.getProductId());
		Assertions.assertEquals(2, dealEntity.getBuyQuantity());
		Assertions.assertEquals(1, dealEntity.getProducts().keySet().size());
	}

	@Test
	public void shouldFailToCreateDealForInvalidProduct() throws CustomException {
		Mockito.doNothing().when(dealValidator).validate(Mockito.any());
		Mockito.when(productRepository.findById(mockData.PRODUCT_ID)).thenReturn(Optional.empty());
		ArgumentCaptor<DealEntity> argumentCaptor = ArgumentCaptor.forClass(DealEntity.class);
		Assertions.assertThrows(CustomException.class, () -> {
			storeOwnerService.create(mockData.PRODUCT_ID, mockData.getDealDTO());

		});
		Mockito.verify(dealRepository, Mockito.times(0)).save(argumentCaptor.capture());
	}

	@Test
	public void shouldFailToCreateDealForZeroFreeProducts() throws CustomException {
		Mockito.doNothing().when(dealValidator).validate(Mockito.any());
		DealDTO dealDTO = mockData.getDealDTO();
		dealDTO.setFreeProducts(new HashMap());
		ArgumentCaptor<DealEntity> argumentCaptor = ArgumentCaptor.forClass(DealEntity.class);
		Assertions.assertThrows(CustomException.class, () -> {
			storeOwnerService.create(mockData.PRODUCT_ID, mockData.getDealDTO());

		});
		Mockito.verify(dealRepository, Mockito.times(0)).save(argumentCaptor.capture());
	}

	@Test
	public void shouldGetAllActiveDealss() throws CustomException {
		Mockito.when(dealRepository.findByProductIdAndApplicableTillAfter(Mockito.any(), Mockito.any()))
				.thenReturn(Arrays.asList(mockData.getDealEntity()));
		List<DealDTO> deals = storeOwnerService.getAllDeals(mockData.PRODUCT_ID);
		Assertions.assertEquals(1, deals.size());
		DealDTO dealDTO = deals.get(0);
		Assertions.assertEquals("description", dealDTO.getDescription());
		Assertions.assertEquals(1, dealDTO.getFreeProducts().size());
		Assertions.assertEquals(1, dealDTO.getFreeProducts().get(mockData.PRODUCT_ID));
		Assertions.assertEquals(2, dealDTO.getBuyQuantity());
	}
}
