package xyz.algogo.desktop.editor.component.tree;

import xyz.algogo.core.language.Language;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * The algorithm tree renderer.
 */

public class AlgorithmTreeRenderer extends DefaultTreeCellRenderer {

	/**
	 * Root block statements color.
	 */

	public static final Color ROOT_BLOCK_STATEMENT_COLOR = Color.decode("#D35400");

	/**
	 * Block statements color.
	 */

	public static final Color BLOCK_STATEMENT_COLOR = Color.decode("#3498DB");

	/**
	 * Simple statements color.
	 */

	public static final Color SIMPLE_STATEMENT_COLOR = Color.decode("#22313F");

	/**
	 * Comments color.
	 */

	public static final Color COMMENT_COLOR = Color.decode("#27AE60");

	/**
	 * Selection border color.
	 */

	private static final Color BORDER_SELECTION_COLOR = Color.decode("#95A5A6");

	/**
	 * Selection background color.
	 */

	private static final Color BACKGROUND_SELECTION_COLOR = Color.decode("#BDC3C7");

	/**
	 * The current display language.
	 */

	private Language displayLanguage;

	/**
	 * Creates a new algorithm tree renderer.
	 *
	 * @param displayLanguage Nodes' display language.
	 */

	public AlgorithmTreeRenderer(final Language displayLanguage) {
		this.setLeafIcon(null);
		this.setClosedIcon(null);
		this.setOpenIcon(null);
		this.setBorderSelectionColor(BORDER_SELECTION_COLOR);
		this.setBackgroundSelectionColor(BACKGROUND_SELECTION_COLOR);

		this.displayLanguage = displayLanguage;
	}

	/**
	 * Returns the current display language.
	 *
	 * @return The current display language.
	 */

	public Language getDisplayLanguage() {
		return displayLanguage;
	}

	/**
	 * Sets the current display language.
	 *
	 * @param displayLanguage The current display language.
	 */

	public final void setDisplayLanguage(final Language displayLanguage) {
		this.displayLanguage = displayLanguage;
	}

	@Override
	public final Component getTreeCellRendererComponent(final JTree tree, Object value, final boolean selected, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) {
		if(value instanceof AlgorithmTreeNode) {
			value = "<html>" + ((AlgorithmTreeNode)value).statement.toLanguage(displayLanguage) + "</html>";
		}

		return super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
	}

}