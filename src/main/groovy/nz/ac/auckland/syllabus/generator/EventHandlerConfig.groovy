package nz.ac.auckland.syllabus.generator

import nz.ac.auckland.syllabus.events.Event
import nz.ac.auckland.syllabus.events.EventHandler
import nz.ac.auckland.syllabus.payload.EventRequestBase
import nz.ac.auckland.syllabus.payload.EventResponseBase

/**
 * Author: Marnix
 *
 * Event Handler configuration combines an event handler instance and the
 * information normally placed in the configuration.
 */
class EventHandlerConfig {

	/**
	 * Is the namespace tho place this handler in
	 */
	String namespace = Event.DEFAULT_NAMESPACE;

	/**
	 * Is the name of the handler
	 */
	String name = null;

	/**
	 * Event handler instance
	 */
	EventHandler<? extends EventRequestBase, ? extends EventResponseBase> handler;

}
