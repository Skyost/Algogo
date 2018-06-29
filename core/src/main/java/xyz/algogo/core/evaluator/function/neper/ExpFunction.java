package xyz.algogo.core.evaluator.function.neper;

import ch.obermuhlner.math.big.BigDecimalMath;
import xyz.algogo.core.evaluator.EvaluationContext;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.NumberAtom;
import xyz.algogo.core.evaluator.function.Function;

import java.math.BigDecimal;

/**
 * Represents the <a href="https://en.wikipedia.org/wiki/Exponential_function">Exponential function</a>.
 */

public class ExpFunction extends Function {

	/**
	 * Creates a new Exponential function instance.
	 */

	public ExpFunction() {
		super("EXP");
	}

	@Override
	public final NumberAtom evaluate(final EvaluationContext context, final Atom... arguments) {
		if(arguments.length == 0 || !NumberAtom.hasNumberType(arguments[0])) {
			return NumberAtom.ZERO;
		}

		return new NumberAtom(BigDecimalMath.exp((BigDecimal)arguments[0].getValue(), context.getMathContext()));
	}

}