package xyz.algogo.core.language;

import xyz.algogo.core.evaluator.variable.VariableType;
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
 * Java language implementation.
 */

public class JavaLanguage extends DefaultLanguageImplementation {

	/**
	 * The current class name.
	 */

	private String className;

	/**
	 * Creates a Java language.
	 *
	 * @param className Class name.
	 */

	public JavaLanguage(final String className) {
		super("Java Class", "java");

		this.className = className;
	}

	/**
	 * Returns the current class name.
	 *
	 * @return The current class name.
	 */

	public final String getClassName() {
		return className;
	}

	/**
	 * Sets the current class name.
	 *
	 * @param className The current class name.
	 */

	public final void setClassName(final String className) {
		this.className = className;
	}

	@Override
	public final String getHeader() {
		return "package xyz.algogo;" +
				LINE_SEPARATOR +
				LINE_SEPARATOR +
				"import java.util.Scanner;" +
				LINE_SEPARATOR +
				LINE_SEPARATOR +
				"public class " +
				className + " {" +
				LINE_SEPARATOR +
				LINE_SEPARATOR +
				"\tpublic static void main(final String[] args) {" +
				LINE_SEPARATOR;
	}

	@Override
	public final String getFooter() {
		return "\t}" + LINE_SEPARATOR + LINE_SEPARATOR + "}";
	}

	@Override
	public final String translateVariablesBlock(final VariablesBlock statement) {
		final String scanner = "\tfinal Scanner scanner = new Scanner(System.in);" + LINE_SEPARATOR;
		return indentStringBlock(scanner + translateBlockStatement("", statement));
	}

	@Override
	public final String translateBeginningBlock(final BeginningBlock statement) {
		return indentStringBlock(translateBlockStatement("", statement));
	}

	@Override
	public final String translateEndBlock(final EndBlock statement) {
		return "";
	}

	@Override
	public final String translateCreateVariableStatement(final CreateVariableStatement statement) {
		return (statement.getType() == VariableType.NUMBER ? "Integer " : "String ") + statement.getIdentifier() + " = null;" + LINE_SEPARATOR;
	}

	@Override
	public final String translateAssignStatement(final AssignStatement statement) {
		return statement.getIdentifier() + " = " + statement.getValue().toLanguage(this) + ";" + LINE_SEPARATOR;
	}

	@Override
	public final String translatePrintStatement(final PrintStatement statement) {
		final String print = statement.shouldLineBreak() ? "System.out.println" : "System.out.print";
		return print + "(\"" + statement.getMessage().replace("\"", "\\\"") + "\");" + LINE_SEPARATOR;
	}

	@Override
	public final String translatePrintVariableStatement(final PrintVariableStatement statement) {
		final String print = statement.shouldLineBreak() ? "System.out.println" : "System.out.print";
		String content = print + "(" + statement.getIdentifier() + ");" + LINE_SEPARATOR;

		if(statement.getMessage() != null) {
			content = print + "(\"" + statement.getMessage().replace("\"", "\\\"") + "\");" + LINE_SEPARATOR + content;
		}

		return content;
	}

	@Override
	public final String translatePromptStatement(final PromptStatement statement) {
		final String comment = "# " + statement.getIdentifier() + " = Integer.parseInt(scanner.nextLine()); # Uncomment if " + statement.getIdentifier() + " has a number type and comment the above line." + LINE_SEPARATOR;

		String content = statement.getIdentifier() + " = scanner.nextLine();" + LINE_SEPARATOR + comment;
		if(statement.getMessage() != null) {
			content = "System.out.println(\"" + statement.getMessage().replace("\"", "\\\"") + "\");" + LINE_SEPARATOR + content;
		}

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