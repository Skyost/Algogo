package xyz.algogo.core;

import org.antlr.v4.runtime.tree.TerminalNode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import xyz.algogo.core.antlr.AlgogoBaseVisitor;
import xyz.algogo.core.antlr.AlgogoParser;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.BooleanAtom;
import xyz.algogo.core.evaluator.atom.IdentifierAtom;
import xyz.algogo.core.evaluator.atom.NumberAtom;
import xyz.algogo.core.evaluator.atom.StringAtom;
import xyz.algogo.core.evaluator.expression.AbsoluteValueExpression;
import xyz.algogo.core.evaluator.expression.AdditiveExpression;
import xyz.algogo.core.evaluator.expression.AndExpression;
import xyz.algogo.core.evaluator.expression.AtomExpression;
import xyz.algogo.core.evaluator.expression.EqualityExpression;
import xyz.algogo.core.evaluator.expression.Expression;
import xyz.algogo.core.evaluator.expression.FunctionExpression;
import xyz.algogo.core.evaluator.expression.MultiplicationExpression;
import xyz.algogo.core.evaluator.expression.NotExpression;
import xyz.algogo.core.evaluator.expression.OrExpression;
import xyz.algogo.core.evaluator.expression.ParenthesisExpression;
import xyz.algogo.core.evaluator.expression.PowerExpression;
import xyz.algogo.core.evaluator.expression.RelationalExpression;
import xyz.algogo.core.evaluator.expression.UnaryMinusExpression;
import xyz.algogo.core.evaluator.variable.VariableType;
import xyz.algogo.core.statement.Statement;
import xyz.algogo.core.statement.block.conditional.ElseBlock;
import xyz.algogo.core.statement.block.conditional.IfBlock;
import xyz.algogo.core.statement.block.loop.ForLoop;
import xyz.algogo.core.statement.block.loop.WhileLoop;
import xyz.algogo.core.statement.block.root.AlgorithmRootBlock;
import xyz.algogo.core.statement.block.root.BeginningBlock;
import xyz.algogo.core.statement.block.root.EndBlock;
import xyz.algogo.core.statement.block.root.VariablesBlock;
import xyz.algogo.core.statement.simple.comment.BlockComment;
import xyz.algogo.core.statement.simple.comment.Comment;
import xyz.algogo.core.statement.simple.comment.LineComment;
import xyz.algogo.core.statement.simple.io.PrintStatement;
import xyz.algogo.core.statement.simple.io.PrintVariableStatement;
import xyz.algogo.core.statement.simple.io.PromptStatement;
import xyz.algogo.core.statement.simple.variable.AssignStatement;
import xyz.algogo.core.statement.simple.variable.CreateVariableStatement;

/**
 * ANTLR parser visitor.
 */

public class AlgorithmParserVisitor extends AlgogoBaseVisitor<Object> {

	@Override
	public AlgorithmRootBlock visitScript(final AlgogoParser.ScriptContext context) {
		final AlgorithmRootBlock algorithmRootBlock = new AlgorithmRootBlock();
		for(final AlgogoParser.RootStatementContext rootStatementContext : context.rootStatement()) {
			algorithmRootBlock.addStatement((Statement)visitRootStatement(rootStatementContext));
		}

		return algorithmRootBlock;
	}

	@Override
	public VariablesBlock visitVariablesBlockRootStatement(final AlgogoParser.VariablesBlockRootStatementContext context) {
		final VariablesBlock variablesBlock = new VariablesBlock();
		for(final AlgogoParser.CreateVariableStatementContext createVariableStatementContext : context.createVariableStatement()) {
			variablesBlock.addStatement(visitCreateVariableStatement(createVariableStatementContext));
		}

		return variablesBlock;
	}

	@Override
	public BeginningBlock visitBeginningBlockRootStatement(final AlgogoParser.BeginningBlockRootStatementContext context) {
		final BeginningBlock beginningBlock = new BeginningBlock();
		for(final AlgogoParser.StatementContext statementContext : context.statement()) {
			beginningBlock.addStatement(visitStatement(statementContext));
		}

		return beginningBlock;
	}

