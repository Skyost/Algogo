package xyz.algogo.desktop.utils;

import xyz.algogo.desktop.AppLanguage;
import xyz.algogo.desktop.dialog.ErrorDialog;
import xyz.algogo.desktop.editor.EditorFrame;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

/**
 * This class contains some useful methods.
 */

public class Utils {

	/**
	 * Converts a stack trace to a string.
	 *
	 * @param throwable The throwable.
	 *
	 * @return The stack trace as a string.
	 */

	public static String fromStackTrace(final Throwable throwable) {
		final StringWriter stringWriter = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringWriter);
		throwable.printStackTrace(printWriter);

		return stringWriter.toString();
	}

	/**
	 * Shows a Ok/Cancel dialog.
	 *
	 * @param editor The editor.
	 * @param parent The parent component.
	 * @param title The title of the dialog.
	 * @param components Components to add to the dialog.
	 *
	 * @return Whether the user has clicked on Ok or on Cancel.
	 */

	public static boolean okCancelDialog(final EditorFrame editor, final Component parent, final String title, final Object... components) {
		return okCancelDialog(editor.getAppLanguage(), parent, title, components);
	}

	/**
	 * Shows a Ok/Cancel dialog.
	 *
	 * @param language Application language.
	 * @param parent The parent component.
	 * @param title The title of the dialog.
	 * @param components Components to add to the dialog.
	 *
	 * @return Whether the user has clicked on Ok or on Cancel.
	 */

	public static boolean okCancelDialog(final AppLanguage language, final Component parent, final String title, final Object... components) {
		final JPanel panel = new JPanel(new GridLayout(0, 1));

		for(final Object component : components) {
			if(component instanceof String) {
				panel.add(new JLabel(language.getString(component.toString())));
				continue;
			}

			panel.add((JComponent)component);
		}

		return JOptionPane.showConfirmDialog(parent, panel, language.getString(title), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
	}

	/**
	 * Creates a grid bag constraints object.
	 *
	 * @param gridx The grid X.
	 * @param gridy The grid Y.
	 * @param gridheight The grid height.
	 * @param gridwidth The grid width.
	 * @param fill How to fill the parent container.
	 * @param weightx X weight of the component.
	 * @param weighty Y weight of the component.
	 * @param insets Component insets.
	 * @param anchor Component anchor.
	 *
	 * @return The grid bag constraints object.
	 */

	public static GridBagConstraints createGridBagConstraints(final int gridx, final int gridy, final int gridheight, final int gridwidth, final int fill, final float weightx, final float weighty, final Insets insets, final int anchor)  {
		final GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = gridx;
		constraints.gridy = gridy;
		constraints.gridheight = gridheight;
		constraints.gridwidth = gridwidth;
		constraints.fill = fill;
		constraints.weightx = weightx;
		constraints.weighty = weighty;
		constraints.insets = insets == null ? new Insets(0, 0, 0, 0) : insets;
		constraints.anchor = anchor;

		return constraints;
	}

	/**
	 * Visits an URL if supported by the Desktop.
	 *
	 * @param url The URL.
	 */

	public static void visitIfPossible(final String url) {
		try {
			if(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				Desktop.getDesktop().browse(new URL(url).toURI());
			}
		}
		catch(final Exception ex) {
			ErrorDialog.fromThrowable(ex);
		}
	}

	/**
	 * Escapes a HTML string.
	 *
	 * @param string The HTML string.
	 *
	 * @return The escaped HTML string.
	 */

	public static String escapeHTML(final String string) {
		final StringBuilder out = new StringBuilder(Math.max(16, string.length()));
		for(int i = 0; i < string.length(); i++) {
			final char c = string.charAt(i);
			if(c > 127 || c == '"' || c == '<' || c == '>' || c == '&') {
				out.append("&#");
				out.append((int) c);
				out.append(';');
			}
			else {
				out.append(c);
			}
		}
		return out.toString();
	}

}