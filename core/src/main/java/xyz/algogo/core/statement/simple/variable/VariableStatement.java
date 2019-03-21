package xyz.algogo.core.statement.simple.variable;

import xyz.algogo.core.statement.simple.SimpleStatement;

/**
 * Represents a statement that needs a variable identifier.
 */

public abstract class VariableStatement extends SimpleStatement {

	/**
	 * The variable identifier.
	 */

	private String identifier;

	/**
	 * Creates a new variable statement.
	 *
	 * @param identifier The variable identifier.
	 */

	public VariableStatement(final String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Returns the variable identifier.
	 *
	 * @return The variable identifier.
	 */

	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Sets the variable identifier.
	 *
	 * @param identifier The identifier.
	 */

	public void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}

	@Override
	public abstract VariableStatement copy();

}