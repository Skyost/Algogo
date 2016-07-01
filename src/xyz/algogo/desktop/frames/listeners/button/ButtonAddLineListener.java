package xyz.algogo.desktop.frames.listeners.button;

import java.awt.event.ActionEvent;

import xyz.algogo.desktop.dialogs.AddLineDialog;
import xyz.algogo.desktop.frames.EditorFrame;
import xyz.algogo.desktop.frames.listeners.AlgorithmEditorActionListener;

public class ButtonAddLineListener extends AlgorithmEditorActionListener {
	
	public ButtonAddLineListener(final EditorFrame editor) {
		super(editor);
	}

	@Override
	public final void actionPerformed(final ActionEvent event, final EditorFrame editor) {
		new AddLineDialog(editor, editor, editor.getAlgorithm().getVariables()).setVisible(true);
	}

}