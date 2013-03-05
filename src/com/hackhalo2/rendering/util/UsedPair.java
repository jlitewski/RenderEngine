package com.hackhalo2.rendering.util;

/**
 * A modified Pair<F, S> to keep track of how many times it has been used. Used with {@link UsedPairSorter}
 * for caching the most used Pairs<F, S> for quicker access
 * 
 * @author hackhalo2
 *
 * @param <F> The First Object
 * @param <S> The Second Object
 */
public class UsedPair<F, S> extends Pair<F, S> {
	
	protected int usedCounter = 0; //The internal counter of how many times this Pair has been used

	public UsedPair(F first, S second) {
		super(first, second);
	}
	
	public void incrementUsedCounter() {
		this.usedCounter++;
	}

}
