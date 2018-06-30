package xyz.algogo.desktop.dialog;

import xyz.algogo.core.evaluator.variable.VariableType;
import xyz.algogo.core.statement.Statement;
import xyz.algogo.core.statement.block.conditional.IfBlock;
import xyz.algogo.core.statement.block.loop.ForLoop;
import xyz.algogo.core.statement.block.loop.WhileLoop;
import xyz.algogo.core.statement.simple.comment.BlockComment;
import xyz.algogo.core.statement.simple.comment.LineComment;
import xyz.algogo.core.statement.simple.io.PrintStatement;
import xyz.algogo.core.statement.simple.io.PrintVariableStatement;
import xyz.algogo.core.statement.simple.io.PromptStatement;
import xyz.algogo.core.statement.simple.variable.AssignStatement;
import xyz.algogo.core.statement.simple.variable.CreateVariableStatement;
import xyz.algogo.desktop.AlgogoDesktop;
import xyz.algogo.desktop.AlgorithmDesktopLineEditor;
import xyz.algogo.desktop.AppLanguage;
import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.component.tree.AlgorithmTreeModel;

import javax.swing.*;
import java.awt.*;

/**
 * Represents an add line dialog which prompts the user to add a new algorithm line.
 */

public class AddLineDialog extends AlgorithmDesktopLineEditor {

	/**
	 * Creates a new add line dialog.
	 *
	 * @param editor The editor.
	 */

	public AddLineDialog(final EditorFrame editor) {
		super(null, editor);

		this.setParentUI(createDialog());
	}
	
	@Override
	public final JDialog getParentUI() {
		return (JDialog)super.getParentUI();
	}
	
	@Override
	public final void setParentUI(final Component parentUI) {
		if(!(parentUI instanceof JDialog)) {
			throw new IllegalArgumentException("Parent UI must be an instance of JDialog.");
		}
		
		super.setParentUI(parentUI);
	}

	/**
	 * Creates the corresponding dialog.
	 *
	 * @return The add line dialog.
	 */

	private JDialog createDialog() {
		final EditorFrame editor = getEditor();
		final AppLanguage appLanguage = editor.getAppLanguage();
		final JDialog dialog = new JDialog(editor, appLanguage.getString("addLineDialog.title"));

		final JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		final JButton createVariable = new JButton(appLanguage.getString("addLineDialog.button.createVariable"));
		createVariable.addActionListener(actionEvent -> createVariableStatement(null));
		final JButton assignValueToVariable = new JButton(appLanguage.getString("addLineDialog.button.assignValueToVariable"));
		assignValueToVariable.addActionListener(actionEvent -> assignStatement(null));
		createPanel(panel, appLanguage.getString("addLineDialog.panel.variables"), 0, 0, 1, createVariable, assignValueToVariable, Box.createVerticalBox());

		final JButton prompt = new JButton(appLanguage.getString("addLineDialog.button.prompt"));
		prompt.addActionListener(actionEvent -> promptStatement(null));
		final JButton printVariable = new JButton(appLanguage.getString("addLineDialog.button.printVariable"));
		printVariable.addActionListener(actionEvent -> printVariableStatement(null));
		final JButton print = new JButton(appLanguage.getString("addLineDialog.button.print"));
		print.addActionListener(actionEvent -> printStatement(null));
		createPanel(panel, appLanguage.getString("addLineDialog.panel.io"), 1, 0, 1, prompt, printVariable, print);

		final JButton ifBlock = new JButton(appLanguage.getString("addLineDialog.button.ifElse"));
		ifBlock.addActionListener(actionEvent -> ifElseBlock(null));
		final JButton forLoop = new JButton(appLanguage.getString("addLineDialog.button.for"));
		forLoop.addActionListener(actionEvent -> forLoop(null));
		final JButton whileLoop = new JButton(appLanguage.getString("addLineDialog.button.while"));
		whileLoop.addActionListener(actionEvent -> whileLoop(null));
		createPanel(panel, appLanguage.getString("addLineDialog.panel.blocks"), 0, 1, 2, ifBlock, forLoop, whileLoop);

		final JButton lineComment = new JButton(appLanguage.getString("addLineDialog.button.singleLineComment"));
		lineComment.addActionListener(actionEvent -> lineComment(null));
		final JButton blockComment = new JButton(appLanguage.getString("addLineDialog.button.multiLineComment"));
		blockComment.addActionListener(actionEvent -> blockComment(null));
		createPanel(panel, appLanguage.getString("addLineDialog.panel.comments"), 0, 2, 2, lineComment, blockComment);

		final JButton cancel = new JButton(appLanguage.getString("addLineDialog.button.cancel"));
		cancel.addActionListener(actionEvent -> dialog.dispose());

		createPanel(panel, null, 0, 3, 2, false, 20, cancel);

		final AlgorithmTreeModel model = editor.getMainPane().getAlgorithmTreeModel();
		final boolean hasVariables = model.hasVariables();
		assignValueToVariable.setEnabled(hasVariables);
		prompt.setEnabled(hasVariables);
		printVariable.setEnabled(hasVariables);
		forLoop.setEnabled(model.hasVariables(VariableType.NUMBER));

		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setIconImages(AlgogoDesktop.ICONS);
		dialog.setContentPane(panel);
		dialog.pack();
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.setLocationRelativeTo(editor);
		return dialog;
	}

