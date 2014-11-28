package nz.ac.auckland.syllabus.events

import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import nz.ac.auckland.common.stereotypes.UniversityComponent
import nz.ac.auckland.syllabus.SyllabusContext
import nz.ac.auckland.syllabus.SyllabusHandle
import nz.ac.auckland.syllabus.errors.ErrorHandlerCollection
import nz.ac.auckland.syllabus.errors.SyllabusExceptionHandler
import nz.ac.auckland.syllabus.errors.TransmissionException
import nz.ac.auckland.syllabus.events.EventDispatcher.DecodeCallback
import nz.ac.auckland.syllabus.generator.EventHandlerConfig
import nz.ac.auckland.syllabus.hooks.AfterEvent
import nz.ac.auckland.syllabus.hooks.EventHookCollection
import nz.ac.auckland.syllabus.hooks.EventHookException
import nz.ac.auckland.syllabus.payload.ErrorResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.inject.Inject

/**
 *
 * @author: Richard Vowles - https://plus.google.com/+RichardVowles
 */
@UniversityComponent
@CompileStatic
class EventDispatcherImpl implements EventDispatcher {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(EventDispatcher.class);

	private static final int FOUR_O_FOUR = 404

	/**
	 * Event collection bound here
	 */
	@Inject
	private EventHandlerCollection eventCollection;

	/**
	 * Allows us to properly handle errors
	 */
	@Inject
	private ErrorHandlerCollection errorHandlerCollection;

	/**
	 * Event hook collection
	 */
	@Inject
	private EventHookCollection eventHookCollection;

	/**
	 * Dispatch the for a certain URL with a particular payload
	 *
	 * @param url is the url to dispatch to
	 * @param payload is the payload to pass into the event handler
	 */
	public Object dispatch(SyllabusContext context, DecodeCallback decodeCallback)
		throws TransmissionException, EventHookException {
		// figure out who should handle it
		context.handlerConfig = getEventHandlerByName(context.action, context.namespace)

		// handle the event and catch and try to do something with
		Object returnObject

		// run the @BeforeEvent annotated EventHooks for this event, it could change the SyllabusHandle
		try {
			runBeforeEventHooks(context)
		} catch (Exception beforeHookException) {
			returnObject = handleError(beforeHookException)

			return returnObject
		}

		// make sure we can actually call something
		if (!context.currentHandle) {
				throw new TransmissionException("No event handler found for ${context.namespace}::${context.action} (v${context.version})", FOUR_O_FOUR);
		}

		try {
			returnObject = context.currentHandle.invoke(decodeCallback.decode(context), context)
		} catch (TransmissionException eh) {
			throw eh
		} catch (Exception eeEx) {
			returnObject = handleError(eeEx)
		}

		runAfterEventHooks(context)

		return returnObject
	}

	void runAfterEventHooks(SyllabusContext context) {
		eventHookCollection.runHooks(AfterEvent, context)
	}

	@CompileStatic(TypeCheckingMode.SKIP)
	protected ErrorResponse handleError(Exception e) {
		// something "expected" happened, let's go looking for an error handler
		SyllabusExceptionHandler<? extends Exception> exHandler = errorHandlerCollection.getHandlerFor(e);

		if (!exHandler) {
			log.error("No exception handler found for", e);
			return null;
		}

		return exHandler.handleError(e);
	}
/**
 * Run before event hooks
 *
 * @param eventHandler is the current event handler
 * @param namespace is the namespace to run the event hooks for
 */
	protected void runBeforeEventHooks(SyllabusContext context) {
		 eventHookCollection.runBeforeEventHooks(context)
	}

	/**
	 * Find an eventhandler instance by name
	 *
	 * @param action is the action to look for
	 * @param namespace is the namespace to be looking in
	 * @return is null when there is no such event handler, or an eventhandler instance when found.
	 */
	protected EventHandlerConfig getEventHandlerByName(String action, String namespace) {
		return eventCollection.findByName(action, namespace)
	}
}
