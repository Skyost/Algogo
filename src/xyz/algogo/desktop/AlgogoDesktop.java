package xyz.algogo.desktop;

import java.awt.Font;
import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Properties;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.jtattoo.plaf.acryl.AcrylLookAndFeel;

import xyz.algogo.desktop.dialogs.ErrorDialog;
import xyz.algogo.desktop.frames.EditorFrame;
import xyz.algogo.desktop.utils.Utils;

public class AlgogoDesktop {
	
	public static final String APP_NAME = "Algogo Desktop";
	public static final String APP_VERSION = "0.2.4";
	public static final String[] APP_AUTHORS = new String[]{"Skyost"};
	public static final String APP_WEBSITE = "http://www.algogo.xyz";
	
	public static Font CONSOLE_FONT;
	public static AppSettings SETTINGS;
	
	public static final void main(final String[] args) {
		try {
			Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

				@Override
				public final void uncaughtException(final Thread thread, final Throwable throwable) {
					ErrorDialog.errorMessage(null, throwable);
				}
				
			});
			SETTINGS = new AppSettings(new File(Utils.getParentFolder(), "settings.json"));
			SETTINGS.load();
			final Properties properties = new Properties();
			properties.put("logoString", APP_NAME);
			AcrylLookAndFeel.setTheme(properties);
			UIManager.setLookAndFeel(new AcrylLookAndFeel());
			final Icon empty = new ImageIcon();
			UIManager.put("Tree.collapsedIcon", empty);
			UIManager.put("Tree.expandedIcon", empty);
			CONSOLE_FONT = Font.createFont(Font.TRUETYPE_FONT, AlgogoDesktop.class.getResourceAsStream("/xyz/algogo/desktop/res/fonts/DejaVuSansMono.ttf")).deriveFont(12.0f);
			SwingUtilities.invokeLater(new Runnable() {
	
				@Override
				public final void run() {
					final EditorFrame frame = new EditorFrame();
					frame.setVisible(true);
					if(args == null || args.length == 0) {
						return;
					}
					final File file = new File(args[0]);
					if(file.exists()) {
						try {
							frame.open(file);
						}
						catch(final Exception ex) {
							ex.printStackTrace();
						}
					}
				}
				
			});
		}
		catch(final Exception ex) {
			ErrorDialog.errorMessage(null, ex);
		}
	}
	
}