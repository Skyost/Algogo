package xyz.algogo.desktop;

import xyz.algogo.core.evaluator.expression.Expression;
import xyz.algogo.core.evaluator.variable.VariableType;
import xyz.algogo.core.exception.ParseException;
import xyz.algogo.core.language.AlgogoLanguage;
import xyz.algogo.core.statement.block.conditional.ElseBlock;
import xyz.algogo.core.statement.block.conditional.IfBlock;
import xyz.algogo.core.statement.block.loop.ForLoop;
import xyz.algogo.core.statement.block.loop.WhileLoop;
import xyz.algogo.core.statement.simple.SimpleStatement;
import xyz.algogo.core.statement.simple.comment.BlockComment;
import xyz.algogo.core.statement.simple.comment.LineComment;
import xyz.algogo.core.statement.simple.io.PrintStatement;
import xyz.algogo.core.statement.simple.io.PrintVariableStatement;
import xyz.algogo.core.statement.simple.io.PromptStatement;
import xyz.algogo.core.statement.simple.variable.AssignStatement;
import xyz.algogo.core.statement.simple.variable.CreateVariableStatement;
import xyz.algogo.desktop.dialog.PickerDialog;
import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.utils.ComponentBorder;
import xyz.algogo.desktop.utils.Utils;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class contains a set of methods that allow to visually edit statements.
 */

public class AlgorithmDesktopLineEditor {

	/**
	 * The dialogs parent component.
	 */

	private Component parentUI;

	/**
	 * The editor.
	 */
	
	private final EditorFrame editor;

	/**
	 * Creates a new algorithm desktop line editor.
	 * 
	 * @param parentUI The dialogs parent component.
	 * @param editor The editor.
	 */

	public AlgorithmDesktopLineEditor(final Component parentUI, final EditorFrame editor) {
		this.parentUI = parentUI;
		this.editor = editor;
	}

	/**
	 * Returns the dialogs parent component.
	 * 
	 * @return The dialogs parent component.
	 */

	public Component getParentUI() {
		return parentUI;
	}

	/**
	 * Sets the dialogs parent component.
	 * 
	 * @param parentUI The dialogs parent component.
	 */

	public void setParentUI(final Component parentUI) {
		this.parentUI = parentUI;
	}

	/**
	 * Returns the editor.
	 * 
	 * @return The editor.
	 */

	public EditorFrame getEditor() {
		return editor;
	}

	/**
	 * Opens an editing dialog for a create variable statement.
	 *
	 * @param statement The create variable statement.
	 *
	 * @return The edited statement.
	 */

	public CreateVariableStatement createVariableStatement(final CreateVariableStatement statement) {
		return createVariableStatement(statement, statement == null ? null : statement.getIdentifier(), null, null);
	}

	/**
	 * Opens an editing dialog for a create variable statement.
	 * 
	 * @param statement The create variable statement.
	 * @param checkException Identifier check exception.
	 * @param identifierTextField Identifier text field.
	 * @param typeComboBox Type combo box.
	 *
	 * @return The edited statement.
	 */

	private CreateVariableStatement createVariableStatement(CreateVariableStatement statement, String checkException, JTextField identifierTextField, JComboBox<String> typeComboBox) {
		final AppLanguage language = editor.getAppLanguage();
		if(statement == null) {
			statement = new CreateVariableStatement(null, null);
		}

		if(identifierTextField == null) {
			identifierTextField = new JTextField(statement.getIdentifier());
		}

		if(typeComboBox == null) {
			typeComboBox = new JComboBox<>(new String[]{language.getString("lineEditor.createVariableStatement.number"), language.getString("lineEditor.createVariableStatement.string")});
			if(statement.getType() == VariableType.STRING) {
				typeComboBox.setSelectedIndex(1);
			}
		}

		if(!Utils.okCancelDialog(language, parentUI, "lineEditor.createVariableStatement.title", "lineEditor.global.variableIdentifier", identifierTextField, "lineEditor.createVariableStatement.type", typeComboBox)) {
			return null;
		}

		final CreateVariableStatement copy = new CreateVariableStatement(identifierTextField.getText(), typeComboBox.getSelectedIndex() == 0 ? VariableType.NUMBER : VariableType.STRING);
		if(!validate(copy)) {
			return createVariableStatement(statement, checkException, identifierTextField, typeComboBox);
		}

		final ArrayList<String> variables = new ArrayList<>(Arrays.asList(editor.getMainPane().getAlgorithmTree().getModel().buildVariablesList()));
		if(checkException != null) {
			variables.remove(checkException);
		}

		if(variables.contains(copy.getIdentifier())) {
			JOptionPane.showMessageDialog(parentUI, language.getString("lineEditor.validationError.identifierAlreadyExists"), language.getString("lineEditor.validationError.title"), JOptionPane.ERROR_MESSAGE);
			return createVariableStatement(statement, checkException, identifierTextField, typeComboBox);
		}

		statement.setIdentifier(copy.getIdentifier());
		statement.setType(copy.getType());
		return statement;
	}

