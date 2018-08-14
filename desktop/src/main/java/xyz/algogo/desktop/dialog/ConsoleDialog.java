package xyz.algogo.desktop.dialog;

import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.kordamp.ikonli.swing.FontIcon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultCaret;

import xyz.algogo.core.Algorithm;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.evaluator.context.InputListener;
import xyz.algogo.core.evaluator.context.OutputListener;
import xyz.algogo.core.statement.Statement;
import xyz.algogo.desktop.AlgogoDesktop;
import xyz.algogo.desktop.AlgorithmDesktopLineEditor;
import xyz.algogo.desktop.AppLanguage;
import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.menubar.menu.listener.file.FileDialogMenuListener;
import xyz.algogo.desktop.utils.Utils;

/**
 * Represents a console dialog that allows to interpret an algorithm.
 */

public class ConsoleDialog extends JDialog implements ActionListener, InputListener, OutputListener {

	/**
	 * The system line separator.
	 */

	private static final String LINE_SEPARATOR = System.lineSeparator();

	/**
	 * The run button.
	 */

	private final JButton run = new JButton();

	/**
	 * The main text area.
	 */

	private final JTextArea textArea = new JTextArea();

	/**
	 * The current evaluation context.
	 */

	private EvaluationContext currentContext;

	/**
	 * The editor.
	 */

	private final EditorFrame editor;

	/**
	 * Creates a new console dialog.
	 *
	 * @param editor The editor.
	 */

	public ConsoleDialog(final EditorFrame editor) {
		this.editor = editor;

		final AppLanguage appLanguage = editor.getAppLanguage();

		final JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		textArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		textArea.setBackground(Color.BLACK);
		textArea.setForeground(Color.WHITE);
		textArea.setEnabled(false);
		textArea.setFont(Font.decode("Lucida Console").deriveFont(14f));

		final JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(400, 400));
		panel.add(scrollPane, Utils.createGridBagConstraints(0, 0, 2, 1, GridBagConstraints.BOTH, 0.7f, 1, null, GridBagConstraints.PAGE_START));

		final JPanel leftPanel = new JPanel(new GridLayout(0, 1));
		leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

		run.addActionListener(this);
		setRunDefaultStyle();
		leftPanel.add(run);

		final JCheckBox autoScroll = new JCheckBox(appLanguage.getString("consoleDialog.button.autoScroll"));
		autoScroll.addChangeListener(changeEvent -> ((DefaultCaret)textArea.getCaret()).setUpdatePolicy(autoScroll.isSelected() ? DefaultCaret.ALWAYS_UPDATE : DefaultCaret.UPDATE_WHEN_ON_EDT));
		autoScroll.setSelected(true);
		leftPanel.add(autoScroll);

		panel.add(leftPanel, Utils.createGridBagConstraints(2, 0, 1, 1, GridBagConstraints.HORIZONTAL, 0.2f, 1, null, GridBagConstraints.PAGE_START));

		final JButton save = new JButton(appLanguage.getString("consoleDialog.button.save"));
		save.addActionListener(new FileDialogMenuListener(editor, new FileNameExtensionFilter(appLanguage.getString("menu.file.export.html"), "html")) {

			@Override
			public final void actionPerformed(final ActionEvent event) {
				final File file = chooseFile(false);
				if(file == null) {
					return;
				}

				saveTextAreaContent(file);
			}

		});
		save.setIcon(FontIcon.of(MaterialDesign.MDI_CONTENT_SAVE));

		panel.add(save, Utils.createGridBagConstraints(2, 1, 1, 1, GridBagConstraints.HORIZONTAL, 0.2f, 1, new Insets(0, 10, 0, 0), GridBagConstraints.PAGE_END));

