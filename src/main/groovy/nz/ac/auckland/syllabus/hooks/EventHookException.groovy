package nz.ac.auckland.syllabus.hooks

import groovy.transform.CompileStatic

/**
 * User: marnix
 * Date: 3/04/13
 * Time: 12:37 PM
 *
 * This exception is thrown when an error occured in a @BeforeEvent element
 */
@CompileStatic
class EventHookException extends Exception {
	int statusCode

	EventHookException(String message) {
		super(message)
	}

	EventHookException(String message, Throwable cause) {
		super(message, cause)
	}

	EventHookException(String message, int status, Throwable cause) {
		super(message, cause)

		this.statusCode = status
	}

	EventHookException(String message, int status) {
		super(message)

		this.statusCode = status
	}
}
