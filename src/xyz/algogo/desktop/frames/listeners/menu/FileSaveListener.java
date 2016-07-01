package xyz.algogo.desktop.frames.listeners.menu;

import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import xyz.algogo.desktop.frames.EditorFrame;
import xyz.algogo.desktop.frames.listeners.AlgorithmEditorActionListener;

public class FileSaveListener extends AlgorithmEditorActionListener {
	
	public FileSaveListener(final EditorFrame editor) {
		super(editor);
	}

	@Override
	public final void actionPerformed(final ActionEvent event, final EditorFrame editor) {
		final String algoPath = editor.getAlgorithmPath();
		if(algoPath == null || !Files.isWritable(Paths.get(algoPath))) {
			editor.saveAs();
			return;
		}
		final int index = algoPath.lastIndexOf(".");
		editor.save(new File(algoPath), index == -1 ? "agg" : algoPath.substring(index));
	}

}