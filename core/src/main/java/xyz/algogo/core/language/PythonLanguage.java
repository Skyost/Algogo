package xyz.algogo.core.language;

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
 * Python language implementation.
 */

public class PythonLanguage extends DefaultLanguageImplementation {

	/**
	 * Creates a new Python language.
	 */

	public PythonLanguage() {
		super("Python File", "py");

		addTranslations();
	}

	@Override
	public final String getHeader() {
		return "";
	}

	@Override
	public final String getFooter() {
		return "";
	}

	/**
	 * Adds translations.
	 */

	private void addTranslations() {
		this.putTranslation(VariablesBlock.class, (TranslationFunction<VariablesBlock>) statement -> "");
		this.putTranslation(BeginningBlock.class, (TranslationFunction<BeginningBlock>) this::translateBlockChildren);
		this.putTranslation(EndBlock.class, (TranslationFunction<EndBlock>) statement -> "");

		this.putTranslation(CreateVariableStatement.class, (TranslationFunction<CreateVariableStatement>) statement -> "");
		this.putTranslation(AssignStatement.class, (TranslationFunction<AssignStatement>) statement -> statement.getIdentifier() + " = " + statement.getValue().toLanguage(this) + ";" + LINE_SEPARATOR);
		this.putTranslation(PrintStatement.class, (TranslationFunction<PrintStatement>) statement -> "print('" + statement.getMessage().replace("'", "\\'") + "');" + LINE_SEPARATOR);
		this.putTranslation(PrintVariableStatement.class, (TranslationFunction<PrintVariableStatement>) statement -> {
			String content = "print(" + statement.getIdentifier() + ");" + LINE_SEPARATOR;

			if(statement.getMessage() != null) {
				content = "print('" + statement.getMessage().replace("'", "\\'") + "');" + LINE_SEPARATOR + content;
			}

			return content;
		});
		this.putTranslation(PromptStatement.class, (TranslationFunction<PromptStatement>) statement -> {
			final String comment = "# " + statement.getIdentifier() + " = int(" + statement.getIdentifier() + "); # Uncomment if " + statement.getIdentifier() + " has a number type." + LINE_SEPARATOR;

			String content = statement.getIdentifier() + " = input('";
			content += statement.getMessage() == null ? "Enter the value of " + statement.getIdentifier() + " :" : statement.getMessage().replace("'", "\\\'");
			content += "');" + LINE_SEPARATOR;

			return content + comment;
		});
		this.putTranslation(IfBlock.class, (TranslationFunction<IfBlock>) statement -> {
			String content = translateBlockStatement("if " + statement.getCondition().toLanguage(this) + ":", statement) + LINE_SEPARATOR;
			if(statement.hasElseBlock()) {
				content += statement.getElseBlock().toLanguage(this);
			}
			return content;
		});
		this.putTranslation(ElseBlock.class, (TranslationFunction<ElseBlock>) statement -> translateBlockStatement("else:", statement) + LINE_SEPARATOR);
		this.putTranslation(WhileLoop.class, (TranslationFunction<WhileLoop>) statement -> translateBlockStatement("while " + statement.getCondition().toLanguage(this) + ":", statement) + LINE_SEPARATOR);
		this.putTranslation(LineComment.class, (TranslationFunction<LineComment>) statement -> "# " + statement.getContent() + LINE_SEPARATOR);
		this.putTranslation(BlockComment.class, (TranslationFunction<BlockComment>) statement -> "'''" + statement.getContent().replace("\t", "") +  "'''" + LINE_SEPARATOR);
	}

}