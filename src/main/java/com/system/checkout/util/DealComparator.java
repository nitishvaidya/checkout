package com.system.checkout.util;

import java.util.Comparator;

import com.system.checkout.entity.DealEntity;

public class DealComparator implements Comparator<DealEntity> {

	public int compare(DealEntity a, DealEntity b) {
		if (a.getBuyQuantity() > b.getBuyQuantity()) {
			return 1;
		}
		if (a.getBuyQuantity() == b.getBuyQuantity()) {
			return 0;
		}
		return -1;
	}
}
