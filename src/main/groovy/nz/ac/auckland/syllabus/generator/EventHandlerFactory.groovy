package nz.ac.auckland.syllabus.generator

/**
 * Author: Marnix
 *
 * Event generator interface. When a @UniversityComponent annotated class
 * implements this interface it will be asked to provide the event collection
 * a set of EventHandlers. These handlers can be generated on the fly.
 */
public interface EventHandlerFactory {

	/**
	 * This method is called on initialization of the EventHandlerCollection
	 *
	 * @return a list of EventHandlerConfiguration instances that need to be inserted.
	 * Returning null is fine, it will be ignored.
	 */
	public List<EventHandlerConfig> generateEventHandlers();

}