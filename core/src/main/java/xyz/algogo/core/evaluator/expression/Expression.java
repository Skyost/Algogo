package xyz.algogo.core.evaluator.expression;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import xyz.algogo.core.AlgorithmParserErrorListener;
import xyz.algogo.core.AlgorithmParserVisitor;
import xyz.algogo.core.antlr.AlgogoLexer;
import xyz.algogo.core.antlr.AlgogoParser;
import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.exception.ParseException;
import xyz.algogo.core.language.Translatable;

import java.io.Serializable;

/**
 * Represents an expression.
 */

public abstract class Expression implements Serializable, Translatable {

	/**
	 * Evaluates the current expression value.
	 *
	 * @param evaluator The expression evaluator.
	 * @param context The evaluation context.
	 *
	 * @return The evaluated value of this expression.
	 */

	public abstract Atom evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context);

	/**
	 * Copies this expression instance.
	 *
	 * @return A copy of this expression instance.
	 */

	public abstract Expression copy();

	/**
	 * Parses an expression and returns its instance (if possible).
	 *
	 * @param content The expression string to parse.
	 *
	 * @return The parsed expression.
	 *
	 * @throws ParseException If any error occurs during the parsing.
	 */

	public static Expression parse(final String content) throws ParseException {
		final AlgorithmParserErrorListener errorListener = new AlgorithmParserErrorListener();

		final AlgogoLexer lexer = new AlgogoLexer(CharStreams.fromString(content));
		lexer.removeErrorListeners();
		lexer.addErrorListener(errorListener);

		final AlgogoParser parser = new AlgogoParser(new CommonTokenStream(lexer));
		parser.removeErrorListeners();
		parser.addErrorListener(errorListener);

		final AlgorithmParserVisitor visitor = new AlgorithmParserVisitor();
		return visitor.visitExpression(parser.expression());
	}

}