	/**
	 * Opens an editing dialog for an assign statement.
	 *
	 * @param statement The assign statement.
	 *
	 * @return The edited statement.
	 */

	public AssignStatement assignStatement(final AssignStatement statement) {
		return assignStatement(statement, null, null);
	}

	/**
	 * Opens an editing dialog for an assign statement.
	 *
	 * @param statement The assign statement.
	 * @param identifierComboBox Identifier combo box.
	 * @param valueTextField Value text field.
	 *
	 * @return The edited statement.
	 */

	private AssignStatement assignStatement(AssignStatement statement, JComboBox<String> identifierComboBox, JTextField valueTextField) {
		if(statement == null) {
			statement = new AssignStatement(null, null);
		}

		if(identifierComboBox == null) {
			identifierComboBox = new JComboBox<>(editor.getMainPane().getAlgorithmTreeModel().buildVariablesList());
			if(statement.getIdentifier() != null) {
				selectElement(identifierComboBox, statement.getIdentifier());
			}
		}

		if(valueTextField == null) {
			valueTextField = new JTextField(statement.getValue() == null ? null : statement.getValue().toLanguage(new AlgogoLanguage()));
			setFunctionButtonBorder(valueTextField);
		}

		if(!Utils.okCancelDialog(editor, parentUI, "lineEditor.assignValueToVariable.title", "lineEditor.global.variableIdentifier", identifierComboBox, "lineEditor.assignValueToVariable.value", valueTextField)) {
			return null;
		}

		final AssignStatement copy = new AssignStatement((String)identifierComboBox.getSelectedItem(), parseExpression(valueTextField.getText()));
		if(copy.getValue() == null || !validate(copy)) {
			return assignStatement(statement, identifierComboBox, valueTextField);
		}

		statement.setIdentifier(copy.getIdentifier());
		statement.setValue(copy.getValue());
		return statement;
	}

	/**
	 * Opens an editing dialog for a prompt statement.
	 *
	 * @param statement The prompt statement.
	 *
	 * @return The edited statement.
	 */

	public PromptStatement promptStatement(final PromptStatement statement) {
		return promptStatement(statement, null, null);
	}

	/**
	 * Opens an editing dialog for a prompt statement.
	 *
	 * @param statement The prompt statement.
	 * @param identifierComboBox Identifier combo box.
	 * @param messageTextField Message text field.
	 *
	 * @return The edited statement.
	 */

	private PromptStatement promptStatement(PromptStatement statement, JComboBox<String> identifierComboBox, JTextField messageTextField) {
		if(statement == null) {
			statement = new PromptStatement(null, null);
		}

		if(identifierComboBox == null) {
			identifierComboBox = new JComboBox<>(editor.getMainPane().getAlgorithmTreeModel().buildVariablesList());
			if(statement.getIdentifier() != null) {
				selectElement(identifierComboBox, statement.getIdentifier());
			}
		}

		if(messageTextField == null) {
			messageTextField = new JTextField(statement.getMessage());
		}

		if(!Utils.okCancelDialog(editor, parentUI, "lineEditor.prompt.title", "lineEditor.global.variableIdentifier", identifierComboBox, "lineEditor.prompt.message", messageTextField)) {
			return null;
		}

		final PromptStatement copy = new PromptStatement((String)identifierComboBox.getSelectedItem(), messageTextField.getText().isEmpty() ? null : messageTextField.getText());
		if(!validate(copy)) {
			return promptStatement(statement, identifierComboBox, messageTextField);
		}

		statement.setIdentifier(copy.getIdentifier());
		statement.setMessage(copy.getMessage());
		return statement;
	}

	/**
	 * Opens an editing dialog for a print variable statement.
	 *
	 * @param statement The print variable statement.
	 *
	 * @return The edited statement.
	 */

