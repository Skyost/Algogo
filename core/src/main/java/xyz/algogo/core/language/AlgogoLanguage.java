package xyz.algogo.core.language;

import xyz.algogo.core.Algorithm;
import xyz.algogo.core.evaluator.expression.AbsoluteValueExpression;
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
		addTranslations();
	}

	/**
	 * Adds translations.
	 */

	private void addTranslations() {
		final TranslationFunction superFunction = this.getTranslationFunction(Algorithm.class);
		this.putTranslation(Algorithm.class, (TranslationFunction<Algorithm>) algorithm -> {
			String content = superFunction.translate(algorithm);
			if(addCredits) {
				content = new LineComment(algorithm.getTitle() + " by " + algorithm.getAuthor()).toLanguage(this) + content;
			}

			return content;
		});

		this.putTranslation(VariablesBlock.class, (TranslationFunction<VariablesBlock>) statement -> translateBlockStatement("VARIABLES", statement));
		this.putTranslation(BeginningBlock.class, (TranslationFunction<BeginningBlock>) statement -> translateBlockStatement("BEGINNING", statement));
		this.putTranslation(EndBlock.class, (TranslationFunction<EndBlock>) statement -> "END" + LINE_SEPARATOR);

		this.putTranslation(CreateVariableStatement.class, (TranslationFunction<CreateVariableStatement>) statement -> statement.getIdentifier() + " : " + statement.getType().name() + LINE_SEPARATOR);
		this.putTranslation(AssignStatement.class, (TranslationFunction<AssignStatement>) statement -> statement.getIdentifier() + " <- " + statement.getValue().toLanguage(this) + LINE_SEPARATOR);
		this.putTranslation(PrintStatement.class, (TranslationFunction<PrintStatement>) statement -> {
			String content = "PRINT \"" + statement.getMessage().replace("\"", "\\\"") + "\"";
			if(!statement.shouldLineBreak()) {
				content += " NLB";
			}

			return content + LINE_SEPARATOR;
		});
		this.putTranslation(PrintVariableStatement.class, (TranslationFunction<PrintVariableStatement>) statement -> {
			String content = "PRINT_VARIABLE " + statement.getIdentifier() + (statement.getMessage() == null ? "" : " \"" + statement.getMessage().replace("\"", "\\\"") + "\"");
			if(!statement.shouldLineBreak()) {
				content += " NLB";
			}

			return content + LINE_SEPARATOR;
		});
		this.putTranslation(PromptStatement.class, (TranslationFunction<PromptStatement>) statement -> "PROMPT " + statement.getIdentifier() + (statement.getMessage() == null ? "" : " \"" + statement.getMessage() + "\"") + LINE_SEPARATOR);
		this.putTranslation(IfBlock.class, (TranslationFunction<IfBlock>) statement -> {
			String content = translateBlockStatement("IF " + statement.getCondition().toLanguage(this) + " THEN", statement);
			if(statement.hasElseBlock()) {
				content += statement.getElseBlock().toLanguage(this);
			}
			return content;
		});
		this.putTranslation(ElseBlock.class, (TranslationFunction<ElseBlock>) statement -> translateBlockStatement("ELSE", statement));
		this.putTranslation(ForLoop.class, (TranslationFunction<ForLoop>) statement -> translateBlockStatement("FOR " + statement.getIdentifier() + " FROM " + statement.getStart().toLanguage(this) + " TO " + statement.getEnd().toLanguage(this) + " DO", statement));
		this.putTranslation(WhileLoop.class, (TranslationFunction<WhileLoop>) statement -> translateBlockStatement("WHILE " + statement.getCondition().toLanguage(this) + " DO", statement));
		this.putTranslation(LineComment.class, (TranslationFunction<LineComment>) statement -> "// " + statement.getContent() + LINE_SEPARATOR);
		this.putTranslation(BlockComment.class, (TranslationFunction<BlockComment>) statement -> "/*" + statement.getContent().replace("\t", "") +  "*/" + LINE_SEPARATOR);

		this.putTranslation(AbsoluteValueExpression.class, (TranslationFunction<AbsoluteValueExpression>) expression -> "|" + expression.getExpression().toLanguage(this) + "|" + LINE_SEPARATOR);
	}

	/**
	 * Checks whether credits should be added at the end of the file.
	 *
	 * @return Whether credits should be added at the end of the file.
	 */

	public boolean shouldAddCredits() {
		return addCredits;
	}

	/**
	 * Sets whether credits should be added at the end of the file.
	 *
	 * @param addCredits Whether credits should be added at the end of the file.
	 */

	public void setAddCredits(final boolean addCredits) {
		this.addCredits = addCredits;
	}

}