		this.setContentPane(panel);
		this.setTitle(appLanguage.getString("consoleDialog.title"));
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setIconImages(AlgogoDesktop.ICONS);
		this.pack();
		this.setMinimumSize(this.getSize());
		this.setLocationRelativeTo(editor);
	}

	@Override
	public final void actionPerformed(final ActionEvent event) {
		if(currentContext == null) {
			final Algorithm algorithm = editor.getMainPane().getCurrentComponent().toAlgorithm();
			if(algorithm == null) {
				return;
			}

			currentContext = new EvaluationContext(this, this);
			textArea.setText("");
			run.setText(editor.getAppLanguage().getString("consoleDialog.button.stop"));
			run.setIcon(FontIcon.of(MaterialDesign.MDI_STOP));
			new Thread(() -> {
				final Exception ex = algorithm.evaluate(currentContext);
				if(ex != null) {
					output(null, LINE_SEPARATOR);
					output(null, editor.getAppLanguage().getString("consoleDialog.message.error") + LINE_SEPARATOR);
					output(null, Utils.fromStackTrace(ex));

					final String message = ex.getMessage() == null ? "" : ex.getMessage();
					JOptionPane.showMessageDialog(this, "<html>" + editor.getAppLanguage().getString("consoleDialog.message.error") + "<br>" + (message.isEmpty() ? ex.getClass().getName() : message) + "</html>", "", JOptionPane.ERROR_MESSAGE);
				}

				setRunDefaultStyle();
				currentContext = null;
			}).start();
			return;
		}

		currentContext.setStopped(true);
		currentContext = null;
	}

	@Override
	public final Object input(final Statement source, final Object... arguments) {
		final AppLanguage language = editor.getAppLanguage();

		final JLabel message = new JLabel(arguments[1] == null ? language.getString("consoleDialog.promptDialog.defaultMessage", arguments[0].toString()) : arguments[1].toString());
		final JTextField input = new JTextField();
		AlgorithmDesktopLineEditor.setFunctionButtonBorder(input, editor);

		if(!Utils.okCancelDialog(language, this, "consoleDialog.promptDialog.title", message, input)) {
			return null;
		}

		return input.getText();
	}

	@Override
	public final void output(final Statement source, final String content) {
		textArea.append(content);
	}

	/**
	 * Returns the main text area.
	 *
	 * @return The main text area.
	 */

	public final JTextArea getTextArea() {
		return textArea;
	}

	/**
	 * Returns the editor.
	 *
	 * @return The editor.
	 */

	public final EditorFrame getEditor() {
		return editor;
	}

	/**
	 * Saves text area content to a file.
	 *
	 * @param file The file.
	 */

	private void saveTextAreaContent(final File file) {
		try {
			final StringBuilder builder = new StringBuilder();
			builder.append("<!DOCTYPE html>").append(LINE_SEPARATOR);
			builder.append("<html lang=\"" + editor.getAppLanguage().getCurrentLanguageCode() + "\">").append(LINE_SEPARATOR);
			builder.append("<meta charset=\"utf-8\">").append(LINE_SEPARATOR);
			builder.append("<head>").append(LINE_SEPARATOR);
			builder.append("<title>");
			builder.append(editor.getAppLanguage().getString("editor.title", editor.getCredits().getTitle(), "", editor.getCredits().getAuthor(), AlgogoDesktop.APP_NAME, AlgogoDesktop.APP_VERSION));
			builder.append("</title>").append(LINE_SEPARATOR);
			builder.append("</head>").append(LINE_SEPARATOR);
			builder.append("<link rel=\"icon\" type=\"image/png\" href=\"https://www.algogo.xyz/assets/img/icon.png\"/>").append(LINE_SEPARATOR);
			builder.append("<body style=\"background-color: black; color: white; font-family: Lucida Console, Monaco, monospace; font-size: 1.2em;\">").append(LINE_SEPARATOR);
			builder.append(Utils.escapeHTML(textArea.getText()).replace(LINE_SEPARATOR, "<br>"));
			builder.append("</body>").append(LINE_SEPARATOR);
			builder.append("</html>");

			Files.write(file.toPath(), builder.toString().getBytes(StandardCharsets.UTF_8));
		}
		catch(final Exception ex) {
			ErrorDialog.fromThrowable(ex, editor);
		}
	}

	/**
	 * Sets the default styles to the run button.
	 */

	private void setRunDefaultStyle() {
		run.setText(editor.getAppLanguage().getString("consoleDialog.button.run"));
		run.setIcon(FontIcon.of(MaterialDesign.MDI_PLAY));
	}

}