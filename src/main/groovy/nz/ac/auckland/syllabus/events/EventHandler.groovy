package nz.ac.auckland.syllabus.events

import nz.ac.auckland.syllabus.payload.EventRequestBase
import nz.ac.auckland.syllabus.payload.EventResponseBase

/**
 * Do not use this class any longer.
 *
 * @author: Richard Vowles - https://plus.google.com/+RichardVowles
 */
@Deprecated
interface EventHandler<RequestType extends EventRequestBase, ResponseType extends EventResponseBase> {

	/**
	 * Event handler
	 *
	 * @param payload is the message that has been sent
	 */
	public ResponseType handleEvent(RequestType payload) throws Exception;

}
