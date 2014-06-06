package nz.ac.auckland.syllabus.hooks

import groovy.transform.CompileStatic
import nz.ac.auckland.syllabus.SyllabusContext
import nz.ac.auckland.syllabus.SyllabusHandle

/**
 * User: marnix
 * Date: 3/04/13
 * Time: 12:36 PM
 *
 * This is the interface to the hook that will be executed before an event handler is executed.
 */
@CompileStatic
interface EventHook {

	/**
	 * It might throw a EventHookException. The Syllabus servlet would convert this into the
	 * proper HTTP status codes, but otherwise is implementation specific.
	 *
	 * @param event is the event that is about to be executed
	 *
	 * @throws EventHookException
	 */
	public void executeHook(SyllabusContext context) throws EventHookException;

}
