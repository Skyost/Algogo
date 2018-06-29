package xyz.algogo.core.language;

import xyz.algogo.core.Algorithm;
import xyz.algogo.core.statement.block.BlockStatement;
import xyz.algogo.core.statement.block.conditional.ElseBlock;
import xyz.algogo.core.statement.block.conditional.IfBlock;
import xyz.algogo.core.statement.block.loop.ForLoop;
import xyz.algogo.core.statement.block.loop.WhileLoop;
import xyz.algogo.core.statement.block.root.BeginningBlock;
import xyz.algogo.core.statement.block.root.EndBlock;
import xyz.algogo.core.statement.block.root.VariablesBlock;
import xyz.algogo.core.statement.simple.comment.BlockComment;
import xyz.algogo.core.statement.simple.comment.LineComment;
import xyz.algogo.core.statement.simple.io.PrintStatement;
import xyz.algogo.core.statement.simple.io.PrintVariableStatement;
import xyz.algogo.core.statement.simple.io.PromptStatement;
import xyz.algogo.core.statement.simple.variable.AssignStatement;
import xyz.algogo.core.statement.simple.variable.CreateVariableStatement;

/**
 * Algogo v1.x language implementation.
 */

public class AlgogoLanguage extends DefaultLanguageImplementation {

	/**
	 * Whether credits should be added at the end of the file.
	 */

	private boolean addCredits;

	/**
	 * Creates a new Algogo language.
	 */

	public AlgogoLanguage() {
		this(true);
	}

	/**
	 * Creates a new Algogo language.
	 *
	 * @param addCredits Whether credits should be added at the end of the file.
	 */

	public AlgogoLanguage(final boolean addCredits) {
		super("Algogo v1.x Algorithm", "agg2");

		this.addCredits = addCredits;
	}

	@Override
	public final String translateAlgorithm(final Algorithm algorithm) {
		String content = super.translateAlgorithm(algorithm);
		if(addCredits) {
			content = new LineComment(algorithm.getTitle() + " by " + algorithm.getAuthor()).toLanguage(this) + content;
		}

		return content;
	}

	@Override
	public final String translateVariablesBlock(final VariablesBlock statement) {
		return translateBlockStatement("VARIABLES", statement);
	}

	@Override
	public final String translateBeginningBlock(final BeginningBlock statement) {
		return translateBlockStatement("BEGINNING", statement);
	}

	@Override
	public final String translateEndBlock(final EndBlock statement) {
		return "END" + LINE_SEPARATOR;
	}

	@Override
	public final String translateCreateVariableStatement(final CreateVariableStatement statement) {
		return statement.getIdentifier() + " : " + statement.getType().name() + LINE_SEPARATOR;
	}

	@Override
	public final String translateAssignStatement(final AssignStatement statement) {
		return statement.getIdentifier() + " <- " + statement.getValue().toLanguage(this) + LINE_SEPARATOR;
	}

	@Override
	public final String translatePrintStatement(final PrintStatement statement) {
		String content = "PRINT \"" + statement.getMessage().replace("\"", "\\\"") + "\"";
		if(!statement.shouldLineBreak()) {
			content += " NLB";
		}

		return content + LINE_SEPARATOR;
	}

	@Override
	public final String translatePrintVariableStatement(final PrintVariableStatement statement) {
		String content = "PRINT_VARIABLE " + statement.getIdentifier() + (statement.getMessage() == null ? "" : " \"" + statement.getMessage().replace("\"", "\\\"") + "\"");
		if(!statement.shouldLineBreak()) {
			content += " NLB";
		}

		return content + LINE_SEPARATOR;
	}

	@Override
	public final String translatePromptStatement(final PromptStatement statement) {
		return "PROMPT " + statement.getIdentifier() + (statement.getMessage() == null ? "" : " \"" + statement.getMessage() + "\"") + LINE_SEPARATOR;
	}

	@Override
	public final String translateIfBlock(final IfBlock statement) {
		String content = translateBlockStatement("IF " + statement.getCondition().toLanguage(this) + " THEN", statement);
		if(statement.hasElseBlock()) {
			content += statement.getElseBlock().toLanguage(this);
		}
		return content;
	}

	@Override
	public final String translateElseBlock(final ElseBlock statement) {
		return translateBlockStatement("ELSE", statement);
	}

	@Override
	public final String translateForLoop(final ForLoop statement) {
		return translateBlockStatement("FOR " + statement.getIdentifier() + " FROM " + statement.getStart().toLanguage(this) + " TO " + statement.getEnd().toLanguage(this) + " DO", statement);
	}

	@Override
	public final String translateWhileLoop(final WhileLoop statement) {
		return translateBlockStatement("WHILE " + statement.getCondition().toLanguage(this) + " DO", statement);
	}

	@Override
	public final String translateLineComment(final LineComment statement) {
		return "// " + statement.getContent() + LINE_SEPARATOR;
	}

	@Override
	public final String translateBlockComment(final BlockComment statement) {
		return "/*" + statement.getContent().replace("\t", "") +  "*/" + LINE_SEPARATOR;
	}

	/**
	 * Checks whether credits should be added at the end of the file.
	 *
	 * @return Whether credits should be added at the end of the file.
	 */

	public final boolean shouldAddCredits() {
		return addCredits;
	}

	/**
	 * Sets whether credits should be added at the end of the file.
	 *
	 * @param addCredits Whether credits should be added at the end of the file.
	 */

	public final void setAddCredits(final boolean addCredits) {
		this.addCredits = addCredits;
	}

	/**
	 * Translates a block statement.
	 *
	 * @param blockTitle The block title.
	 * @param blockStatement The block statement.
	 *
	 * @return The translated block statement.
	 */

	private String translateBlockStatement(final String blockTitle, final BlockStatement blockStatement) {
		String content = blockTitle + LINE_SEPARATOR;
		if(blockStatement.getStatementCount() > 0) {
			content += indentStringBlock(translateBlockChildren(blockStatement));
		}

		return content;
	}

}