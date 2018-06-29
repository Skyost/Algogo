package xyz.algogo.core.evaluator.function.root;

import ch.obermuhlner.math.big.BigDecimalMath;
import xyz.algogo.core.evaluator.EvaluationContext;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.NumberAtom;
import xyz.algogo.core.evaluator.function.Function;

import java.math.BigDecimal;

/**
 * Represents the <a href="https://en.wikipedia.org/wiki/Nth_root">Root function</a>.
 */

public class RootFunction extends Function {

	/**
	 * Creates a new root function instance.
	 */

	public RootFunction() {
		super("ROOT");
	}

	@Override
	public final NumberAtom evaluate(final EvaluationContext context, final Atom... arguments) {
		if(arguments.length < 2 || !NumberAtom.hasNumberType(arguments[0]) || !NumberAtom.hasNumberType(arguments[1])) {
			return NumberAtom.ZERO;
		}

		return new NumberAtom(BigDecimalMath.root((BigDecimal)arguments[0].getValue(), (BigDecimal)arguments[1].getValue(), context.getMathContext()));
	}

}
