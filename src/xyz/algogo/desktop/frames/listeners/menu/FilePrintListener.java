package xyz.algogo.desktop.frames.listeners.menu;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;

import javax.swing.JTextPane;

import xyz.algogo.core.Algorithm;
import xyz.algogo.desktop.AlgogoDesktop;
import xyz.algogo.desktop.dialogs.ErrorDialog;
import xyz.algogo.desktop.frames.EditorFrame;
import xyz.algogo.desktop.frames.listeners.AlgorithmEditorActionListener;
import xyz.algogo.desktop.utils.HtmlLanguage;
import xyz.algogo.desktop.utils.LanguageManager;

public class FilePrintListener extends AlgorithmEditorActionListener {
	
	public FilePrintListener(final EditorFrame editor) {
		super(editor);
	}

	@Override
	public final void actionPerformed(final ActionEvent event, final EditorFrame editor) {
		try {
			final Algorithm algorithm = editor.getAlgorithm();
			final String separator = System.lineSeparator();
			final JTextPane textPane = new JTextPane();
			textPane.setContentType("text/html");
			textPane.setText(algorithm.toLanguage(new HtmlLanguage(false, true)).replace("<body>" + separator, "<body>" + separator + "<br><br>"));
			textPane.print(
					new MessageFormat(LanguageManager.getString("editor.menu.file.print.header", algorithm.getTitle(), algorithm.getAuthor())),
					new MessageFormat(LanguageManager.getString("editor.menu.file.print.footer", AlgogoDesktop.APP_NAME, AlgogoDesktop.APP_VERSION))
			);
		}
		catch(final Exception ex) {
			ErrorDialog.errorMessage(editor, ex);
		}
	}

}
