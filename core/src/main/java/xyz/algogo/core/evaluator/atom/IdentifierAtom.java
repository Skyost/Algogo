package xyz.algogo.core.evaluator.atom;

import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.evaluator.variable.Variable;
import xyz.algogo.core.evaluator.variable.VariableType;

import java.math.BigDecimal;

/**
 * Represents an identifier atom.
 */

public class IdentifierAtom extends StringAtom {

	/**
	 * Creates a new identifier atom.
	 *
	 * @param value The variable identifier.
	 */

	public IdentifierAtom(final String value) {
		super(value);
	}

	@Override
	public Atom evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		final Variable variable = evaluator.getVariable(this.getValue());
		if(variable == null) {
			return null;
		}

		if(variable.getType() == VariableType.NUMBER) {
			return new NumberAtom(((BigDecimal)variable.getValue()).round(context.getMathContext()));
		}

		return new StringAtom((String)variable.getValue());
	}

	@Override
	public final IdentifierAtom copy() {
		return new IdentifierAtom(this.getValue());
	}

}