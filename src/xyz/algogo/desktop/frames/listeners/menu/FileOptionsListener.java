package xyz.algogo.desktop.frames.listeners.menu;

import java.awt.event.ActionEvent;

import xyz.algogo.desktop.dialogs.OptionsDialog;
import xyz.algogo.desktop.frames.EditorFrame;
import xyz.algogo.desktop.frames.listeners.AlgorithmEditorActionListener;

public class FileOptionsListener extends AlgorithmEditorActionListener {
	
	public FileOptionsListener(final EditorFrame editor) {
		super(editor);
	}

	@Override
	public final void actionPerformed(final ActionEvent event, final EditorFrame editor) {
		new OptionsDialog(editor, editor).setVisible(true);
	}

}