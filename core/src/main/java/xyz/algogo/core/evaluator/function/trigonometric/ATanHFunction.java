package xyz.algogo.core.evaluator.function.trigonometric;

import ch.obermuhlner.math.big.BigDecimalMath;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.NumberAtom;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.evaluator.function.Function;

import java.math.BigDecimal;

/**
 * Represents the <a href="https://en.wikipedia.org/wiki/Inverse_hyperbolic_functions#Inverse_hyperbolic_tangent">Inverse hyperbolic tangent function</a>.
 */

public class ATanHFunction extends Function {

	/**
	 * Creates a new inverse hyperbolic tangent function.
	 */

	public ATanHFunction() {
		super("ATANH");
	}

	@Override
	public final NumberAtom evaluate(final EvaluationContext context, final Atom... arguments) {
		if(arguments.length == 0 || !NumberAtom.hasNumberType(arguments[0])) {
			return NumberAtom.ZERO;
		}

		return new NumberAtom(BigDecimalMath.atanh((BigDecimal)arguments[0].getValue(), context.getMathContext()));
	}

}
