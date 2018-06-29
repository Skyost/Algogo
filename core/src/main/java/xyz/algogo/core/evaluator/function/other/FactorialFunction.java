package xyz.algogo.core.evaluator.function.other;

import ch.obermuhlner.math.big.BigDecimalMath;
import xyz.algogo.core.evaluator.EvaluationContext;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.NumberAtom;
import xyz.algogo.core.evaluator.function.Function;

import java.math.BigDecimal;

/**
 * Represents the <a href="https://en.wikipedia.org/wiki/Factorial">Factorial function</a>.
 */

public class FactorialFunction extends Function {

	/**
	 * Creates a new Factorial function instance.
	 */

	public FactorialFunction() {
		super("FACTORIAL");
	}

	@Override
	public final NumberAtom evaluate(final EvaluationContext context, final Atom... arguments) {
		if(arguments.length == 0 || !NumberAtom.hasNumberType(arguments[0])) {
			return NumberAtom.ZERO;
		}

		return new NumberAtom(BigDecimalMath.factorial(((BigDecimal)arguments[0].getValue()).intValue()));
	}

}
