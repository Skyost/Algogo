package xyz.algogo.core.exception;

import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.misc.ParseCancellationException;

/**
 * Occurs when an invalid parsing is submitted.
 */

public class ParseException extends ParseCancellationException {

	/**
	 * The symbol.
	 */

	private final Object symbol;

	/**
	 * The line.
	 */

	private final int line;

	/**
	 * The position.
	 */

	private final int position;

	/**
	 * The message.
	 */

	private final String message;

	/**
	 * Creates a new parse exception.
	 *
	 * @param message The message.
	 */

	public ParseException(final String message) {
		super(message);

		this.symbol = null;
		this.line = -1;
		this.position = -1;
		this.message = message;
	}

	/**
	 * Creates a new parse exception.
	 *
	 * @param symbol The symbol.
	 * @param line The line.
	 * @param position The position.
	 * @param message The message.
	 * @param cause The cause.
	 */

	public ParseException(final Object symbol, final int line, final int position, final String message, final RecognitionException cause) {
		super("Error : line = " + line + ", position : " + position + ", symbol : " + symbol + ", message : \"" + message + "\"", cause);

		this.symbol = symbol;
		this.line = line;
		this.position = position;
		this.message = message;
	}

	/**
	 * Returns the symbol.
	 *
	 * @return The symbol.
	 */

	public final Object getSymbol() {
		return symbol;
	}

	/**
	 * Returns the line.
	 *
	 * @return The line.
	 */

	public final int getLine() {
		return line;
	}

	/**
	 * Returns the position.
	 *
	 * @return The position.
	 */

	public final int getPosition() {
		return position;
	}

	/**
	 * Returns the error message.
	 *
	 * @return The error message.
	 */

	public final String getErrorMessage() {
		return message;
	}

}