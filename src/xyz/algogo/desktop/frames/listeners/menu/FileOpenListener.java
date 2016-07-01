package xyz.algogo.desktop.frames.listeners.menu;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import xyz.algogo.desktop.dialogs.ErrorDialog;
import xyz.algogo.desktop.frames.EditorFrame;
import xyz.algogo.desktop.frames.listeners.AlgorithmEditorActionListener;
import xyz.algogo.desktop.utils.LanguageManager;
import xyz.algogo.desktop.utils.Utils;

public class FileOpenListener extends AlgorithmEditorActionListener {
	
	public FileOpenListener(final EditorFrame editor) {
		super(editor);
	}

	@Override
	public final void actionPerformed(final ActionEvent event, final EditorFrame editor) {
		try {
			final String algoPath = editor.getAlgorithmPath();
			final JFileChooser chooser = new JFileChooser();
			final File currentDir = Utils.getParentFolder();
			chooser.setFileFilter(new FileNameExtensionFilter(LanguageManager.getString("editor.menu.file.filter.algorithms"), "agg", "aggc"));
			chooser.addChoosableFileFilter(new FileNameExtensionFilter(LanguageManager.getString("editor.menu.file.filter.agg"), "agg"));
			chooser.addChoosableFileFilter(new FileNameExtensionFilter(LanguageManager.getString("editor.menu.file.filter.aggc"), "aggc"));
			chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
			chooser.setMultiSelectionEnabled(false);
			chooser.setCurrentDirectory(currentDir);
			chooser.setSelectedFile(algoPath == null ? new File(currentDir, editor.getAlgorithm().getTitle()) : new File(algoPath));
			if(chooser.showOpenDialog(editor) == JFileChooser.APPROVE_OPTION) {
				editor.open(chooser.getSelectedFile());
			}
		}
		catch(final Exception ex) {
			ErrorDialog.errorMessage(editor, ex);
		}
	}

}