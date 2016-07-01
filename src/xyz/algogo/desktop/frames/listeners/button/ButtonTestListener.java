package xyz.algogo.desktop.frames.listeners.button;

import java.awt.event.ActionEvent;

import xyz.algogo.desktop.frames.ConsoleFrame;
import xyz.algogo.desktop.frames.EditorFrame;
import xyz.algogo.desktop.frames.listeners.AlgorithmEditorActionListener;

public class ButtonTestListener extends AlgorithmEditorActionListener {
	
	public ButtonTestListener(final EditorFrame editor) {
		super(editor);
	}

	@Override
	public final void actionPerformed(final ActionEvent event, final EditorFrame editor) {
		new ConsoleFrame(editor, editor).setVisible(true);
	}

}