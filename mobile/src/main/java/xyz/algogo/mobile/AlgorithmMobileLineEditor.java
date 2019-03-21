package xyz.algogo.mobile;

import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.appcompat.app.AlertDialog;
import de.mateware.snacky.Snacky;
import xyz.algogo.core.evaluator.expression.Expression;
import xyz.algogo.core.evaluator.variable.VariableType;
import xyz.algogo.core.exception.ParseException;
import xyz.algogo.core.language.AlgogoLanguage;
import xyz.algogo.core.statement.Statement;
import xyz.algogo.core.statement.block.conditional.ElseBlock;
import xyz.algogo.core.statement.block.conditional.IfBlock;
import xyz.algogo.core.statement.block.loop.ForLoop;
import xyz.algogo.core.statement.block.loop.WhileLoop;
import xyz.algogo.core.statement.block.root.AlgorithmRootBlock;
import xyz.algogo.core.statement.block.root.BeginningBlock;
import xyz.algogo.core.statement.block.root.EndBlock;
import xyz.algogo.core.statement.block.root.VariablesBlock;
import xyz.algogo.core.statement.simple.SimpleStatement;
import xyz.algogo.core.statement.simple.comment.BlockComment;
import xyz.algogo.core.statement.simple.comment.LineComment;
import xyz.algogo.core.statement.simple.io.PrintStatement;
import xyz.algogo.core.statement.simple.io.PrintVariableStatement;
import xyz.algogo.core.statement.simple.io.PromptStatement;
import xyz.algogo.core.statement.simple.variable.AssignStatement;
import xyz.algogo.core.statement.simple.variable.CreateVariableStatement;
import xyz.algogo.mobile.activity.MainActivity;
import xyz.algogo.mobile.utils.Utils;

/**
 * This class contains a set of methods that allow to visually edit statements.
 */

public class AlgorithmMobileLineEditor implements Statement.StatementTypeInterface {

	/**
	 * The listener.
	 */

	private Listener listener;

	/**
	 * The activity.
	 */

	private MainActivity activity;

	/**
	 * Creates a new algorithm mobile line editor.
	 *
	 * @param listener The listener.
	 * @param activity The activity.
	 */

	public AlgorithmMobileLineEditor(final Listener listener, final MainActivity activity) {
		this.listener = listener;
		this.activity = activity;
	}

	@Override
	public final void isAlgorithmRootBlock(final AlgorithmRootBlock statement) {
		showNotEditableMessage();
	}

	@Override
	public final void isVariablesBlock(final VariablesBlock statement) {
		showNotEditableMessage();
	}

	@Override
	public final void isBeginningBlock(final BeginningBlock statement) {
		showNotEditableMessage();
	}

	@Override
	public final void isEndBlock(final EndBlock statement) {
		showNotEditableMessage();
	}

	/**
	 * Opens an editing dialog for a create variable statement.
	 *
	 * @param statement The create variable statement.
	 */

