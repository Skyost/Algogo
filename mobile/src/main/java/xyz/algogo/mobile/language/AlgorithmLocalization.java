package xyz.algogo.mobile.language;

import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import xyz.algogo.core.Algorithm;
import xyz.algogo.core.evaluator.variable.VariableType;
import xyz.algogo.core.language.DefaultLanguageImplementation;
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
	 * Creates a new algorithm localization language.
	 *
	 * @param adapter The adapter.
	 */
	
	public AlgorithmLocalization(final AlgorithmAdapter adapter) {
		super("Algorithm localization");
		
		this.adapter = adapter;
		addTranslations();
	}

	/**
	 * Translates the statement and applies this translation to the text view.
	 *
	 * @param statement The statement.
	 * @param textView The text view.
	 */

	public final void translate(final Statement statement, final TextView textView) {
		textView.setText(Utils.fromHtml(statement.toLanguage(this)));
		textView.setTextColor(ContextCompat.getColor(adapter.getActivity(), getColor(statement)));
	}

	/**
	 * Returns the color of a statement.
	 *
	 * @param statement The statement.
	 *
	 * @return The color.
	 */

	public final int getColor(final Statement statement) {
		switch(statement.getStatementId()) {
			case AlgorithmRootBlock.STATEMENT_ID:
				return android.R.color.white;
			case VariablesBlock.STATEMENT_ID:
			case BeginningBlock.STATEMENT_ID:
			case EndBlock.STATEMENT_ID:
				return R.color.rootBlockStatementColor;
			case IfBlock.STATEMENT_ID:
			case ElseBlock.STATEMENT_ID:
			case ForLoop.STATEMENT_ID:
			case WhileLoop.STATEMENT_ID:
				return R.color.blockStatementColor;
			case LineComment.STATEMENT_ID:
			case BlockComment.STATEMENT_ID:
				return R.color.commentColor;
			default:
				return R.color.simpleStatementColor;
		}
	}

	/**
	 * Adds translations.
	 */

	private void addTranslations() {
		this.putTranslation(AlgorithmRootBlock.class, (TranslationFunction<AlgorithmRootBlock>) statement -> {
			final Algorithm algorithm = adapter.getAlgorithm();
			return getString(R.string.main_title, algorithm.getTitle(), algorithm.getAuthor());
		});
		this.putTranslation(VariablesBlock.class, (TranslationFunction<VariablesBlock>) statement -> getString(R.string.statement_root_variables));
		this.putTranslation(BeginningBlock.class, (TranslationFunction<BeginningBlock>) statement -> getString(R.string.statement_root_beginning));
		this.putTranslation(EndBlock.class, (TranslationFunction<EndBlock>) statement -> getString(R.string.statement_root_end));

		this.putTranslation(CreateVariableStatement.class, (TranslationFunction<CreateVariableStatement>) statement -> getString(R.string.statement_simple_createVariableStatement, statement.getIdentifier(), getString(statement.getType() == VariableType.NUMBER ? R.string.statement_simple_createVariableStatement_number : R.string.statement_simple_createVariableStatement_string)));
		this.putTranslation(AssignStatement.class, (TranslationFunction<AssignStatement>) statement -> getString(R.string.statement_simple_assignStatement, statement.getIdentifier(), Utils.escapeHTML(statement.getValue().toLanguage(this))));
		this.putTranslation(PromptStatement.class, (TranslationFunction<PromptStatement>) statement -> {
			String message = getString(R.string.statement_simple_promptStatement, statement.getIdentifier());
			if(statement.getMessage() != null) {
				message += " " + getString(R.string.statement_simple_optionalString, Utils.escapeHTML(statement.getMessage()));
			}

			return message;
		});
		this.putTranslation(PrintVariableStatement.class, (TranslationFunction<PrintVariableStatement>) statement -> {
			String message = getString(R.string.statement_simple_printVariableStatement, statement.getIdentifier());
			if(statement.getMessage() != null) {
				message += " " + getString(R.string.statement_simple_optionalString, Utils.escapeHTML(statement.getMessage()));
			}

			if(!statement.shouldLineBreak()) {
				message += " " + getString(R.string.statement_simple_noLineBreak);
			}

			return message;
		});
		this.putTranslation(PrintStatement.class, (TranslationFunction<PrintStatement>) statement -> {
			String content = getString(R.string.statement_simple_printStatement, Utils.escapeHTML(statement.getMessage()));
			if(!statement.shouldLineBreak()) {
				content += " " + getString(R.string.statement_simple_noLineBreak);
			}

			return content;
		});
		this.putTranslation(IfBlock.class, (TranslationFunction<IfBlock>) statement -> getString(R.string.statement_block_ifBlock, Utils.escapeHTML(statement.getCondition().toLanguage(this))));
		this.putTranslation(ElseBlock.class, (TranslationFunction<ElseBlock>) statement -> getString(R.string.statement_block_elseBlock));
		this.putTranslation(ForLoop.class, (TranslationFunction<ForLoop>) statement -> getString(R.string.statement_loop_forLoop, statement.getIdentifier(), Utils.escapeHTML(statement.getStart().toLanguage(this)), Utils.escapeHTML(statement.getEnd().toLanguage(this))));
		this.putTranslation(WhileLoop.class, (TranslationFunction<WhileLoop>) statement -> getString(R.string.statement_loop_whileLoop, Utils.escapeHTML(statement.getCondition().toLanguage(this))));
		this.putTranslation(LineComment.class, (TranslationFunction<LineComment>) statement -> getString(R.string.statement_comment_lineComment, Utils.escapeHTML(statement.getContent())));
		this.putTranslation(BlockComment.class, (TranslationFunction<BlockComment>) statement -> getString(R.string.statement_comment_blockComment, Utils.escapeHTML(statement.getContent()).replace(System.getProperty("line.separator"), "<br>")));
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
	 * Returns the HTML content of a statement.
	 *
	 * @param key Language key.
	 * @param arguments Formatting arguments.
	 *
	 * The HTML content of a statement.
	 */

	private String getString(final int key, final Object... arguments) {
		return adapter.getActivity().getString(key, arguments);
	}

}
