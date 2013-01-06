package com.hackhalo2.rendering.util;

import java.util.Comparator;

import com.hackhalo2.rendering.interfaces.core.IPluggable;

public class TreeSetPlugableSorter implements Comparator<IPluggable> {

	public TreeSetPlugableSorter() { }

	@Override
	public int compare(IPluggable o1, IPluggable o2) {
		return o1.getPriority().getLevel() - o2.getPriority().getLevel();
	}

}
