package xyz.algogo.core.evaluator.function.other;

import java.math.BigDecimal;
import java.util.Random;

import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.NumberAtom;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.evaluator.function.Function;

/**
 * Represents a random number generator.
 */

public class RandomFunction extends Function {

	/**
	 * Creates a new random number generator function.
	 */

	public RandomFunction() {
		super("RANDOM");
	}

	@Override
	public NumberAtom evaluate(final EvaluationContext context, final Atom... arguments) {
		if(arguments.length < 2 || !NumberAtom.hasNumberType(arguments[0]) || !NumberAtom.hasNumberType(arguments[1])) {
			return NumberAtom.ZERO;
		}

		final int min = ((BigDecimal)arguments[0].getValue()).intValueExact();
		final int max = ((BigDecimal)arguments[1].getValue()).intValueExact();

		return new NumberAtom(new BigDecimal(new Random().nextInt(max - min + 1) + min));
	}

}
