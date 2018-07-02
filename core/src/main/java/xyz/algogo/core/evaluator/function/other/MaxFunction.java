package xyz.algogo.core.evaluator.function.other;

import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.NumberAtom;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.evaluator.function.Function;

import java.math.BigDecimal;

/**
 * Finds the greatest number between provided arguments.
 */

public class MaxFunction extends Function {

	/**
	 * Creates a max function.
	 */

	public MaxFunction() {
		super("MAX");
	}

	@Override
	public final Atom<BigDecimal> evaluate(final EvaluationContext context, final Atom... arguments) {
		Atom value = null;
		for(final Atom argument : arguments) {
			if(!NumberAtom.hasNumberType(argument)) {
				continue;
			}

			if(value == null || argument.compareTo(value) > 0) {
				value = argument;
			}
		}

		return value == null ? NumberAtom.ZERO : value;
	}

}
