package xyz.algogo.core;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xyz.algogo.core.antlr.AlgogoLexer;
import xyz.algogo.core.antlr.AlgogoParser;
import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.evaluator.context.InputListener;
import xyz.algogo.core.evaluator.context.OutputListener;
import xyz.algogo.core.exception.ParseException;
import xyz.algogo.core.language.Language;
import xyz.algogo.core.language.Translatable;
import xyz.algogo.core.statement.block.root.AlgorithmRootBlock;
import xyz.algogo.core.statement.block.root.BeginningBlock;
import xyz.algogo.core.statement.block.root.EndBlock;
import xyz.algogo.core.statement.block.root.VariablesBlock;

/**
 * Represents an algorithm.
 */

public class Algorithm implements Serializable, Translatable {

	/**
	 * The algorithm title.
	 */

	private String title;

	/**
	 * The algorithm author.
	 */

	private String author;

	/**
	 * The root block.
	 */

	private AlgorithmRootBlock rootBlock = new AlgorithmRootBlock(new VariablesBlock(), new BeginningBlock(), new EndBlock());

	/**
	 * Creates a new algorithm.
	 */

	public Algorithm() {
		this(null, null);
	}

	/**
	 * Creates a new algorithm.
	 *
	 * @param title The algorithm title.
	 */

	public Algorithm(final String title) {
		this(title, null);
	}

	/**
	 * Creates a new algorithm.
	 *
	 * @param title The algorithm title.
	 * @param author The algorithm author.
	 */

	public Algorithm(final String title, final String author) {
		setTitle(title);
		setAuthor(author);
	}

	/**
	 * Returns the algorithm title.
	 *
	 * @return The algorithm title.
	 */

	public String getTitle() {
		return title;
	}

	/**
	 * Sets the algorithm title.
	 *
	 * @param title The algorithm title.
	 */

	public void setTitle(String title) {
		if(title == null) {
			title = "Untitled";
		}

		this.title = title;
	}

	/**
	 * Returns the algorithm author.
	 *
	 * @return The algorithm author.
	 */

	public String getAuthor() {
		return author;
	}

	/**
	 * Sets the algorithm author.
	 *
	 * @param author The algorithm author.
	 */

	public void setAuthor(String author) {
		if(author == null) {
			author = "Anonymous";
		}

		this.author = author;
	}

	/**
	 * Returns the algorithm root block.
	 *
	 * @return The algorithm root block;
	 */

	public AlgorithmRootBlock getRootBlock() {
		return rootBlock;
	}

	/**
	 * Returns the variables block.
	 *
	 * @return The variables block.
	 */

	public VariablesBlock getVariablesBlock() {
		return rootBlock.getVariablesBlock();
	}

	/**
	 * Returns the beginning block.
	 *
	 * @return The beginning block.
	 */

	public BeginningBlock getBeginningBlock() {
		return rootBlock.getBeginningBlock();
	}

	/**
	 * Evaluates the current algorithm.
	 *
	 * @param inputListener The input listener.
	 * @param outputListener The output listener.
	 *
	 * @return Nothing if the execution is a success.
	 */

	public Exception evaluate(final InputListener inputListener, final OutputListener outputListener) {
		return evaluate(new EvaluationContext(inputListener, outputListener));
	}

	/**
	 * Evaluates the current algorithm.
	 *
	 * @param context The evaluation context.
	 *
	 * @return Nothing if the execution is a success.
	 */

	public Exception evaluate(final EvaluationContext context) {
		return evaluate(new ExpressionEvaluator(), context);
	}

	/**
	 * Evaluates the current algorithm.
	 *
	 * @param evaluator The expression evaluator.
	 * @param context The evaluation context.
	 *
	 * @return Nothing if the execution is a success.
	 */

	public Exception evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		return rootBlock.evaluate(evaluator, context);
	}

	/**
	 * Translates the current algorithm to the specified language.
	 *
	 * @param language The language.
	 *
	 * @return The translated algorithm.
	 */

	public String toLanguage(final Language language) {
		return language.translate(this);
	}

	/**
	 * Parses an algorithm and returns its instance (if possible).
	 *
	 * @param content The algorithm string to parse.
	 *
	 * @return The parsed algorithm.
	 *
	 * @throws ParseException If any error occurs during the parsing.
	 */

	public static Algorithm parse(final String content) throws ParseException {
		final AlgorithmParserErrorListener errorListener = new AlgorithmParserErrorListener();

		final AlgogoLexer lexer = new AlgogoLexer(CharStreams.fromString(content));
		lexer.removeErrorListeners();
		lexer.addErrorListener(errorListener);

		final AlgogoParser parser = new AlgogoParser(new CommonTokenStream(lexer));
		parser.removeErrorListeners();
		parser.addErrorListener(errorListener);

		final Algorithm algorithm = new Algorithm();

		final AlgogoParser.ScriptContext context = parser.script();
		final AlgorithmParserVisitor visitor = new AlgorithmParserVisitor();

		algorithm.rootBlock = visitor.visitScript(context);

		final boolean variablesBlockCount = algorithm.rootBlock.listStatementsById(VariablesBlock.STATEMENT_ID).length == 1;
		final boolean beginningBlockCount = algorithm.rootBlock.listStatementsById(BeginningBlock.STATEMENT_ID).length == 1;

		if(!variablesBlockCount || !beginningBlockCount) {
			throw new ParseException("An algorithm must contain one and only one VARIABLES block and BEGINNING block.");
		}

		final AlgogoParser.CommentContext comment = context.header;
		if(comment != null) {
			final String credits = visitor.visitComment(comment).getContent();
			final Matcher groups = Pattern.compile("(.*) by (.*)").matcher(credits);
			if(groups.matches() && groups.groupCount() >= 2) {
				algorithm.setTitle(groups.group(1));
				algorithm.setAuthor(groups.group(2));
			}
			else {
				algorithm.rootBlock.insertStatement(visitor.visitComment(comment), 0);
			}
		}

		return algorithm;
	}

}