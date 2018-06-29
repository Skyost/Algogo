package xyz.algogo.desktop;

import com.jtattoo.plaf.acryl.AcrylLookAndFeel;
import xyz.algogo.desktop.dialog.ErrorDialog;
import xyz.algogo.desktop.editor.EditorFrame;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Main class of Algogo Desktop.
 */

public class AlgogoDesktop {

	/**
	 * App name constant.
	 */

	public static final String APP_NAME = "Algogo Desktop";

	/**
	 * App version constant.
	 */

	public static final String APP_VERSION = "v1.0";

	/**
	 * App icons list.
	 */

	public static final List<Image> ICONS = buildIconsList();

	/**
	 * Main method of the program.
	 *
	 * @param args Arguments.
	 *
	 * @throws UnsupportedLookAndFeelException If the specified look and feel is not supported by the system.
	 * @throws IOException If an I/O exception occurs.
	 * @throws IllegalAccessException If an error occurs while loading the settings.
	 */

	public static void main(final String... args) throws UnsupportedLookAndFeelException, IOException, IllegalAccessException {
		Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> ErrorDialog.fromThrowable(throwable));

		final Properties properties = new Properties();
		properties.put("logoString", APP_NAME);
		AcrylLookAndFeel.setTheme(properties);

		UIManager.setLookAndFeel(new AcrylLookAndFeel());

		File file = getJARFile();
		file = file == null ? null : new File(file.getParentFile(), "settings.properties");
		final AppSettings appSettings = new AppSettings(file);
		appSettings.load();

		final EditorFrame editor = new EditorFrame(appSettings);
		editor.setVisible(true);

		if(args.length > 0) {
			editor.open(new File(args[0]));
		}
	}

	/**
	 * Returns the JAR file.
	 *
	 * @return The JAR file.
	 */

	public static File getJARFile() {
		try {
			return new File(AlgogoDesktop.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		}
		catch(final Exception ex) {
			ErrorDialog.fromThrowable(ex);
		}
		return null;
	}

	/**
	 * Creates a list of app icons.
	 *
	 * @return A list of app icons.
	 */

	private static List<Image> buildIconsList() {
		final Image icon = Toolkit.getDefaultToolkit().getImage(Thread.currentThread().getContextClassLoader().getResource("icon.png"));
		final List<Image> icons = new ArrayList<>(Arrays.asList(
				icon.getScaledInstance(16, 16, Image.SCALE_SMOOTH),
				icon.getScaledInstance(32, 32, Image.SCALE_SMOOTH),
				icon.getScaledInstance(64, 64, Image.SCALE_SMOOTH),
				icon.getScaledInstance(128, 128, Image.SCALE_SMOOTH),
				icon.getScaledInstance(256, 256, Image.SCALE_SMOOTH),
				icon//.getScaledInstance(512, 512, Image.SCALE_SMOOTH) // Already in 512x512.
		));

		return Collections.unmodifiableList(icons);
	}

}