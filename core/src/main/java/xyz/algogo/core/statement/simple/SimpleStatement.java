package xyz.algogo.core.statement.simple;

import xyz.algogo.core.statement.Statement;

import java.io.Serializable;

/**
 * Represents a simple one-line statement.
 */

public abstract class SimpleStatement extends Statement implements Serializable {

	/**
	 * Validates the current statement.
	 *
	 * @return No exception if the validation is a success.
	 */

	public abstract Exception validate();

	@Override
	public abstract SimpleStatement copy();

}