package nz.ac.auckland.syllabus

import groovy.transform.CompileStatic
import nz.ac.auckland.syllabus.generator.EventHandlerConfig
import org.codehaus.groovy.reflection.CachedClass

import java.lang.reflect.Method

/**
 * @author Richard Vowles - https://google.com/+RichardVowles
 */
@CompileStatic
class SyllabusHandle {
	MetaMethod method
	Object instance
	CachedClass[] requestType
	Class serializeObject

	/**
	 * Supports calling a handle method with 0, 1 or 2 parameters. When we have
	 * parameters, one could be a SyllabusContext or not.
	 *
	 * @param requestObject - the object to pass
	 * @param context - the SyllabusContext
	 *
	 * @return - the thing we are returning to the client
	 */
	Object invoke(Object requestObject, SyllabusContext context) {
		if (requestType.size() == 0) {
			return method.invoke(instance)
		} else if (requestType.size() == 1) {
			if (requestType[0] == SyllabusContext) {
				return method.invoke(instance, context)
			} else {
				return method.invoke(instance, requestObject)
			}
		} else if (requestType.size() == 2) {
			if (requestType[0] == SyllabusContext) {
				return method.invoke(instance, context, requestObject)
			} else {
				return method.invoke(instance, requestObject, context)
			}
		} else {
			return null
		}
	}

	public SyllabusHandle() {
	}

	public SyllabusHandle(EventHandlerConfig config) {
		this.method = config.method
		this.instance = config.instance
		this.requestType = config.paramaterTypes

		// figure out the type of object that will be serialized
		if (this.requestType?.size() == 1 && this.requestType[0] != SyllabusContext.class) {
			serializeObject = this.requestType[0].theClass
		} else if (this.requestType?.size() == 2) {
			if (this.requestType[0] == SyllabusContext.class) {
				serializeObject = this.requestType[1].theClass
			} else {
				serializeObject = this.requestType[0].theClass
			}
		}
	}
}
