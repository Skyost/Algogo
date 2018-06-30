package xyz.algogo.desktop.editor.component.tree;

import xyz.algogo.core.evaluator.variable.VariableType;
import xyz.algogo.core.language.AlgogoLanguage;
import xyz.algogo.core.language.DefaultLanguageImplementation;
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
import xyz.algogo.desktop.AppLanguage;
import xyz.algogo.desktop.utils.Utils;

import java.awt.*;

/**
 * Useful class that allows to localize a statement according to the localization files.
 */

public class AlgorithmLocalization extends DefaultLanguageImplementation {

	/**
	 * Application language instance.
	 */

	private final AppLanguage language;

	/**
	 * Creates a new algorithm localization.
	 */

	public AlgorithmLocalization(final AppLanguage language) {
		super(language.getString("menu.file.export.html"), "html");

		this.language = language;
	}

	@Override
	public final String translateVariablesBlock(final VariablesBlock statement) {
		return toHTML(AlgorithmTreeRenderer.ROOT_BLOCK_STATEMENT_COLOR, "statement.root.variables");
	}

	@Override
	public final String translateBeginningBlock(final BeginningBlock statement) {
		return toHTML(AlgorithmTreeRenderer.ROOT_BLOCK_STATEMENT_COLOR, "statement.root.beginning");
	}

	@Override
	public final String translateEndBlock(final EndBlock statement) {
		return toHTML(AlgorithmTreeRenderer.ROOT_BLOCK_STATEMENT_COLOR, "statement.root.end");
	}

	@Override
	public final String translateCreateVariableStatement(final CreateVariableStatement statement) {
		return toHTML(AlgorithmTreeRenderer.SIMPLE_STATEMENT_COLOR, "statement.simple.createVariableStatement", statement.getIdentifier(), language.getString(statement.getType() == VariableType.NUMBER ? "statement.simple.createVariableStatement.number" : "statement.simple.createVariableStatement.string"));
	}

	@Override
	public final String translateAssignStatement(final AssignStatement statement) {
		return toHTML(AlgorithmTreeRenderer.SIMPLE_STATEMENT_COLOR, "statement.simple.assignStatement", statement.getIdentifier(), Utils.escapeHTML(statement.getValue().toLanguage(new AlgogoLanguage())));
	}

	@Override
	public final String translatePromptStatement(final PromptStatement statement) {
		String message = language.getString("statement.simple.promptStatement", statement.getIdentifier());
		if(statement.getMessage() != null) {
			message += " " + language.getString("statement.simple.optionalString", Utils.escapeHTML(statement.getMessage()));
		}

		return rawStringToHTML(AlgorithmTreeRenderer.SIMPLE_STATEMENT_COLOR, message);
	}

	@Override
	public final String translatePrintVariableStatement(final PrintVariableStatement statement) {
		String message = language.getString("statement.simple.printVariableStatement", statement.getIdentifier());
		if(statement.getMessage() != null) {
			message += " " + language.getString("statement.simple.optionalString", Utils.escapeHTML(statement.getMessage()));
		}

		if(!statement.shouldLineBreak()) {
			message += " " + language.getString("statement.simple.noLineBreak");
		}

		return rawStringToHTML(AlgorithmTreeRenderer.SIMPLE_STATEMENT_COLOR, message);
	}

	@Override
	public final String translatePrintStatement(final PrintStatement statement) {
		String content = language.getString("statement.simple.printStatement", Utils.escapeHTML(statement.getMessage()));
		if(!statement.shouldLineBreak()) {
			content += " " + language.getString("statement.simple.noLineBreak");
		}

		return rawStringToHTML(AlgorithmTreeRenderer.SIMPLE_STATEMENT_COLOR, content);
	}

	@Override
	public final String translateIfBlock(final IfBlock statement) {
		return toHTML(AlgorithmTreeRenderer.BLOCK_STATEMENT_COLOR, "statement.block.ifBlock", Utils.escapeHTML(statement.getCondition().toLanguage(new AlgogoLanguage())));
	}

	@Override
	public final String translateElseBlock(final ElseBlock statement) {
		return toHTML(AlgorithmTreeRenderer.BLOCK_STATEMENT_COLOR, "statement.block.elseBlock");
	}

	@Override
	public final String translateForLoop(final ForLoop statement) {
		return toHTML(AlgorithmTreeRenderer.BLOCK_STATEMENT_COLOR, "statement.loop.forLoop", statement.getIdentifier(), Utils.escapeHTML(statement.getStart().toLanguage(this)), Utils.escapeHTML(statement.getEnd().toLanguage(this)));
	}

	@Override
	public final String translateWhileLoop(final WhileLoop statement) {
		return toHTML(AlgorithmTreeRenderer.BLOCK_STATEMENT_COLOR, "statement.loop.whileLoop", Utils.escapeHTML(statement.getCondition().toLanguage(new AlgogoLanguage())));
	}

	@Override
	public final String translateLineComment(final LineComment statement) {
		return toHTML(AlgorithmTreeRenderer.COMMENT_COLOR, "statement.comment.lineComment", Utils.escapeHTML(statement.getContent()));
	}

	@Override
	public final String translateBlockComment(final BlockComment statement) {
		return toHTML(AlgorithmTreeRenderer.COMMENT_COLOR, "statement.comment.blockComment", Utils.escapeHTML(statement.getContent()).replace(System.lineSeparator(), "<br>"));
	}

	/**
	 * Creates a HTML content from a language key.
	 *
	 * @param color Statement color.
	 * @param key Language key.
	 * @param arguments Formatting arguments.
	 *
	 * @return The formatted HTML content.
	 */

	private String toHTML(final Color color, final String key, final Object... arguments) {
		return rawStringToHTML(color, language.getString(key, arguments));
	}

	/**
	 * Formats the statement content according to the provided content.
	 *
	 * @param color Statement color.
	 * @param content The content.
	 *
	 * @return The formatted HTML content.
	 */

	private String rawStringToHTML(final Color color, final String content) {
		return language.getString("statement.content", String.format("#%06x", color.getRGB() & 0x00FFFFFF), content);
	}

}