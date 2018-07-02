package xyz.algogo.desktop.dialog;

import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.kordamp.ikonli.swing.FontIcon;
import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.evaluator.function.Function;
import xyz.algogo.core.evaluator.variable.Variable;
import xyz.algogo.desktop.AlgogoDesktop;
import xyz.algogo.desktop.AppLanguage;
import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.utils.ListAction;
import xyz.algogo.desktop.utils.Utils;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Represents a picker dialog which allows to pick a variable or a function.
 */

public class PickerDialog extends JDialog {

	/**
	 * The editor.
	 */

	private final EditorFrame editor;

	/**
	 * Creates a new picker dialog.
	 *
	 * @param editor The editor.
	 * @param target The target.
	 */

	public PickerDialog(final EditorFrame editor, final JTextComponent target) {
		this.editor = editor;
		final AppLanguage appLanguage = editor.getAppLanguage();

		final ExpressionEvaluator defaultEvaluator = new ExpressionEvaluator();
		final DefaultListModel<PickerListObject> model = new DefaultListModel<>();

		model.addElement(new PickerListObject(appLanguage.getString("pickerDialog.variables"), true));

		for(final String variable : editor.getMainPane().getAlgorithmTree().getModel().buildVariablesList()) {
			model.addElement(new PickerListObject(variable));
		}

		for(final Variable variable : defaultEvaluator.getVariables()) {
			model.addElement(new PickerListObject(variable.getIdentifier()));
		}

		model.addElement(new PickerListObject(appLanguage.getString("pickerDialog.functions"), true));

		for(final Function function : defaultEvaluator.getFunctions()) {
			model.addElement(new PickerListObject(function.getIdentifier()));
		}

		final JList<PickerListObject> pickerList = new JList<>(model);
		new ListAction(pickerList, new AbstractAction() {

			@Override
			public final void actionPerformed(final ActionEvent actionEvent) {
				final PickerListObject selected = pickerList.getSelectedValue();
				if(selected.isTitle()) {
					return;
				}

				target.setText(selected.getValue() + (target.getText().isEmpty() ? "" : " ") + target.getText());
				target.setCaretPosition(selected.getValue().length());

				PickerDialog.this.dispose();
			}

		});

		this.add(new JScrollPane(pickerList), BorderLayout.CENTER);

		final JButton onlineDocumentation = new JButton(appLanguage.getString("pickerDialog.footer.onlineDocumentation"));
		onlineDocumentation.addActionListener(actionEvent -> Utils.visitIfPossible("https://github.com/Skyost/Algogo/wiki"));
		onlineDocumentation.setIcon(FontIcon.of(MaterialDesign.MDI_EARTH));
		onlineDocumentation.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		this.add(onlineDocumentation, BorderLayout.SOUTH);

		this.getRootPane().registerKeyboardAction(actionEvent -> this.dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
		this.setUndecorated(true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setIconImages(AlgogoDesktop.ICONS);
		this.setModal(true);
		this.pack();
		this.setLocationRelativeTo(null);
	}

	/**
	 * The picker list object.
	 */

	class PickerListObject {

		/**
		 * The value of the object.
		 */

		private final String value;

		/**
		 * Whether this object is a "Title" object.
		 */

		private final boolean isTitle;

		/**
		 * Creates a new picker list object.
		 *
		 * @param value The value.
		 */

		private PickerListObject(final String value) {
			this(value, false);
		}

		/**
		 * Creates a new picker list object.
		 *
		 * @param value The value.
		 * @param isTitle Whether this object is a "Title" object.
		 */

		private PickerListObject(final String value, final boolean isTitle) {
			this.value = value;
			this.isTitle = isTitle;
		}

		/**
		 * Returns this object's value.
		 *
		 * @return This object's value.
		 */

		private String getValue() {
			return value;
		}

		/**
		 * Returns whether this object is a "Title" object.
		 *
		 * @return Whether this object is a "Title" object.
		 */

		private boolean isTitle() {
			return isTitle;
		}

		@Override
		public final String toString() {
			return editor.getAppLanguage().getString((isTitle ? "pickerDialog.title.content" : "pickerDialog.element.content"), value);
		}

	}

}