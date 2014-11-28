package nz.ac.auckland.syllabus.hooks

import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import nz.ac.auckland.common.stereotypes.UniversityComponent
import nz.ac.auckland.syllabus.SyllabusContext
import nz.ac.auckland.syllabus.SyllabusHandle
import nz.ac.auckland.syllabus.events.EventHandlerCollection
import nz.ac.auckland.syllabus.generator.EventHandlerConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

import javax.annotation.PostConstruct
import javax.inject.Inject

/**
 * User: marnix
 * Date: 3/04/13
 * Time: 1:13 PM
 *
 * This is the collection of event hooks that will be queried on each request
 */
@UniversityComponent
@CompileStatic
class EventHookCollection {

	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory.getLogger(EventHookCollection.class);

	/**
	 * List of event hooks
	 */
	@Autowired(required = false)
	protected List<EventHook> eventHooks;

	/**
	 * Collection injected here
	 */
	@Inject
	EventHandlerCollection eventHandlerCollection;

	/**
	 * This method is run after this bean has been initialized
	 */
	@PostConstruct
	public void initializeHooks() {

		// sort by event hook priority
		eventHooks?.sort { EventHook h1, EventHook h2 ->
			-(hookPriority(h1) <=> hookPriority(h2))
		}

		try {

			// run initializers
			eventHookInitializers.each { EventHookInitializer initializer ->
				initializer.initializeHook(eventHandlers)
			}

		}
		catch (EventHookException ehEx) {
			LOG.error("An expected error occured during the initialization of the event hooks", ehEx);
		}

	}

	/**
	 * Run all the @BeforeEvent hooks
	 *
	 * @param eventHandler is the event handler this hook is being invoked on
	 * @param namespace is the namespace to run hooks for
	 */
	public void runBeforeEventHooks(SyllabusContext context) throws EventHookException {
		runHooks(BeforeEvent.class, context);
	}

	/**
	 * Find the hooks to run for a certain namespace. This method quacks a little to
	 * allow for different types of annotations with the same fields.
	 *
	 * @param namespace is the namespace to run @BeforeEvent event hooks for
	 */
	@CompileStatic(TypeCheckingMode.SKIP)
	public void runHooks(Class<?> annotation, SyllabusContext context) throws EventHookException {
		// iterate over all hooks with a certain annotations
		List hookList = this.getHooksWithAnnotation(annotation)

		if (!hookList) {
			return;
		}

		for (EventHook hook : hookList) {

			def eventAnnotation = hook.class.getAnnotation(annotation)

			// does the namespace match what we're looking for?
			boolean namespaceMatches = (eventAnnotation.namespace() == "" || context.namespace == eventAnnotation.namespace());

			// can run? make it so.
			if (namespaceMatches) {
				hook.executeHook(context);
			}
		}
	}

	/**
	 * Retrieve the priority of the event hook
	 *
	 * @param hook is the hook instance's class to investigate for annotations
	 * @return the priority of the annotation, or null when there is no event hook
	 */
	protected Integer hookPriority(EventHook hook) {
		if (!hook) {
			return null;
		}

		BeforeEvent beforeEventAnnotation = hook.class.getAnnotation(BeforeEvent.class)

		// found? return priority value.
		if (beforeEventAnnotation) {
			return beforeEventAnnotation.priority()
		} else {
			return null
		}
	}

	/**
	 * Retrieve a list of event hooks that have a certain annotatoin
	 *
	 * @param annotationClass is the annotation to go looking for
	 * @return a list
	 */
	protected List<EventHook> getHooksWithAnnotation(Class annotationClass) {
		return this.eventHooks.findAll { EventHook eventHook ->
			return eventHook.getClass().getAnnotation(annotationClass) != null
		} as List<EventHook>
	}

	/**
	 * Get a list of all event hooks that need to be initialized
	 *
	 * @return a list of event hook initializer instances
	 */
	protected List<EventHookInitializer> getEventHookInitializers() {
		return this.eventHooks?.findAll { EventHook eventHook ->
			return eventHook instanceof EventHookInitializer
		} as List<EventHookInitializer>
	}

	/**
	 * @return a flat list of all event handlers
	 */
	protected List<EventHandlerConfig> getEventHandlers() {
		return this.eventHandlerCollection.findAll();
	}

}
