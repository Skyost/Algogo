package xyz.algogo.core.evaluator.function.trigonometric;

import ch.obermuhlner.math.big.BigDecimalMath;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.NumberAtom;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.evaluator.function.Function;

import java.math.BigDecimal;

/**
 * Represents the <a href="https://en.wikipedia.org/wiki/Hyperbolic_function">Hyperbolic sine function</a>.
 */

public class SinHFunction extends Function {

	/**
	 * Creates a new hyperbolic sine function.
	 */

	public SinHFunction() {
		super("SINH");
	}

	@Override
	public final NumberAtom evaluate(final EvaluationContext context, final Atom... arguments) {
		if(arguments.length == 0 || !NumberAtom.hasNumberType(arguments[0])) {
			return NumberAtom.ZERO;
		}

		return new NumberAtom(BigDecimalMath.sinh((BigDecimal)arguments[0].getValue(), context.getMathContext()));
	}

}
