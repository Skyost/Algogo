package xyz.algogo.desktop.editor.menubar.menu;

import xyz.algogo.desktop.editor.EditorFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Represents an editor menu.
 */

public abstract class EditorMenu extends JMenu {

	/**
	 * CTRL key.
	 */

	private static final int CTRL = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

	/**
	 * The editor.
	 */

	private final EditorFrame editor;

	/**
	 * Creates a new editor menu.
	 *
	 * @param editor The editor.
	 * @param key The menu language key.
	 */

	public EditorMenu(final EditorFrame editor, final String key) {
		super(editor.getAppLanguage().getString(key));
		this.editor = editor;

		final InputMap inputMap = editor.getMainPane().getAlgorithmTree().getInputMap();
		for(final JMenuItem menuItem : getMenuItems()) {
			if(menuItem == null) {
				this.addSeparator();
				continue;
			}

			this.add(menuItem);
			inputMap.put(menuItem.getAccelerator(), menuItem.getAction());
		}
	}

	/**
	 * Returns the editor.
	 *
	 * @return The editor.
	 */

	public EditorFrame getEditor() {
		return editor;
	}

	/**
	 * Returns children menu items.
	 *
	 * @return Children menu items.
	 */

	public abstract JMenuItem[] getMenuItems();

	/**
	 * Creates a CTRL + <em>key</em> KeyStroke.
	 *
	 * @param otherKey The corresponding <em>key</em>.
	 *
	 * @return The KeyStroke.
	 */

	final KeyStroke createCTRLKeyStroke(final char otherKey) {
		return KeyStroke.getKeyStroke(otherKey, CTRL);
	}

	/**
	 * Little utility method that creates a menu item.
	 *
	 * @param key The language key.
	 * @param listener The listener.
	 * @param icon The icon.
	 * @param accelerator The accelerator.
	 *
	 * @return The menu item.
	 */

	final JMenuItem createMenu(final String key, final ActionListener listener, final Icon icon, final KeyStroke accelerator) {
		final JMenuItem item = new JMenuItem(editor.getAppLanguage().getString(key));

		if(listener != null) {
			item.addActionListener(listener);
		}

		if(icon != null) {
			item.setIcon(icon);
		}

		if(accelerator != null) {
			item.setAccelerator(accelerator);
		}

		return item;
	}

}