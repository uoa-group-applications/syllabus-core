package nz.ac.auckland.syllabus.hooks

import java.lang.annotation.Target
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * User: marnix
 * Date: 3/04/13
 * Time: 12:33 PM
 *
 * Before event annotation will be used to setup actions that are executed before an EventHandler is invoked
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface BeforeEvent {

	/**
	 * Only execute for certain namespace, if "" execute for all
	 */
	String namespace() default ""

	/**
	 * Priority of the event hook. Higher gets executed first.
	 */
	int priority() default 0


}
