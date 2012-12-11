package com.hackhalo2.rendering.interfaces.other;

/**
 * The Factory Provider Interface for Objects that support Factories.
 *
 * @author Jacob "HACKhalo2" Litewski
 *
 * @param <V> The Factory Object to define
 */
public interface IFactoryProvider<V> {

	/**
	 * Get the factory object
	 * @return The Factory Object
	 */
	public V getFactory();
}
