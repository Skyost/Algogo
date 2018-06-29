package xyz.algogo.desktop;

import xyz.algogo.desktop.utils.SizedStack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.Properties;

/**
 * Useful class that allows to load and save a settings file.
 */

public class AppSettings {

	/**
	 * Recent files limit.
	 */

	private static final transient short RECENT_LIMIT = 10;

	@SerializationOptions(name = "auto-updater")
	public boolean autoUpdater = true;
	@SerializationOptions(name = "custom-language")
	public String customLanguage = AppLanguage.isLanguageAvailable(Locale.getDefault().getLanguage().toLowerCase()) ? Locale.getDefault().getLanguage().toLowerCase() : AppLanguage.getAvailableLanguages()[0];
	@SerializationOptions(name = "recents")
	public SizedStack<String> recent = new SizedStack<>(RECENT_LIMIT);

	/**
	 * The config file.
	 */

	private transient File file;

	/**
	 * Creates a new application settings.
	 *
	 * @param file The config file.
	 */

	public AppSettings(final File file) {
		this.file = file;
	}

	/**
	 * Returns the config file.
	 *
	 * @return The config file.
	 */

	public final File getFile() {
		return file;
	}

	/**
	 * Sets the config file.
	 *
	 * @param file The file.
	 */

	public final void setFile(final File file) {
		this.file = file;
	}

	/**
	 * Loads the configuration file.
	 *
	 * @throws IOException If any I/O exception occurs.
	 * @throws IllegalAccessException If a field is not accessible.
	 */

	public final void load() throws IOException, IllegalAccessException {
		if(file == null) {
			return;
		}

		if(!file.exists()) {
			this.save();
			return;
		}

		boolean needToSave = false;

		final Properties properties = new Properties();
		final FileInputStream input = new FileInputStream(file);
		properties.load(input);
		input.close();

		for(final Field field : this.getClass().getFields()) {
			final SerializationOptions options = this.getAnnotation(field);
			if(options == null) {
				continue;
			}

			final Class<?> type = field.getType();
			if(type.isAssignableFrom(SizedStack.class)) {
				final String name = options.name();
				final SizedStack<String> list = new SizedStack<>(RECENT_LIMIT);

				for(int i = 0;; i++) {
					final String value = properties.getProperty(name + "." + i);
					if(value == null) {
						break;
					}

					list.push(value);
				}
				field.set(this, list);
			}

			final String value = properties.getProperty(options.name());
			if(value == null) {
				needToSave = true;
				continue;
			}

			if(type.isAssignableFrom(boolean.class)) {
				field.set(this, Boolean.parseBoolean(value));
				continue;
			}

			if(type.isAssignableFrom(String.class)) {
				field.set(this, value);
				continue;
			}

			if(type.isAssignableFrom(int.class)) {
				field.set(this, Integer.parseInt(value));
			}
		}
		if(needToSave) {
			save();
		}
	}

	/**
	 * Saves the configuration file.
	 *
	 * @throws IOException If any I/O exception occurs.
	 * @throws IllegalAccessException If a field is not accessible.
	 */

	public final void save() throws IOException, IllegalAccessException {
		if(file == null) {
			return;
		}

		final Properties properties = new Properties();
		for(final Field field : this.getClass().getFields()) {
			final SerializationOptions options = this.getAnnotation(field);
			if(options == null) {
				continue;
			}

			final Class<?> type = field.getType();
			if(type.isAssignableFrom(boolean.class) || type.isAssignableFrom(int.class) || type.isAssignableFrom(String.class)) {
				properties.setProperty(options.name(), field.get(this).toString());
				continue;
			}

			if(type.isAssignableFrom(SizedStack.class)) {
				final String name = options.name();
				final SizedStack stack = (SizedStack)field.get(this);
				for(int i = 0; i < stack.size(); i++) {
					properties.setProperty(name + "." + i, stack.get(i).toString());
				}
			}
		}

		if(file.exists()) {
			file.delete();
		}

		final FileOutputStream output = new FileOutputStream(file);
		properties.store(output, AlgogoDesktop.APP_NAME + " Preferences");
		output.close();
	}

	/**
	 * Gets a field held annotation.
	 *
	 * @param field The field.
	 *
	 * @return The held annotation.
	 */

	private SerializationOptions getAnnotation(final Field field) {
		if(Modifier.isTransient(field.getModifiers())) {
			return null;
		}
		return field.getAnnotation(SerializationOptions.class);
	}

	/**
	 * Annotation that allows to specify some config options.
	 */

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	private @interface SerializationOptions {

		/**
		 * Serialized field name.
		 *
		 * @return The serialized field name.
		 */

		String name();

	}

}