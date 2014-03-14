package nz.ac.auckland.syllabus.errors

/**
 * User: marnix
 * Date: 26/03/13
 * Time: 8:53 AM
 *
 * Exception that is thrown in syllabus code when things go wrong
 */
class TransmissionException extends Exception {

	/**
	 * Status code
	 */
	private int statusCode;

	/**
	 * Message
	 */
	private String message;

	/**
	 * Initialize the syllabus exception
	 *
	 * @param message is the message to return
	 * @param statusCode is the status code, -1 means there's no response to be set
	 */
	public TransmissionException(String message, int statusCode = 502) {
		this.message = message
		this.statusCode = statusCode
	}

	int getStatusCode() {
		return statusCode
	}

	void setStatusCode(int statusCode) {
		this.statusCode = statusCode
	}

	String getMessage() {
		return message
	}

	void setMessage(String message) {
		this.message = message
	}

}
