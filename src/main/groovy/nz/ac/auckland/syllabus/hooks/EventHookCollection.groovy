package nz.ac.auckland.syllabus.hooks

import nz.ac.auckland.common.stereotypes.UniversityComponent
import nz.ac.auckland.syllabus.events.EventHandler
import nz.ac.auckland.syllabus.events.EventHandlerCollection
import org.springframework.beans.factory.annotation.Autowired

import javax.annotation.PostConstruct
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * User: marnix
 * Date: 3/04/13
 * Time: 1:13 PM
 *
 * This is the collection of event hooks that will be queried on each request
 */
@UniversityComponent
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
			-(this.hookPriority(h1) <=> this.hookPriority(h2))
		}

		try {

			// run initializers
			this.eventHookInitializers.each { EventHookInitializer initializer ->
				initializer.initializeHook(this.eventHandlers)
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
	public void runBeforeEventHooks(EventHandler eventHandler, String namespace) throws EventHookException {
		this.runHooks(BeforeEvent.class, eventHandler, namespace);
	}

	/**
	 * Find the hooks to run for a certain namespace. This method quacks a little to
	 * allow for different types of annotations with the same fields.
	 *
	 * @param namespace is the namespace to run @BeforeEvent event hooks for
	 */
	protected void runHooks(Class<?> annotation, EventHandler eventHandler, String namespace) throws EventHookException {

		// iterate over all hooks with a certain annotations
		List hookList = this.getHooksWithAnnotation(annotation)

		if (!hookList) {
			return;
		}

		for (EventHook hook : hookList) {

			def eventAnnotation = hook.class.getAnnotation(annotation)

			// does the namespace match what we're looking for?
			boolean namespaceMatches = (eventAnnotation.namespace() == "" || namespace == eventAnnotation.namespace());

			// can run? make it so.
			if (namespaceMatches) {
				hook.executeHook(eventHandler);
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
	protected List<EventHook> getHooksWithAnnotation(Class<?> annotationClass) {
		return this.eventHooks.findAll {
			it.class.getAnnotation(annotationClass) != null
		}
	}

	/**
	 * Get a list of all event hooks that need to be initialized
	 *
	 * @return a list of event hook initializer instances
	 */
	protected List<EventHookInitializer> getEventHookInitializers() {
		return this.eventHooks?.findAll {
			it instanceof EventHookInitializer
		}
	}

	/**
	 * @return a flat list of all event handlers
	 */
	protected List<EventHandler> getEventHandlers() {
		return this.eventHandlerCollection.findAll();
	}

}
