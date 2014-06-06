package nz.ac.auckland.syllabus.dispatcher

import groovy.transform.CompileStatic
import nz.ac.auckland.syllabus.SyllabusContext

/**
 *
 * @author: Richard Vowles - https://plus.google.com/+RichardVowles
 */
@CompileStatic
interface Dispatcher {
	List<String> supports()
	Object dispatch(SyllabusContext context)
}