	@Override
	public final void isCreateVariableStatement(CreateVariableStatement statement) {
		if(statement == null) {
			statement = new CreateVariableStatement(null, null);
		}

		final EditText identifierEditText = new EditText(activity);
		identifierEditText.setText(statement.getIdentifier());

		final Spinner typeSpinner = new Spinner(activity, Spinner.MODE_DROPDOWN);

		final String[] items = new String[]{activity.getString(R.string.lineEditor_createVariableStatement_number), activity.getString(R.string.lineEditor_createVariableStatement_string)};
		typeSpinner.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, items));

		if(statement.getType() == VariableType.STRING) {
			typeSpinner.setSelection(1);
		}

		isCreateVariableStatement(statement, statement.getIdentifier(), identifierEditText, typeSpinner);
	}

	/**
	 * Opens an editing dialog for a create variable statement.
	 *
	 * @param statement The create variable statement.
	 * @param identifierException Identifier check exception.
	 * @param identifierEditText Identifier edit text.
	 * @param typeSpinner Type spinner.
	 */

	private void isCreateVariableStatement(final CreateVariableStatement statement, final String identifierException, final EditText identifierEditText, final Spinner typeSpinner) {
		createDialog(R.string.lineEditor_createVariableStatement_title, (dialog, selected) -> {
			final CreateVariableStatement copy = new CreateVariableStatement(identifierEditText.getText().toString(), typeSpinner.getSelectedItemPosition() == 0 ? VariableType.NUMBER : VariableType.STRING);
			if(!validate(copy)) {
				isCreateVariableStatement(statement, identifierException, identifierEditText, typeSpinner);
				return;
			}

			final ArrayList<String> variables = new ArrayList<>(Arrays.asList(activity.getAlgorithmAdapter().buildVariablesList()));
			if(identifierException != null) {
				variables.remove(identifierException);
			}

			if(variables.contains(copy.getIdentifier())) {
				Snacky.builder().setActivity(activity).setText(R.string.lineEditor_validationError_identifierAlreadyExists).error().show();
				isCreateVariableStatement(statement, identifierException, identifierEditText, typeSpinner);
				return;
			}

			statement.setIdentifier(copy.getIdentifier());
			statement.setType(copy.getType());

			listener.statementEdited(statement);
		}, R.string.lineEditor_global_variableIdentifier, identifierEditText, R.string.lineEditor_createVariableStatement_type, typeSpinner);
	}

	/**
	 * Opens an editing dialog for an assign statement.
	 *
	 * @param statement The assign statement.
	 */

	@Override
	public final void isAssignStatement(AssignStatement statement) {
		if(statement == null) {
			statement = new AssignStatement(null, null);
		}

		final Spinner identifierSpinner = createVariablesSpinner();
		if(statement.getIdentifier() != null) {
			selectElement(identifierSpinner, statement.getIdentifier());
		}

		final EditText valueEditText = new EditText(activity);
		valueEditText.setText(statement.getValue() == null ? null : statement.getValue().toLanguage(new AlgogoLanguage()));

		isAssignStatement(statement, identifierSpinner, valueEditText);
	}

	/**
	 * Opens an editing dialog for an assign statement.
	 *
	 * @param statement The assign statement.
	 * @param identifierSpinner Identifier spinner.
	 * @param valueEditText Value edit text.
	 */

	private void isAssignStatement(final AssignStatement statement, final Spinner identifierSpinner, final EditText valueEditText) {
		createDialog(R.string.lineEditor_assignValueToVariable_title, (dialog, selected) -> {
			final AssignStatement copy = new AssignStatement((String)identifierSpinner.getSelectedItem(), parseExpression(valueEditText.getText().toString()));
			if(copy.getValue() == null || !validate(copy)) {
				isAssignStatement(statement, identifierSpinner, valueEditText);
				return;
			}

			statement.setIdentifier(copy.getIdentifier());
			statement.setValue(copy.getValue());

			listener.statementEdited(statement);
		}, R.string.lineEditor_global_variableIdentifier, identifierSpinner, R.string.lineEditor_assignValueToVariable_value, valueEditText);
	}

	/**
	 * Opens an editing dialog for a prompt statement.
	 *
	 * @param statement The prompt statement.
	 */

	@Override
	public final void isPromptStatement(PromptStatement statement) {
		if(statement == null) {
			statement = new PromptStatement(null, null);
		}

		final Spinner identifierSpinner = createVariablesSpinner();
		if(statement.getIdentifier() != null) {
			selectElement(identifierSpinner, statement.getIdentifier());
		}

		final EditText messageEditText = new EditText(activity);
		messageEditText.setText(statement.getMessage());

		isPromptStatement(statement, identifierSpinner, messageEditText);
	}

	/**
	 * Opens an editing dialog for a prompt statement.
	 *
	 * @param statement The prompt statement.
	 * @param identifierSpinner Identifier spinner.
	 * @param messageEditText Message edit text.
	 */

	private void isPromptStatement(final PromptStatement statement, final Spinner identifierSpinner, final EditText messageEditText) {
		createDialog(R.string.lineEditor_prompt_title, (dialog, selected) -> {
			final PromptStatement copy = new PromptStatement((String)identifierSpinner.getSelectedItem(), messageEditText.getText().length() == 0 ? null : messageEditText.getText().toString());
			if(!validate(copy)) {
				isPromptStatement(statement, identifierSpinner, messageEditText);
				return;
			}

			statement.setIdentifier(copy.getIdentifier());
			statement.setMessage(copy.getMessage());

			listener.statementEdited(statement);
		}, R.string.lineEditor_global_variableIdentifier, identifierSpinner, R.string.lineEditor_prompt_message, messageEditText);
	}

	/**
	 * Opens an editing dialog for a print variable statement.
	 *
	 * @param statement The print variable statement.
	 */

	@Override
	public final void isPrintVariableStatement(PrintVariableStatement statement) {
		if(statement == null) {
			statement = new PrintVariableStatement(null, null, true);
		}

		final Spinner identifierSpinner = createVariablesSpinner();
		if(statement.getIdentifier() != null) {
			selectElement(identifierSpinner, statement.getIdentifier());
		}

		final EditText messageEditText = new EditText(activity);
		messageEditText.setText(statement.getMessage());

		final CheckBox lineBreakCheckBox = new CheckBox(activity);
		lineBreakCheckBox.setText(R.string.lineEditor_global_lineBreak);
		lineBreakCheckBox.setChecked(statement.shouldLineBreak());

		isPrintVariableStatement(statement, identifierSpinner, messageEditText, lineBreakCheckBox);
	}

	/**
	 * Opens an editing dialog for a print variable statement.
	 *
	 * @param statement The print variable statement.
	 * @param identifierSpinner Identifier spinner.
	 * @param messageEditText Message edit text.
	 * @param lineBreakCheckBox Line break check box.
	 */

	private void isPrintVariableStatement(final PrintVariableStatement statement, final Spinner identifierSpinner, final EditText messageEditText, final CheckBox lineBreakCheckBox) {
		createDialog(R.string.lineEditor_printVariable_title, (dialog, selected) -> {
			final PrintVariableStatement copy = new PrintVariableStatement((String)identifierSpinner.getSelectedItem(), messageEditText.getText().length() == 0 ? null : messageEditText.getText().toString(), lineBreakCheckBox.isChecked());
			if(!validate(copy)) {
				isPrintVariableStatement(statement, identifierSpinner, messageEditText, lineBreakCheckBox);
				return;
			}

			statement.setIdentifier(copy.getIdentifier());
			statement.setMessage(copy.getMessage());
			statement.setShouldLineBreak(copy.shouldLineBreak());

			listener.statementEdited(statement);
		}, R.string.lineEditor_global_variableIdentifier, identifierSpinner, R.string.lineEditor_printVariable_message, messageEditText, lineBreakCheckBox);
	}

	/**
	 * Opens an editing dialog for a print statement.
	 *
	 * @param statement The print statement.
	 */

	@Override
	public final void isPrintStatement(PrintStatement statement) {
		if(statement == null) {
			statement = new PrintStatement(null, true);
		}

		final EditText messageEditText = new EditText(activity);
		messageEditText.setText(statement.getMessage());

		final CheckBox lineBreakCheckBox = new CheckBox(activity);
		lineBreakCheckBox.setText(R.string.lineEditor_global_lineBreak);
		lineBreakCheckBox.setChecked(statement.shouldLineBreak());

		isPrintStatement(statement, messageEditText, lineBreakCheckBox);
	}

	/**
	 * Opens an editing dialog for a print statement.
	 *
	 * @param statement The print statement.
	 * @param lineBreakCheckBox Line break check box.
	 * @param messageEditText Message edit text.
	 */

	private void isPrintStatement(final PrintStatement statement, final EditText messageEditText, final CheckBox lineBreakCheckBox) {
		createDialog(R.string.lineEditor_print_title, (dialog, selected) -> {
			final PrintStatement copy = new PrintStatement(messageEditText.getText().toString(), lineBreakCheckBox.isChecked());
			if(!validate(copy)) {
				isPrintStatement(statement, messageEditText, lineBreakCheckBox);
				return;
			}

			statement.setMessage(copy.getMessage());
			statement.setShouldLineBreak(copy.shouldLineBreak());

			listener.statementEdited(statement);
		}, R.string.lineEditor_print_message, messageEditText, lineBreakCheckBox);
	}

	/**
	 * Opens an editing dialog for an IF block.
	 *
	 * @param statement The IF block.
	 */

	@Override
	public final void isIfBlock(IfBlock statement) {
		if(statement == null) {
			statement = new IfBlock(null);
		}

		final EditText conditionEditText = new EditText(activity);
		conditionEditText.setText(statement.getCondition() == null ? null : statement.getCondition().toLanguage(new AlgogoLanguage()));

		final CheckBox addElseCheckBox = new CheckBox(activity);
		addElseCheckBox.setText(R.string.lineEditor_ifElse_addElse);
		addElseCheckBox.setChecked(statement.hasElseBlock());

		isIfBlock(statement, conditionEditText, addElseCheckBox);
	}

	/**
	 * Opens an editing dialog for an IF block.
	 *
	 * @param statement The IF block.
	 * @param addElseCheckBox Add ELSE check box.
	 * @param conditionEditText Condition edit text.
	 */

	private void isIfBlock(final IfBlock statement, final EditText conditionEditText, final CheckBox addElseCheckBox) {
		createDialog(R.string.lineEditor_ifElse_title, (dialog, position) -> {
			final Expression condition = parseExpression(conditionEditText.getText().toString());
			if(condition == null) {
				isIfBlock(statement);
				return;
			}

			if(addElseCheckBox.isChecked() && !statement.hasElseBlock()) {
				statement.setElseBlock(new ElseBlock());
			}

			if(!addElseCheckBox.isChecked() && statement.hasElseBlock()) {
				statement.setElseBlock(null);
			}

			statement.setCondition(condition);

			listener.statementEdited(statement);
		}, R.string.lineEditor_global_condition, conditionEditText, addElseCheckBox);
	}

	@Override
	public final void isElseBlock(final ElseBlock statement) {
		showNotEditableMessage();
	}

	/**
	 * Opens an editing dialog for a FOR loop.
	 *
	 * @param statement The FOR loop.
	 */

	@Override
	public final void isForLoop(ForLoop statement) {
		final AlgogoLanguage language = new AlgogoLanguage();
		if(statement == null) {
			statement = new ForLoop(null, null, null);
		}

		final Spinner identifierSpinner = createVariablesSpinner();
		if(statement.getIdentifier() != null) {
			selectElement(identifierSpinner, statement.getIdentifier());
		}

		final EditText startEditText = new EditText(activity);
		startEditText.setText(statement.getStart() == null ? null : statement.getStart().toLanguage(language));

		final EditText endEditText = new EditText(activity);
		endEditText.setText(statement.getEnd() == null ? null : statement.getEnd().toLanguage(language));

		isForLoop(statement, identifierSpinner, startEditText, endEditText);
	}

	/**
	 * Opens an editing dialog for a FOR loop.
	 *
	 * @param statement The FOR loop.
	 * @param identifierSpinner Identifier spinner.
	 * @param startEditText Start edit text.
	 * @param endEditText End edit text.
	 */

	private void isForLoop(final ForLoop statement, final Spinner identifierSpinner, final EditText startEditText, final EditText endEditText) {
		createDialog(R.string.lineEditor_for_title, (dialog, selected) -> {
			final Expression start = parseExpression(startEditText.getText().toString());
			final Expression end = parseExpression(endEditText.getText().toString());
			if(start == null || end == null) {
				isForLoop(statement, identifierSpinner, startEditText, endEditText);
				return;
			}

			statement.setIdentifier((String)identifierSpinner.getSelectedItem());
			statement.setStart(start);
			statement.setEnd(end);

			listener.statementEdited(statement);
		}, R.string.lineEditor_global_condition, R.string.lineEditor_global_variableIdentifier, identifierSpinner, R.string.lineEditor_for_startValue, startEditText, R.string.lineEditor_for_endValue, endEditText);
	}

	/**
	 * Opens an editing dialog for a WHILE loop.
	 *
	 * @param statement The WHILE loop.
	 */

	@Override
	public final void isWhileLoop(WhileLoop statement) {
		if(statement == null) {
			statement = new WhileLoop(null);
		}

		final EditText conditionEditText = new EditText(activity);
		conditionEditText.setText(statement.getCondition() == null ? null : statement.getCondition().toLanguage(new AlgogoLanguage()));

		isWhileLoop(statement, conditionEditText);
	}

	/**
	 * Opens an editing dialog for a WHILE loop.
	 *
	 * @param statement The WHILE loop.
	 * @param conditionEditText Condition edit text.
	 */

	private void isWhileLoop(final WhileLoop statement, final EditText conditionEditText) {
		createDialog(R.string.lineEditor_while_title, (dialog, selected) -> {
			final Expression condition = parseExpression(conditionEditText.getText().toString());
			if(condition == null) {
				isWhileLoop(statement, conditionEditText);
				return;
			}

			statement.setCondition(condition);

			listener.statementEdited(statement);
		}, R.string.lineEditor_global_condition, conditionEditText);
	}

	/**
	 * Opens an editing dialog for a line comment.
	 *
	 * @param statement The line comment.
	 */

	@Override
	public final void isLineComment(LineComment statement) {
		if(statement == null) {
			statement = new LineComment(null);
		}

		final EditText contentEditText = new EditText(activity);
		contentEditText.setText(statement.getContent());

		isLineComment(statement, contentEditText);
	}

	/**
	 * Opens an editing dialog for a line comment.
	 *
	 * @param statement The line comment.
	 */

	private void isLineComment(final LineComment statement, final EditText contentEditText) {
		createDialog(R.string.lineEditor_lineComment_title, (dialog, selected) -> {
			final LineComment copy = new LineComment(contentEditText.getText().toString());
			if(!validate(copy)) {
				isLineComment(statement, contentEditText);
				return;
			}

			statement.setContent(contentEditText.getText().toString());

			listener.statementEdited(statement);
		}, R.string.lineEditor_global_content, contentEditText);
	}

	/**
	 * Opens an editing dialog for a block comment.
	 *
	 * @param statement The block comment.
	 */

	@Override
	public final void isBlockComment(BlockComment statement) {
		if(statement == null) {
			statement = new BlockComment(null);
		}

		final EditText contentEditText = new EditText(activity);
		contentEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		contentEditText.setSingleLine(false);
		contentEditText.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
		contentEditText.setText(statement.getContent());

		isBlockComment(statement, contentEditText);
	}

	/**
	 * Opens an editing dialog for a block comment.
	 *
	 * @param statement The block comment.
	 * @param contentEditText Content edit text.
	 */

	private void isBlockComment(final BlockComment statement, final EditText contentEditText) {
		createDialog(R.string.lineEditor_blockComment_title, (dialog, selected) -> {
			final BlockComment copy = new BlockComment(contentEditText.getText().toString());
			if(!validate(copy)) {
				isBlockComment(statement, contentEditText);
				return;
			}

			statement.setContent(contentEditText.getText().toString());

			listener.statementEdited(statement);
		}, R.string.lineEditor_global_content, contentEditText);
	}

	@Override
	public final void isUnknownStatement(final Statement statement) {
		showNotEditableMessage();
	}

	/**
	 * Returns the listener.
	 *
	 * @return The listener.
	 */

	public final Listener getListener() {
		return listener;
	}

	/**
	 * Sets the listener.
	 *
	 * @param listener The listener.
	 */

	public final void setListener(final Listener listener) {
		this.listener = listener;
	}

	/**
	 * Returns the activity.
	 *
	 * @return The activity.
	 */

	public final MainActivity getActivity() {
		return activity;
	}

	/**
	 * Sets the activity.
	 *
	 * @param activity The activity.
	 */

	public final void setActivity(final MainActivity activity) {
		this.activity = activity;
	}

	/**
	 * Parses an expression.
	 *
	 * @param expression The expression to parse.
	 *
	 * @return The parsed expression.
	 */

	private Expression parseExpression(final String expression) {
		try {
			return Expression.parse(expression);
		}
		catch(final ParseException ex) {
			Snacky.builder().setActivity(activity).setText(R.string.lineEditor_expressionError_message).error().show();
		}

		return null;
	}

	/**
	 * Validates an edited statement or shows a dialog.
	 *
	 * @param statement The statement.
	 *
	 * @return Whether the statement has been edited successfully.
	 */

	private boolean validate(final SimpleStatement statement) {
		final Exception exception = statement.validate();
		if(exception == null) {
			return true;
		}

		Snacky.builder().setActivity(activity).setText(R.string.lineEditor_validationError_message).error().show();
		return false;
	}

	/**
	 * Creates and shows a dialog.
	 *
	 * @param title Dialog's title.
	 * @param positiveButtonListener Method to run whenever the user has clicked on OK.
	 * @param message Dialog's views.
	 */

	private void createDialog(final int title, final DialogInterface.OnClickListener positiveButtonListener, final Object... message) {
		final int top = Utils.pixelsToDp(activity, 10);
		final int left = Utils.pixelsToDp(activity, 25);
		final LinearLayout layout = new LinearLayout(activity);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setPadding(left, top, left, top);

		for(Object view : message) {
			final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

			if(view instanceof Integer) {
				final TextView textView = new TextView(activity);
				textView.setText((Integer)view);
				layout.addView(textView, params);
				continue;
			}

			layout.addView((View)view, params);
		}

		new AlertDialog.Builder(activity)
				.setTitle(title)
				.setPositiveButton(android.R.string.ok, (dialog, selected) -> {
					layout.removeAllViews();
					dialog.dismiss();
					positiveButtonListener.onClick(dialog, selected);
				})
				.setNegativeButton(android.R.string.cancel, null)
				.setView(layout)
				.create().show();
	}

	/**
	 * Creates a spinner that contains all variables.
	 *
	 * @return A spinner that contains all variables.
	 */
	
	private Spinner createVariablesSpinner() {
		final Spinner spinner = new Spinner(activity, Spinner.MODE_DROPDOWN);
		spinner.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, activity.getAlgorithmAdapter().buildVariablesList()));
		
		return spinner;
	}

	/**
	 * Selects an element in a spinner.
	 *
	 * @param spinner The spinner.
	 * @param item The element.
	 */
	
	private void selectElement(final Spinner spinner, final Object item) {
		final ArrayAdapter adapter = (ArrayAdapter)spinner.getAdapter();
		spinner.setSelection(adapter.getPosition(item));
	}

	/**
	 * Displayed a message saying that the current statement could not be edited.
	 */

	private void showNotEditableMessage() {
		Snacky.builder().setActivity(activity).setText(R.string.snackbar_error_notEditable).error().show();
	}

	/**
	 * Listener interface.
	 */

	public interface Listener {

		/**
		 * Triggered when a statement is edited.
		 *
		 * @param statement The statement.
		 */

		void statementEdited(final Statement statement);

	}

}