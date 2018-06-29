package xyz.algogo.desktop.dialog;

import xyz.algogo.desktop.AlgogoDesktop;
import xyz.algogo.desktop.AppLanguage;
import xyz.algogo.desktop.AppSettings;
import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.updater.GithubUpdater;
import xyz.algogo.desktop.updater.GithubUpdaterListener;
import xyz.algogo.desktop.utils.Utils;

import javax.swing.*;
import java.awt.*;

/**
 * Represents an updater dialog that allows to show the updater result to the user.
 */

public class UpdaterDialog extends JDialog implements GithubUpdaterListener {

	/**
	 * The editor.
	 */

	private EditorFrame editor;

	/**
	 * Whether unimportant states should be notified to the user.
	 */

	private boolean notifyUnimportantStates;

	/**
	 * Creates a new updater dialog.
	 *
	 * @param editor The editor.
	 * @param notifyUnimportantStates Whether unimportant states should be notified to the user.
	 */

	public UpdaterDialog(final EditorFrame editor, final boolean notifyUnimportantStates) {
		super(editor, editor.getAppLanguage().getString("updateDialog.title"));

		this.editor = editor;
		this.notifyUnimportantStates = notifyUnimportantStates;

		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setIconImages(AlgogoDesktop.ICONS);
		this.setResizable(false);
		this.setModal(true);
	}

	@Override
	public final void updaterStarted() {}

	@Override
	public final void updaterException(final Throwable throwable) {
		if(!notifyUnimportantStates) {
			return;
		}

		final JButton ok = new JButton(editor.getAppLanguage().getString("updateDialog.button.ok"));
		ok.addActionListener(actionEvent -> this.dispose());

		buildDialog("updateDialog.message.exception", new Object[0], ok);
	}

	@Override
	public final void updaterNoUpdate(final String localVersion, final String remoteVersion) {
		if(!notifyUnimportantStates) {
			return;
		}

		final JButton ok = new JButton(editor.getAppLanguage().getString("updateDialog.button.ok"));
		ok.addActionListener(actionEvent -> this.dispose());

		buildDialog("updateDialog.message.noUpdate", new Object[0], ok);
	}

	@Override
	public final void updaterUpdateFound(final String localVersion, final String remoteVersion) {
		final AppLanguage appLanguage = editor.getAppLanguage();

		final JButton github = new JButton(appLanguage.getString("updateDialog.button.github"));
		github.addActionListener(actionEvent -> {
			Utils.visitIfPossible("https://github.com/" + GithubUpdater.REPOSITORY);
			this.dispose();
		});

		final JButton cancel = new JButton(appLanguage.getString("updateDialog.button.cancel"));
		cancel.addActionListener(actionEvent -> this.dispose());

		buildDialog("updateDialog.message.updateFound", new Object[]{remoteVersion}, github, cancel);
	}

	/**
	 * Returns the editor.
	 *
	 * @return The editor.
	 */

	public final EditorFrame getEditor() {
		return editor;
	}

	/**
	 * Sets the editor.
	 *
	 * @param editor The editor.
	 */

	public final void setEditor(final EditorFrame editor) {
		this.editor = editor;
	}

	/**
	 * Returns whether unimportant states should be notified to the user.
	 *
	 * @return Whether unimportant states should be notified to the user.
	 */

	public final boolean shouldNotifyUnimportantStates() {
		return notifyUnimportantStates;
	}

	/**
	 * Sets whether unimportant states should be notified to the user.
	 *
	 * @param notifyUnimportantStates Whether unimportant states should be notified to the user.
	 */

	public final void setNotifyUnimportantStates(final boolean notifyUnimportantStates) {
		this.notifyUnimportantStates = notifyUnimportantStates;
	}

	/**
	 * Checks for updates if automatic updates are enabled in the settings.
	 */

	public final void checkForUpdatesIfNeeded() {
		if(editor.getAppSettings().autoUpdater) {
			checkForUpdates();
		}
	}

	/**
	 * Checks for updates.
	 */

	public final void checkForUpdates() {
		final GithubUpdater updater = new GithubUpdater();
		updater.addListener(this);
		updater.start();
	}

	/**
	 * Builds a dialog and shows it to the user.
	 *
	 * @param id Message ID.
	 * @param arguments Message arguments.
	 * @param footer Footer buttons.
	 */

	private void buildDialog(final String id, final Object[] arguments, final JButton... footer) {
		final AppLanguage appLanguage = editor.getAppLanguage();
		final AppSettings appSettings = editor.getAppSettings();

		final JPanel content = new JPanel(new GridLayout(0, 1));
		content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		final JLabel message = new JLabel(appLanguage.getString(id, arguments));
		content.add(message);

		final JCheckBox update = new JCheckBox(appLanguage.getString("updateDialog.enableCheckForUpdates"));
		update.setSelected(appSettings.autoUpdater);
		update.addActionListener(actionEvent -> {
			try {
				appSettings.autoUpdater = update.isSelected();
				appSettings.save();
			}
			catch(final Exception ex) {
				ErrorDialog.fromThrowable(ex, editor);
			}
		});
		content.add(update);

		content.add(Box.createRigidArea(new Dimension(0, 10)));

		final JPanel panel = new JPanel(new GridLayout());
		for(final JButton button : footer) {
			panel.add(button);
		}
		content.add(panel);

		this.setContentPane(content);
		this.pack();
		this.setLocationRelativeTo(editor);
		this.setVisible(true);
	}

}