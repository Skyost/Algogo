package xyz.algogo.desktop.utils;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import xyz.algogo.desktop.AlgogoDesktop;
import xyz.algogo.desktop.dialogs.ErrorDialog;

import java.util.Properties;

/**
 * A language manager, used to translate Algogo.
 * 
 * @author Skyost.
 */

public class LanguageManager {
	
	/**
	 * If a key is not found.
	 */
	
	public static final String NOT_FOUND_STRING = "Translation not found";
	
	/**
	 * The languages package.
	 */
	
	public static final String PACKAGE = "/xyz/algogo/desktop/res/lang/";
	
	/**
	 * The available languages.
	 */
	
	public static final HashMap<String, String> AVAILABLE_LANGUAGES = new HashMap<String, String>();
	
	private static final HashMap<String, String> strings = new HashMap<String, String>();
	static {
		try {
			AVAILABLE_LANGUAGES.put("fr", "Français");
			AVAILABLE_LANGUAGES.put("en", "English");
			if(AVAILABLE_LANGUAGES != null && AVAILABLE_LANGUAGES.size() > 0) {
				final Properties properties = new Properties();
				properties.load(new InputStreamReader(AlgogoDesktop.class.getResourceAsStream(PACKAGE + (AVAILABLE_LANGUAGES.get(AlgogoDesktop.SETTINGS.customLanguage) == null ? "en" : AlgogoDesktop.SETTINGS.customLanguage) + ".lang"), StandardCharsets.UTF_8));
				for(final Entry<Object, Object> entry : properties.entrySet()) {
					strings.put(entry.getKey().toString(), entry.getValue().toString());
				}
			}
		}
		catch(final Exception ex) {
			ErrorDialog.errorMessage(null, ex);
		}
	}
	
	/**
	 * Gets the String for the specified key.
	 * 
	 * @param key The String's key.
	 * @param args To automatically format the String.
	 * 
	 * @return The String.
	 */
	
	public static final String getString(final String key, final Object... args) {
		final String value = strings.get(key);
		if(value != null) {
			return args == null ? value : String.format(value, args);
		}
		return NOT_FOUND_STRING;
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
	
	/**
	 * Gets a list of available languages.
	 * 
	 * @return A map :
	 * <br><b>Key :</b> Language code.
	 * <br><b>Value :</b> Language name.
	 */
	
	public static final Map<String, String> getAvailableLanguages() {
		return new HashMap<String, String>(AVAILABLE_LANGUAGES);
	}

}