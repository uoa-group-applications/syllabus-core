package nz.ac.auckland.syllabus.dispatcher

import groovy.transform.CompileStatic
import nz.ac.auckland.common.stereotypes.UniversityComponent
import nz.ac.auckland.syllabus.SyllabusContext
import nz.ac.auckland.syllabus.errors.TransmissionException
import nz.ac.auckland.syllabus.events.EventDispatcher
import nz.ac.auckland.util.JacksonException
import nz.ac.auckland.util.JacksonHelperApi
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.inject.Inject

/**
 *
 * @author: Richard Vowles - https://plus.google.com/+RichardVowles
 */
@UniversityComponent
@CompileStatic
class JsonDispatcher implements Dispatcher {
	private Logger log = LoggerFactory.getLogger(getClass())
	@Inject EventDispatcher eventDispatcher
	@Inject JacksonHelperApi jacksonHelperApi

	@Override
	List<String> supports() {
		return ["application/json", "application/javascript"]
	}


	protected Object provideObject(SyllabusContext context) {
		if (context.currentHandle.serializeObject == null) {
			return null
		}

		// try to deserialize the incoming request into the handler's input type
		try {
			return jacksonHelperApi.jsonDeserialize(context.requestBody, context.currentHandle.serializeObject);
		}
		catch (JacksonException jEx) {
			log.warn("Jackson was unable to deserialize JSON into ${context.currentHandle.serializeObject.simpleName}, json: $context.requestBody", jEx);

			// marshalling went wrong
			throw new TransmissionException(
				String.format("Could not marshal requestBody into `%s` because: %s", context.currentHandle.serializeObject.simpleName, jEx.message)
			)
		}
	}

	@Override
	Object dispatch(SyllabusContext context) {
		return eventDispatcher.dispatch(context, this.&provideObject)
	}
}
