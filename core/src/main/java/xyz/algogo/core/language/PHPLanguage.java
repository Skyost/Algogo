package xyz.algogo.core.language;

import xyz.algogo.core.Algorithm;
import xyz.algogo.core.evaluator.atom.IdentifierAtom;
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
	}

	@Override
	public final String getHeader() {
		return "<?php" + LINE_SEPARATOR + LINE_SEPARATOR;
	}

	@Override
	public final String getFooter() {
		return LINE_SEPARATOR + "?>";
	}

	@Override
	public final String translateAlgorithm(final Algorithm algorithm) {
		promptId = 0;
		return super.translateAlgorithm(algorithm);
	}

	@Override
	public final String translateVariablesBlock(final VariablesBlock statement) {
		return "";
	}

	@Override
	public final String translateBeginningBlock(final BeginningBlock statement) {
		return translateBlockChildren(statement);
	}

	@Override
	public final String translateEndBlock(final EndBlock statement) {
		promptId = 0;
		return "";
	}

	@Override
	public final String translateCreateVariableStatement(final CreateVariableStatement statement) {
		return "";
	}

	@Override
	public final String translateAssignStatement(final AssignStatement statement) {
		return "$" + statement.getIdentifier() + " = " + statement.getValue().toLanguage(this) + ";" + LINE_SEPARATOR;
	}

	@Override
	public final String translatePrintStatement(final PrintStatement statement) {
		return "echo('" + statement.getMessage().replace("'", "\\'") + "');" + LINE_SEPARATOR;
	}

	@Override
	public final String translatePrintVariableStatement(final PrintVariableStatement statement) {
		String content = "echo $" + statement.getIdentifier() + ";" + LINE_SEPARATOR;
		if(statement.getMessage() != null) {
			content = "echo '" + statement.getMessage().replace("'", "\\'") + "'" + LINE_SEPARATOR + content;
		}

		return content;
	}

	@Override
	public final String translatePromptStatement(final PromptStatement statement) {
		String content = "if(!isset($_GET['" + statement.getIdentifier() + promptId + "'])) { die('" + statement.getIdentifier() + promptId + " not set'); }" + LINE_SEPARATOR;
		content += "$" + statement.getIdentifier() + " = $_GET['" + statement.getIdentifier() + promptId++ + "'];" + LINE_SEPARATOR;

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
	public final String translateIdentifierAtom(final IdentifierAtom atom) {
		return "$" + atom.getValue();
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