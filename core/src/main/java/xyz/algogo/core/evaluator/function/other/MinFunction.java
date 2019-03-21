package xyz.algogo.core.evaluator.function.other;

import java.math.BigDecimal;

import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.NumberAtom;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.evaluator.function.Function;

/**
 * Finds the smallest number between provided arguments.
 */

public class MinFunction extends Function {

	/**
	 * Creates a min function.
	 */

	public MinFunction() {
		super("MIN");
	}

	@Override
	public Atom<BigDecimal> evaluate(final EvaluationContext context, final Atom... arguments) {
		Atom value = null;
		for(final Atom argument : arguments) {
			if(!NumberAtom.hasNumberType(argument)) {
				continue;
			}

			if(value == null || argument.compareTo(value) < 0) {
				value = argument;
			}
		}

		return value == null ? NumberAtom.ZERO : value;
	}

}
