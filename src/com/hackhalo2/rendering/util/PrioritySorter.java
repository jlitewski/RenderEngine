package com.hackhalo2.rendering.util;

import java.util.Comparator;

import com.hackhalo2.rendering.interfaces.IPlugable;

public class PrioritySorter implements Comparator<IPlugable> {

	public PrioritySorter() { }

	@Override
	public int compare(IPlugable o1, IPlugable o2) {
		return o1.getPriority().getLevel() - o2.getPriority().getLevel();
	}

}
