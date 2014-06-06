package nz.ac.auckland.syllabus

import nz.ac.auckland.syllabus.generator.EventHandlerConfig

/**
 * This context exists so that as we add, over time, any new features to the context
 * then the core code does not require changing.
 *
 * @author Richard Vowles - https://google.com/+RichardVowles
 */
class SyllabusContext {
	/**
	 * the request body (if any)
	 */
	String requestBody

	/**
	 * the event they original requested (if any)
	 */
	EventHandlerConfig handlerConfig

	/**
	 * version requested by end user
	 */
	String version

	/**
	 * namespace originally requested by user
	 */
	String namespace

	/**
	 * action originally requested by user
	 */
	String action

	/*
	 * the content type of the incoming data
	 */
	String contentType

	/**
	 * the current handler, this is here so it can be changed if necessary by @BeforeEvents
	 */
	SyllabusHandle currentHandle

	public void setHandlerConfig(EventHandlerConfig handlerConfig) {
		this.handlerConfig = handlerConfig

		if (handlerConfig) {
			currentHandle = new SyllabusHandle(handlerConfig)
		}
	}

	public String toString() {
		return "SyllabusContext: ${namespace}/${action}/${version} - content type ${contentType} - handle ${handlerConfig.toString()} - requestBody: ${requestBody?.size()}"
	}
}
