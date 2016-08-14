package xyz.algogo.desktop.frames.listeners.menu;

import java.awt.event.ActionEvent;

import xyz.algogo.desktop.frames.EditorFrame;
import xyz.algogo.desktop.frames.listeners.AlgorithmEditorActionListener;

public class EditUndoListener extends AlgorithmEditorActionListener {
	
	public EditUndoListener(final EditorFrame editor) {
		super(editor);
	}

	@Override
	public final void actionPerformed(final ActionEvent event, final EditorFrame editor) {
		editor.undo();
	}

}