	/**
	 * Creates a panel with a title.
	 *
	 * @param container The container.
	 * @param title The title.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param width Panel width.
	 * @param components Components to add to the panel.
	 */

	private void createPanel(final Container container, final String title, final int x, final int y, final int width, final JComponent... components) {
		createPanel(container, title, x, y, width, true, 5, components);
	}

	/**
	 * Creates a panel with a title.
	 *
	 * @param container The container.
	 * @param title The title.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param width Panel width.
	 * @param vertical Whether the display should be vertical.
	 * @param marginTop The top margin.
	 * @param components Components to add to the panel.
	 */

	private void createPanel(final Container container, final String title, final int x, final int y, final int width, final boolean vertical, final int marginTop, final JComponent... components) {
		final GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridheight = 1;
		constraints.gridwidth = width;
		constraints.fill = GridBagConstraints.BOTH;

		final JPanel panel = new JPanel();
		panel.setLayout(vertical ? new GridLayout(0, 1) : new GridLayout(1, 0));

		if(title == null) {
			panel.setBorder(BorderFactory.createEmptyBorder(marginTop, 5, 5, 5));
		}
		else {
			panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(title), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		}

		for(final JComponent component : components) {
			panel.add(component);
		}

		container.add(panel, constraints);
	}

	@Override
	public final CreateVariableStatement createVariableStatement(CreateVariableStatement statement) {
		statement = super.createVariableStatement(statement);

		handleStatement(statement);
		return statement;
	}

	@Override
	public final AssignStatement assignStatement(AssignStatement statement) {
		statement = super.assignStatement(statement);

		handleStatement(statement);
		return statement;
	}

	@Override
	public final PromptStatement promptStatement(PromptStatement statement) {
		statement = super.promptStatement(null);

		handleStatement(statement);
		return statement;
	}

	@Override
	public final PrintVariableStatement printVariableStatement(PrintVariableStatement statement) {
		statement = super.printVariableStatement(null);

		handleStatement(statement);
		return statement;
	}

	@Override
	public final PrintStatement printStatement(PrintStatement statement) {
		statement = super.printStatement(null);

		handleStatement(statement);
		return statement;
	}

	@Override
	public final IfBlock ifElseBlock(IfBlock statement) {
		statement = super.ifElseBlock(null);

		handleStatement(statement);
		return statement;
	}

	@Override
	public final ForLoop forLoop(ForLoop statement) {
		statement = super.forLoop(null);

		handleStatement(statement);
		return statement;
	}

	@Override
	public final WhileLoop whileLoop(WhileLoop statement) {
		statement = super.whileLoop(null);

		handleStatement(statement);
		return statement;
	}

	@Override
	public final LineComment lineComment(LineComment statement) {
		statement = super.lineComment(null);

		handleStatement(statement);
		return statement;
	}

	@Override
	public final BlockComment blockComment(BlockComment statement) {
		statement = super.blockComment(null);

		handleStatement(statement);
		return statement;
	}

	/**
	 * Handles an added statement.
	 *
	 * @param statement The statement.
	 */

	private void handleStatement(final Statement statement) {
		if(statement == null) {
			return;
		}

		this.getEditor().getMainPane().getAlgorithmTree().addStatement(statement);
		this.getParentUI().dispose();
	}

}