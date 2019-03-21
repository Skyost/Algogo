package xyz.algogo.core.language;

import xyz.algogo.core.evaluator.expression.AbsoluteValueExpression;
import xyz.algogo.core.evaluator.variable.VariableType;
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
		addTranslations();
	}

	/**
	 * Returns the current class name.
	 *
	 * @return The current class name.
	 */

	public String getClassName() {
		return className;
	}

	/**
	 * Sets the current class name.
	 *
	 * @param className The current class name.
	 */

	public void setClassName(final String className) {
		this.className = className;
	}

	@Override
	public String getHeader() {
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
	public String getFooter() {
		return "\t}" + LINE_SEPARATOR + LINE_SEPARATOR + "}";
	}

	/**
	 * Adds translations.
	 */

	private void addTranslations() {
		this.putTranslation(VariablesBlock.class, (TranslationFunction<VariablesBlock>) statement -> {
			final String scanner = "\tfinal Scanner scanner = new Scanner(System.in);" + LINE_SEPARATOR;
			return indentStringBlock(scanner + translateBlockStatement("", statement));
		});
		this.putTranslation(BeginningBlock.class, (TranslationFunction<BeginningBlock>) statement -> indentStringBlock(translateBlockStatement("", statement)));
		this.putTranslation(EndBlock.class, (TranslationFunction<EndBlock>) statement -> "");

		this.putTranslation(CreateVariableStatement.class, (TranslationFunction<CreateVariableStatement>) statement -> (statement.getType() == VariableType.NUMBER ? "Integer " : "String ") + statement.getIdentifier() + " = null;" + LINE_SEPARATOR);
		this.putTranslation(AssignStatement.class, (TranslationFunction<AssignStatement>) statement -> statement.getIdentifier() + " = " + statement.getValue().toLanguage(this) + ";" + LINE_SEPARATOR);
		this.putTranslation(PrintStatement.class, (TranslationFunction<PrintStatement>) statement -> {
			final String print = statement.shouldLineBreak() ? "System.out.println" : "System.out.print";
			return print + "(\"" + statement.getMessage().replace("\"", "\\\"") + "\");" + LINE_SEPARATOR;
		});
		this.putTranslation(PrintVariableStatement.class, (TranslationFunction<PrintVariableStatement>) statement -> {
			final String print = statement.shouldLineBreak() ? "System.out.println" : "System.out.print";
			String content = print + "(" + statement.getIdentifier() + ");" + LINE_SEPARATOR;

			if(statement.getMessage() != null) {
				content = print + "(\"" + statement.getMessage().replace("\"", "\\\"") + "\");" + LINE_SEPARATOR + content;
			}

			return content;
		});
		this.putTranslation(PromptStatement.class, (TranslationFunction<PromptStatement>) statement -> {
			final String comment = "// " + statement.getIdentifier() + " = Integer.parseInt(scanner.nextLine()); // Uncomment if " + statement.getIdentifier() + " has a number type and comment the above line." + LINE_SEPARATOR;

			String content = statement.getIdentifier() + " = scanner.nextLine();" + LINE_SEPARATOR + comment;
			if(statement.getMessage() != null) {
				content = "System.out.println(\"" + statement.getMessage().replace("\"", "\\\"") + "\");" + LINE_SEPARATOR + content;
			}

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