package xyz.algogo.core.evaluator.function.neper;

import java.math.BigDecimal;

import ch.obermuhlner.math.big.BigDecimalMath;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.NumberAtom;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.evaluator.function.Function;

/**
 * Represents the <a href="https://en.wikipedia.org/wiki/Common_logarithm">Binary logarithm function</a>.
 */

public class Log2Function extends Function {

	/**
	 * Creates a new Binary logarithm function.
	 */

	public Log2Function() {
		super("LOG2");
	}

	@Override
	public Atom evaluate(final EvaluationContext context, final Atom... arguments) {
		if(arguments.length == 0 || !NumberAtom.hasNumberType(arguments[0])) {
			return NumberAtom.ZERO;
		}

		return new NumberAtom(BigDecimalMath.log2((BigDecimal)arguments[0].getValue(), context.getMathContext()));
	}

}