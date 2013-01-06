package com.hackhalo2.rendering.util;

import java.util.Comparator;

import com.hackhalo2.rendering.RenderEngine.PlugMode;

public class PlugModeSorter implements Comparator<PlugMode> {
	
	public PlugModeSorter() { }

	@Override
	public int compare(PlugMode one, PlugMode two) {
		return one.getPriority().getLevel() - two.getPriority().getLevel();
	}
}
