package fr.skyost.algo.desktop.utils;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public class Utils {
	
	/**
	 * Escapes HTML characters of the specified String.
	 * 
	 * @param string The String.
	 * 
	 * @return An escaped String.
	 */

	public static final String escapeHTML(final String string) {
		final StringBuilder builder = new StringBuilder(Math.max(16, string.length()));
		for(int i = 0; i < string.length(); i++) {
			final char charr = string.charAt(i);
			if(charr > 127 || charr == '"' || charr == '<' || charr == '>' || charr == '&') {
				builder.append("&#");
				builder.append((int)charr);
				builder.append(';');
			}
			else {
				builder.append(charr);
			}
		}
		return builder.toString();
	}
	
	/**
	 * Joins a String array.
	 * 
	 * @param joiner The character used to join arrays.
	 * @param strings The array.
	 * 
	 * @return The joined array.
	 */

	public static final String join(final char joiner, final String... strings) {
		final StringBuilder builder = new StringBuilder();
		for(final String string : strings) {
			builder.append(string + joiner);
		}
		builder.setLength(builder.length() - 1);
		return builder.toString();
	}
	
	/**
	 * Creates a dialog.
	 * 
	 * @param component The parent component.
	 * @param title The dialog's title.
	 * @param message The message of the dialog.
	 * @param tip The dialog's tip.
	 * @param objects The objects of the dialog.
	 * 
	 * @return <b>true</b> If the user has clicked on "OK".
	 * <br><b>false</b> Otherwise.
	 */

	public static final boolean createDialog(final Component component, final String title, final String message, final String tip, final Object... objects) {
		final List<Object> components = new ArrayList<Object>();
		components.add(new JLabel(message));
		components.addAll(Arrays.asList(objects));
		if(tip != null) {
			components.add(new JLabel("<html><b>" + LanguageManager.getString("utils.tip") + " :</b> <i>" + tip + "</i></html>"));
		}
		return JOptionPane.showConfirmDialog(component, components.toArray(new Object[components.size()]), title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION;
	}
	
	/**
	 * Reloads a JTree.
	 * 
	 * @param tree The JTree.
	 */

	public static final void reloadTree(final JTree tree) {
		reloadTree(tree, null);
	}
	
	/**
	 * Reloads a node in a JTree.
	 * 
	 * @param tree The JTree.
	 * @param node The node, can be null.
	 */

	public static final void reloadTree(final JTree tree, final TreeNode node) {
		if(node == null) {
			((DefaultTreeModel)tree.getModel()).reload();
		}
		else {
			((DefaultTreeModel)tree.getModel()).reload(node);
		}
		for(int i = 0; i < tree.getRowCount(); i++) {
			tree.expandRow(i);
		}
	}
	
	/**
	 * Checks if a String is alpha.
	 * 
	 * @param string The String.
	 * 
	 * @return <b>true</b> If the String is alpha.
	 * <br><b>false</b> Otherwise.
	 */

	public static final boolean isAlpha(final String string) {
		for(char charr : string.toCharArray()) {
			if(!Character.isLetter(charr)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Gets a node's content.
	 * 
	 * @param node The node.
	 * @param spaces Current spaces.
	 * 
	 * @return The node's content.
	 */
	
	public static final String getNodeContent(final AlgoTreeNode node, final StringBuilder spaces) {
		final StringBuilder builder = new StringBuilder();
		builder.append(node.toString() + System.lineSeparator());
		final int childCount = node.getChildCount();
		if(childCount > 0) {
			spaces.append("  ");
			for(int i = 0; i != childCount; i++) {
				builder.append(spaces.toString() + getNodeContent((AlgoTreeNode)node.getChildAt(i), spaces));
			}
			spaces.delete(0, 2);
		}
		return builder.toString();
	}

}