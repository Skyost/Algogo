package xyz.algogo.desktop.dialog;

import xyz.algogo.desktop.AlgogoDesktop;
import xyz.algogo.desktop.AppLanguage;
import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.utils.Utils;

import javax.swing.*;
import java.awt.*;

/**
 * Represents an error dialog (where exceptions are shown to the user).
 */

public class ErrorDialog extends JDialog {

	/**
	 * Creates a new error dialog.
	 *
	 * @param stackTraceText The error stack trace.
	 * @param titleText The dialog title.
	 * @param messageText The dialog message.
	 * @param submitText The submit button text.
	 * @param closeText The close button text.
	 */

	public ErrorDialog(final String stackTraceText, final String titleText, final String messageText, final String submitText, final String closeText) {
		final JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		final JLabel message = new JLabel(messageText);
		message.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		panel.add(message, BorderLayout.NORTH);

		final JTextArea stackTraceTextArea = new JTextArea(stackTraceText);
		stackTraceTextArea.setRows(10);
		stackTraceTextArea.setEditable(false);
		panel.add(new JScrollPane(stackTraceTextArea), BorderLayout.CENTER);

		final JPanel footer = new JPanel(new FlowLayout());

		final JButton submit = new JButton(submitText);
		submit.addActionListener(actionEvent -> Utils.visitIfPossible("https://github.com/Skyost/Algogo/issues"));
		footer.add(submit);

		final JButton close = new JButton(closeText);
		close.addActionListener(actionEvent -> this.dispose());
		footer.add(close);

		panel.add(footer, BorderLayout.SOUTH);

		this.setContentPane(panel);
		this.setTitle(titleText);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setIconImages(AlgogoDesktop.ICONS);
		this.setResizable(false);
		this.setModal(true);
		this.pack();
		this.setLocationRelativeTo(null);
	}

	/**
	 * Creates an error dialog using a Throwable.
	 *
	 * @param throwable The throwable.
	 */

	public static void fromThrowable(final Throwable throwable) {
		fromThrowable(throwable, null);
	}

	/**
	 * Creates an error dialog using a Throwable.
	 *
	 * @param throwable The throwable.
	 * @param editor The editor.
	 */

	public static void fromThrowable(final Throwable throwable, final EditorFrame editor) {
		String title = "Error occurred";
		String message = "<html>Oops, an error occurred ! The StackTrace is available under this message.<br>If you want to contribute to the development of the software, please consider submit it on Github by clicking on the button below.</html>";
		String submit = "Submit StackTrace on Github";
		String close = "Close";

		if(editor != null) {
			final AppLanguage language = editor.getAppLanguage();
			title = language.getString("errorDialog.title");
			message = language.getString("errorDialog.message");
			submit = language.getString("errorDialog.button.submit");
			close = language.getString("errorDialog.button.close");
		}

		fromThrowable(throwable, title, message, submit, close);
	}

	/**
	 * Creates an error dialog using a Throwable.
	 *
	 * @param throwable The throwable.
	 * @param title The dialog title.
	 * @param message The dialog message.
	 * @param submit The submit button text.
	 * @param close The close button text.
	 */

	private static void fromThrowable(final Throwable throwable, final String title, final String message, final String submit, final String close) {
		throwable.printStackTrace();
		new ErrorDialog(Utils.fromStackTrace(throwable), title, message, submit, close).setVisible(true);
	}

}