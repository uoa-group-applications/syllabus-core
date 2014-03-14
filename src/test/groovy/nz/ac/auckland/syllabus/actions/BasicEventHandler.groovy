package nz.ac.auckland.syllabus.actions

import groovy.transform.CompileStatic
import nz.ac.auckland.syllabus.events.Event
import org.slf4j.LoggerFactory
import org.slf4j.Logger
import nz.ac.auckland.syllabus.events.EventHandler

import nz.ac.auckland.syllabus.payload.EventResponseBase
import nz.ac.auckland.syllabus.payload.EventRequestBase

/**
 * User: marnix
 * Date: 25/03/13
 * Time: 11:08 AM
 *
 * Test implementation for event handler
 */
@CompileStatic
@Event(name = "MyAction", namespace = "pcf")
class BasicEventHandler implements EventHandler<BasicEventHandler.Input, BasicEventHandler.Output> {

	private static final Logger LOG = LoggerFactory.getLogger(BasicEventHandler.class);

	public String name;
	public String description;

	/**
	 * Input handler test
	 *
	 * @param input
	 */
	public Output handleEvent(Input input) throws Exception {
		this.name = input.name;
		this.description = input.description;

		Output o = new Output();
		o.output = "Returned this"
		return o
	}


	static class Output extends EventResponseBase {
		String output
	}

	/**
	 * Payload
	 */
	static class Input extends EventRequestBase {

		String name;
		String description;

	}
}
