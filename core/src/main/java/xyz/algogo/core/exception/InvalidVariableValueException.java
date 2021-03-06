package xyz.algogo.core.exception;

/**
 * Occurs when an invalid variable / function identifier is submitted.
 */

public class InvalidVariableValueException extends Exception {

	/**
	 * The identifier.
	 */

	private final String identifier;

	/**
	 * Creates a new invalid identifier exception.
	 *
	 * @param identifier The identifier.
	 */

	public InvalidVariableValueException(final String identifier) {
		this(identifier, "Invalid variable value for variable : \"" + identifier + "\".");
	}

	/**
	 * Creates a new invalid identifier exception.
	 *
	 * @param identifier The identifier.
	 * @param message The exception message.
	 */

	public InvalidVariableValueException(final String identifier, final String message) {
		super(message);
		this.identifier = identifier;
	}

	/**
	 * Returns the identifier.
	 *
	 * @return The identifier.
	 */

	public String getIdentifier() {
		return identifier;
	}

}