package xyz.algogo.core.language;

/**
 * Translatable interface.
 */

public interface Translatable {

	/**
	 * Translates the current object to the specified language.
	 *
	 * @param language The language.
	 *
	 * @return The current object translated to the specified language.
	 */

	default String toLanguage(final Language language) {
		return language.translate(this);
	}

}