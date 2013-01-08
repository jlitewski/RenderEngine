package com.hackhalo2.rendering.util;

import java.util.Comparator;

import com.hackhalo2.rendering.RenderEngine.PlugMode.Priority;

public class PrioritySorter implements Comparator<Priority> {
	
	public PrioritySorter() { }

	@Override
	public int compare(Priority one, Priority two) {
		return one.getLevel() - two.getLevel();
	}

}
