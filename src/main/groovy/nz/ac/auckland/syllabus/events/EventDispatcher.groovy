package nz.ac.auckland.syllabus.events

import net.stickycode.stereotype.Configured
import nz.ac.auckland.common.config.ConfigKey
import nz.ac.auckland.common.stereotypes.UniversityComponent
import nz.ac.auckland.syllabus.errors.ErrorHandlerCollection
import nz.ac.auckland.syllabus.errors.SyllabusExceptionHandler
import nz.ac.auckland.syllabus.errors.TransmissionException
import nz.ac.auckland.syllabus.hooks.EventHookCollection
import nz.ac.auckland.syllabus.hooks.EventHookException
import nz.ac.auckland.syllabus.payload.EventRequestBase
import nz.ac.auckland.syllabus.payload.EventResponseBase
import nz.ac.auckland.util.JacksonException
import nz.ac.auckland.util.JacksonHelperApi
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl

import javax.inject.Inject
import java.lang.reflect.ParameterizedType
import java.util.concurrent.ConcurrentHashMap

/**
 * User: marnix
 * Date: 25/03/13
 * Time: 3:47 PM
 *
 * This class dispatches events and marshals between JSON and the payload types specified on the events
 */
@UniversityComponent
class EventDispatcher {

	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory.getLogger(EventDispatcher.class);

	private static final int FOUR_O_FOUR = 404

	@Inject
	JacksonHelperApi jacksonHelperApi

	/**
	 * @see nz.ac.auckland.common.config.JarManifestConfigurationSource#KEY_IMPLEMENTATION_VERSION
	 */
	@ConfigKey("Implementation-Version")
	protected String currentVersion = 'unknown';

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
	 * Contains the types for a specific type that needs to be marshalled
	 */
	protected
	final Map<EventHandler<? extends EventRequestBase, ? extends EventResponseBase>, List<Class<?>>> marshallingTypes;

	/**
	 * Index of getMarshallingTypes that is the request input type
	 */
	private static final int REQUEST = 0;

	/**
	 * Index of getMarshallingTypes that is the response output type
	 */
	private static final int RESPONSE = 1;

	public EventDispatcher() {
		marshallingTypes = new ConcurrentHashMap<>();
	}

	/**
	 * Dispatch the for a certain URL with a particular payload
	 *
	 * @param url is the url to dispatch to
	 * @param payload is the payload to pass into the event handler
	 */
	public Object dispatch(String action, String namespace, String version, String requestBody)
		throws TransmissionException, EventHookException {

		// asking for the version we're expecting?
		if (currentVersion != version) {
			throw new TransmissionException("Version mismatch, sent $version, expected $currentVersion", FOUR_O_FOUR);
		}

		// event handler found?
		EventHandler<? extends EventRequestBase, ? extends EventResponseBase> eventHandler =
			this.getEventHandlerByName(action, namespace)

		if (!eventHandler) {
			String msg = "No event handler found for $namespace::$action (v$version)";
			throw new TransmissionException(msg, FOUR_O_FOUR);
		}

		// run the @BeforeEvent annotated EventHooks for this eventHandler (could throw EventHookException)
		this.runBeforeEventHooks(eventHandler, namespace);

		// get the input type class
		Class<? extends EventRequestBase> requestType = this.getRequestType(eventHandler)

		EventRequestBase requestInstance = deserializeRequestBody(requestBody, requestType)

		// handle the event and catch and try to do something with
		try {
			return eventHandler.handleEvent(requestInstance)
		}
		catch (Exception eeEx) {

			// something "expected" happened, let's go looking for an error handler
			SyllabusExceptionHandler<? extends Exception> exHandler = errorHandlerCollection.getHandlerFor(eeEx);

			if (!exHandler) {
				LOG.error("No exception handler found for", eeEx);
				return null;
			}

			return exHandler.handleError(eeEx);
		}

	}

	/**
	 * Run before event hooks
	 *
	 * @param eventHandler is the current event handler
	 * @param namespace is the namespace to run the event hooks for
	 */
	protected void runBeforeEventHooks(EventHandler<? extends EventRequestBase, ? extends EventResponseBase> eventHandler, String namespace) {
		eventHookCollection.runBeforeEventHooks(eventHandler, namespace)
	}

