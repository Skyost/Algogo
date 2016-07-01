package xyz.algogo.desktop.frames.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import xyz.algogo.desktop.frames.EditorFrame;

public abstract class AlgorithmEditorActionListener implements ActionListener {
	
	private final EditorFrame editor;
	
	public AlgorithmEditorActionListener(final EditorFrame editor) {
		this.editor = editor;
	}
	
	@Override
	public final void actionPerformed(final ActionEvent event) {
		actionPerformed(event, editor);
	}
	
	public abstract void actionPerformed(final ActionEvent event, final EditorFrame editor);

}