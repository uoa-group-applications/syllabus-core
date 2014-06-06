package nz.ac.auckland.syllabus.events

import groovy.transform.CompileStatic
import nz.ac.auckland.common.stereotypes.UniversityComponent
import nz.ac.auckland.syllabus.generator.EventHandlerConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent

import javax.inject.Inject

/**
 * Author: Marnix
 *
 * Event collection is able to scan for and retain information regarding events in a certain package
 */
@CompileStatic
@UniversityComponent
class EventHandlerCollection implements ApplicationListener<ContextRefreshedEvent> {

	/**
	 * Logger
	 */
	public static final Logger log = LoggerFactory.getLogger(EventHandlerCollection);

	/**
	 * Event map, structured by { namespace -> { eventName -> objInstance }}*/
	private Map<String, Map<String, EventHandlerConfig>> eventMap;

	/**
	 * a list of event handler configurations
	 */
	private List<EventHandlerConfig> configurations

	@Inject ApplicationContext applicationContext

	/**
	 * A list of event handlers
	 *
	 * @return an array of event handlers
	 */
	public List<EventHandlerConfig> findAll() {
		if (!configurations) {
			refreshEvents()
		}

		return configurations
	}

	/**
	 * Find a specific event handler
	 *
	 * @param action is the action to find
	 * @param namespace is the namespace to look in
	 */
	public EventHandlerConfig findByName(String action, String namespace = Event.DEFAULT_NAMESPACE) {

		EventHandlerConfig cfg = eventMap[namespace]?.get(action)

		if (cfg) {
			return cfg
		} else {
			log.debug("Unable to find `$action` in namespace `$namespace`")
			return null
		}
	}

	/**
	 * @return the event map
	 */
	public Map<String, Map<String, EventHandlerConfig>> getEventMap() {
		if (!eventMap) {
			refreshEvents()
		}

		return this.eventMap
	}

	/**
	 * withDefault makes the map creation faster, but leaves it open to memory abuse by the caller.
	 */
	protected void cloneEventMap(Map<String, Map<String, EventHandlerConfig>> map) {
		Map<String, Map<String, EventHandlerConfig>> eventMap = [:]

		map.each { String namespace, Map<String, EventHandlerConfig> nextMap ->
			Map<String, EventHandlerConfig> newMap = [:]
			newMap.putAll(nextMap)

			eventMap[namespace] = newMap
		}

		this.eventMap = eventMap
	}

	@Override
	void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		refreshEvents()
	}

	protected void refreshEvents() {
		Map<String, Map<String, EventHandlerConfig>> eventMap = [:].withDefault { namespace ->
			return new HashMap<String, EventHandlerConfig>()
		}

		Map<String, Object> events = applicationContext.getBeansWithAnnotation(Event)

		List<EventHandlerConfig> configs = []

		events.values().each { Object bean ->
			EventHandlerConfig cfg = EventHandlerConfig.fromBean(bean)

			eventMap[cfg.namespace].put(cfg.name, cfg)
			configs << cfg
		}

		configurations = configs

		// setup event map
		cloneEventMap(eventMap)
	}
}
