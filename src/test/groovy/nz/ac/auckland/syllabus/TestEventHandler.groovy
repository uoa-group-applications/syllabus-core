package nz.ac.auckland.syllabus

import groovy.transform.CompileStatic
import junit.framework.TestCase
import org.junit.Test

import nz.ac.auckland.syllabus.actions.BasicEventHandler

/**
 * User: marnix
 * Date: 25/03/13
 * Time: 11:20 AM
 *
 * Test event handler functionality
 */
@CompileStatic
class TestEventHandler extends TestCase {

	@Test
	public void testDefaultBehaviors() {
		BasicEventHandler eh = new BasicEventHandler();
		BasicEventHandler.Input inp = new BasicEventHandler.Input();
		inp.description = "lorem";
		inp.name = "title";

		eh.handleEvent(inp);

		assert eh.name == inp.name;
		assert eh.description == eh.description;
	}

}
