package nz.ac.auckland.syllabus.hooks

/**
 * User: marnix
 * Date: 3/04/13
 * Time: 12:37 PM
 *
 * This exception is thrown when an error occured in a @BeforeEvent element
 */
class EventHookException extends Exception {

	EventHookException(String message) {
		super(message)
	}

	EventHookException(String message, Throwable cause) {
		super(message, cause)
	}

}
