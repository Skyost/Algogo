package xyz.algogo.desktop.editor;

import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.kordamp.ikonli.swing.FontIcon;
import xyz.algogo.desktop.AlgogoDesktop;
import xyz.algogo.desktop.AppLanguage;
import xyz.algogo.desktop.AppSettings;
import xyz.algogo.desktop.dialog.ConsoleDialog;
import xyz.algogo.desktop.dialog.UpdaterDialog;
import xyz.algogo.desktop.editor.component.EditorMainPane;
import xyz.algogo.desktop.editor.history.EditorHistory;
import xyz.algogo.desktop.editor.history.command.CreditsChangeCommand;
import xyz.algogo.desktop.editor.menubar.EditorMenuBar;
import xyz.algogo.desktop.editor.menubar.menu.listener.file.SaveMenuListener;
import xyz.algogo.desktop.editor.panel.EditorButtonsPanel;
import xyz.algogo.desktop.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashSet;

/**
 * The editor frame. One of the main classes of Algogo Desktop.
 */

public class EditorFrame extends JFrame {

	/**
	 * I/O listeners.
	 */

	private final HashSet<EditorIOListener> listeners = new HashSet<>();

	/**
	 * Editor current opened file path.
	 */

	private String currentPath;

	/**
	 * Current algorithm credits.
	 */

	private AlgorithmCredits credits;

	/**
	 * Application settings.
	 */

	private final AppSettings appSettings;

	/**
	 * Application language.
	 */

	private final AppLanguage appLanguage;

	/**
	 * Main pane.
	 */

	private final EditorMainPane mainPane;

	/**
	 * Editor history.
	 */

	private final EditorHistory history = new EditorHistory(this);

	/**
	 * Console dialog.
	 */

	private final ConsoleDialog console;

	/**
	 * Creates a new editor frame.
	 *
	 * @param appSettings The application settings.
	 */

	public EditorFrame(final AppSettings appSettings) {
		this.appSettings = appSettings;
		this.appLanguage = new AppLanguage(appSettings);
		this.credits = new AlgorithmCredits(this);
		this.mainPane = new EditorMainPane(this);
		this.console = new ConsoleDialog(this);

		this.getContentPane().setLayout(new GridBagLayout());

		this.add(mainPane, Utils.createGridBagConstraints(0, 0, 2, 1, GridBagConstraints.BOTH, 1, 1, new Insets(10, 10, 10, 5), GridBagConstraints.CENTER));
		this.add(new EditorButtonsPanel(this), Utils.createGridBagConstraints(2, 0, 1, 1, GridBagConstraints.HORIZONTAL, 0.2f, 1, new Insets(10, 5, 10, 10), GridBagConstraints.PAGE_START));

		final JButton run = new JButton(appLanguage.getString("consoleDialog.button.run"));
		run.addActionListener(actionEvent -> console.setVisible(true));
		run.setIcon(FontIcon.of(MaterialDesign.MDI_PLAY));

		this.add(run, Utils.createGridBagConstraints(2, 1, 1, 1, GridBagConstraints.HORIZONTAL, 0.2f, 1, new Insets(10, 5, 10, 10), GridBagConstraints.PAGE_END));

		newAlgorithm();

		console.dispose();
		this.addWindowListener(new EditorClosingListener(this));
		this.setJMenuBar(new EditorMenuBar(this));
		this.setIconImages(AlgogoDesktop.ICONS);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.pack();
		this.setMinimumSize(this.getSize());
		this.setLocationRelativeTo(null);

		new UpdaterDialog(this, false).checkForUpdatesIfNeeded();
	}

	/**
	 * Returns the current path.
	 *
	 * @return The current path.
	 */

	public final String getCurrentPath() {
		return currentPath;
	}

	/**
	 * Sets the current path.
	 *
	 * @param currentPath The current path.
	 */

	public final void setCurrentPath(final String currentPath) {
		this.currentPath = currentPath;
	}

	/**
	 * Returns the algorithm credits.
	 *
	 * @return The algorithm credits.
	 */

	public final AlgorithmCredits getCredits() {
		return credits;
	}

	/**
	 * Sets the algorithm credits.
	 *
	 * @param credits The algorithm credits.
	 */

