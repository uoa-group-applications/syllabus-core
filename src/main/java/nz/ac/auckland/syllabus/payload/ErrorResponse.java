package nz.ac.auckland.syllabus.payload;

import groovy.transform.CompileStatic;

import java.util.Map;

/**
 * This response implementation contains error-specific information in the payload, and should be the returning type of
 * any {@link nz.ac.auckland.syllabus.errors.SyllabusExceptionHandler#handleError SyllabusExceptionHandler.handleError}
 * implementation.
 * <p/>
 * <p>Author: <a href="http://gplus.to/tzrlk">Peter Cummuskey</a></p>
 */
public class ErrorResponse extends EventResponseBase {

	/**
	 * The i18n code of the error being returned.
	 */
	String error;

	/**
	 * Relevant context information to assist client-handling of the error.
	 */
	Map<String, ?> context;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Map<String, ?> getContext() {
		return context;
	}

	public void setContext(Map<String, ?> context) {
		this.context = context;
	}
}
