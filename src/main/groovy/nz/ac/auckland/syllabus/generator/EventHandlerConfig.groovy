package nz.ac.auckland.syllabus.generator

import groovy.transform.CompileStatic
import nz.ac.auckland.syllabus.events.Event
import org.codehaus.groovy.reflection.CachedClass

import java.lang.reflect.Method
import java.lang.reflect.Modifier

/**
 * Author: Marnix
 *
 * Event Handler configuration combines an event handler instance and the
 * information normally placed in the configuration.
 */
@CompileStatic
class EventHandlerConfig {

	/**
	 * Is the namespace tho place this handler in
	 */
	String namespace = Event.DEFAULT_NAMESPACE

	/**
	 * Is the name of the handler
	 */
	String name = null

	/**
	 * Event handler instance
	 */
	Object instance

	/*
	* The method to call
	 */
  MetaMethod method

	/*
	 * The type of the bean expected
	 */
	CachedClass[] paramaterTypes

	public static final List<String> handleMethodNames = ['handleEvent', 'handle']

	static EventHandlerConfig fromBean(Object bean) {
		Event event = bean.getClass().getAnnotation(Event)

		MetaMethod method = bean.getMetaClass().methods.find({MetaMethod m -> return handleMethodNames.contains(m.name) && Modifier.isPublic(m.modifiers)})

		if (method) {
			EventHandlerConfig handlerConfig = new EventHandlerConfig(name: event.name(), namespace: event.namespace(),
			    instance: bean, method: method)

			handlerConfig.paramaterTypes = method.getParameterTypes()

			if (handlerConfig.paramaterTypes.size() > 2) {
				throw new RuntimeException("Bean ${bean.getClass().name} ${method.name} has too many parameters.")
			}

			return handlerConfig
		} else {
			throw new RuntimeException("Bean ${bean.getClass().name} is missing a handle or handleEvent method.")
		}
	}

	public String toString() {
		return "handler: ${namespace}/${name} - bean ${instance.getClass().name} - method ${method.name} - parameters ${paramaterTypes*.name}"
	}
}