	public final void setCredits(final AlgorithmCredits credits) {
		history.execute(new CreditsChangeCommand(this, credits));
	}

	/**
	 * Returns the main pane.
	 *
	 * @return The main pane.
	 */

	public final EditorMainPane getMainPane() {
		return mainPane;
	}

	/**
	 * Returns the editor history.
	 *
	 * @return The editor history.
	 */

	public final EditorHistory getEditorHistory() {
		return history;
	}

	/**
	 * Returns the application settings.
	 *
	 * @return The application settings.
	 */

	public final AppSettings getAppSettings() {
		return appSettings;
	}

	/**
	 * Returns the application language.
	 *
	 * @return The application language.
	 */

	public final AppLanguage getAppLanguage() {
		return appLanguage;
	}

	/**
	 * Creates a new algorithm.
	 */

	public final void newAlgorithm() {
		mainPane.getCurrentComponent().newAlgorithm();

		currentPath = null;
		credits = new AlgorithmCredits(this);
		history.setAlgorithmChanged(false);
		refreshTitle();

		history.clearUndoHistory();
		history.clearRedoHistory();
		history.setAlgorithmChanged(false);
	}

	/**
	 * Loads a file to the current component.
	 *
	 * @param file The file.
	 *
	 * @throws IOException If any I/O exception occurs.
	 */

	public final void open(final File file) throws IOException {
		mainPane.getCurrentComponent().open(new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8));

		appSettings.recent.remove(file.getPath());
		appSettings.recent.push(file.getPath());

		history.clearUndoHistory();
		history.clearRedoHistory();
		history.setAlgorithmChanged(false);

		currentPath = file.getPath();
		refreshTitle();

		notifyAlgorithmLoaded(file);
	}

	/**
	 * Saves the current component content to a file.
	 *
	 * @param file The file.
	 *
	 * @throws IOException If any I/O exception occurs.
	 */

	public final void save(final File file) throws IOException {
		mainPane.getCurrentComponent().save(file);

		history.setAlgorithmChanged(false);
		refreshTitle();

		notifyAlgorithmSaved(file);
	}

	/**
	 * Refreshes the frame title.
	 */

	public void refreshTitle() {
		String star = "";
		if(history.hasAlgorithmChanged()) {
			star += " *";
		}

		this.setTitle(appLanguage.getString("editor.title", credits.getTitle(), star, credits.getAuthor(), AlgogoDesktop.APP_NAME, AlgogoDesktop.APP_VERSION));
	}

	/**
	 * Asks the user before running the specified action.
	 *
	 * @param action The action.
	 */

	public void askBeforeAction(final Runnable action) {
		if(!history.hasAlgorithmChanged()) {
			action.run();
			return;
		}

		switch(JOptionPane.showConfirmDialog(this, appLanguage.getString("editor.dialog.askToSave.message"), appLanguage.getString("editor.dialog.askToSave.title"), JOptionPane.YES_NO_CANCEL_OPTION)) {
			case JOptionPane.YES_OPTION:
				new SaveMenuListener(this).actionPerformed(null);
			case JOptionPane.NO_OPTION:
				action.run();
				break;
			default:
				break;
		}
	}

	/**
	 * Registers an I/O listener.
	 *
	 * @param ioListener The I/O listener.
	 */

	public final void addIOListener(final EditorIOListener ioListener) {
		listeners.add(ioListener);
	}

	/**
	 * Removes an I/O listener.
	 *
	 * @param ioListener The I/O listener.
	 */

	public final void removeIOListener(final EditorIOListener ioListener) {
		listeners.remove(ioListener);
	}

	/**
	 * Clears all I/O listeners.
	 */

	public final void clearIOListeners() {
		listeners.clear();
	}

	/**
	 * Notify all listeners that an algorithm has been loaded.
	 *
	 * @param file The loaded file.
	 */

	private void notifyAlgorithmLoaded(final File file) {
		for(final EditorIOListener ioListener : listeners) {
			ioListener.algorithmLoaded(file);
		}
	}

	/**
	 * Notify all listeners that an algorithm has been saved.
	 *
	 * @param file The saved file.
	 */

	private void notifyAlgorithmSaved(final File file) {
		for(final EditorIOListener ioListener : listeners) {
			ioListener.algorithmSaved(file);
		}
	}

}