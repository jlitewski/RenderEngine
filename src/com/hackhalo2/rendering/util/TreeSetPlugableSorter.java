package com.hackhalo2.rendering.util;

import java.util.Comparator;

import com.hackhalo2.rendering.interfaces.core.IPlugable;

public class TreeSetPlugableSorter implements Comparator<IPlugable> {

	public TreeSetPlugableSorter() { }

	@Override
	public int compare(IPlugable o1, IPlugable o2) {
		return o1.getPriority().getLevel() - o2.getPriority().getLevel();
	}

}
