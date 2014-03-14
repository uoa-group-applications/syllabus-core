package nz.ac.auckland.syllabus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author: Marnix
 * <p/>
 * This annotation will help you define the purpose of your event
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiDoc {

	String value() default "";

}
