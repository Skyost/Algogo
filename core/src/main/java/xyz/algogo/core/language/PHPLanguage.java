package xyz.algogo.core.language;

import xyz.algogo.core.Algorithm;
import xyz.algogo.core.evaluator.atom.IdentifierAtom;
import xyz.algogo.core.statement.block.conditional.ElseBlock;
import xyz.algogo.core.statement.block.conditional.IfBlock;
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
 * PHP language implementation.
 */

public class PHPLanguage extends DefaultLanguageImplementation {

	/**
	 * Current prompt identifier.
	 */

	private int promptId = 0;

	/**
	 * Creates a new PHP language.
	 */

	public PHPLanguage() {
		super("PHP Script", "php");

		addTranslations();
	}

	@Override
	public final String getHeader() {
		return "<?php" + LINE_SEPARATOR + LINE_SEPARATOR;
	}

	@Override
	public final String getFooter() {
		return LINE_SEPARATOR + "?>";
	}

	/**
	 * Adds translations.
	 */

	private void addTranslations() {
		final TranslationFunction superFunction = this.getTranslationFunction(Algorithm.class);
		this.putTranslation(Algorithm.class, (TranslationFunction<Algorithm>) algorithm -> {
			promptId = 0;
			return superFunction.translate(algorithm);
		});

		this.putTranslation(VariablesBlock.class, (TranslationFunction<VariablesBlock>) statement -> "");
		this.putTranslation(BeginningBlock.class, (TranslationFunction<BeginningBlock>) statement -> translateBlockStatement("", statement));
		this.putTranslation(EndBlock.class, (TranslationFunction<EndBlock>) statement -> {
			promptId = 0;
			return "";
		});

		this.putTranslation(CreateVariableStatement.class, (TranslationFunction<CreateVariableStatement>) statement -> "");
		this.putTranslation(AssignStatement.class, (TranslationFunction<AssignStatement>) statement -> "$" + statement.getIdentifier() + " = " + statement.getValue().toLanguage(this) + ";" + LINE_SEPARATOR);
		this.putTranslation(PrintStatement.class, (TranslationFunction<PrintStatement>) statement -> "echo('" + statement.getMessage().replace("'", "\\'") + "');" + LINE_SEPARATOR);
		this.putTranslation(PrintVariableStatement.class, (TranslationFunction<PrintVariableStatement>) statement -> {
			String content = "echo $" + statement.getIdentifier() + ";" + LINE_SEPARATOR;
			if(statement.getMessage() != null) {
				content = "echo '" + statement.getMessage().replace("'", "\\'") + "'" + LINE_SEPARATOR + content;
			}

			return content;
		});
		this.putTranslation(PromptStatement.class, (TranslationFunction<PromptStatement>) statement -> {
			String content = "if(!isset($_GET['" + statement.getIdentifier() + promptId + "'])) { die('" + statement.getIdentifier() + promptId + " not set'); }" + LINE_SEPARATOR;
			content += "$" + statement.getIdentifier() + " = $_GET['" + statement.getIdentifier() + promptId++ + "'];" + LINE_SEPARATOR;

			return content;
		});
		this.putTranslation(IfBlock.class, (TranslationFunction<IfBlock>) statement -> {
			String content = translateBlockStatement("if(" + statement.getCondition().toLanguage(this) + ") {", statement) + "}" + LINE_SEPARATOR;
			if(statement.hasElseBlock()) {
				content += statement.getElseBlock().toLanguage(this);
			}
			return content;
		});
		this.putTranslation(ElseBlock.class, (TranslationFunction<ElseBlock>) statement -> translateBlockStatement("else {", statement) + "}" + LINE_SEPARATOR);
		this.putTranslation(WhileLoop.class, (TranslationFunction<WhileLoop>) statement -> translateBlockStatement("while(" + statement.getCondition().toLanguage(this) + ") {", statement) + "}" + LINE_SEPARATOR);
		this.putTranslation(LineComment.class, (TranslationFunction<LineComment>) statement -> "// " + statement.getContent() + LINE_SEPARATOR);
		this.putTranslation(BlockComment.class, (TranslationFunction<BlockComment>) statement -> "/*" + statement.getContent().replace("\t", "") + "*/" + LINE_SEPARATOR);

		this.putTranslation(IdentifierAtom.class, (TranslationFunction<IdentifierAtom>) atom -> "$" + atom.getValue());
	}

}