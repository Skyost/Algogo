package xyz.algogo.core.evaluator.function.other;

import java.math.BigDecimal;

import ch.obermuhlner.math.big.BigDecimalMath;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.NumberAtom;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.evaluator.function.Function;

/**
 * Calculates <a href="https://en.wikipedia.org/wiki/Bernoulli_number">Bernoulli numbers</a>.
 */

public class BernoulliFunction extends Function {

	/**
	 * Creates a new Bernoulli function instance.
	 */

	public BernoulliFunction() {
		super("BERNOULLI");
	}

	@Override
	public NumberAtom evaluate(final EvaluationContext context, final Atom... arguments) {
		if(arguments.length == 0 || !NumberAtom.hasNumberType(arguments[0])) {
			return NumberAtom.ZERO;
		}

		return new NumberAtom(BigDecimalMath.bernoulli(((BigDecimal)arguments[0].getValue()).intValue(), context.getMathContext()));
	}

}
