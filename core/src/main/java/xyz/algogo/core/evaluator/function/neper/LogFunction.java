package xyz.algogo.core.evaluator.function.neper;

import java.math.BigDecimal;

import ch.obermuhlner.math.big.BigDecimalMath;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.NumberAtom;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.evaluator.function.Function;

/**
 * Represents the <a href="https://en.wikipedia.org/wiki/Natural_logarithm">Natural logarithm function</a>.
 */

public class LogFunction extends Function {

	/**
	 * Creates a new Natural logarithm function instance.
	 */

	public LogFunction() {
		super("LOG");
	}

	@Override
	public NumberAtom evaluate(final EvaluationContext context, final Atom... arguments) {
		if(arguments.length == 0 || !NumberAtom.hasNumberType(arguments[0])) {
			return NumberAtom.ZERO;
		}

		return new NumberAtom(BigDecimalMath.log((BigDecimal)arguments[0].getValue(), context.getMathContext()));
	}

}