package xyz.algogo.desktop.frames.listeners.menu;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JTree;
import javax.swing.filechooser.FileNameExtensionFilter;

import xyz.algogo.desktop.dialogs.ErrorDialog;
import xyz.algogo.desktop.frames.EditorFrame;
import xyz.algogo.desktop.frames.listeners.AlgorithmEditorActionListener;
import xyz.algogo.desktop.utils.LanguageManager;
import xyz.algogo.desktop.utils.Utils;

public class FileExportImageListener extends AlgorithmEditorActionListener {
	
	public FileExportImageListener(final EditorFrame editor) {
		super(editor);
	}
	
	@Override
	public final void actionPerformed(final ActionEvent event, final EditorFrame editor) {
		try {
			final String[] availableWriters = ImageIO.getWriterFormatNames();
			final List<String> addedWriters = new ArrayList<String>();
			final JFileChooser chooser = new JFileChooser();
			for(int i = 0; i != availableWriters.length; i++) {
				final String extension = availableWriters[i].toLowerCase();
				if(addedWriters.contains(extension)) {
					continue;
				}
				final FileNameExtensionFilter filter = new FileNameExtensionFilter(LanguageManager.getString("editor.menu.file.export.image.filter", extension.toUpperCase()), extension);
				if(i == 0) {
					chooser.setFileFilter(filter);
				}
				else {
					chooser.addChoosableFileFilter(filter);
				}
				addedWriters.add(extension);
			}
			final String algoPath = editor.getAlgorithmPath();
			final File currentDir = Utils.getParentFolder();
			chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
			chooser.setMultiSelectionEnabled(false);
			chooser.setCurrentDirectory(currentDir);
			chooser.setSelectedFile(algoPath == null ? new File(currentDir, editor.getAlgorithm().getTitle()) : new File(algoPath));
			if(chooser.showSaveDialog(editor) == JFileChooser.APPROVE_OPTION) {
				final String extension = ((FileNameExtensionFilter)chooser.getFileFilter()).getExtensions()[0];
				File file = chooser.getSelectedFile();
				if(!file.getName().endsWith("." + extension)) {
					file = new File(file.getPath() + "." + extension);
				}
				if(file.exists()) {
					file.delete();
				}
				file.createNewFile();
				final JTree tree = editor.getCurrentTreeComponent();
				final BufferedImage image = new BufferedImage(tree.getWidth(), tree.getHeight(), BufferedImage.TYPE_INT_RGB);
				final Graphics graphics = image.getGraphics();
				graphics.setColor(tree.getForeground());
				graphics.setFont(tree.getFont());
				tree.paintAll(graphics);
				final Rectangle region = new Rectangle(0, 0, image.getWidth(), image.getHeight());
				ImageIO.write(image.getSubimage(region.x, region.y, region.width, region.height), extension.toUpperCase(), file);
			}
		}
		catch(final Exception ex) {
			ErrorDialog.errorMessage(editor, ex);
		}
	}

}