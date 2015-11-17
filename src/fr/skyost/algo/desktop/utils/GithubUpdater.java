package fr.skyost.algo.desktop.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;

/**
 * An update checker based on Github releases.
 * 
 * @author Skyost.
 */

public class GithubUpdater extends Thread {
	
	public static final String UPDATER_NAME = "GithubUpdater";
	public static final String UPDATER_VERSION = "0.1";
	public static final String[] UPDATER_AUTHORS = new String[]{"Skyost"};
		
	private final String githubAuthor;
	private final String githubRepo;
	private final String localVersion;
	private final GithubUpdaterResultListener caller;
	
	/**
	 * Creates a new <b>GithubUpdater</b> instance.
	 * 
	 * @param githubAuthor The author of the Github repository.
	 * @param githubRepo The Github repository.
	 * @param localVersion The local version.
	 * @param caller The caller.
	 */
	
	public GithubUpdater(final String githubAuthor, final String githubRepo, final String localVersion, final GithubUpdaterResultListener caller) {
		this.githubAuthor = githubAuthor;
		this.githubRepo = githubRepo;
		this.localVersion = localVersion;
		this.caller = caller;
	}
	
	@Override
	public final void run() {
		caller.updaterStarted();
		try {
			final HttpURLConnection connection = (HttpURLConnection)new URL("https://api.github.com/repos/" + githubAuthor + "/" + githubRepo + "/releases").openConnection();
			connection.addRequestProperty("User-Agent", UPDATER_NAME + " by " + Utils.join(" ", UPDATER_AUTHORS) + " v" + UPDATER_VERSION);
			final String response = connection.getResponseCode() + " " + connection.getResponseMessage();
			caller.updaterResponse(response);
			if(!response.startsWith("2")) {
				return;
			}
			final InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
			final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			final JsonArray releases = Json.parse(bufferedReader.readLine()).asArray();
			if(releases.size() < 1) {
				return;
			}
			final String remoteVersion = releases.get(0).asObject().get("tag_name").asString().substring(1);
			if(compareVersions(remoteVersion, localVersion)) {
				caller.updaterUpdateAvailable(localVersion, remoteVersion);
			}
			else {
				caller.updaterNoUpdate(localVersion, remoteVersion);
			}
		}
		catch(final Exception ex) {
			caller.updaterException(ex);
		}
	}
	
	/**
	 * Compares two versions.
	 * 
	 * @param version1 The version you want to compare to.
	 * @param version2 The version you want to compare with.
	 * 
	 * @return <b>true</b> If <b>versionTo</b> is inferior than <b>versionWith</b>.
	 * <br><b>false</b> If <b>versionTo</b> is superior or equals to <b>versionWith</b>.
	 */
	
	private static final boolean compareVersions(final String versionTo, final String versionWith) {
		return normalisedVersion(versionTo, ".", 4).compareTo(normalisedVersion(versionWith, ".", 4)) > 0;
	}
	
	/**
	 * Gets the formatted name of a version.
	 * <br>Used for the method <b>compareVersions(...)</b> of this class.
	 * 
	 * @param version The version you want to format.
	 * @param separator The separator between the numbers of this version.
	 * @param maxWidth The max width of the formatted version.
	 * 
	 * @return A string which the formatted version of your version.
	 * 
	 * @author Peter Lawrey.
	 */

	private static final String normalisedVersion(final String version, final String separator, final int maxWidth) {
		final StringBuilder stringBuilder = new StringBuilder();
		for(final String normalised : Pattern.compile(separator, Pattern.LITERAL).split(version)) {
			stringBuilder.append(String.format("%" + maxWidth + 's', normalised));
		}
		return stringBuilder.toString();
	}
	
	public interface GithubUpdaterResultListener {
		
		/**
		 * When the updater starts.
		 */
		
		public void updaterStarted();
		
		/**
		 * When an Exception occurs.
		 * 
		 * @param ex
		 */
		
		public void updaterException(final Exception ex);
		
		/**
		 * The response of the request.
		 * 
		 * @param response The response.
		 */
		
		public void updaterResponse(final String response);
		
		/**
		 * If an update is available.
		 * 
		 * @param localVersion The local version (used to create the updater).
		 * @param remoteVersion The remove version.
		 */
		
		public void updaterUpdateAvailable(final String localVersion, final String remoteVersion);
		
		/**
		 * If there is no update.
		 * 
		 * @param localVersion The local version (used to create the updater).
		 * @param remoteVersion The remove version.
		 */
		
		public void updaterNoUpdate(final String localVersion, final String remoteVersion);
		
	}

}