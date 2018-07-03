package xyz.algogo.desktop.editor.component.tree;

import xyz.algogo.core.evaluator.variable.VariableType;
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
		addTranslations();
	}

	/**
	 * Adds translations.
	 */

	private void addTranslations() {
		this.putTranslation(VariablesBlock.class, (TranslationFunction<VariablesBlock>) statement -> toHTML(AlgorithmTreeRenderer.ROOT_BLOCK_STATEMENT_COLOR, "statement.root.variables"));
		this.putTranslation(BeginningBlock.class, (TranslationFunction<BeginningBlock>) statement -> toHTML(AlgorithmTreeRenderer.ROOT_BLOCK_STATEMENT_COLOR, "statement.root.beginning"));
		this.putTranslation(EndBlock.class, (TranslationFunction<EndBlock>) statement -> toHTML(AlgorithmTreeRenderer.ROOT_BLOCK_STATEMENT_COLOR, "statement.root.end"));

		this.putTranslation(CreateVariableStatement.class, (TranslationFunction<CreateVariableStatement>) statement -> toHTML(AlgorithmTreeRenderer.SIMPLE_STATEMENT_COLOR, "statement.simple.createVariableStatement", statement.getIdentifier(), language.getString(statement.getType() == VariableType.NUMBER ? "statement.simple.createVariableStatement.number" : "statement.simple.createVariableStatement.string")));
		this.putTranslation(AssignStatement.class, (TranslationFunction<AssignStatement>) statement -> toHTML(AlgorithmTreeRenderer.SIMPLE_STATEMENT_COLOR, "statement.simple.assignStatement", statement.getIdentifier(), Utils.escapeHTML(statement.getValue().toLanguage(this))));
		this.putTranslation(PromptStatement.class, (TranslationFunction<PromptStatement>) statement -> {
			String message = language.getString("statement.simple.promptStatement", statement.getIdentifier());
			if(statement.getMessage() != null) {
				message += " " + language.getString("statement.simple.optionalString", Utils.escapeHTML(statement.getMessage()));
			}

			return rawStringToHTML(AlgorithmTreeRenderer.SIMPLE_STATEMENT_COLOR, message);
		});
		this.putTranslation(PrintVariableStatement.class, (TranslationFunction<PrintVariableStatement>) statement -> {
			String message = language.getString("statement.simple.printVariableStatement", statement.getIdentifier());
			if(statement.getMessage() != null) {
				message += " " + language.getString("statement.simple.optionalString", Utils.escapeHTML(statement.getMessage()));
			}

			if(!statement.shouldLineBreak()) {
				message += " " + language.getString("statement.simple.noLineBreak");
			}

			return rawStringToHTML(AlgorithmTreeRenderer.SIMPLE_STATEMENT_COLOR, message);
		});
		this.putTranslation(PrintStatement.class, (TranslationFunction<PrintStatement>) statement -> {
			String content = language.getString("statement.simple.printStatement", Utils.escapeHTML(statement.getMessage()));
			if(!statement.shouldLineBreak()) {
				content += " " + language.getString("statement.simple.noLineBreak");
			}

			return rawStringToHTML(AlgorithmTreeRenderer.SIMPLE_STATEMENT_COLOR, content);
		});
		this.putTranslation(IfBlock.class, (TranslationFunction<IfBlock>) statement -> toHTML(AlgorithmTreeRenderer.BLOCK_STATEMENT_COLOR, "statement.block.ifBlock", Utils.escapeHTML(statement.getCondition().toLanguage(this))));
		this.putTranslation(ElseBlock.class, (TranslationFunction<ElseBlock>) statement -> toHTML(AlgorithmTreeRenderer.BLOCK_STATEMENT_COLOR, "statement.block.elseBlock"));
		this.putTranslation(ForLoop.class, (TranslationFunction<ForLoop>) statement -> toHTML(AlgorithmTreeRenderer.BLOCK_STATEMENT_COLOR, "statement.loop.forLoop", statement.getIdentifier(), Utils.escapeHTML(statement.getStart().toLanguage(this)), Utils.escapeHTML(statement.getEnd().toLanguage(this))));
		this.putTranslation(WhileLoop.class, (TranslationFunction<WhileLoop>) statement -> toHTML(AlgorithmTreeRenderer.BLOCK_STATEMENT_COLOR, "statement.loop.whileLoop", Utils.escapeHTML(statement.getCondition().toLanguage(this))));
		this.putTranslation(LineComment.class, (TranslationFunction<LineComment>) statement -> toHTML(AlgorithmTreeRenderer.COMMENT_COLOR, "statement.comment.lineComment", Utils.escapeHTML(statement.getContent())));
		this.putTranslation(BlockComment.class, (TranslationFunction<BlockComment>) statement -> toHTML(AlgorithmTreeRenderer.COMMENT_COLOR, "statement.comment.blockComment", Utils.escapeHTML(statement.getContent()).replace(System.lineSeparator(), "<br>")));
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