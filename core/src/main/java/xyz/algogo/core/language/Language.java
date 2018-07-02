package xyz.algogo.core.language;

import xyz.algogo.core.Algorithm;
import xyz.algogo.core.evaluator.atom.BooleanAtom;
import xyz.algogo.core.evaluator.atom.IdentifierAtom;
import xyz.algogo.core.evaluator.atom.NumberAtom;
import xyz.algogo.core.evaluator.atom.StringAtom;
import xyz.algogo.core.evaluator.expression.*;
import xyz.algogo.core.statement.block.conditional.ElseBlock;
import xyz.algogo.core.statement.block.conditional.IfBlock;
import xyz.algogo.core.statement.block.loop.ForLoop;
import xyz.algogo.core.statement.block.loop.WhileLoop;
import xyz.algogo.core.statement.block.root.AlgorithmRootBlock;
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
 * Represents an Algogo Core language.
 */

public abstract class Language {

	/**
	 * The language name.
	 */
	
	private String name;

	/**
	 * The language extension.
	 */

	private String extension;

	/**
	 * Creates a new language.
	 *
	 * @param name The language name.
	 */
	
	public Language(final String name) {
		this(name, null);
	}

	/**
	 * Creates a new language.
	 *
	 * @param name The language name.
	 * @param extension The language extension.
	 */
	
	public Language(final String name, final String extension) {
		this.name = name;
		this.extension = extension;
	}

	/**
	 * Returns the language name.
	 *
	 * @return The language name.
	 */
	
	public String getName() {
		return name;
	}

	/**
	 * Sets the language name.
	 *
	 * @param name The name.
	 */
	
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Returns the language extension.
	 *
	 * @return The language extension.
	 */
	
	public String getExtension() {
		return extension;
	}

	/**
	 * Sets the language extension.
	 *
	 * @param extension The extension.
	 */
	
	public void setExtension(final String extension) {
		this.extension = extension;
	}

	/**
	 * Returns the language header.
	 *
	 * @return The language header.
	 */

	public String getHeader() {
		return null;
	}

	/**
	 * Returns the language footer.
	 *
	 * @return The language footer.
	 */

	public String getFooter() {
		return null;
	}

	/**
	 * Translates an algorithm.
	 *
	 * @param algorithm The algorithm.
	 *
	 * @return The translated algorithm.
	 */

	public String translateAlgorithm(final Algorithm algorithm) {
		String content = getHeader() == null ? "" : getHeader();
		content += translateAlgorithmRootBlock(algorithm.getRootBlock());
		content += getFooter() == null ? "" : getFooter();

		return content;
	}

	/**
	 * Translates an algorithm root block.
	 *
	 * @param statement The algorithm root block.
	 *
	 * @return The translated statement.
	 */

	public abstract String translateAlgorithmRootBlock(final AlgorithmRootBlock statement);

	/**
	 * Translates a variables block.
	 * 
	 * @param statement The variables block.
	 * 
	 * @return The translated statement.
	 */

	public abstract String translateVariablesBlock(final VariablesBlock statement);

	/**
	 * Translates a beginning block.
	 *
	 * @param statement The beginning block.
	 *
	 * @return The translated statement.
	 */
	
	public abstract String translateBeginningBlock(final BeginningBlock statement);

	/**
	 * Translates a end block.
	 *
	 * @param statement The end block.
	 *
	 * @return The translated statement.
	 */
	
	public abstract String translateEndBlock(final EndBlock statement);

	/**
	 * Translates a create variable statement.
	 *
	 * @param statement The create variable statement.
	 *
	 * @return The translated statement.
	 */

	public abstract String translateCreateVariableStatement(final CreateVariableStatement statement);

	/**
	 * Translates an assign statement.
	 *
	 * @param statement The assign statement.
	 *
	 * @return The translated statement.
	 */
	
	public abstract String translateAssignStatement(final AssignStatement statement);

	/**
	 * Translates a print statement.
	 *
	 * @param statement The print statement.
	 *
	 * @return The translated statement.
	 */

	public abstract String translatePrintStatement(final PrintStatement statement);

	/**
	 * Translates a print variable statement.
	 *
	 * @param statement The print variable statement.
	 *
	 * @return The translated statement.
	 */
	
	public abstract String translatePrintVariableStatement(final PrintVariableStatement statement);

	/**
	 * Translates a prompt statement.
	 *
	 * @param statement The prompt statement.
	 *
	 * @return The translated statement.
	 */
	
	public abstract String translatePromptStatement(final PromptStatement statement);

	/**
	 * Translates an if block.
	 *
	 * @param statement The if block.
	 *
	 * @return The translated statement.
	 */

