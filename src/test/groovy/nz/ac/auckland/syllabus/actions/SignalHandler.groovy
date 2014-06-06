package nz.ac.auckland.syllabus.actions

import groovy.transform.CompileStatic
import nz.ac.auckland.syllabus.events.Event

/**
 * User: marnix
 * Date: 25/03/13
 * Time: 11:35 AM
 *
 * Is an action that has no input.
 */
@CompileStatic
@Event(name = "MySignalEvent")
class SignalHandler {

	public String handleEvent() {
		return null
	}

}