	/**
	 * Find an eventhandler instance by name
	 *
	 * @param action is the action to look for
	 * @param namespace is the namespace to be looking in
	 * @return is null when there is no such event handler, or an eventhandler instance when found.
	 */
	protected EventHandler getEventHandlerByName(String action, String namespace) {
		return eventCollection.findByName(action, namespace)
	}

	/**
	 * Try to deserialize the request body. If not able to deserialize, throw transmission exception
	 *
	 * @param requestBody is the body string to deserialize
	 * @param requestType is the request type to serialize to
	 * @return the object type
	 *
	 * @throws TransmissionException
	 */
	protected <T> T deserializeRequestBody(String requestBody, Class<T> requestType) throws TransmissionException {

		// try to deserialize the incoming request into the handler's input type
		try {
			return jacksonHelperApi.jsonDeserialize(requestBody, requestType);
		}
		catch (JacksonException jEx) {

			LOG.warn("Jackson was unable to deserialize JSON into ${requestType.simpleName}, json: $requestBody", jEx);

			// marshalling went wrong
			throw new TransmissionException(
				String.format("Could not marshal requestBody into `%s` because: %s", requestType.simpleName, jEx.message)
			)
		}

	}

	/**
	 * Discover the payload type
	 *
	 * @param eventHandler
	 * @return a tuple with the request and response class types for this eventHandler
	 */
	protected List<Class<?>> getMarshallingTypes(
		EventHandler<? extends EventRequestBase, ? extends EventResponseBase> eventHandler
	) {
		if (!eventHandler) {
			return null;
		}

		List<Class<?>> eventTypes = marshallingTypes[eventHandler];
		if (eventTypes) {
			return eventTypes;
		}

		ParameterizedType[] pType = eventHandler.class.genericInterfaces.findAll {
			return it instanceof ParameterizedType
		} as ParameterizedType[]

		ParameterizedType eventHandlerType = pType.find { ParameterizedType type ->
			return type.rawType == EventHandler.class
		}

		// get generic type in EventHandler interrface implementation
		if (eventHandlerType.actualTypeArguments?.length == 2) {

			// get raw types
			Class<?> reqType = this.drillUpToRawType(eventHandlerType.actualTypeArguments[REQUEST]);
			Class<?> respType = this.drillUpToRawType(eventHandlerType.actualTypeArguments[RESPONSE]);

			// store
			eventTypes = [reqType, respType];
			marshallingTypes[eventHandler] = eventTypes;

			// return
			return eventTypes;
		}

		// shouldn't happen
		throw new IllegalStateException("This shouldn't happen, eventHandler should always have a request and " +
			"response type")
	}

	/**
	 * Make sure we get the raw base type for serialization
	 *
	 * @param type is the type to analyze
	 * @return the raw type or the incoming type argument when it's already a raw type
	 */
	protected Class<?> drillUpToRawType(Object type) {
		if (type instanceof ParameterizedTypeImpl) {
			return type.rawType
		} else {
			return type;
		}
	}

	/**
	 * Return the request type class for this event handler
	 *
	 * @param eventHandler is the handler to get the information for
	 * @return a class or null
	 */
	protected Class<? extends EventRequestBase> getRequestType(
		EventHandler<? extends EventRequestBase, ? extends EventResponseBase> eventHandler
	) {
		List<Class<?>> types = this.getMarshallingTypes(eventHandler);
		return types ? types[REQUEST] as Class<? extends EventRequestBase> : null;
	}

	/**
	 * Return the response type class for this type
	 *
	 * @param eventHandler is the handler to get the information for
	 * @return a class or null
	 */
	protected Class<? extends EventResponseBase> getResponseType(
		EventHandler<? extends EventRequestBase, ? extends EventResponseBase> eventHandler
	) {
		List<Class<?>> types = this.getMarshallingTypes(eventHandler);
		return types ? types[RESPONSE] as Class<? extends EventResponseBase> : null;
	}

}
