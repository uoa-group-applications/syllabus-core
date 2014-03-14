package nz.ac.auckland.syllabus.errors

import nz.ac.auckland.syllabus.payload.ErrorResponse

/**
 * Author: Marnix
 *
 * This interface defines a service that will gracefully handle an exception thrown during request processing, and
 * return an appropriate error response.
 */
public interface SyllabusExceptionHandler<T extends Exception> {

	/**
	 * Used by the {@link ErrorHandlerCollection} to determine the best instance to deal with a thrown exception. More
	 * specific exceptions trump less specific ones.
	 * @return The exception type this exception handler responds to.
	 */
	public Class<T> respondsTo();

	/**
	 * Given the provided exception thrown during request processing, construct and return an error response after
	 * logging relevant information and performing any other actions that need doing.
	 * @param exception The exception to look at and handle properly.
	 * @return A structured error response to provide the client with.
	 */
	public ErrorResponse handleError(T exception);

}