	@Override
	public EndBlock visitEndBlockRootStatement(final AlgogoParser.EndBlockRootStatementContext context) {
		return new EndBlock();
	}

	@Override
	public Statement visitCreateVariableStatement(final AlgogoParser.CreateVariableStatementContext context) {
		final AlgogoParser.CommentContext commentContext = context.comment();
		if(commentContext != null) {
			return visitComment(commentContext);
		}

		return new CreateVariableStatement(context.ID().getText(), VariableType.valueOf(context.type.getText()));
	}

	@Override
	public Statement visitStatement(final AlgogoParser.StatementContext context) {
		return (Statement)super.visitStatement(context);
	}

	@Override
	public PromptStatement visitPromptStatement(final AlgogoParser.PromptStatementContext context) {
		return new PromptStatement(context.ID().getText(), getString(context.STRING()));
	}

	@Override
	public PrintStatement visitPrintStatement(final AlgogoParser.PrintStatementContext context) {
		return new PrintStatement(getString(context.STRING()), context.NO_LINE_BREAK() == null);
	}

	@Override
	public PrintVariableStatement visitPrintVariableStatement(final AlgogoParser.PrintVariableStatementContext context) {
		return new PrintVariableStatement(context.ID().toString(), getString(context.STRING()), context.NO_LINE_BREAK() == null);
	}

	@Override
	public IfBlock visitIfBlockStatement(final AlgogoParser.IfBlockStatementContext context) {
		final IfBlock ifBlock = new IfBlock(visitExpression(context.expression()), visitElseBlock(context.elseBlock()));
		for(final AlgogoParser.StatementContext statementContext : context.statement()) {
			ifBlock.addStatement(visitStatement(statementContext));
		}

		return ifBlock;
	}

	@Override
	public WhileLoop visitWhileBlockStatement(final AlgogoParser.WhileBlockStatementContext context) {
		final WhileLoop whileLoop = new WhileLoop(visitExpression(context.expression()));
		for(final AlgogoParser.StatementContext statementContext : context.statement()) {
			whileLoop.addStatement(visitStatement(statementContext));
		}

		return whileLoop;
	}

	@Override
	public ForLoop visitForBlockStatement(final AlgogoParser.ForBlockStatementContext context) {
		final ForLoop forLoop = new ForLoop(context.ID().getText(), visitExpression(context.start), visitExpression(context.end));
		for(final AlgogoParser.StatementContext statementContext : context.statement()) {
			forLoop.addStatement(visitStatement(statementContext));
		}

		return forLoop;
	}

	@Override
	public AssignStatement visitAssignStatement(final AlgogoParser.AssignStatementContext context) {
		return new AssignStatement(context.ID().getText(), visitExpression(context.expression()));
	}

	@Override
	public ElseBlock visitElseBlock(final AlgogoParser.ElseBlockContext context) {
		if(context == null) {
			return null;
		}

		final ElseBlock elseBlock = new ElseBlock();
		for(final AlgogoParser.StatementContext statementContext : context.statement()) {
			elseBlock.addStatement(visitStatement(statementContext));
		}

		return elseBlock;
	}

	@Override
	public Comment visitComment(final AlgogoParser.CommentContext context) {
		final TerminalNode lineComment = context.LineComment();
		if(lineComment == null) {
			final String blockComment = context.BlockComment().getText();
			return new BlockComment(blockComment.substring(2, blockComment.length() - 2));
		}

		return new LineComment(lineComment.getText().substring(2).trim());
	}

	@Override
	public OrExpression visitOrExpression(final AlgogoParser.OrExpressionContext context) {
		return new OrExpression(visitExpression(context.expression(0)), visitExpression(context.expression(1)));
	}

	@Override
	public UnaryMinusExpression visitUnaryMinusExpression(final AlgogoParser.UnaryMinusExpressionContext context) {
		return new UnaryMinusExpression(visitExpression(context.expression()));
	}

	@Override
	public AndExpression visitAndExpression(final AlgogoParser.AndExpressionContext context) {
		return new AndExpression(visitExpression(context.expression(0)), visitExpression(context.expression(1)));
	}