	public PrintVariableStatement printVariableStatement(final PrintVariableStatement statement) {
		return printVariableStatement(statement, null, null, null);
	}

	/**
	 * Opens an editing dialog for a print variable statement.
	 *
	 * @param statement The print variable statement.
	 * @param identifierComboBox Identifier combo box.
	 * @param messageTextField Message text field.
	 * @param lineBreakCheckBox Line break check box.
	 *
	 * @return The edited statement.
	 */

	private PrintVariableStatement printVariableStatement(PrintVariableStatement statement, JComboBox<String> identifierComboBox, JTextField messageTextField, JCheckBox lineBreakCheckBox) {
		if(statement == null) {
			statement = new PrintVariableStatement(null, null, true);
		}

		if(identifierComboBox == null) {
			identifierComboBox = new JComboBox<>(editor.getMainPane().getAlgorithmTreeModel().buildVariablesList());
			if(statement.getIdentifier() != null) {
				selectElement(identifierComboBox, statement.getIdentifier());
			}
		}

		if(messageTextField == null) {
			messageTextField = new JTextField(statement.getMessage());
		}

		if(lineBreakCheckBox == null) {
			lineBreakCheckBox = new JCheckBox(editor.getAppLanguage().getString("lineEditor.global.lineBreak"));
			lineBreakCheckBox.setSelected(statement.shouldLineBreak());
		}

		if(!Utils.okCancelDialog(editor, parentUI, "lineEditor.printVariable.title", "lineEditor.global.variableIdentifier", identifierComboBox, "lineEditor.printVariable.message", messageTextField, lineBreakCheckBox)) {
			return null;
		}

		final PrintVariableStatement copy = new PrintVariableStatement((String)identifierComboBox.getSelectedItem(), messageTextField.getText().isEmpty() ? null : messageTextField.getText(), lineBreakCheckBox.isSelected());
		if(!validate(copy)) {
			return printVariableStatement(statement, identifierComboBox, messageTextField, lineBreakCheckBox);
		}

		statement.setIdentifier(copy.getIdentifier());
		statement.setMessage(copy.getMessage());
		statement.setShouldLineBreak(copy.shouldLineBreak());
		return statement;
	}

	/**
	 * Opens an editing dialog for a print statement.
	 *
	 * @param statement The print statement.
	 *
	 * @return The edited statement.
	 */

	public PrintStatement printStatement(final PrintStatement statement) {
		return printStatement(statement, null, null);
	}

	/**
	 * Opens an editing dialog for a print statement.
	 *
	 * @param statement The print statement.
	 * @param messageTextField Message text field.
	 * @param lineBreakCheckBox Line break check box.
	 *
	 * @return The edited statement.
	 */

	private PrintStatement printStatement(PrintStatement statement, JTextField messageTextField, JCheckBox lineBreakCheckBox) {
		if(statement == null) {
			statement = new PrintStatement(null, true);
		}

		if(messageTextField == null) {
			messageTextField = new JTextField(statement.getMessage());
		}

		if(lineBreakCheckBox == null) {
			lineBreakCheckBox = new JCheckBox(editor.getAppLanguage().getString("lineEditor.global.lineBreak"));
			lineBreakCheckBox.setSelected(statement.shouldLineBreak());
		}

		if(!Utils.okCancelDialog(editor, parentUI, "lineEditor.print.title", "lineEditor.print.message", messageTextField, lineBreakCheckBox)) {
			return null;
		}

		final PrintStatement copy = new PrintStatement(messageTextField.getText(), lineBreakCheckBox.isSelected());
		if(!validate(copy)) {
			return printStatement(statement, messageTextField, lineBreakCheckBox);
		}

		statement.setMessage(copy.getMessage());
		statement.setShouldLineBreak(copy.shouldLineBreak());
		return statement;
	}

	/**
	 * Opens an editing dialog for an IF block.
	 *
	 * @param statement The IF block.
	 *
	 * @return The edited statement.
	 */

	public IfBlock ifElseBlock(final IfBlock statement) {
		return ifElseBlock(statement, null, null);
	}

	/**
	 * Opens an editing dialog for an IF block.
	 *
	 * @param statement The IF block.
	 * @param conditionTextField Condition text field.
	 * @param addElseCheckBox Add ELSE check box.
	 *
	 * @return The edited statement.
	 */

