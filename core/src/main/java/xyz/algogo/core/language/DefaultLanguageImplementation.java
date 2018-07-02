package xyz.algogo.core.language;

import xyz.algogo.core.evaluator.atom.BooleanAtom;
import xyz.algogo.core.evaluator.atom.IdentifierAtom;
import xyz.algogo.core.evaluator.atom.NumberAtom;
import xyz.algogo.core.evaluator.atom.StringAtom;
import xyz.algogo.core.evaluator.expression.*;
import xyz.algogo.core.statement.Statement;
import xyz.algogo.core.statement.block.BlockStatement;
import xyz.algogo.core.statement.block.root.AlgorithmRootBlock;

import java.util.regex.Pattern;

/**
 * Default language implementation, with default expression and atom implemented.
 */

public abstract class DefaultLanguageImplementation extends Language {

	/**
	 * Line start pattern.
	 */

	private static final Pattern LINE_START = Pattern.compile("(?m)^");

	/**
	 * Line separator string.
	 */

	static final String LINE_SEPARATOR = System.getProperty("line.separator");

	/**
	 * Creates a default expression language.
	 *
	 * @param name The language name.
	 */
	
	public DefaultLanguageImplementation(final String name) {
		super(name);
	}

	/**
	 * Creates a default expression language.
	 *
	 * @param name The language name.
	 * @param extension The language extension.
	 */
	
	public DefaultLanguageImplementation(final String name, final String extension) {
		super(name, extension);
	}

	@Override
	public String translateAlgorithmRootBlock(final AlgorithmRootBlock statement) {
		return translateBlockChildren(statement);
	}

	@Override
	public String translateAdditiveExpression(final AdditiveExpression expression) {
		return translateLeftOpRightExpression(expression);
	}

	@Override
	public String translateMultiplicationExpression(final MultiplicationExpression expression) {
		return translateLeftOpRightExpression(expression);
	}

	@Override
	public String translatePowerExpression(final PowerExpression expression) {
		return expression.getBase().toLanguage(this) + "^" + expression.getExponent().toLanguage(this);
	}

	@Override
	public String translateRelationalExpression(final RelationalExpression expression) {
		return translateLeftOpRightExpression(expression);
	}

	@Override
	public String translateEqualityExpression(final EqualityExpression expression) {
		return translateLeftOpRightExpression(expression);
	}

	@Override
	public String translateAndExpression(final AndExpression expression) {
		return translateLeftOpRightExpression(expression);
	}

	@Override
	public String translateOrExpression(final OrExpression expression) {
		return translateLeftOpRightExpression(expression);
	}

	@Override
	public String translateNotExpression(final NotExpression expression) {
		return "!" + expression.toLanguage(this);
	}

	@Override
	public String translateParenthesisExpression(final ParenthesisExpression expression) {
		return "(" + expression.getExpression().toLanguage(this) + ")";
	}

	@Override
	public String translateAbsoluteValueExpression(final AbsoluteValueExpression expression) {
		return "abs(" + expression.getExpression().toLanguage(this) + ")";
	}

	@Override
	public String translateUnaryMinusExpression(final UnaryMinusExpression expression) {
		return "-" + expression.getExpression().toLanguage(this);
	}

	@Override
	public String translateFunctionExpression(final FunctionExpression expression) {
		final StringBuilder builder = new StringBuilder();
		for(final Expression argument : expression.getArguments()) {
			builder.append(argument.toLanguage(this) + ", ");
		}
		builder.setLength(builder.length() - 2);

		return expression.getIdentifier() + "(" + builder.toString() + ")";
	}

	@Override
	public String translateAtomExpression(final AtomExpression expression) {
		return expression.getAtom().toLanguage(this);
	}

	@Override
	public String translateNumberAtom(final NumberAtom atom) {
		return atom.getValue().toPlainString();
	}

	@Override
	public String translateStringAtom(final StringAtom atom) {
		return "\"" + atom.getValue().replace("\"", "\\\"") + "\"";
	}

	@Override
	public String translateIdentifierAtom(final IdentifierAtom atom) {
		return atom.getValue();
	}

	@Override
	public String translateBooleanAtom(final BooleanAtom atom) {
		return String.valueOf(atom.getBooleanValue());
	}

	/**
	 * Translates a LeftOpRightExpression.
	 *
	 * @param expression The LeftOpRightExpression.
	 *
	 * @return The translated expression.
	 */
	
	public String translateLeftOpRightExpression(final LeftOpRightExpression expression) {
		return expression.getLeft().toLanguage(this) + " " + expression.getOperator() + " " + expression.getRight().toLanguage(this);
	}

	/**
	 * Indent a string block.
	 *
	 * @param block The string block.
	 *
	 * @return The indented string block.
	 */

	protected String indentStringBlock(final String block) {
		return LINE_START.matcher(block).replaceAll("\t");
	}

	/**
	 * Translates a block statement children.
	 *
	 * @param blockStatement The block statement.
	 *
	 * @return Translated block statement children.
	 */

	protected String translateBlockChildren(final BlockStatement blockStatement) {
		final StringBuilder builder = new StringBuilder();
		for(final Statement statement : blockStatement.listStatements()) {
			builder.append(statement.toLanguage(this));
		}

		return builder.toString();
	}
	
}