	@Override
	public AtomExpression visitAtomExpression(final AlgogoParser.AtomExpressionContext context) {
		return new AtomExpression(visitAtom(context.atom()));
	}

	@Override
	public AdditiveExpression visitAdditiveExpression(final AlgogoParser.AdditiveExpressionContext context) {
		return new AdditiveExpression(visitExpression(context.expression(0)), context.op.getText(), visitExpression(context.expression(1)));
	}

	@Override
	public PowerExpression visitPowExpression(final AlgogoParser.PowExpressionContext context) {
		return new PowerExpression(visitExpression(context.expression(0)), visitExpression(context.expression(1)));
	}

	@Override
	public RelationalExpression visitRelationalExpression(final AlgogoParser.RelationalExpressionContext context) {
		return new RelationalExpression(visitExpression(context.expression(0)), context.op.getText(), visitExpression(context.expression(1)));
	}

	@Override
	public EqualityExpression visitEqualityExpression(final AlgogoParser.EqualityExpressionContext context) {
		return new EqualityExpression(visitExpression(context.expression(0)), context.op.getText(), visitExpression(context.expression(1)));
	}

	@Override
	public NotExpression visitNotExpression(final AlgogoParser.NotExpressionContext context) {
		return new NotExpression(visitExpression(context.expression()));
	}

	@Override
	public MultiplicationExpression visitMultiplicationExpression(final AlgogoParser.MultiplicationExpressionContext context) {
		return new MultiplicationExpression(visitExpression(context.expression(0)), context.op.getText(), visitExpression(context.expression(1)));
	}

	@Override
	public FunctionExpression visitFunctionExpression(final AlgogoParser.FunctionExpressionContext context) {
		final List<Expression> arguments = new ArrayList<>();
		for(final AlgogoParser.ExpressionContext expressionContext : context.functionParams().expression()) {
			arguments.add(visitExpression(expressionContext));
		}

		return new FunctionExpression(context.ID().getText(), arguments.toArray(new Expression[0]));
	}

	@Override
	public ParenthesisExpression visitParenthesisExpression(final AlgogoParser.ParenthesisExpressionContext context) {
		return new ParenthesisExpression(visitExpression(context.expression()));
	}

	@Override
	public AbsoluteValueExpression visitAbsoluteValueExpression(final AlgogoParser.AbsoluteValueExpressionContext context) {
		return new AbsoluteValueExpression(visitExpression(context.expression()));
	}

	@Override
	public Atom visitAtom(final AlgogoParser.AtomContext context) {
		return (Atom)super.visitAtom(context);
	}

	@Override
	public NumberAtom visitNumberAtom(final AlgogoParser.NumberAtomContext context) {
		return new NumberAtom(new BigDecimal(context.number.getText()));
	}

	@Override
	public BooleanAtom visitBooleanAtom(final AlgogoParser.BooleanAtomContext context) {
		return new BooleanAtom(context.bool.getText().equalsIgnoreCase("true"));
	}

	@Override
	public IdentifierAtom visitIdentifierAtom(final AlgogoParser.IdentifierAtomContext context) {
		return new IdentifierAtom(context.ID().getText());
	}

	@Override
	public StringAtom visitStringAtom(final AlgogoParser.StringAtomContext context) {
		return new StringAtom(getString(context.STRING()));
	}

	/**
	 * Default implementation of <em>visitExpression(...)</em> from ANTLR.
	 *
	 * @param context The context.
	 *
	 * @return The expected result of <em>visitExpression(...)</em>.
	 */

	public Expression visitExpression(final AlgogoParser.ExpressionContext context) {
		return (Expression)this.visit(context);
	}

	/**
	 * Converts a STRING terminal node to a string.
	 *
	 * @param string The terminal node.
	 *
	 * @return The converted terminal node.
	 */

	private String getString(final TerminalNode string) {
		if(string == null) {
			return null;
		}

		String content = string.getText();
		content = content.substring(1, content.length() - 1);

		if(content.isEmpty()) {
			return null;
		}

		return content;
	}

}