	private IfBlock ifElseBlock(IfBlock statement, JTextField conditionTextField, JCheckBox addElseCheckBox) {
		if(statement == null) {
			statement = new IfBlock(null);
		}

		if(conditionTextField == null) {
			conditionTextField = new JTextField(statement.getCondition() == null ? null : statement.getCondition().toLanguage(new AlgogoLanguage()));
			setFunctionButtonBorder(conditionTextField);
		}

		if(addElseCheckBox == null) {
			addElseCheckBox = new JCheckBox(editor.getAppLanguage().getString("lineEditor.ifElse.addElse"));
			addElseCheckBox.setSelected(statement.hasElseBlock());
		}

		if(!Utils.okCancelDialog(editor, parentUI, "lineEditor.ifElse.title", "lineEditor.global.condition", conditionTextField, addElseCheckBox)) {
			return null;
		}

		final Expression condition = parseExpression(conditionTextField.getText());
		if(condition == null) {
			return ifElseBlock(statement, conditionTextField, addElseCheckBox);
		}

		if(addElseCheckBox.isSelected() && !statement.hasElseBlock()) {
			statement.setElseBlock(new ElseBlock());
		}

		if(!addElseCheckBox.isSelected() && statement.hasElseBlock()) {
			statement.setElseBlock(null);
		}

		statement.setCondition(condition);
		return statement;
	}

	/**
	 * Opens an editing dialog for a FOR loop.
	 *
	 * @param statement The FOR loop.
	 *
	 * @return The edited statement.
	 */

	public ForLoop forLoop(final ForLoop statement) {
		return forLoop(statement, null, null, null);
	}

	/**
	 * Opens an editing dialog for a FOR loop.
	 *
	 * @param statement The FOR loop.
	 * @param identifierComboBox Identifier combo box.
	 * @param startTextField Start text field.
	 * @param endTextField End text field.
	 *
	 * @return The edited statement.
	 */

	private ForLoop forLoop(ForLoop statement, JComboBox<String> identifierComboBox, JTextField startTextField, JTextField endTextField) {
		final AlgogoLanguage language = new AlgogoLanguage();
		if(statement == null) {
			statement = new ForLoop(null, null, null);
		}

		if(identifierComboBox == null) {
			identifierComboBox = new JComboBox<>(editor.getMainPane().getAlgorithmTreeModel().buildVariablesList(VariableType.NUMBER));
			if(statement.getIdentifier() != null) {
				selectElement(identifierComboBox, statement.getIdentifier());
			}
		}

		if(startTextField == null) {
			startTextField = new JTextField(statement.getStart() == null ? null : statement.getStart().toLanguage(language));
			setFunctionButtonBorder(startTextField);
		}

		if(endTextField == null) {
			endTextField = new JTextField(statement.getEnd() == null ? null : statement.getEnd().toLanguage(language));
			setFunctionButtonBorder(endTextField);
		}

		if(!Utils.okCancelDialog(editor, parentUI, "lineEditor.for.title", "lineEditor.global.variableIdentifier", identifierComboBox, "lineEditor.for.startValue", startTextField, "lineEditor.for.endValue", endTextField)) {
			return null;
		}

		final Expression start = parseExpression(startTextField.getText());
		final Expression end = parseExpression(endTextField.getText());
		if(start == null || end == null) {
			return forLoop(statement, identifierComboBox, startTextField, endTextField);
		}

		statement.setIdentifier((String)identifierComboBox.getSelectedItem());
		statement.setStart(start);
		statement.setEnd(end);
		return statement;
	}

	/**
	 * Opens an editing dialog for a WHILE loop.
	 *
	 * @param statement The WHILE loop.
	 *
	 * @return The edited statement.
	 */

	public WhileLoop whileLoop(final WhileLoop statement) {
		return whileLoop(statement, null);
	}

	/**
	 * Opens an editing dialog for a WHILE loop.
	 *
	 * @param statement The WHILE loop.
	 * @param conditionTextField Condition text field.
	 *
	 * @return The edited statement.
	 */

	private WhileLoop whileLoop(WhileLoop statement, JTextField conditionTextField) {
		if(statement == null) {
			statement = new WhileLoop(null);
		}

		if(conditionTextField == null) {
			conditionTextField = new JTextField(statement.getCondition() == null ? null : statement.getCondition().toLanguage(new AlgogoLanguage()));
			setFunctionButtonBorder(conditionTextField);
		}

		if(!Utils.okCancelDialog(editor, parentUI, "lineEditor.while.title", "lineEditor.global.condition", conditionTextField)) {
			return null;
		}

		final Expression condition = parseExpression(conditionTextField.getText());
		if(condition == null) {
			return whileLoop(statement, conditionTextField);
		}

		statement.setCondition(condition);
		return statement;
	}

