package com.hackhalo2.rendering.util;

import java.util.Comparator;

public class UsedPairSorter implements Comparator<UsedPair<?, ?>> {
	
	public UsedPairSorter() { }

	@Override
	public int compare(UsedPair<?, ?> one, UsedPair<?, ?> two) {
		return one.usedCounter - two.usedCounter;
	}

}
