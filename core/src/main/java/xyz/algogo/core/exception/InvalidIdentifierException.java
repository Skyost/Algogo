package xyz.algogo.core.exception;

/**
 * Occurs when an invalid variable / function identifier is submitted.
 */

public class InvalidIdentifierException extends Exception {

	/**
	 * The identifier.
	 */

	private final String identifier;

	/**
	 * Creates a new invalid identifier exception.
	 *
	 * @param identifier The identifier.
	 */

	public InvalidIdentifierException(final String identifier) {
		this(identifier, null);
	}

	/**
	 * Creates a new invalid identifier exception.
	 *
	 * @param identifier The identifier.
	 * @param message The exception message.
	 */

	public InvalidIdentifierException(final String identifier, final String message) {
		super(message);
		this.identifier = identifier;
	}

	/**
	 * Returns the identifier.
	 *
	 * @return The identifier.
	 */

	public final String getIdentifier() {
		return identifier;
	}

}