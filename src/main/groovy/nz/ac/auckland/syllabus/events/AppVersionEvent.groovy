package nz.ac.auckland.syllabus.events

import net.stickycode.stereotype.Configured
import nz.ac.auckland.common.config.ConfigKey
import nz.ac.auckland.syllabus.payload.EventRequestBase
import nz.ac.auckland.syllabus.payload.EventResponseBase
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * This event simultaneously prevents Spring from throwing a fit when it can't find any @Event-s on the classpath, and
 * also provides an easy way to check what version an application may be.
 *
 * <p>Author: <a href="http://gplus.to/tzrlk">Peter Cummuskey</a></p>
 */
@Event(name = "version", namespace = "meta")
public class AppVersionEvent implements EventHandler<AppVersionRequest, AppVersionResponse> {

	private static final Logger logger = LoggerFactory.getLogger(AppVersionEvent);

	/**
	 * @see nz.ac.auckland.common.config.JarManifestConfigurationSource#KEY_IMPLEMENTATION_VERSION
	 */
	@ConfigKey("Implementation-Version")
	protected String version = 'unknown';

	/**
	 * Simply wraps the version in the required response object.
	 */
	@Override
	public AppVersionResponse handleEvent(AppVersionRequest payload) throws Exception {
		return new AppVersionResponse(version: version);
	}

	/**
	 * No request parameters are necessary
	 */
	public static class AppVersionRequest extends EventRequestBase {
		//! nothing-to-do-here.jpg
	}

	/**
	 * The only response parameter should be the version
	 */
	public static class AppVersionResponse extends EventResponseBase {
		public String version;
	}
}
