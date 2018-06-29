package xyz.algogo.mobile.language;

import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.widget.TextView;
import xyz.algogo.core.Algorithm;
import xyz.algogo.core.evaluator.VariableType;
import xyz.algogo.core.language.AlgogoLanguage;
import xyz.algogo.core.language.DefaultLanguageImplementation;
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
import xyz.algogo.mobile.R;
import xyz.algogo.mobile.adapter.AlgorithmAdapter;
import xyz.algogo.mobile.utils.Utils;

/**
 * Represents the algorithm localization language.
 */

public class AlgorithmLocalization extends DefaultLanguageImplementation {

	/**
	 * The adapter.
	 */
	
	private AlgorithmAdapter adapter;

	/**
	 * The text view.
	 */

	private TextView textView;

	/**
	 * Creates a new algorithm localization language.
	 *
	 * @param adapter The adapter.
	 * @param textView The text view to translate.
	 */
	
	public AlgorithmLocalization(final AlgorithmAdapter adapter, final TextView textView) {
		super("Algorithm localization");
		
		this.adapter = adapter;
		this.textView = textView;
	}

	@Override
	public final String translateAlgorithmRootBlock(final AlgorithmRootBlock statement) {
		final Algorithm algorithm = adapter.getAlgorithm();
		toHTML(android.R.color.white, R.string.main_title, algorithm.getTitle(), algorithm.getAuthor());
		return null;
	}

	@Override
	public final String translateVariablesBlock(final VariablesBlock statement) {
		toHTML(R.color.rootBlockStatementColor, R.string.statement_root_variables);
		return null;
	}

	@Override
	public final String translateBeginningBlock(final BeginningBlock statement) {
		toHTML(R.color.rootBlockStatementColor, R.string.statement_root_beginning);
		return null;
	}

	@Override
	public final String translateEndBlock(final EndBlock statement) {
		toHTML(R.color.rootBlockStatementColor, R.string.statement_root_end);
		return null;
	}

	@Override
	public final String translateCreateVariableStatement(final CreateVariableStatement statement) {
		toHTML(R.color.simpleStatementColor, R.string.statement_simple_createVariableStatement, statement.getIdentifier(), adapter.getActivity().getString(statement.getType() == VariableType.NUMBER ? R.string.statement_simple_createVariableStatement_number : R.string.statement_simple_createVariableStatement_string));
		return null;
	}

	@Override
	public final String translateAssignStatement(final AssignStatement statement) {
		toHTML(R.color.simpleStatementColor, R.string.statement_simple_assignStatement, statement.getIdentifier(), Utils.escapeHTML(statement.getValue().toLanguage(new AlgogoLanguage())));
		return null;
	}

	@Override
	public final String translatePromptStatement(final PromptStatement statement) {
		String message = adapter.getActivity().getString(R.string.statement_simple_promptStatement, statement.getIdentifier());
		if(statement.getMessage() != null) {
			message += " " + adapter.getActivity().getString(R.string.statement_simple_optionalString, Utils.escapeHTML(statement.getMessage()));
		}

		rawStringToHTML(R.color.simpleStatementColor, message);
		return null;
	}

	@Override
	public final String translatePrintVariableStatement(final PrintVariableStatement statement) {
		String message = adapter.getActivity().getString(R.string.statement_simple_printVariableStatement, statement.getIdentifier());
		if(statement.getMessage() != null) {
			message += " " + adapter.getActivity().getString(R.string.statement_simple_optionalString, Utils.escapeHTML(statement.getMessage()));
		}

		if(!statement.shouldLineBreak()) {
			message += " " + adapter.getActivity().getString(R.string.statement_simple_noLineBreak);
		}

		rawStringToHTML(R.color.simpleStatementColor, message);
		return null;
	}

	@Override
	public final String translatePrintStatement(final PrintStatement statement) {
		String content = adapter.getActivity().getString(R.string.statement_simple_printStatement, Utils.escapeHTML(statement.getMessage()));
		if(!statement.shouldLineBreak()) {
			content += " " + adapter.getActivity().getString(R.string.statement_simple_noLineBreak);
		}

		rawStringToHTML(R.color.simpleStatementColor, content);
		return null;
	}

	@Override
	public final String translateIfBlock(final IfBlock statement) {
		toHTML(R.color.blockStatementColor, R.string.statement_block_ifBlock, Utils.escapeHTML(statement.getCondition().toLanguage(new AlgogoLanguage())));
		return null;
	}

	@Override
	public final String translateElseBlock(final ElseBlock statement) {
		toHTML(R.color.blockStatementColor, R.string.statement_block_elseBlock);
		return null;
	}

	@Override
	public final String translateForLoop(final ForLoop statement) {
		toHTML(R.color.blockStatementColor, R.string.statement_loop_forLoop, statement.getIdentifier(), Utils.escapeHTML(statement.getStart().toLanguage(this)), Utils.escapeHTML(statement.getEnd().toLanguage(this)));
		return null;
	}

	@Override
	public final String translateWhileLoop(final WhileLoop statement) {
		toHTML(R.color.blockStatementColor, R.string.statement_loop_whileLoop, Utils.escapeHTML(statement.getCondition().toLanguage(new AlgogoLanguage())));
		return null;
	}

	@Override
	public final String translateLineComment(final LineComment statement) {
		toHTML(R.color.commentColor, R.string.statement_comment_lineComment, Utils.escapeHTML(statement.getContent()));
		return null;
	}

	@Override
	public final String translateBlockComment(final BlockComment statement) {
		toHTML(R.color.commentColor, R.string.statement_comment_blockComment, Utils.escapeHTML(statement.getContent()).replace(System.getProperty("line.separator"), "<br>"));
		return null;
	}

	/**
	 * Returns the adapter.
	 *
	 * @return The adapter.
	 */

	public final AlgorithmAdapter getAdapter() {
		return adapter;
	}

	/**
	 * Sets the adapter.
	 *
	 * @param adapter The adapter.
	 */

	public final void setAdapter(final AlgorithmAdapter adapter) {
		this.adapter = adapter;
	}

	/**
	 * Returns the text view to translate.
	 *
	 * @return The text view.
	 */

	public final TextView getTextView() {
		return textView;
	}

	/**
	 * Sets the text view to translate.
	 *
	 * @param textView The text view.
	 */

	public final void setTextView(final TextView textView) {
		this.textView = textView;
	}

	/**
	 * Creates a HTML content from a language key.
	 *
	 * @param color Statement color.
	 * @param key Language key.
	 * @param arguments Formatting arguments.
	 */

	private void toHTML(final int color, final int key, final Object... arguments) {
		rawStringToHTML(color, adapter.getActivity().getString(key, arguments));
	}

	/**
	 * Formats the statement content according to the provided content.
	 *
	 * @param color Statement color.
	 * @param content The content.
	 */

	private void rawStringToHTML(final int color, final String content) {
		textView.setTextColor(ContextCompat.getColor(adapter.getActivity(), color));
		textView.setText(Html.fromHtml(content));
	}

}
