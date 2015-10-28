package fr.skyost.algo.desktop.utils;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.JLabel;

import fr.skyost.algo.desktop.dialogs.ErrorDialog;

/**
 * A clickable label.
 * 
 * @author Skyost.
 */

public class JLabelLink extends JLabel {

	private static final long serialVersionUID = 1L;
	
	private URL link;
	
	/**
	 * Creates a new JLabelLink instance.
	 * 
	 * @param text The JLabel's text.
	 * @param link The URL.
	 */
	
	public JLabelLink(final String text, final URL link) {
		super(text);
		this.link = link;
		registerListeners();
	}
	
	/**
	 * Creates a new JLabelLink instance.
	 * 
	 * @param image The JLabel's image.
	 * @param link The URL.
	 */
	
	public JLabelLink(final Icon image, final URL link) {
		super(image);
		this.link = link;
		registerListeners();
	}
	
	/**
	 * Gets the current link.
	 * 
	 * @return The link.
	 */
	
	public final URL getLink() {
		return link;
	}
	
	/**
	 * Sets the current link.
	 * 
	 * @param link The current link.
	 */
	
	public final void setLink(final URL link) {
		this.link = link;
	}
	
	/**
	 * Register listeners for this label.
	 */
	
	private final void registerListeners() {
		this.addMouseListener(new MouseListener() {

			@Override
			public final void mouseClicked(final MouseEvent event) {
				openBrowser(link);
			}

			@Override
			public final void mouseEntered(final MouseEvent event) {}

			@Override
			public final void mouseExited(final MouseEvent event) {}

			@Override
			public final void mousePressed(final MouseEvent event) {}

			@Override
			public final void mouseReleased(final MouseEvent event) {}
			
		});
		this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		this.setToolTipText(LanguageManager.getString("jlabellink.tooltip", link.toString()));
	}
	
	/**
	 * Opens a link if browser is supported.
	 * 
	 * @param link The link.
	 */
	
	public static final void openBrowser(final URL link) {
		try {
			if(Desktop.isDesktopSupported()) {
				Desktop.getDesktop().browse(link.toURI());
			}
		}
		catch(final Exception ex) {
			ErrorDialog.errorMessage(null, ex);
		}
	}
	
}