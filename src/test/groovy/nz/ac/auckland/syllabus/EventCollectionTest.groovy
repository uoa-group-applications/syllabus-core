package nz.ac.auckland.syllabus

import groovy.transform.CompileStatic
import org.junit.Test
import nz.ac.auckland.syllabus.actions.BasicEventHandler
import nz.ac.auckland.syllabus.actions.SignalHandler

import org.junit.runner.RunWith

import org.springframework.context.ApplicationContext
import javax.inject.Inject
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.ContextConfiguration
import nz.ac.auckland.syllabus.events.EventHandlerCollection
import nz.ac.auckland.syllabus.events.EventHandler;

/**
 * User: marnix
 * Date: 25/03/13
 * Time: 11:33 AM
 *
 * Test functionality of EventHandlerCollection class
 */
@CompileStatic
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class EventCollectionTest {

	/**
	 * Where to start scanning
	 */
	private static final String PACKAGE_BASE = "nz.ac.auckland.syllabus.actions";


	@Inject
	ApplicationContext applicationContext;

	@Test
	public void canFindEventHandler() {

		EventHandlerCollection ec = applicationContext.getBean(EventHandlerCollection.class)

		List<EventHandler> foundBeans = ec.findAll();

		List<Class<?>> classes = []
		foundBeans?.each { EventHandler handler -> classes << handler.class }

		assert foundBeans.size() == 2 + 1; // One is the AppVersionEvent.
		assert BasicEventHandler.class in classes;
		assert SignalHandler.class in classes;
	}


	@Test
	public void createsEventMapProperly() {
		EventHandlerCollection ec = applicationContext.getBean(EventHandlerCollection.class)

		// event map namespaces
		assert ec.eventMap['app'] != null
		assert ec.eventMap['pcf'] != null

		// test classes
		assert ec.eventMap['app']['MySignalEvent']?.class == SignalHandler.class
		assert ec.eventMap['pcf']['MyAction']?.class == BasicEventHandler.class
	}

	/**
	 * Try and find something by a specific class
	 */
	@Test
	public void canFindSpecificHandler() {
		EventHandlerCollection ec = applicationContext.getBean(EventHandlerCollection.class)

		assert ec.findByName("MyAction") == null
		assert ec.findByName("MyAction", "pcf") instanceof BasicEventHandler
	}

}
