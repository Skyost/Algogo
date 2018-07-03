package xyz.algogo.core.language;

import xyz.algogo.core.Algorithm;
import xyz.algogo.core.statement.simple.comment.LineComment;

import java.util.HashMap;

/**
 * Represents an Algogo Core language.
 */

public abstract class Language {

	/**
	 * All translations.
	 */

	private final HashMap<Class<? extends Translatable>, TranslationFunction> translations = new HashMap<>();

	/**
	 * The language name.
	 */
	
	private String name;

	/**
	 * The language extension.
	 */

	private String extension;

	/**
	 * Creates a new language.
	 *
	 * @param name The language name.
	 */
	
	public Language(final String name) {
		this(name, null);
	}

	/**
	 * Creates a new language.
	 *
	 * @param name The language name.
	 * @param extension The language extension.
	 */
	
	public Language(final String name, final String extension) {
		this.name = name;
		this.extension = extension;

		translations.put(Algorithm.class, (TranslationFunction<Algorithm>) algorithm -> {
			String content = getHeader() == null ? "" : getHeader();
			content += translate(algorithm.getRootBlock());
			content += getFooter() == null ? "" : getFooter();

			return content;
		});
	}

	/**
	 * Returns the language name.
	 *
	 * @return The language name.
	 */
	
	public String getName() {
		return name;
	}

	/**
	 * Sets the language name.
	 *
	 * @param name The name.
	 */
	
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Returns the language extension.
	 *
	 * @return The language extension.
	 */
	
	public String getExtension() {
		return extension;
	}

	/**
	 * Sets the language extension.
	 *
	 * @param extension The extension.
	 */
	
	public void setExtension(final String extension) {
		this.extension = extension;
	}

	/**
	 * Returns the language header.
	 *
	 * @return The language header.
	 */

	public String getHeader() {
		return null;
	}

	/**
	 * Returns the language footer.
	 *
	 * @return The language footer.
	 */

	public String getFooter() {
		return null;
	}

	/**
	 * Translates a translatable.
	 *
	 * @param translatable The translatable.
	 *
	 * @return The translated element.
	 */

	public String translate(final Translatable translatable) {
		final TranslationFunction function = translations.get(translatable.getClass());
		if(function == null) {
			final TranslationFunction commentFunction = translations.get(LineComment.class);
			if(commentFunction == null) {
				return "";
			}

			return commentFunction.translate(new LineComment("Cannot translate \"" + translatable.getClass().getName() + "\" in this language. Remember that translation implementation is still a Beta feature !"));
		}

		return function.translate(translatable);
	}

	/**
	 * Returns the corresponding translation function.
	 *
	 * @param translatableClass The translatable class.
	 *
	 * @return The corresponding translation function.
	 */

	public TranslationFunction getTranslationFunction(final Class<? extends Translatable> translatableClass) {
		return translations.get(translatableClass);
	}

	/**
	 * Puts a translation.
	 *
	 * @param translatableClass The translatable class.
	 * @param function The translation function.
	 */

	protected void putTranslation(final Class<? extends Translatable> translatableClass, final TranslationFunction function) {
		translations.put(translatableClass, function);
	}

	/**
	 * Functional interface that allows to translate a Translatable object.
	 * <br><b>NOTE :</b> No <em>@FunctionalInterface</em> used because of Android implementation.
	 *
	 * @param <T> The translatable type.
	 */

	public interface TranslationFunction<T extends Translatable> {

		String translate(final T translatable);

	}

}