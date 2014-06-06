package nz.ac.auckland.syllabus.events

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * Add this to any class and add a "handle" or "handleEvent" method. The method can take 0, 1 or 2 parameters.
 * One of those parameters can be a SyllabusContext if you need it, the other can be any object that the request body
 * will be posted into.
 *
 * Event description
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface Event {

	/**
	 * Default namespace
	 */
	public static final String DEFAULT_NAMESPACE = "app"

	/**
	 * Event's name on the bus
	 */
	String name()

	/**
	 * Namespace the action lives in, default is "app"
	 */
	String namespace() default "app"

	/**
	 * Tells us what version of the application we last changed this API.
	 *
	 * @return
	 */
	String since() default "1.1-SNAPSHOT"
}
