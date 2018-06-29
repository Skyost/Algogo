package xyz.algogo.core.evaluator.expression;

/**
 * Represents a &lt;left expression&gt; &lt;operator&gt; &lt;right expression&gt; expression.
 */

public abstract class LeftOpRightExpression extends Expression {

	/**
	 * The left side expression.
	 */

	private Expression left;

	/**
	 * The operator.
	 */

	private String operator;

	/**
	 * The right side expression.
	 */

	private Expression right;

	/**
	 * Creates a new &lt;left expression&gt; &lt;operator&gt; &lt;right expression&gt; expression.
	 *
	 * @param left The left expression.
	 * @param operator The operator.
	 * @param right The right expression.
	 */

	protected LeftOpRightExpression(final Expression left, final String operator, final Expression right) {
		this.left = left;
		this.operator = operator;
		this.right = right;
	}

	/**
	 * Returns the left side expression.
	 *
	 * @return The left side expression.
	 */

	public final Expression getLeft() {
		return left;
	}

	/**
	 * Sets the left side expression.
	 *
	 * @param left The left side expression.
	 */

	public final void setLeft(final Expression left) {
		this.left = left;
	}

	/**
	 * Returns the operator.
	 *
	 * @return The operator.
	 */

	public final String getOperator() {
		return operator;
	}

	/**
	 * Sets the operator.
	 *
	 * @param operator The operator.
	 */

	public final void setOperator(final String operator) {
		this.operator = operator;
	}

	/**
	 * Returns the right side expression.
	 *
	 * @return The right side expression.
	 */

	public final Expression getRight() {
		return right;
	}

	/**
	 * Sets the right side expression.
	 *
	 * @param right The right side expression.
	 */

	public final void setRight(final Expression right) {
		this.right = right;
	}

	@Override
	public abstract LeftOpRightExpression copy();

}