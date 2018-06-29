package xyz.algogo.desktop.editor.menubar.menu.listener.file;

import xyz.algogo.core.Algorithm;
import xyz.algogo.desktop.AlgogoDesktop;
import xyz.algogo.desktop.AppLanguage;
import xyz.algogo.desktop.dialog.ErrorDialog;
import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.component.EditorMainPane;
import xyz.algogo.desktop.editor.export.HTMLExportTarget;
import xyz.algogo.desktop.editor.menubar.menu.listener.EditorMenuListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;

/**
 * File &rarr; Print... listener.
 */

public class PrintMenuListener extends EditorMenuListener {

	/**
	 * Creates a new File &rarr; Print... listener.
	 *
	 * @param editor The editor.
	 */

	public PrintMenuListener(final EditorFrame editor) {
		super(editor);
	}

	@Override
	public final void actionPerformed(final ActionEvent actionEvent) {
		try {
			final EditorFrame editor = this.getEditor();
			final EditorMainPane mainPane = editor.getMainPane();
			final String content;
			if(mainPane.isShowingTree()) {
				content = new HTMLExportTarget(editor).modelToHTML();
			}
			else {
				final Algorithm algorithm = mainPane.getAlgorithmTextArea().toAlgorithm();
				if(algorithm == null) {
					mainPane.getAlgorithmTextArea().print();
					return;
				}

				content = new HTMLExportTarget(editor).algorithmRootBlockToHTML(algorithm.getRootBlock());
			}

			final AppLanguage appLanguage = this.getEditor().getAppLanguage();
			final JTextPane textPane = new JTextPane();

			textPane.setContentType("text/html");
			textPane.setText("<html><body style=\"font-family: Helvetica;\"><br><br>" + content + "</body></html>");
			textPane.print(
					new MessageFormat(appLanguage.getString("menu.file.print.header", editor.getCredits().getTitle(), editor.getCredits().getAuthor())),
					new MessageFormat(appLanguage.getString("menu.file.print.footer", AlgogoDesktop.APP_NAME, AlgogoDesktop.APP_VERSION))
			);
		}
		catch(final Exception ex) {
			ErrorDialog.fromThrowable(ex, this.getEditor());
		}
	}

}
