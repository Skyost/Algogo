package xyz.algogo.core.evaluator.expression;

import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.context.EvaluationContext;

/**
 * Represents an atom expression.
 */

public class AtomExpression extends Expression {

	/**
	 * The atom.
	 */

	private Atom atom;

	/**
	 * Creates a new atom expression.
	 *
	 * @param atom The atom.
	 */

	public AtomExpression(final Atom atom) {
		this.atom = atom;
	}

	/**
	 * Returns the atom.
	 *
	 * @return The atom.
	 */

	public Atom getAtom() {
		return atom;
	}

	/**
	 * Sets the atom.
	 *
	 * @param atom The atom.
	 */

	public void setAtom(final Atom atom) {
		this.atom = atom;
	}

	@Override
	public Atom evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		return atom.evaluate(evaluator, context);
	}

	@Override
	public AtomExpression copy() {
		return new AtomExpression(atom.copy());
	}

}