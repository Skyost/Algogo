package xyz.algogo.core.evaluator;

import java.math.MathContext;

/**
 * Represents an evaluation context.
 */

public class EvaluationContext {

	/**
	 * Whether the current evaluation should be stopped.
	 */

	private boolean isStopped = false;

	/**
	 * The current math context.
	 */

	private MathContext mathContext;

	/**
	 * Creates a new evaluation context.
	 */

	public EvaluationContext() {
		this(MathContext.DECIMAL64);
	}

	/**
	 * Creates a new evaluation context.
	 *
	 * @param mathContext The math context.
	 */

	public EvaluationContext(final MathContext mathContext) {
		this.mathContext = mathContext;
	}

	/**
	 * Gets whether the current evaluation should be stopped.
	 *
	 * @return Whether the current evaluation should be stopped.
	 */

	public final synchronized boolean isStopped() {
		return isStopped;
	}

	/**
	 * Sets whether the current evaluation should be stopped.
	 *
	 * @param isStopped Whether the current evaluation should be stopped.
	 */

	public final synchronized void setStopped(final boolean isStopped) {
		this.isStopped = isStopped;
	}

	/**
	 * Gets the math context of the current evaluation.
	 *
	 * @return The math context of the current evaluation.
	 */

	public final MathContext getMathContext() {
		return mathContext;
	}

	/**
	 * Sets the math context of the current evaluation.
	 *
	 * @param mathContext The math context.
	 */

	public final void setMathContext(final MathContext mathContext) {
		this.mathContext = mathContext;
	}

}