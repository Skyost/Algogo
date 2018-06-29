package xyz.algogo.desktop.editor.menubar.menu;

import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.kordamp.ikonli.swing.FontIcon;
import xyz.algogo.desktop.dialog.AboutDialog;
import xyz.algogo.desktop.dialog.UpdaterDialog;
import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.utils.Utils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Help editor menu.
 */

public class HelpEditorMenu extends EditorMenu {

	/**
	 * Creates a new Help menu.
	 *
	 * @param editor The editor.
	 */

	public HelpEditorMenu(final EditorFrame editor) {
		super(editor, "menu.help");
	}

	@Override
	public final JMenuItem[] getMenuItems() {
		final EditorFrame editor = this.getEditor();
		final List<JMenuItem> items = new ArrayList<>();

		items.add(createMenu(
				"menu.help.checkForUpdates",
				actionListener -> new UpdaterDialog(editor, true).checkForUpdates(),
				FontIcon.of(MaterialDesign.MDI_SYNC),
				null
		));

		items.add(createMenu(
				"menu.help.onlineHelp",
				actionListener -> Utils.visitIfPossible("https://github.com/Skyost/Algogo/wiki/Algogo-Desktop"),
				FontIcon.of(MaterialDesign.MDI_HELP),
				createCTRLKeyStroke('H')
		));

		items.add(null);

		items.add(createMenu(
				"menu.help.about",
				actionListener -> new AboutDialog(editor).setVisible(true),
				FontIcon.of(MaterialDesign.MDI_HEART),
				null
		));

		return items.toArray(new JMenuItem[items.size()]);
	}

}