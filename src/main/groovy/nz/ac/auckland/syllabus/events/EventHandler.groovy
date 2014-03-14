package nz.ac.auckland.syllabus.events

import nz.ac.auckland.syllabus.payload.EventRequestBase
import nz.ac.auckland.syllabus.payload.EventResponseBase

/**
 * User: marnix
 * Date: 25/03/13
 * Time: 11:36 AM
 *
 * Implement this class for every eventHandler
 */
interface EventHandler<RequestType extends EventRequestBase, ResponseType extends EventResponseBase> {

	/**
	 * Event handler
	 *
	 * @param payload is the message that has been sent
	 */
	public ResponseType handleEvent(RequestType payload) throws Exception;

}
