package nz.ac.auckland.syllabus.hooks

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 *
 * @author: Richard Vowles - https://plus.google.com/+RichardVowles
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface AfterEvent {
	/**
	 * Only execute for certain namespace, if "" execute for all
	 */
	String namespace() default ""
}
