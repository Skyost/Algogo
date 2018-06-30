package xyz.algogo.core.evaluator.context;

import java.math.MathContext;

/**
 * Represents an evaluation context.
 */

public class EvaluationContext {

	/**
	 * The current input listener.
	 */

	private InputListener inputListener;

	/**
	 * The current output listener.
	 */

	private OutputListener outputListener;

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
		this((source, arguments) -> "", (source, content) -> System.out.println(content));
	}

	/**
	 * Creates a new evaluation context.
	 *
	 * @param inputListener The input listener.
	 * @param outputListener The output listener.
	 */

	public EvaluationContext(final InputListener inputListener, final OutputListener outputListener) {
		this(inputListener, outputListener, MathContext.DECIMAL64);
	}

	/**
	 * Creates a new evaluation context.
	 *
	 * @param inputListener The input listener.
	 * @param outputListener The output listener.
	 * @param mathContext The math context.
	 */

	public EvaluationContext(final InputListener inputListener, final OutputListener outputListener, final MathContext mathContext) {
		this.inputListener = inputListener;
		this.outputListener = outputListener;
		this.mathContext = mathContext;
	}

	/**
	 * Returns the current input listener.
	 *
	 * @return The current input listener.
	 */

	public final InputListener getInputListener() {
		return inputListener;
	}

	/**
	 * Sets the input listener.
	 *
	 * @param inputListener The input listener.
	 */

	public final void setInputListener(final InputListener inputListener) {
		this.inputListener = inputListener;
	}

	/**
	 * Returns the current output listener.
	 *
	 * @return The current output listener.
	 */

	public final OutputListener getOutputListener() {
		return outputListener;
	}

	/**
	 * Sets the output listener.
	 *
	 * @param outputListener The output listener.
	 */

	public final void setOutputListener(final OutputListener outputListener) {
		this.outputListener = outputListener;
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