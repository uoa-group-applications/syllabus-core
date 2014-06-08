package nz.ac.auckland.syllabus.errors

import groovy.transform.CompileStatic
import nz.ac.auckland.common.stereotypes.UniversityComponent
import nz.ac.auckland.syllabus.payload.ErrorResponse
import nz.ac.auckland.util.JacksonHelperApi
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.inject.Inject;

/**
 * A default exception handler that catches all that haven't already been accounted for in other handlers. Stops any
 * errors bubbling up to the connection layer.
 * <p>Author: <a href="http://gplus.to/tzrlk">Peter Cummuskey</a></p>
 */
@CompileStatic
@UniversityComponent
public class UncaughtExceptionHandler implements SyllabusExceptionHandler {
	private static final Logger LOG = LoggerFactory.getLogger(UncaughtExceptionHandler);

	public static final String ERROR_UNKNOWN = 'error.unknown';
	public static final String ERROR_MESSAGE = 'An unhandled exception has been thrown by the application';

	@Inject
	protected JacksonHelperApi jacksonHelperApi

	/**
	 * Returns a basic error response to avoid exceptions bubbling any further toward the connection.
	 * @param exception The exception being thrown.
	 */
	@Override
	public ErrorResponse handleError(Exception exception) {
		Map errorIdentifier = [uuid: UUID.randomUUID().toString()];
		LOG.error("$ERROR_MESSAGE: ${jacksonHelperApi.jsonSerialize(errorIdentifier)}", exception);
		return new ErrorResponse(error: ERROR_UNKNOWN, context: errorIdentifier);
	}

}
