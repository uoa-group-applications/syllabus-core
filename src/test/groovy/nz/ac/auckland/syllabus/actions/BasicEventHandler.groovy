package nz.ac.auckland.syllabus.actions

import groovy.transform.CompileStatic
import nz.ac.auckland.syllabus.events.Event
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * User: marnix
 * Date: 25/03/13
 * Time: 11:08 AM
 *
 * Test implementation for event handler
 */
@CompileStatic
@Event(name = "MyAction", namespace = "pcf")
class BasicEventHandler {

	private static final Logger LOG = LoggerFactory.getLogger(BasicEventHandler.class);

	public String name;
	public String description;

	/**
	 * Input handler test
	 *
	 * @param input
	 */
	public Output handleEvent(Input input) {
		this.name = input.name;
		this.description = input.description;

		Output o = new Output();
		o.output = "Returned this"
		return o
	}


	static class Output {
		String output
	}

	/**
	 * Payload
	 */
	static class Input {

		String name;
		String description;

	}
}
