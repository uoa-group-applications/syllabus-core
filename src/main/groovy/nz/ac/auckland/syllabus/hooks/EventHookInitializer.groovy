package nz.ac.auckland.syllabus.hooks

import groovy.transform.CompileStatic
import nz.ac.auckland.syllabus.generator.EventHandlerConfig

/**
 * User: marnix
 * Date: 3/04/13
 * Time: 1:11 PM
 *
 * Is an interface that will be invoked when present on EventHook instance.
 */
@CompileStatic
interface EventHookInitializer {

	/**
	 * Called with a list of all event handlers that might need some sort of initialization
	 *
	 * @param eventHandlerList is a list of event handlers
	 */
	public void initializeHook(List<EventHandlerConfig> eventHandlerList) throws EventHookException

}
