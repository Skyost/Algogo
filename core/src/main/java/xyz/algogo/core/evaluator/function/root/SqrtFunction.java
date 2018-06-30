package xyz.algogo.core.evaluator.function.root;

import ch.obermuhlner.math.big.BigDecimalMath;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.NumberAtom;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.evaluator.function.Function;

import java.math.BigDecimal;

/**
 * Represents the <a href="https://en.wikipedia.org/wiki/Square_root">Square root function</a>.
 */

public class SqrtFunction extends Function {

	/**
	 * Creates a new square root function instance.
	 */

	public SqrtFunction() {
		super("SQRT");
	}

	@Override
	public final NumberAtom evaluate(final EvaluationContext context, final Atom... arguments) {
		if(arguments.length == 0 || !NumberAtom.hasNumberType(arguments[0])) {
			return NumberAtom.ZERO;
		}

		return new NumberAtom(BigDecimalMath.sqrt((BigDecimal)arguments[0].getValue(), context.getMathContext()));
	}

}
