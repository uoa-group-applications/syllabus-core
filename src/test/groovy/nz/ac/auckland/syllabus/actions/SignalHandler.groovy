package nz.ac.auckland.syllabus.actions

import groovy.transform.CompileStatic
import nz.ac.auckland.syllabus.events.Event
import nz.ac.auckland.syllabus.events.EventHandler

import nz.ac.auckland.syllabus.payload.EventResponseBase
import nz.ac.auckland.syllabus.payload.EventRequestBase

/**
 * User: marnix
 * Date: 25/03/13
 * Time: 11:35 AM
 *
 * Is an action that has no input.
 */
@CompileStatic
@Event(name = "MySignalEvent")
class SignalHandler implements EventHandler<EventRequestBase, EventResponseBase> {

	public EventResponseBase handleEvent(EventRequestBase v) throws Exception {
		return null
	}

}
