package xyz.algogo.desktop.editor.menubar.menu.listener.edit;

import xyz.algogo.desktop.editor.AlgorithmCredits;
import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.menubar.menu.listener.EditorMenuListener;
import xyz.algogo.desktop.utils.Utils;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Edit &rarr; Algorithm credits... listener.
 */

public class CreditsMenuListener extends EditorMenuListener {

	/**
	 * Creates a new Edit &rarr; Algorithm credits... listener.
	 *
	 * @param editor The editor.
	 */

	public CreditsMenuListener(final EditorFrame editor) {
		super(editor);
	}

	@Override
	public final void actionPerformed(final ActionEvent actionEvent) {
		final EditorFrame editor = this.getEditor();

		final JTextField title = new JTextField(editor.getCredits().getTitle());
		final JTextField author = new JTextField(editor.getCredits().getAuthor());

		if(Utils.okCancelDialog(editor, editor, "creditsDialog.title", "creditsDialog.algorithm.title", title, "creditsDialog.algorithm.author", author)) {
			editor.setCredits(new AlgorithmCredits(title.getText(), author.getText()));
		}
	}

}