package nz.ac.auckland.syllabus.hooks

import nz.ac.auckland.syllabus.events.EventHandler

/**
 * User: marnix
 * Date: 3/04/13
 * Time: 1:11 PM
 *
 * Is an interface that will be invoked when present on EventHook instance.
 */
interface EventHookInitializer {

	/**
	 * Called with a list of all event handlers that might need some sort of initialization
	 *
	 * @param eventHandlerList is a list of event handlers
	 */
	public void initializeHook(List<EventHandler> eventHandlerList) throws EventHookException

}
