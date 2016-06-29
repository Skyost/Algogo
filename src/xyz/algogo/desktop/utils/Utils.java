package xyz.algogo.desktop.utils;

import java.awt.Component;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import xyz.algogo.desktop.AlgogoDesktop;

public class Utils {

	/**
	 * Strips accents of the input String.
	 * 
	 * @param input The input String.
	 * 
	 * @return A String without accents.
	 */
	
	public static String stripAccents(final String input) {
		return Normalizer.normalize(input, Normalizer.Form.NFD).replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
	}

	/**
	 * Escapes HTML characters of the specified String.
	 * 
	 * @param string The String.
	 * 
	 * @return An escaped String.
	 */

	public static final String escapeHTML(final String string) {
		final StringBuilder builder = new StringBuilder(Math.max(16, string.length()));
		for(int i = 0; i < string.length(); i++) {
			final char charr = string.charAt(i);
			if(charr > 127 || charr == '"' || charr == '<' || charr == '>' || charr == '&') {
				builder.append("&#");
				builder.append((int)charr);
				builder.append(';');
			}
			else {
				builder.append(charr);
			}
		}
		return builder.toString();
	}

	/**
	 * Joins a String array.
	 * 
	 * @param joiner The String used to join arrays.
	 * @param strings The array.
	 * 
	 * @return The joined array.
	 */

	public static final String join(final String joiner, final String... strings) {
		final StringBuilder builder = new StringBuilder();
		for(final String string : strings) {
			builder.append(string + joiner);
		}
		builder.setLength(builder.length() - joiner.length());
		return builder.toString();
	}

	/**
	 * Creates a dialog.
	 * 
	 * @param component The parent component.
	 * @param title The dialog's title.
	 * @param message The message of the dialog.
	 * @param tip The dialog's tip.
	 * @param objects The objects of the dialog.
	 * 
	 * @return <b>true</b> If the user has clicked on "OK".
	 * <br><b>false</b> Otherwise.
	 */

	public static final boolean createDialog(final Component component, final String title, final String message, final String tip, final Object... objects) {
		final List<Object> components = new ArrayList<Object>();
		components.add(new JLabel(message));
		components.addAll(Arrays.asList(objects));
		if(tip != null) {
			components.add(new JLabel("<html><b>" + LanguageManager.getString("utils.tip") + " :</b> <i>" + tip + "</i></html>"));
		}
		return JOptionPane.showConfirmDialog(component, components.toArray(new Object[components.size()]), title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION;
	}

	/**
	 * Checks if a String is alpha.
	 * 
	 * @param string The String.
	 * 
	 * @return <b>true</b> If the String is alpha.
	 * <br><b>false</b> Otherwise.
	 */

	public static final boolean isAlpha(final String string) {
		for(char charr : string.toCharArray()) {
			if(!Character.isLetter(charr)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Gets the JAR parent folder.
	 * 
	 * @return The JAR parent folder.
	 * 
	 * @throws URISyntaxException If the destination is not a valid path.
	 */

	public static final File getParentFolder() throws URISyntaxException {
		return new File(AlgogoDesktop.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
	}

	/**
	 * GZIP a String.
	 * 
	 * @param string The String.
	 * 
	 * @return The compressed String.
	 * 
	 * @throws IOException If an Exception occurs.
	 */

	public static final byte[] gzip(final String string) throws IOException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final GZIPOutputStream gzip = new GZIPOutputStream(output);
		final OutputStreamWriter writer = new OutputStreamWriter(gzip, StandardCharsets.UTF_8);
		writer.write(string);
		writer.close();
		gzip.close();
		writer.close();
		return output.toByteArray();
	}

	/**
	 * UnGZIP a String.
	 * 
	 * @param bytes The String (bytes).
	 * 
	 * @return The unGZIPed String.
	 * 
	 * @throws IOException If an Exception occurs.
	 */

	public static final String ungzip(final byte[] bytes) throws IOException {
		final InputStreamReader input = new InputStreamReader(new GZIPInputStream(new ByteArrayInputStream(bytes)), StandardCharsets.UTF_8);
		final StringWriter writer = new StringWriter();
		final char[] chars = new char[1024];
		for(int length; (length = input.read(chars)) > 0;) {
			writer.write(chars, 0, length);
		}
		return writer.toString();
	}
	
	/**
	 * Checks if the given String is a boolean.
	 * 
	 * @param input The String.
	 * 
	 * @return <b>true</b> If the String is a boolean.
	 * <br><b>false</b> Otherwise.
	 */
	
	public static final boolean isBoolean(final String input) {
		try {
			Boolean.valueOf(input);
			return true;
		}
		catch(final Exception ex) {}
		return false;
	}

}