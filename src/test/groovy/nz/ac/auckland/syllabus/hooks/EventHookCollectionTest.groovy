package nz.ac.auckland.syllabus.hooks

import groovy.transform.CompileStatic
import nz.ac.auckland.common.testrunner.BatheCommandLine
import nz.ac.auckland.common.testrunner.SimpleSpringRunner
import nz.ac.auckland.syllabus.SyllabusContext
import nz.ac.auckland.syllabus.SyllabusHandle
import nz.ac.auckland.syllabus.generator.EventHandlerConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

import javax.inject.Inject

/**
 * User: marnix
 * Date: 3/04/13
 * Time: 1:48 PM
 *
 *
 */
@CompileStatic
@BatheCommandLine(["-Pclasspath:/test.properties"])
@RunWith(SimpleSpringRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class EventHookCollectionTest {

	@Inject
	private EventHookCollection ehc

	/**
	 * Test whether the event hooks are picked up
	 */
	@Test
	public void testInjection() {


		assert ehc.eventHooks
		assert ehc.eventHooks.size() == 4
	}

	@Test
	public void testHookPriority() {
		assert ehc.hookPriority(new EventHookTestWithInit()) == 0
		assert ehc.hookPriority(new EventHookNSFirst()) == 2
		assert ehc.hookPriority(new EventHookNSSecond()) == 1
	}


	@Test
	public void testAutosortingOnInitialize() {
		assert ehc
		assert ehc.eventHooks
		assert ehc.eventHooks.size() == 4
		assert ehc.eventHooks[0] instanceof EventHookNSFirst
		assert ehc.eventHooks[1] instanceof EventHookNSSecond
		assert ehc.hookPriority(ehc.eventHooks[2]) == 0
		assert ehc.hookPriority(ehc.eventHooks[3]) == 0
	}

	@Test
	public void testGetEventHookInitializers() {
		assert ehc.eventHookInitializers
		assert ehc.eventHookInitializers.size() == 1
		assert ehc.eventHookInitializers[0] instanceof EventHookTestWithInit
	}

	// -------------------------------------------------------------------------------------------
	//      Classes used in this test
	// -------------------------------------------------------------------------------------------

	// simple event hook class
	@BeforeEvent
	static class EventHookTest implements EventHook {
		@Override
		void executeHook(SyllabusContext context) throws EventHookException {
		}
	}

	// event hook class with initializer
	@BeforeEvent
	static class EventHookTestWithInit implements EventHook, EventHookInitializer {
		@Override
		void executeHook(SyllabusContext context) throws EventHookException {
		}

		@Override
		void initializeHook(List<EventHandlerConfig> eventHandlerList) throws EventHookException {

		}
	}

	// event hook class with namespace restriction and priority
	@BeforeEvent(namespace = "app", priority = 2)
	static class EventHookNSFirst implements EventHook {

		@Override
		void executeHook(SyllabusContext context) throws EventHookException {
		}
	}

	// event hook class with namespace restriction and lower priority
	@BeforeEvent(namespace = "app", priority = 1)
	static class EventHookNSSecond implements EventHook {

		@Override
		void executeHook(SyllabusContext context) throws EventHookException {
		}
	}
}
