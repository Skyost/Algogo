package xyz.algogo.desktop.editor.export;

import xyz.algogo.desktop.dialog.ErrorDialog;
import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.component.tree.AlgorithmTree;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * The PNG image export target.
 */

public class ImageExportTarget extends ExportTarget {

	/**
	 * Creates a new PNG image export target.
	 *
	 * @param editor The editor.
	 */

	public ImageExportTarget(final EditorFrame editor) {
		super(editor, "menu.file.export.image", "png");
	}

	@Override
	public final void export(final File file) {
		try {
			if(file.exists()) {
				file.delete();
			}

			final AlgorithmTree tree = this.getEditor().getMainPane().getAlgorithmTree();

			final BufferedImage image = new BufferedImage(tree.getWidth(), tree.getHeight(), BufferedImage.TYPE_INT_RGB);
			final Graphics graphics = image.getGraphics();
			graphics.setColor(tree.getForeground());
			graphics.setFont(tree.getFont());
			tree.paintAll(graphics);

			final Rectangle region = new Rectangle(0, 0, image.getWidth(), image.getHeight());
			ImageIO.write(image.getSubimage(region.x, region.y, region.width, region.height), this.getExtension().toUpperCase(), file);
		}
		catch(final Exception ex) {
			ErrorDialog.fromThrowable(ex, this.getEditor());
		}
	}

}