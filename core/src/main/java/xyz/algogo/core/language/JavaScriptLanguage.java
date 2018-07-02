package xyz.algogo.core.language;

import xyz.algogo.core.evaluator.expression.AbsoluteValueExpression;
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
 * JavaScript language implementation.
 */

public class JavaScriptLanguage extends DefaultLanguageImplementation {

	/**
	 * Creates a new JavaScript language.
	 */

	public JavaScriptLanguage() {
		super("JavaScript File", "js");
	}

	@Override
	public final String getHeader() {
		return "";
	}

	@Override
	public final String getFooter() {
		return "";
	}

	@Override
	public final String translateVariablesBlock(final VariablesBlock statement) {
		return translateBlockChildren(statement);
	}

	@Override
	public final String translateBeginningBlock(final BeginningBlock statement) {
		return translateBlockChildren(statement);
	}

	@Override
	public final String translateEndBlock(final EndBlock statement) {
		return "";
	}

	@Override
	public final String translateCreateVariableStatement(final CreateVariableStatement statement) {
		return "let " + statement.getIdentifier() + " = null;" + LINE_SEPARATOR;
	}

	@Override
	public final String translateAssignStatement(final AssignStatement statement) {
		return statement.getIdentifier() + " = " + statement.getValue().toLanguage(this) + ";" + LINE_SEPARATOR;
	}

	@Override
	public final String translatePrintStatement(final PrintStatement statement) {
		return "window.alert('" + statement.getMessage().replace("'", "\\'") + "');" + LINE_SEPARATOR;
	}

	@Override
	public final String translatePrintVariableStatement(final PrintVariableStatement statement) {
		String content = "window.alert(" + statement.getIdentifier() + ");" + LINE_SEPARATOR;

		if(statement.getMessage() != null) {
			content = "window.alert('" + statement.getMessage().replace("'", "\\'") + "');" + LINE_SEPARATOR + content;
		}

		return content;
	}

	@Override
	public final String translatePromptStatement(final PromptStatement statement) {
		String content = statement.getIdentifier() + " = window.prompt('";
		content += statement.getMessage() == null ? "Enter the value of " + statement.getIdentifier() + " :" : statement.getMessage().replace("'", "\\\'");
		content += "');" + LINE_SEPARATOR;

		return content;
	}

	@Override
	public final String translateIfBlock(final IfBlock statement) {
		String content = translateBlockStatement("if(" + statement.getCondition().toLanguage(this) + ") {", statement) + "}" + LINE_SEPARATOR;
		if(statement.hasElseBlock()) {
			content += statement.getElseBlock().toLanguage(this);
		}
		return content;
	}

	@Override
	public final String translateElseBlock(final ElseBlock statement) {
		return translateBlockStatement("else {", statement) + "}" + LINE_SEPARATOR;
	}

	@Override
	public final String translateForLoop(final ForLoop statement) {
		return "// Cannot translate a FOR loop for the moment, sorry. Remember that other languages implementation is still in Beta !" + LINE_SEPARATOR;
	}

	@Override
	public final String translateWhileLoop(final WhileLoop statement) {
		return translateBlockStatement("while(" + statement.getCondition().toLanguage(this) + ") {", statement) + "}" + LINE_SEPARATOR;
	}

	@Override
	public final String translateLineComment(final LineComment statement) {
		return "// " + statement.getContent() + LINE_SEPARATOR;
	}

	@Override
	public final String translateBlockComment(final BlockComment statement) {
		return "/*" + statement.getContent().replace("\t", "") +  "*/" + LINE_SEPARATOR;
	}

	@Override
	public final String translateAbsoluteValueExpression(final AbsoluteValueExpression expression) {
		return "Math.abs(" + expression.getExpression().toLanguage(this) + ")";
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