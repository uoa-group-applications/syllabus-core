package nz.ac.auckland.syllabus

import groovy.transform.CompileStatic
import nz.ac.auckland.common.testrunner.BatheCommandLine
import nz.ac.auckland.common.testrunner.SimpleSpringRunner
import nz.ac.auckland.syllabus.actions.BasicEventHandler
import nz.ac.auckland.syllabus.actions.SignalHandler
import nz.ac.auckland.syllabus.events.EventHandlerCollection
import nz.ac.auckland.syllabus.generator.EventHandlerConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration

import javax.inject.Inject

/**
 * User: marnix
 * Date: 25/03/13
 * Time: 11:33 AM
 *
 * Test functionality of EventHandlerCollection class
 */
@CompileStatic
@BatheCommandLine(["-Pclasspath:/test.properties"])
@RunWith(SimpleSpringRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class EventCollectionTest {
	@Inject
	ApplicationContext applicationContext;

	@Test
	public void canFindEventHandler() {

		EventHandlerCollection ec = applicationContext.getBean(EventHandlerCollection.class)

		List<EventHandlerConfig> foundBeans = ec.findAll();

		List<Class> classes = foundBeans.collect({EventHandlerConfig cfg -> cfg.instance.getClass()})

		assert foundBeans.size() == 2 + 1; // One is the AppVersionEvent.
		assert classes.contains(BasicEventHandler.class);
		assert classes.contains(SignalHandler.class);
	}


	@Test
	public void createsEventMapProperly() {
		EventHandlerCollection ec = applicationContext.getBean(EventHandlerCollection.class)

		// event map namespaces
		assert ec.eventMap['app'] != null
		assert ec.eventMap['pcf'] != null

		// test classes
		assert ec.eventMap['app']['MySignalEvent'].instance.getClass() == SignalHandler.class
		assert ec.eventMap['pcf']['MyAction'].instance.class == BasicEventHandler.class
	}

	/**
	 * Try and find something by a specific class
	 */
	@Test
	public void canFindSpecificHandler() {
		EventHandlerCollection ec = applicationContext.getBean(EventHandlerCollection.class)

		assert ec.findByName("MyAction") == null
		assert ec.findByName("MyAction", "pcf").instance instanceof BasicEventHandler
	}

}
