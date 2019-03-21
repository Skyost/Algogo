package xyz.algogo.core.language;

import java.util.regex.Pattern;

import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.BooleanAtom;
import xyz.algogo.core.evaluator.atom.IdentifierAtom;
import xyz.algogo.core.evaluator.atom.NumberAtom;
import xyz.algogo.core.evaluator.atom.StringAtom;
import xyz.algogo.core.evaluator.expression.AbsoluteValueExpression;
import xyz.algogo.core.evaluator.expression.AdditiveExpression;
import xyz.algogo.core.evaluator.expression.AndExpression;
import xyz.algogo.core.evaluator.expression.AtomExpression;
import xyz.algogo.core.evaluator.expression.EqualityExpression;
import xyz.algogo.core.evaluator.expression.Expression;
import xyz.algogo.core.evaluator.expression.FunctionExpression;
import xyz.algogo.core.evaluator.expression.LeftOpRightExpression;
import xyz.algogo.core.evaluator.expression.MultiplicationExpression;
import xyz.algogo.core.evaluator.expression.NotExpression;
import xyz.algogo.core.evaluator.expression.OrExpression;
import xyz.algogo.core.evaluator.expression.ParenthesisExpression;
import xyz.algogo.core.evaluator.expression.PowerExpression;
import xyz.algogo.core.evaluator.expression.RelationalExpression;
import xyz.algogo.core.evaluator.expression.UnaryMinusExpression;
import xyz.algogo.core.statement.Statement;
import xyz.algogo.core.statement.block.BlockStatement;
import xyz.algogo.core.statement.block.root.AlgorithmRootBlock;

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
		addDefaultTranslations();
	}

	/**
	 * Creates a default expression language.
	 *
	 * @param name The language name.
	 * @param extension The language extension.
	 */
	
	public DefaultLanguageImplementation(final String name, final String extension) {
		super(name, extension);
		addDefaultTranslations();
	}

	/**
	 * Adds default translations.
	 */

	private void addDefaultTranslations() {
		this.putTranslation(AlgorithmRootBlock.class, (TranslationFunction<AlgorithmRootBlock>) this::translateBlockChildren);

		this.putTranslation(AdditiveExpression.class, (TranslationFunction<AdditiveExpression>) this::translateLeftOpRightExpression);
		this.putTranslation(MultiplicationExpression.class, (TranslationFunction<MultiplicationExpression>) this::translateLeftOpRightExpression);
		this.putTranslation(PowerExpression.class, (TranslationFunction<PowerExpression>) expression -> expression.getBase().toLanguage(this) + "^" + expression.getExponent().toLanguage(this));
		this.putTranslation(RelationalExpression.class, (TranslationFunction<RelationalExpression>) this::translateLeftOpRightExpression);
		this.putTranslation(EqualityExpression.class, (TranslationFunction<EqualityExpression>) this::translateLeftOpRightExpression);
		this.putTranslation(AndExpression.class, (TranslationFunction<AndExpression>) this::translateLeftOpRightExpression);
		this.putTranslation(OrExpression.class, (TranslationFunction<OrExpression>) this::translateLeftOpRightExpression);
		this.putTranslation(NotExpression.class, (TranslationFunction<NotExpression>) expression -> "!" + expression.getExpression().toLanguage(this));
		this.putTranslation(ParenthesisExpression.class, (TranslationFunction<ParenthesisExpression>) expression -> "(" + expression.getExpression().toLanguage(this) + ")");
		this.putTranslation(AbsoluteValueExpression.class, (TranslationFunction<AbsoluteValueExpression>) expression -> "abs(" + expression.getExpression().toLanguage(this) + ")");
		this.putTranslation(UnaryMinusExpression.class, (TranslationFunction<UnaryMinusExpression>) expression -> "-" + expression.getExpression().toLanguage(this));
		this.putTranslation(FunctionExpression.class, (TranslationFunction<FunctionExpression>) expression -> {
			final StringBuilder builder = new StringBuilder();
			for(final Expression argument : expression.getArguments()) {
				builder.append(argument.toLanguage(this)).append(", ");
			}

			if(builder.length() >= 2) {
				builder.setLength(builder.length() - 2);
			}

			return expression.getIdentifier() + "(" + builder.toString() + ")";
		});
		this.putTranslation(AtomExpression.class, (TranslationFunction<AtomExpression>) expression -> expression.getAtom().toLanguage(this));

		this.putTranslation(NumberAtom.class, (TranslationFunction<NumberAtom>) atom -> atom.getValue().toPlainString());
		this.putTranslation(StringAtom.class, (TranslationFunction<StringAtom>) atom -> "\"" + atom.getValue().replace("\"", "\\\"") + "\"");
		this.putTranslation(IdentifierAtom.class, (TranslationFunction<IdentifierAtom>) Atom::getValue);
		this.putTranslation(BooleanAtom.class, (TranslationFunction<BooleanAtom>) atom -> String.valueOf(atom.getBooleanValue()));
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

	/**
	 * Translates a block statement.
	 *
	 * @param blockTitle The block title.
	 * @param blockStatement The block statement.
	 *
	 * @return The translated block statement.
	 */

	protected String translateBlockStatement(final String blockTitle, final BlockStatement blockStatement) {
		String content = blockTitle + LINE_SEPARATOR;
		if(blockStatement.getStatementCount() > 0) {
			content += indentStringBlock(translateBlockChildren(blockStatement));
		}

		return content;
	}
	
}