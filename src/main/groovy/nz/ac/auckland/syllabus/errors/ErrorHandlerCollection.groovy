package nz.ac.auckland.syllabus.errors

import groovy.transform.CompileStatic
import nz.ac.auckland.common.stereotypes.UniversityComponent
import org.springframework.beans.factory.annotation.Autowired

import javax.annotation.PostConstruct

/**
 * Contains a collection of sorted silly bus exception handler instances that have been
 * created throughout the different packages.
 */
@UniversityComponent
@CompileStatic
class ErrorHandlerCollection {

	/**
	 * List of bus exception handlers found in the classpath
	 */
	@Autowired(required = false)
	protected List<SyllabusExceptionHandler> handlers


	static class ErrorHandler {
		SyllabusExceptionHandler exceptionHandler
		Class exceptionClass
		List<Class> superclassesToException
	}

	protected List<ErrorHandler> errorHandlers = []

	/**
	 * Make sure the handlers are ordered appropriately
	 */
	@PostConstruct
	public void orderBySpecificity() {
		handlers.each { SyllabusExceptionHandler handler ->

			ErrorHandler errorHandler = new ErrorHandler(exceptionHandler: handler,
				exceptionClass: findExceptionHandled(handler)
			)

			errorHandler.superclassesToException = findSuperclasses(errorHandler.exceptionClass)

			errorHandlers.add(errorHandler)
		}
	}

	protected List<Class> findSuperclasses(Class clazz) {
		List<Class> classes = []

		if (clazz != Exception.class) {
			clazz = clazz.getSuperclass()
			while (clazz != Exception.class) {
				classes << clazz
				clazz = clazz.getSuperclass()
			}
		}


		return classes
	}

	/**
	 * Try to find the correct exception handler
	 *
	 * @param ex is the exception to find an exception handler for
	 * @return the best matching exception handler, or null when none was found.
	 */
	public SyllabusExceptionHandler getHandlerFor(Exception ex) {

		// optimise for exact matches, that is what is most likely
		ErrorHandler bestHandler = errorHandlers?.find { ErrorHandler busHandler ->
			return ex.getClass() == busHandler.exceptionClass
		}

		if (!bestHandler) {
			// find all  the error handlers that superclass to that exception and find the one that is closest to
			// the exception itself
			bestHandler = errorHandlers
				.findAll({ErrorHandler eh -> return eh.superclassesToException.contains(ex.getClass())})
			  .min({ErrorHandler eh -> return eh.superclassesToException.indexOf(ex.getClass())})
		}

		return bestHandler?.exceptionHandler
	}

	/**
	 * returns the Exception Class as from the handleError parameter. It has to be an exception, the interface enforces it.
	 *
	 * @param exceptionHandler - the SyllabusExceptionHandler
	 * @return the exception this handler manages
	 */
	protected Class findExceptionHandled(SyllabusExceptionHandler exceptionHandler) {
		// java version returns a method that has a param Exception instead of correct version
		return exceptionHandler.metaClass.methods.find({MetaMethod mm -> return mm.name == 'handleError'}).parameterTypes[0].theClass
	}
}
