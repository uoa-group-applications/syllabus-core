package nz.ac.auckland.syllabus.hooks

import groovy.transform.CompileStatic
import nz.ac.auckland.syllabus.errors.ErrorHandlerCollection
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.ContextConfiguration
import nz.ac.auckland.syllabus.events.EventHandler
import org.junit.Test

import javax.inject.Inject;

/**
 * User: marnix
 * Date: 3/04/13
 * Time: 1:48 PM
 *
 *
 */
@CompileStatic
@RunWith(SpringJUnit4ClassRunner.class)
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
		void executeHook(EventHandler event) {
			return   //To change body of implemented methods use File | Settings | File Templates.
		}
	}

	// event hook class with initializer
	@BeforeEvent
	static class EventHookTestWithInit implements EventHook, EventHookInitializer {

		@Override
		void initializeHook(List<EventHandler> eventHandlerList) {

		}

		@Override
		void executeHook(EventHandler event) {
			return   //To change body of implemented methods use File | Settings | File Templates.
		}
	}

	// event hook class with namespace restriction and priority
	@BeforeEvent(namespace = "app", priority = 2)
	static class EventHookNSFirst implements EventHook {

		@Override
		void executeHook(EventHandler event) {
			return   //To change body of implemented methods use File | Settings | File Templates.
		}
	}

	// event hook class with namespace restriction and lower priority
	@BeforeEvent(namespace = "app", priority = 1)
	static class EventHookNSSecond implements EventHook {

		@Override
		void executeHook(EventHandler event) {
			return   //To change body of implemented methods use File | Settings | File Templates.
		}
	}
}
