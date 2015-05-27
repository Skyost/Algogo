package fr.skyost.algo.desktop.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Properties;

import fr.skyost.algo.desktop.AlgogoDesktop;
import fr.skyost.algo.desktop.dialogs.ErrorDialog;

/**
 * A language manager, used to translate Algogo.
 * 
 * @author Skyost.
 */

public class LanguageManager {
	
	private static final HashMap<String, String> strings = new HashMap<String, String>();
	static {
		try {
			final Properties properties = new Properties();
			final InputStream input = AlgogoDesktop.class.getResourceAsStream("/fr/skyost/algo/desktop/res/lang/" + Locale.getDefault().getLanguage() + ".lang");
			properties.load(new InputStreamReader(input == null ? AlgogoDesktop.class.getResourceAsStream("/fr/skyost/algo/desktop/res/lang/en.lang") : input, StandardCharsets.UTF_8));
			for(final Entry<Object, Object> entry : properties.entrySet()) {
				strings.put(entry.getKey().toString(), entry.getValue().toString());
			}
		}
		catch(final Exception ex) {
			ex.printStackTrace();
			ErrorDialog.errorMessage(null, ex);
		}
	}
	
	/**
	 * Gets the String for the specified key.
	 * 
	 * @param key The String's key.
	 * 
	 * @return The String.
	 */
	
	public static final String getString(final String key) {
		return strings.get(key);
	}
	
	/**
	 * Gets the language's name.
	 * 
	 * @return The name.
	 */
	
	public static final String getCurrentLanguageName() {
		return getString("language.name");
	}
	
	/**
	 * Gets the translation's version.
	 * 
	 * @return The version.
	 */
	
	public static final int getCurrentLanguageVersion() {
		return Integer.parseInt(getString("language.version"));
	}

}