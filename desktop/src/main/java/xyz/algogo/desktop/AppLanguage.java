package xyz.algogo.desktop;

import java.util.Arrays;
import java.util.Properties;

/**
 * Useful class that allows to completely localize the program.
 */

public class AppLanguage {

	/**
	 * Array containing available languages.
	 */

	private static final String[] AVAILABLE_LANGUAGES = new String[]{"default", "fr"};

	/**
	 * Default app language (in case of a string is not found in the current language).
	 */

	private final Properties defaultLanguage;

	/**
	 * Current app language.
	 */

	private final Properties currentLanguage;

	/**
	 * Creates a new app language.
	 *
	 * @param appSettings Application settings.
	 */

	public AppLanguage(final AppSettings appSettings) {
		defaultLanguage = loadResourceProperties("default");
		currentLanguage = appSettings.customLanguage.equals("default") || !isLanguageAvailable(appSettings.customLanguage) ? null : loadResourceProperties(appSettings.customLanguage);
	}

	/**
	 * Gets available languages.
	 *
	 * @return Available languages.
	 */

	public static String[] getAvailableLanguages() {
		return AVAILABLE_LANGUAGES;
	}

	/**
	 * Checks whether a given language is available.
	 *
	 * @param languageCode The language code.
	 *
	 * @return Whether a given language is available.
	 */

	public static boolean isLanguageAvailable(final String languageCode) {
		return Arrays.asList(AVAILABLE_LANGUAGES).contains(languageCode);
	}

	/**
	 * Returns the current language title.
	 *
	 * @return The current language title.
	 */

	public String getCurrentLanguageTitle() {
		return getString("language.title");
	}

	/**
	 * Returns the current language code.
	 *
	 * @return The current language code.
	 */

	public String getCurrentLanguageCode() {
		return getString("language.code");
	}

	/**
	 * Returns a formatted string (or submitted string if not found).
	 *
	 * @param key String key.
	 * @param args Format arguments.
	 *
	 * @return The formatted string.
	 */

	public String getString(final String key, final Object... args) {
		if(currentLanguage != null && currentLanguage.containsKey(key)) {
			return String.format(currentLanguage.getProperty(key), args);
		}

		if(defaultLanguage != null && defaultLanguage.containsKey(key)) {
			return String.format(defaultLanguage.getProperty(key), args);
		}

		return key;
	}

	/**
	 * Loads a property file.
	 *
	 * @param propertyFile The property file.
	 *
	 * @return Loaded properties.
	 */

	private Properties loadResourceProperties(final String propertyFile) {
		final ClassLoader loader = Thread.currentThread().getContextClassLoader();
		final Properties properties = new Properties();

		try {
			properties.load(loader.getResourceAsStream("language/" + propertyFile + ".properties"));
			return properties;
		}
		catch(final Exception ex) {}
		return null;
	}

}