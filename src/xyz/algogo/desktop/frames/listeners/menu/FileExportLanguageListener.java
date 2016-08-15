package xyz.algogo.desktop.frames.listeners.menu;

import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import xyz.algogo.core.Algorithm;
import xyz.algogo.core.language.AlgorithmLanguage;
import xyz.algogo.desktop.dialogs.ErrorDialog;
import xyz.algogo.desktop.frames.EditorFrame;
import xyz.algogo.desktop.frames.listeners.AlgorithmEditorActionListener;
import xyz.algogo.desktop.utils.LanguageManager;
import xyz.algogo.desktop.utils.Utils;

public class FileExportLanguageListener extends AlgorithmEditorActionListener {
	
	private final AlgorithmLanguage language;
	
	public FileExportLanguageListener(final EditorFrame editor, final AlgorithmLanguage language) {
		super(editor);
		this.language = language;
	}
	
	@Override
	public final void actionPerformed(final ActionEvent event, final EditorFrame editor) {
		try {
			final Algorithm algorithm = editor.getAlgorithm();
			final String algoPath = editor.getAlgorithmPath();
			final String extension = language.getExtension();
			final JFileChooser chooser = new JFileChooser();
			final File currentDir = Utils.getParentFolder();
			chooser.setFileFilter(new FileNameExtensionFilter(LanguageManager.getString("editor.menu.file.export.filter", language.getName(), extension), extension));
			chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
			chooser.setMultiSelectionEnabled(false);
			chooser.setCurrentDirectory(currentDir);
			chooser.setSelectedFile(algoPath == null ? new File(currentDir, algorithm.getTitle()) : new File(algoPath));
			if(chooser.showSaveDialog(editor) == JFileChooser.APPROVE_OPTION) {
				String path = chooser.getSelectedFile().getPath();
				if(!path.endsWith("." + extension)) {
					path += "." + extension;
				}
				final File file = new File(path);
				if(file.exists()) {
					file.delete();
				}
				Files.write(Paths.get(path), algorithm.toLanguage(language).getBytes(StandardCharsets.UTF_8));
			}
		}
		catch(final Exception ex) {
			ErrorDialog.errorMessage(editor, ex);
		}
	}

}