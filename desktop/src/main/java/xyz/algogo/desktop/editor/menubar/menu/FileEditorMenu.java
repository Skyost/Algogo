package xyz.algogo.desktop.editor.menubar.menu;

import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.kordamp.ikonli.swing.FontIcon;
import xyz.algogo.desktop.AppLanguage;
import xyz.algogo.desktop.AppSettings;
import xyz.algogo.desktop.dialog.ErrorDialog;
import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.EditorIOListener;
import xyz.algogo.desktop.editor.component.EditorMainComponent;
import xyz.algogo.desktop.editor.component.EditorMainPaneToggleListener;
import xyz.algogo.desktop.editor.export.ExportTarget;
import xyz.algogo.desktop.editor.menubar.menu.listener.file.*;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The File editor menu.
 */

public class FileEditorMenu extends EditorMenu implements EditorIOListener, EditorMainPaneToggleListener {

	/**
	 * The recent files menu.
	 */

	private JMenu recentFiles;

	/**
	 * The export menu.
	 */

	private JMenu export;

	/**
	 * Creates a new File menu.
	 *
	 * @param editor The editor.
	 */

	public FileEditorMenu(final EditorFrame editor) {
		super(editor, "menu.file");

		editor.addIOListener(this);
		editor.getMainPane().addToggleListener(this);
	}

	@Override
	public final JMenuItem[] getMenuItems() {
		try {
			final EditorFrame editor = this.getEditor();
			final List<JMenuItem> items = new ArrayList<>();
			final AppLanguage language = editor.getAppLanguage();

			items.add(createMenu(
					"menu.file.new",
					listener -> editor.askBeforeAction(editor::newAlgorithm),
					FontIcon.of(MaterialDesign.MDI_FILE_OUTLINE),
					createCTRLKeyStroke('N')
			));

			items.add(createMenu(
					"menu.file.open",
					new OpenMenuListener(editor),
					FontIcon.of(MaterialDesign.MDI_FOLDER),
					createCTRLKeyStroke('O')
			));

			recentFiles = new JMenu(language.getString("menu.file.recent"));
			refreshRecentFiles();
			items.add(recentFiles);

			items.add(createMenu(
					"menu.file.save",
					new SaveMenuListener(editor),
					FontIcon.of(MaterialDesign.MDI_CONTENT_SAVE),
					createCTRLKeyStroke('S')
			));

			items.add(createMenu(
					"menu.file.saveAs",
					new SaveAsMenuListener(editor),
					null,
					null
			));

			items.add(null);

			export = new JMenu(language.getString("menu.file.export"));
			export.setIcon(FontIcon.of(MaterialDesign.MDI_EXPORT));
			for(final ExportTarget target : ExportTarget.getExportTargets(editor)) {
				final JMenuItem item = new JMenuItem(target.getName());
				item.addActionListener(new ExportMenuListener(editor, target));
				export.add(item);
			}

			export.setEnabled(editor.getMainPane().isShowingTree());

			items.add(export);

			items.add(createMenu(
					"menu.file.print",
					new PrintMenuListener(editor),
					FontIcon.of(MaterialDesign.MDI_PRINTER),
					createCTRLKeyStroke('P')
			));

			items.add(null);

			items.add(createMenu(
					"menu.file.close",
					actionListener -> editor.askBeforeAction(editor::dispose),
					FontIcon.of(MaterialDesign.MDI_CLOSE),
					createCTRLKeyStroke('Q')
			));

			return items.toArray(new JMenuItem[items.size()]);
		}
		catch(final Exception ex) {
			ErrorDialog.fromThrowable(ex, this.getEditor());
		}

		return new JMenuItem[0];
	}

	@Override
	public final void algorithmLoaded(final File file) {
		algorithmIOAction(file);
	}

	@Override
	public final void algorithmSaved(final File file) {
		algorithmIOAction(file);
	}

	@Override
	public final void onToggle(final EditorMainComponent oldComponent, final EditorMainComponent newComponent) {
		export.setEnabled(newComponent == this.getEditor().getMainPane().getAlgorithmTree());
	}

	/**
	 * Called when an I/O operation has been executed on the current algorithm.
	 *
	 * @param file The target file.
	 */

	private void algorithmIOAction(final File file) {
		try {
			final AppSettings appSettings = this.getEditor().getAppSettings();
			appSettings.recent.remove(file.getPath());
			appSettings.recent.push(file.getPath());
			appSettings.save();

			refreshRecentFiles();
		}
		catch(final Exception ex) {
			ErrorDialog.fromThrowable(ex, this.getEditor());
		}
	}

	/**
	 * Refresh the recent files menu.
	 *
	 * @throws IOException If any I/O exception occurs.
	 * @throws IllegalAccessException If an error occurs while loading or saving the configuration.
	 */

	private void refreshRecentFiles() throws IOException, IllegalAccessException {
		final AppSettings appSettings = this.getEditor().getAppSettings();
		final AppLanguage appLanguage = this.getEditor().getAppLanguage();

		recentFiles.removeAll();
		if(appSettings.recent.isEmpty()) {
			recentFiles.add(appLanguage.getString("menu.file.recent.empty"));
			return;
		}

		final int n = appSettings.recent.size() - 1;
		for(int i = n; i >= 0; i--) {
			final String path = appSettings.recent.get(i);
			final File target = new File(path);
			if(!target.exists()) {
				appSettings.recent.remove(path);
				continue;
			}

			final JMenuItem recentPath = new JMenuItem(path);
			recentPath.addActionListener(event -> {
				try {
					if(!target.exists()) {
						refreshRecentFiles();
						return;
					}

					this.getEditor().open(target);
				}
				catch(final Exception ex) {
					ErrorDialog.fromThrowable(ex, this.getEditor());
				}
			});
			recentFiles.add(recentPath);
		}
		appSettings.save();
		recentFiles.addSeparator();

		final JMenuItem clearPaths = new JMenuItem(appLanguage.getString("menu.file.recent.clear"));
		clearPaths.addActionListener(event -> {
			try {
				appSettings.recent.clear();
				appSettings.save();

				refreshRecentFiles();
			}
			catch(final Exception ex) {
				ErrorDialog.fromThrowable(ex, this.getEditor());
			}
		});
		recentFiles.add(clearPaths);
	}

}