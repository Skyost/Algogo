package xyz.algogo.desktop.editor.menubar.menu.listener.edit;

import xyz.algogo.desktop.AppLanguage;
import xyz.algogo.desktop.AppSettings;
import xyz.algogo.desktop.dialog.ErrorDialog;
import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.menubar.menu.listener.EditorMenuListener;
import xyz.algogo.desktop.utils.Utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

/**
 * Edit &rarr; Editor settings... listener.
 */

public class SettingsMenuListener extends EditorMenuListener {

	/**
	 * Creates a new Edit &rarr; Editor settings... listener.
	 *
	 * @param editor The editor.
	 */

	public SettingsMenuListener(final EditorFrame editor) {
		super(editor);
	}

	@Override
	public final void actionPerformed(final ActionEvent actionEvent) {
		try {
			final EditorFrame editor = this.getEditor();
			final AppLanguage language = editor.getAppLanguage();
			final AppSettings appSettings = editor.getAppSettings();

			final String[] availableLanguages = AppLanguage.getAvailableLanguages();
			final JComboBox<String> languages = new JComboBox<>(availableLanguages);
			final int index = Arrays.asList(availableLanguages).indexOf(editor.getAppSettings().customLanguage);

			languages.setSelectedIndex(index == -1 ? 0 : index);

			final JCheckBox update = new JCheckBox(language.getString("updateDialog.enableCheckForUpdates"));
			update.setSelected(editor.getAppSettings().autoUpdater);

			if(!Utils.okCancelDialog(editor, editor, "settingsDialog.title", "settingsDialog.settings.language", languages, update)) {
				return;
			}

			JOptionPane.showMessageDialog(editor, language.getString("settingsDialog.dialog.restart.message"), language.getString("settingsDialog.dialog.restart.title"), JOptionPane.WARNING_MESSAGE);

			appSettings.customLanguage = languages.getSelectedItem().toString();
			appSettings.autoUpdater = update.isSelected();
			appSettings.save();
		}
		catch(final Exception ex) {
			ErrorDialog.fromThrowable(ex, this.getEditor());
		}
	}

}