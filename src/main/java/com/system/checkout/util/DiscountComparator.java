package com.system.checkout.util;

import java.util.Comparator;

import com.system.checkout.entity.DiscountEntity;

public class DiscountComparator implements Comparator<DiscountEntity> {

	public int compare(DiscountEntity a, DiscountEntity b) {
		if (a.getBuyQuantity() > b.getBuyQuantity()) {
			return 1;
		}
		if (a.getBuyQuantity() == b.getBuyQuantity()) {
			return 0;
		}
		return -1;
	}
}
