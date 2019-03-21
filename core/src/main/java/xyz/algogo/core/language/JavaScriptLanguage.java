package xyz.algogo.core.language;

import xyz.algogo.core.evaluator.expression.AbsoluteValueExpression;
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
 * JavaScript language implementation.
 */

public class JavaScriptLanguage extends DefaultLanguageImplementation {

	/**
	 * Creates a new JavaScript language.
	 */

	public JavaScriptLanguage() {
		super("JavaScript File", "js");

		addTranslations();
	}

	@Override
	public String getHeader() {
		return "";
	}

	@Override
	public String getFooter() {
		return "";
	}

	/**
	 * Adds translations.
	 */

	private void addTranslations() {
		this.putTranslation(VariablesBlock.class, (TranslationFunction<VariablesBlock>) this::translateBlockChildren);
		this.putTranslation(BeginningBlock.class, (TranslationFunction<BeginningBlock>) this::translateBlockChildren);
		this.putTranslation(EndBlock.class, (TranslationFunction<EndBlock>) statement -> "");

		this.putTranslation(CreateVariableStatement.class, (TranslationFunction<CreateVariableStatement>) statement -> "let " + statement.getIdentifier() + " = null;" + LINE_SEPARATOR);
		this.putTranslation(AssignStatement.class, (TranslationFunction<AssignStatement>) statement -> statement.getIdentifier() + " = " + statement.getValue().toLanguage(this) + ";" + LINE_SEPARATOR);
		this.putTranslation(PrintStatement.class, (TranslationFunction<PrintStatement>) statement -> "window.alert('" + statement.getMessage().replace("'", "\\'") + "');" + LINE_SEPARATOR);
		this.putTranslation(PrintVariableStatement.class, (TranslationFunction<PrintVariableStatement>) statement -> {
			String content = "window.alert(" + statement.getIdentifier() + ");" + LINE_SEPARATOR;

			if(statement.getMessage() != null) {
				content = "window.alert('" + statement.getMessage().replace("'", "\\'") + "');" + LINE_SEPARATOR + content;
			}

			return content;
		});
		this.putTranslation(PromptStatement.class, (TranslationFunction<PromptStatement>) statement -> {
			String content = statement.getIdentifier() + " = window.prompt('";
			content += statement.getMessage() == null ? "Enter the value of " + statement.getIdentifier() + " :" : statement.getMessage().replace("'", "\\\'");
			content += "');" + LINE_SEPARATOR;

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
		this.putTranslation(BlockComment.class, (TranslationFunction<BlockComment>) statement -> "/*" + statement.getContent().replace("\t", "") +  "*/" + LINE_SEPARATOR);

		this.putTranslation(AbsoluteValueExpression.class, (TranslationFunction<AbsoluteValueExpression>) expression -> "Math.abs(" + expression.getExpression().toLanguage(this) + ")");
	}

}