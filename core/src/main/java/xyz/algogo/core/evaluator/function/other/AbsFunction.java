package xyz.algogo.core.evaluator.function.other;

import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.NumberAtom;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.evaluator.function.Function;

import java.math.BigDecimal;

/**
 * Represents the <a href="https://en.wikipedia.org/wiki/Absolute_value">Absolute value function</a>.
 */

public class AbsFunction extends Function {

	/**
	 * Creates an absolute value function.
	 */

	public AbsFunction() {
		super("ABS");
	}

	@Override
	public final Atom<BigDecimal> evaluate(final EvaluationContext context, final Atom... arguments) {
		if(arguments.length == 0 || !NumberAtom.hasNumberType(arguments[0])) {
			return NumberAtom.ZERO;
		}

		return abs(arguments[0]);
	}

	/**
	 * Calculates the absolute value of a given number.
	 *
	 * @param atom The number atom.
	 *
	 * @return The absolute value atom.
	 */

	public static NumberAtom abs(final Atom<BigDecimal> atom) {
		return new NumberAtom(atom.compareTo(NumberAtom.ZERO) < 0 ? atom.getValue().negate() : atom.getValue());
	}

}
