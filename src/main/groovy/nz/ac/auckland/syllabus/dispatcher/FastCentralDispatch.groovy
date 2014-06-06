package nz.ac.auckland.syllabus.dispatcher

import groovy.transform.CompileStatic
import nz.ac.auckland.common.stereotypes.UniversityComponent
import nz.ac.auckland.syllabus.SyllabusContext

import javax.annotation.PostConstruct
import javax.inject.Inject

/**
 *
 * @author: Richard Vowles - https://plus.google.com/+RichardVowles
 */
@CompileStatic
@UniversityComponent
class FastCentralDispatch implements CentralDispatch {
	Map<String, Dispatcher> contentMap = [:]

	@Inject List<Dispatcher> dispatchers

	@PostConstruct
	public void makeDispatchersIntoAMap() {
		dispatchers.each { Dispatcher dispatcher ->
			dispatcher.supports().each { String contentType ->
				contentMap[contentType] = dispatcher
			}
		}
	}

	/**
	 * The context as constructed by the transport. When considering adding parameters, consider adding it to
	 * the context instead.
	 *
	 * @param context - the context from the transport
	 * @return - the result from the handler
	 */
	public Object dispatch(SyllabusContext context) {
		return contentMap[context.contentType]?.dispatch(context)
	}
}
