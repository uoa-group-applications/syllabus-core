package nz.ac.auckland.syllabus.generator

import groovy.transform.CompileStatic
import nz.ac.auckland.common.stereotypes.UniversityComponent
import nz.ac.auckland.syllabus.events.Event
import nz.ac.auckland.syllabus.events.EventHandler
import org.springframework.beans.factory.annotation.Autowired

import javax.annotation.PostConstruct
import java.lang.annotation.Annotation

/**
 * Author: Marnix
 *
 * Event generator implementation that gathers all Event annotated handlers
 */
@CompileStatic
@UniversityComponent
class AnnotatedEventHandlerFactory implements EventHandlerFactory {

	/**
	 * A list of event handlers
	 */
	@Autowired(required = false)
	List<EventHandler> handlers;

	/**
	 * only keep ones that have the @Event annotation
	 */
	@PostConstruct
	public void throwAwayWithoutAnnotation() {

		this.handlers.retainAll { EventHandler eventHandler ->
			return eventHandler.class.getAnnotation(Event) != null
		}

	}

	/**
	 * @return a list of EventHandlerConfig instances
	 */
	@Override
	public List<EventHandlerConfig> generateEventHandlers() {

		// convert from annotated to a list of event handler configurations
		List<EventHandlerConfig> configs = this.handlers.collect { EventHandler handler ->

			// get @Event annotation
			Event event = (Event) handler.class.annotations.find { Annotation a -> a instanceof Event }

			return new EventHandlerConfig(
				name: event.name(),
				namespace: event.namespace(),
				handler: handler
			)
		}

	}
}
