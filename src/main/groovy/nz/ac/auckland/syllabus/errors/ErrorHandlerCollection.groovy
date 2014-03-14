package nz.ac.auckland.syllabus.errors

import org.springframework.beans.factory.annotation.Autowired

import javax.inject.Inject
import javax.annotation.PostConstruct

import nz.ac.auckland.common.stereotypes.UniversityComponent

/**
 * User: marnix
 * Date: 26/03/13
 * Time: 12:42 PM
 *
 * Contains a collection of sorted silly bus exception handler instances that have been
 * created throughout the different packages.
 */
@UniversityComponent
class ErrorHandlerCollection {

	/**
	 * List of bus exception handlers found in the classpath
	 */
	@Autowired(required = false)
	protected List<SyllabusExceptionHandler> handlers;

	/**
	 * Make sure the handlers are ordered appropriately
	 */
	@PostConstruct
	public void orderBySpecificity() {
		this.handlers?.sort(new SpecificityComparator())
	}

	/**
	 * Try to find the correct exception handler
	 *
	 * @param ex is the exception to find an exception handler for
	 * @return the best matching exception handler, or null when none was found.
	 */
	public SyllabusExceptionHandler<? extends Exception> getHandlerFor(Exception ex) {

		// find the first exception handler that seems to work
		SyllabusExceptionHandler<?> bestHandler = handlers?.find { SyllabusExceptionHandler<?> busHandler ->
			Class<?> respondsToClass = busHandler.respondsTo()

			if (respondsToClass == ex.class) {
				return busHandler
			} else if (respondsToClass.isAssignableFrom(ex.class)) {
				return busHandler
			}
			return null;
		}


		return bestHandler
	}

	/**
	 * Order exception handlers in such a way that they are ordered by the specificity
	 */
	class SpecificityComparator implements Comparator<SyllabusExceptionHandler<? extends Exception>> {

		@Override
		public int compare(SyllabusExceptionHandler<? extends Exception> o1, SyllabusExceptionHandler<? extends Exception> o2) {

			Class<?> o1Response = o1.respondsTo()
			Class<?> o2Response = o2.respondsTo()

			if (o1Response == o2Response) {
				// same?
				return 0
			} else if (o1Response.isAssignableFrom(o2Response)) {
				// o1 is superclass? less important, push to bottom
				return 1
			} else if (o2Response.isAssignableFrom(o1Response)) {
				// o2 is superclass? more important, push to top
				return -1
			} else {
				// otherwise, just order alphabetically
				return o1Response.simpleName.compareTo(o2Response.simpleName)
			}
		}

	}

}
