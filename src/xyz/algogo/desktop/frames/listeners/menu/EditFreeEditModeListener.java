package xyz.algogo.desktop.frames.listeners.menu;

import java.awt.event.ActionEvent;
import xyz.algogo.desktop.frames.EditorFrame;
import xyz.algogo.desktop.frames.listeners.AlgorithmEditorActionListener;

public class EditFreeEditModeListener extends AlgorithmEditorActionListener {
	
	private final boolean freeEditMode;
	
	public EditFreeEditModeListener(final EditorFrame editor, final boolean freeEditMode) {
		super(editor);
		this.freeEditMode = freeEditMode;
	}

	@Override
	public final void actionPerformed(final ActionEvent event, final EditorFrame editor) {
		editor.setFreeEditMode(freeEditMode);
	}

}