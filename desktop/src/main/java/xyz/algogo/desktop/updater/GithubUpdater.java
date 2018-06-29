package xyz.algogo.desktop.updater;

import com.goebl.david.Response;
import com.goebl.david.Webb;
import org.json.JSONObject;
import xyz.algogo.desktop.AlgogoDesktop;

import java.util.HashSet;

/**
 * Ore update checker.
 */

public class GithubUpdater extends Thread {

	/**
	 * Updater listeners.
	 */

	private final HashSet<GithubUpdaterListener> listeners = new HashSet<>();

	/**
	 * Github repository.
	 */

	public static final String REPOSITORY = "Skyost/Algogo";

	@Override
	public final void run() {
		try {
			for(final GithubUpdaterListener listener : listeners) {
				listener.updaterStarted();
			}

			// Now we can request the remote version.
			final Webb webb = Webb.create();
			webb.setBaseUri("https://api.github.com");

			final Response<JSONObject> response = webb.get("/repos/" + REPOSITORY + "/releases/latest").ensureSuccess().asJsonObject();
			final String version = response.getBody().getString("tag_name").substring(1);

			// And we can compare them.
			if(versionCompare(AlgogoDesktop.APP_VERSION.substring(1), version) >= 0) {
				for(final GithubUpdaterListener listener : listeners) {
					listener.updaterNoUpdate(AlgogoDesktop.APP_VERSION, version);
				}
				return;
			}

			for(final GithubUpdaterListener listener : listeners) {
				listener.updaterUpdateFound(AlgogoDesktop.APP_VERSION, version);
			}
		}
		catch(final Exception ex) {
			for(final GithubUpdaterListener listener : listeners) {
				listener.updaterException(ex);
			}
		}
	}

	/**
	 * Compares two version strings.
	 * <br>
	 * <br>Use this instead of String.compareTo() for a non-lexicographical
	 * <br>comparison that works for version strings. e.g. "1.10".compareTo("1.6").
	 * <br><strong>NOTE :</strong> It does not work if "1.10" is supposed to be equal to "1.10.0".
	 *
	 * @param str1 a string of ordinal numbers separated by decimal points.
	 * @param str2 a string of ordinal numbers separated by decimal points.
	 * @return The result is a negative integer if str1 is _numerically_ less than str2.
	 * <br>The result is a positive integer if str1 is _numerically_ greater than str2.
	 * <br>The result is zero if the strings are _numerically_ equal.
	 *
	 * @author Alex Gitelman.
	 */

	private static int versionCompare(final String str1, final String str2) {
		final String[] vals1 = str1.split("\\.");
		final String[] vals2 = str2.split("\\.");
		int i = 0;
		// set index to first non-equal ordinal or length of shortest version string
		while(i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
			i++;
		}
		// compare first non-equal ordinal number
		if(i < vals1.length && i < vals2.length) {
			int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
			return Integer.signum(diff);
		}
		// the strings are equal or one string is a substring of the other
		// e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
		return Integer.signum(vals1.length - vals2.length);
	}

	/**
	 * Adds a listener.
	 *
	 * @param listener The listener.
	 */

	public final void addListener(final GithubUpdaterListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes a listener.
	 *
	 * @param listener The listener.
	 */

	public final void removeListener(final GithubUpdaterListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Clears all listeners.
	 */

	public final void clearListeners() {
		listeners.clear();
	}

}