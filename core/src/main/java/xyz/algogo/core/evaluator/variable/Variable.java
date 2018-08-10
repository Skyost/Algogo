package xyz.algogo.core.evaluator.variable;

/**
 * Represents a variable.
 */

public class Variable {

	/**
	 * The variable identifier.
	 */

	private final String identifier;

	/**
	 * The variable type.
	 */

	private VariableType type;

	/**
	 * The variable value.
	 */

	private Object value;

	/**
	 * Creates a new variable.
	 *
	 * @param identifier Variable identifier.
	 */

	public Variable(final String identifier) {
		this(identifier, VariableType.NUMBER);
	}

	/**
	 * Creates a new variable.
	 *
	 * @param identifier Variable identifier.
	 * @param type Variable type.
	 */

	public Variable(final String identifier, final VariableType type) {
		this(identifier, type, type.getDefaultValue());
	}

	/**
	 * Creates a new variable.
	 *
	 * @param identifier Variable identifier.
	 * @param type Variable type.
	 * @param value Variable value.
	 */

	public Variable(final String identifier, final VariableType type, final Object value) {
		this.identifier = identifier;
		this.type = type;
		this.value = value;
	}

	/**
	 * Returns the variable identifier.
	 *
	 * @return The variable identifier.
	 */

	public final String getIdentifier() {
		return identifier;
	}

	/**
	 * Returns the variable type.
	 *
	 * @return The variable type.
	 */

	public final VariableType getType() {
		return type;
	}

	/**
	 * Sets the variable type.
	 *
	 * @param type The type.
	 */

	public final void setType(final VariableType type) {
		this.type = type;
	}

	/**
	 * Returns the variable value.
	 *
	 * @return The variable value.
	 */

	public final Object getValue() {
		return value;
	}

	/**
	 * Sets the variable value.
	 *
	 * @param value The value.
	 */

	public final void setValue(final Object value) {
		this.value = value;
	}

}