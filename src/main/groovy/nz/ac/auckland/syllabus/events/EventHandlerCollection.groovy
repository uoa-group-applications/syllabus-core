package nz.ac.auckland.syllabus.events

import groovy.transform.CompileStatic
import nz.ac.auckland.syllabus.generator.EventHandlerFactory
import nz.ac.auckland.syllabus.generator.EventHandlerConfig
import org.springframework.beans.factory.annotation.Autowired
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.annotation.PostConstruct

import nz.ac.auckland.common.stereotypes.UniversityComponent

/**
 * Author: Marnix
 *
 * Event collection is able to scan for and retain information regarding events in a certain package
 */
@CompileStatic
@UniversityComponent
class EventHandlerCollection {

	/**
	 * Logger
	 */
	public static final Logger LOG = LoggerFactory.getLogger(EventHandlerCollection);

	/**
	 * A list of generators
	 */
	@Autowired(required = false)
	private List<EventHandlerFactory> generators;

	/**
	 * Event map, structured by { namespace -> { eventName -> objInstance }}*/
	private Map<String, Map<String, EventHandler>> eventMap;

	/**
	 * a list of event handler configurations
	 */
	private List<EventHandlerConfig> configurations;

	/**
	 * Index cleanup list
	 */
	@PostConstruct
	public void runAfterInjection() {

		this.configurations = [];

		// persist configurations by iterating through all generators
		this.generators?.each { EventHandlerFactory eventGenerator ->

			// generate
			List<EventHandlerConfig> generatedConfigurations = eventGenerator?.generateEventHandlers();

			if (!generatedConfigurations) {
				return;
			}

			// only keep the non-null ones
			generatedConfigurations.retainAll {
				EventHandlerConfig config -> return config != null;
			};

			this.configurations.addAll(generatedConfigurations);
		}

		// setup event map
		this.eventMap = this.createEventMap();

	}

	/**
	 * A list of event handlers
	 *
	 * @return an array of event handlers
	 */
	public List<EventHandler> findAll() {
		List<EventHandler> handlers =
			(List<EventHandler>) this.configurations.collect { EventHandlerConfig config ->
				return config.handler
			};

		return handlers;
	}

	/**
	 * Find a specific event handler
	 *
	 * @param action is the action to find
	 * @param namespace is the namespace to look in
	 */
	public EventHandler findByName(String action, String namespace = Event.DEFAULT_NAMESPACE) {

		Map<String, EventHandler> namespaceEvents = eventMap[namespace];

		if (namespaceEvents && namespaceEvents[action]) {
			return namespaceEvents[action];
		}

		LOG.info("Unable to find `$action` in namespace `$namespace`");
		return null
	}

	/**
	 * Get the event map which is a map structured by the event's name. This will throw
	 * an exception when one or more events in the same namespace with the same name
	 * have been found
	 *
	 * @return a structured event map
	 */
	protected Map<String, Map<String, EventHandler>> createEventMap() {

		Map<String, Map<String, EventHandler>> eventMap = [:]

		this.configurations.each { EventHandlerConfig eventConfig ->

			if (!eventConfig) {
				return;
			}

			String eventName = eventConfig.name,
			       namespace = eventConfig.namespace;

			if (!eventMap[namespace]) {
				eventMap[namespace] = [:];
			}

			Map<String, EventHandler> namespaceEvents = eventMap[namespace];

			if (namespaceEvents[eventName]) {
				throw new IllegalStateException(
					"There is a duplicate event in namespace $namespace with name $eventName. Aborting.");
			}

			namespaceEvents[eventName] = eventConfig.handler;
		}

		return eventMap;
	}

	/**
	 * @return the event map
	 */
	public Map<String, Map<String, EventHandler>> getEventMap() {
		return this.eventMap
	}

}
