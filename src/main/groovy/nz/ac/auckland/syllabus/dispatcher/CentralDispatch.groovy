package nz.ac.auckland.syllabus.dispatcher

import nz.ac.auckland.syllabus.SyllabusContext

/**
 * This is the main entry point through which an adapter (such as http or web sockets) would
 * call us. It should construct a SyllabusContext (or descendant thereof) and request a dispatch.
 *
 * The dispatcher expects to find the contentType set and passes that on to any dispatcher that says it can
 * deal with that particular content type. It will return an object that needs to be serialized the other way.
 *
 * @author: Richard Vowles - https://plus.google.com/+RichardVowles
 */
interface CentralDispatch {
	public Object dispatch(SyllabusContext context)
}