	public abstract String translateIfBlock(final IfBlock statement);

	/**
	 * Translates an else block.
	 *
	 * @param statement The else block.
	 *
	 * @return The translated statement.
	 */
	
	public abstract String translateElseBlock(final ElseBlock statement);

	/**
	 * Translates a for loop.
	 *
	 * @param statement The for loop.
	 *
	 * @return The translated statement.
	 */

	public abstract String translateForLoop(final ForLoop statement);

	/**
	 * Translates a while loop.
	 *
	 * @param statement The while loop.
	 *
	 * @return The translated statement.
	 */
	
	public abstract String translateWhileLoop(final WhileLoop statement);

	/**
	 * Translates a line comment.
	 *
	 * @param statement The line comment.
	 *
	 * @return The translated statement.
	 */

	public abstract String translateLineComment(final LineComment statement);

	/**
	 * Translates a block comment.
	 *
	 * @param statement The block comment.
	 *
	 * @return The translated statement.
	 */
	
	public abstract String translateBlockComment(final BlockComment statement);

	/**
	 * Translates an additive expression.
	 *
	 * @param expression The additive expression.
	 *
	 * @return The translated expression.
	 */
	
	public abstract String translateAdditiveExpression(final AdditiveExpression expression);

	/**
	 * Translates a multiplication expression.
	 *
	 * @param expression The multiplication expression.
	 *
	 * @return The translated expression.
	 */
	
	public abstract String translateMultiplicationExpression(final MultiplicationExpression expression);

	/**
	 * Translates a power expression.
	 *
	 * @param expression The power expression.
	 *
	 * @return The translated expression.
	 */

	public abstract String translatePowerExpression(final PowerExpression expression);

	/**
	 * Translates a relational expression.
	 *
	 * @param expression The relational expression.
	 *
	 * @return The translated expression.
	 */

	public abstract String translateRelationalExpression(final RelationalExpression expression);

	/**
	 * Translates an equality expression.
	 *
	 * @param expression The equality expression.
	 *
	 * @return The translated expression.
	 */
	
	public abstract String translateEqualityExpression(final EqualityExpression expression);

	/**
	 * Translates an AND expression.
	 *
	 * @param expression The AND expression.
	 *
	 * @return The translated expression.
	 */
	
	public abstract String translateAndExpression(final AndExpression expression);

	/**
	 * Translates an OR expression.
	 *
	 * @param expression The OR expression.
	 *
	 * @return The translated expression.
	 */
	
	public abstract String translateOrExpression(final OrExpression expression);

	/**
	 * Translates a NOT expression.
	 *
	 * @param expression The NOT expression.
	 *
	 * @return The translated expression.
	 */
	
	public abstract String translateNotExpression(final NotExpression expression);

	/**
	 * Translates a parenthesis expression.
	 *
	 * @param expression The parenthesis expression.
	 *
	 * @return The translated expression.
	 */

	public abstract String translateParenthesisExpression(final ParenthesisExpression expression);

	/**
	 * Translates an absolute value expression.
	 *
	 * @param expression The absolute value expression.
	 *
	 * @return The translated expression.
	 */

	public abstract String translateAbsoluteValueExpression(final AbsoluteValueExpression expression);

	/**
	 * Translates an unary minus expression.
	 *
	 * @param expression The unary minus expression.
	 *
	 * @return The translated expression.
	 */
	
	public abstract String translateUnaryMinusExpression(final UnaryMinusExpression expression);

	/**
	 * Translates a function expression.
	 *
	 * @param expression The function expression.
	 *
	 * @return The translated expression.
	 */

	public abstract String translateFunctionExpression(final FunctionExpression expression);

	/**
	 * Translates an atom expression.
	 *
	 * @param expression The atom expression.
	 *
	 * @return The translated expression.
	 */

	public abstract String translateAtomExpression(final AtomExpression expression);

	/**
	 * Translates a number atom.
	 *
	 * @param atom The number atom.
	 *
	 * @return The translated atom.
	 */

	public abstract String translateNumberAtom(final NumberAtom atom);

	/**
	 * Translates a string atom.
	 *
	 * @param atom The string atom.
	 *
	 * @return The translated atom.
	 */

	public abstract String translateStringAtom(final StringAtom atom);

	/**
	 * Translates an identifier atom.
	 *
	 * @param atom The identifier atom.
	 *
	 * @return The translated atom.
	 */

	public abstract String translateIdentifierAtom(final IdentifierAtom atom);

	/**
	 * Translates a boolean atom.
	 *
	 * @param atom The boolean atom.
	 *
	 * @return The translated atom.
	 */

	public abstract String translateBooleanAtom(final BooleanAtom atom);

}