package xyz.algogo.core.evaluator.function.other;

import java.math.BigDecimal;
import java.math.RoundingMode;

import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.NumberAtom;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.evaluator.function.Function;

/**
 * Represents the <a href="https://en.wikipedia.org/wiki/Factorial">Ceiling function</a>.
 */

public class CeilingFunction extends Function {

	/**
	 * Creates a new Ceiling function instance.
	 */

	public CeilingFunction() {
		super("CEILING");
	}

	@Override
	public NumberAtom evaluate(final EvaluationContext context, final Atom... arguments) {
		if(arguments.length == 0 || !NumberAtom.hasNumberType(arguments[0])) {
			return NumberAtom.ZERO;
		}

		return new NumberAtom(((BigDecimal)arguments[0].getValue()).setScale(0, RoundingMode.CEILING));
	}

}