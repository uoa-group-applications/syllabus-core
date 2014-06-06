package nz.ac.auckland.syllabus.events

import nz.ac.auckland.lmz.common.LmzAppVersion

import javax.inject.Inject

/**
 * This event simultaneously prevents Spring from throwing a fit when it can't find any @Event-s on the classpath, and
 * also provides an easy way to check what version an application may be.
 *
 * <p>Author: <a href="http://gplus.to/tzrlk">Peter Cummuskey</a></p>
 */
@Event(name = "version", namespace = "meta")
public class AppVersionEvent {
	@Inject LmzAppVersion appVersion

	/**
	 * Simply wraps the version in the required response object.
	 */
	public AppVersionResponse handle() {
		return new AppVersionResponse(version: appVersion.version);
	}

	/**
	 * The only response parameter should be the version
	 */
	public static class AppVersionResponse {
		public String version;
	}
}
