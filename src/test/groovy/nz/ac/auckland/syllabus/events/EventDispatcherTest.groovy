package nz.ac.auckland.syllabus.events

import groovy.transform.CompileStatic
import nz.ac.auckland.syllabus.actions.BasicEventHandler
import nz.ac.auckland.syllabus.actions.SignalHandler
import nz.ac.auckland.syllabus.payload.EventRequestBase
import nz.ac.auckland.syllabus.payload.EventResponseBase
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import javax.inject.Inject

/**
 * User: marnix
 * Date: 25/03/13
 * Time: 3:53 PM
 *
 * Test the dispatcher
 */
@CompileStatic
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class EventDispatcherTest {

	private static final String PACKAGE_BASE = "nz.ac.auckland.syllabus.actions";

	@Inject
	private ApplicationContext applicationContext

	@Test
	public void canFindEventHandler() {

		EventHandlerCollection ec = applicationContext.getBean(EventHandlerCollection.class)

		List<EventHandler<?, ?>> foundBeans = ec.findAll();

		List<Class<?>> classes = []
		foundBeans?.each { EventHandler<?, ?> handler -> classes << handler.class }

		assert foundBeans.size() == 2 + 1; // The +1 is the AppVersionEvent
		assert BasicEventHandler.class in classes;
		assert SignalHandler.class in classes;
	}


	@Test
	public void findsPayloadTypeProperly() {
		EventHandlerCollection ec = applicationContext.getBean(EventHandlerCollection.class)
		EventDispatcher ed = applicationContext.getBean(EventDispatcher.class)
		EventHandler<? extends EventRequestBase, ? extends EventResponseBase> basic = ec.findByName("MyAction", "pcf")
		assert ed.getRequestType(basic) == BasicEventHandler.Input
		assert ed.getResponseType(basic) == BasicEventHandler.Output
	}

}