	/**
	 * Opens an editing dialog for a line comment.
	 *
	 * @param statement The line comment.
	 *
	 * @return The edited statement.
	 */

	public LineComment lineComment(final LineComment statement) {
		return lineComment(statement, null);
	}

	/**
	 * Opens an editing dialog for a line comment.
	 *
	 * @param statement The line comment.
	 * @param contentTextField Content text field.
	 *
	 * @return The edited statement.
	 */

	private LineComment lineComment(LineComment statement, JTextField contentTextField) {
		if(statement == null) {
			statement = new LineComment(null);
		}

		if(contentTextField == null) {
			contentTextField = new JTextField(statement.getContent());
		}

		if(!Utils.okCancelDialog(editor, parentUI, "lineEditor.lineComment.title", "lineEditor.global.content", contentTextField)) {
			return null;
		}

		final LineComment copy = new LineComment(contentTextField.getText());
		if(!validate(copy)) {
			return lineComment(statement, contentTextField);
		}

		statement.setContent(copy.getContent());
		return statement;
	}

	/**
	 * Opens an editing dialog for a block comment.
	 *
	 * @param statement The block comment.
	 *
	 * @return The edited statement.
	 */

	public BlockComment blockComment(final BlockComment statement) {
		return blockComment(statement, null);
	}

	/**
	 * Opens an editing dialog for a block comment.
	 *
	 * @param statement The block comment.
	 * @param contentTextArea Content text area.
	 *
	 * @return The edited statement.
	 */

	private BlockComment blockComment(BlockComment statement, JTextPane contentTextArea) {
		if(statement == null) {
			statement = new BlockComment(null);
		}

		if(contentTextArea == null) {
			contentTextArea = new JTextPane();
			contentTextArea.setText(statement.getContent());
		}

		if(!Utils.okCancelDialog(editor, parentUI, "lineEditor.blockComment.title", "lineEditor.global.content", new JScrollPane(contentTextArea))) {
			return null;
		}

		final BlockComment copy = new BlockComment(contentTextArea.getText());
		if(!validate(copy)) {
			return blockComment(statement, contentTextArea);
		}

		statement.setContent(copy.getContent());
		return statement;
	}

	/**
	 * Tries to parse an expression and returns it if possible.
	 *
	 * @param expression The expression.
	 *
	 * @return The expression if possible.
	 */

	private Expression parseExpression(final String expression) {
		try {
			return Expression.parse(expression);
		}
		catch(final ParseException ex) {
			final AppLanguage language = editor.getAppLanguage();
			JOptionPane.showMessageDialog(parentUI, language.getString("lineEditor.expressionError.message"), language.getString("lineEditor.expressionError.title"), JOptionPane.ERROR_MESSAGE);
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

		final AppLanguage appLanguage = editor.getAppLanguage();
		JOptionPane.showMessageDialog(parentUI, appLanguage.getString("lineEditor.validationError.message"), appLanguage.getString("lineEditor.validationError.title"), JOptionPane.ERROR_MESSAGE);
		return false;
	}

	/**
	 * Adds a little button on the right of the specified component.
	 *
	 * @param component The component.
	 */

	private void setFunctionButtonBorder(final JTextComponent component) {
		setFunctionButtonBorder(component, editor);
	}

	/**
	 * Adds a little button on the right of the specified component.
	 * 
	 * @param component The component.
	 * @param editor The editor.
	 */

	public static void setFunctionButtonBorder(final JTextComponent component, final EditorFrame editor) {
		final JButton button = new JButton("{ }");
		button.addActionListener(actionEvent -> new PickerDialog(editor, component).setVisible(true));

		final ComponentBorder componentBorder = new ComponentBorder(button);
		componentBorder.install(component);
	}

	/**
	 * Selects the specified item from the specified combo-box.
	 * 
	 * @param comboBox The combo-box.
	 * @param element The specified item.
	 * @param <T> The combo-box type.
	 */

	private <T> void selectElement(final JComboBox<T> comboBox, final T element) {
		final int n = comboBox.getItemCount();
		for(int i = 0; i < n; i++) {
			if(comboBox.getItemAt(i).equals(element)) {
				comboBox.setSelectedIndex(i);
				return;
			}
		}
	}

}