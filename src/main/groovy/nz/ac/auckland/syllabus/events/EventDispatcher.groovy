package nz.ac.auckland.syllabus.events

import groovy.transform.CompileStatic
import nz.ac.auckland.syllabus.SyllabusContext
import nz.ac.auckland.syllabus.errors.TransmissionException
import nz.ac.auckland.syllabus.hooks.EventHookException

/**
 * User: marnix
 *
 * This class dispatches events and marshals between JSON and the payload types specified on the events
 *
 * @author Richard Vowles - https://plus.google.com/+RichardVowles
 * @author Marnix
 */
@CompileStatic
interface EventDispatcher {
	interface DecodeCallback {
		/**
		 * Based on whatever was passed, this is a request to decode this object
		 *
		 * @return - the deserialized object
		 */
		public Object decode(SyllabusContext context)
	}

	/**
	 * A request to find the handler that will deal with this method and its incoming data type so we can call it.
	 * This method calls back to get the object to pass, allowing the calling layer to implement support for JSON, XML and DOGE.
	 *
	 * @param action
	 * @param namespace
	 * @param version
	 * @param requestBody
	 * @param decodeCallback
	 *
	 * @return the object to be passed to the client
	 */

	public Object dispatch(SyllabusContext context, DecodeCallback decodeCallback)
		throws TransmissionException, EventHookException
}
