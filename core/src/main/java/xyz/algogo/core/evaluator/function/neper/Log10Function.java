package xyz.algogo.core.evaluator.function.neper;

import java.math.BigDecimal;

import ch.obermuhlner.math.big.BigDecimalMath;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.NumberAtom;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.evaluator.function.Function;

/**
 * Represents the <a href="https://en.wikipedia.org/wiki/Common_logarithm">Decimal logarithm function</a>.
 */

public class Log10Function extends Function {

	/**
	 * Creates a new Decimal logarithm function.
	 */

	public Log10Function() {
		super("LOG10");
	}

	@Override
	public Atom evaluate(final EvaluationContext context, final Atom... arguments) {
		if(arguments.length == 0 || !NumberAtom.hasNumberType(arguments[0])) {
			return NumberAtom.ZERO;
		}

		return new NumberAtom(BigDecimalMath.log10((BigDecimal)arguments[0].getValue(), context.getMathContext()));
	}

}