package xyz.algogo.desktop.updater;

/**
 * Interface that allows to listen to update events.
 */

public interface GithubUpdaterListener {

	/**
	 * Triggered when the updater has been started.
	 */

	void updaterStarted();

	/**
	 * Triggered when an exception occurs.
	 *
	 * @param throwable The throwable.
	 */

	void updaterException(final Throwable throwable);

	/**
	 * Triggered when there is actually no update.
	 *
	 * @param localVersion The local version.
	 * @param remoteVersion The remote version.
	 */

	void updaterNoUpdate(final String localVersion, final String remoteVersion);

	/**
	 * Triggered when an update has been found.
	 *
	 * @param localVersion The local version.
	 * @param remoteVersion The remote version.
	 */

	void updaterUpdateFound(final String localVersion, final String remoteVersion);

}