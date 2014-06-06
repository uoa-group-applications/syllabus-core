package nz.ac.auckland.syllabus.events

import groovy.transform.CompileStatic
import nz.ac.auckland.common.testrunner.BatheCommandLine
import nz.ac.auckland.common.testrunner.SimpleSpringRunner
import nz.ac.auckland.syllabus.actions.BasicEventHandler
import nz.ac.auckland.syllabus.actions.SignalHandler
import nz.ac.auckland.syllabus.generator.EventHandlerConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration

import javax.inject.Inject

/**
 * User: marnix
 * Date: 25/03/13
 * Time: 3:53 PM
 *
 * Test the dispatcher
 */
@CompileStatic
@BatheCommandLine(["-Pclasspath:/test.properties"])
@RunWith(SimpleSpringRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class EventDispatcherTest {

	@Inject
	private ApplicationContext applicationContext

	@Test
	public void canFindEventHandler() {

		EventHandlerCollection ec = applicationContext.getBean(EventHandlerCollection.class)

		def foundBeans = ec.findAll();

		List<Class<?>> classes = foundBeans.collect({EventHandlerConfig cfg -> return cfg.instance.getClass()})

		assert foundBeans.size() == 2 + 1; // The +1 is the AppVersionEvent
		assert BasicEventHandler.class in classes;
		assert SignalHandler.class in classes;
	}


	@Test
	public void findsPayloadTypeProperly() {
		EventHandlerCollection ec = applicationContext.getBean(EventHandlerCollection.class)
		EventDispatcher ed = applicationContext.getBean(EventDispatcher.class)
		EventHandlerConfig basic = ec.findByName("MyAction", "pcf")

		assert basic.instance == applicationContext.getBean(BasicEventHandler)
		assert basic.method.name == 'handleEvent'
		assert basic.name == 'MyAction'
		assert basic.namespace == 'pcf'
	}

}
