package xyz.algogo.core.evaluator.variable;

import java.math.BigDecimal;

/**
 * Represents all available variable types.
 */

public enum VariableType {

	/**
	 * Number type.
	 */

	NUMBER(BigDecimal.ZERO),

	/**
	 * String type.
	 */

	STRING("");

	/**
	 * The type's default value.
	 */

	private final Object defaultValue;

	/**
	 * Creates a new variable type instance.
	 *
	 * @param defaultValue The type's default value.
	 */

	VariableType(final Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * Returns the type's default value.
	 *
	 * @return The type's default value.
	 */

	public final Object getDefaultValue() {
		return defaultValue;
	}

}