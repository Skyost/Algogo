package fr.skyost.algo.desktop.utils;

import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
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
	
	/**
	 * If a key is not found.
	 */
	
	public static final String NOT_FOUND_STRING = "Translation not found";
	
	/**
	 * The languages package.
	 */
	
	public static final String PACKAGE = "/fr/skyost/algo/desktop/res/lang/";
	
	private static final HashMap<String, String> languages = new HashMap<String, String>();
	private static final HashMap<String, String> strings = new HashMap<String, String>();
	static {
		try {
			final Collection<String> availableLanguages = Utils.getResourcesInPackage(PACKAGE.replace("/", File.separator));
			if(availableLanguages != null && availableLanguages.size() > 0) {
				final Properties properties = new Properties();
				for(final String language : availableLanguages) {
					final String languageCode = language.substring(language.lastIndexOf(File.separator) + 1);
					properties.load(new InputStreamReader(AlgogoDesktop.class.getResourceAsStream(PACKAGE + languageCode), StandardCharsets.UTF_8));
					languages.put(languageCode.substring(0, languageCode.lastIndexOf(".")), properties.getProperty("language.name"));
					properties.clear();
				}
				properties.load(new InputStreamReader(AlgogoDesktop.class.getResourceAsStream(PACKAGE + (languages.get(AlgogoDesktop.SETTINGS.customLanguage) == null ? "en" : AlgogoDesktop.SETTINGS.customLanguage) + ".lang"), StandardCharsets.UTF_8));
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
		return new HashMap<String, String>(languages);
	}

}