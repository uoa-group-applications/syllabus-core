package nz.ac.auckland.syllabus.events

import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Retention

import java.lang.annotation.Target
import java.lang.annotation.ElementType

/**
 * User: marnix
 * Date: 25/03/13
 * Time: 11:10 AM
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


}
