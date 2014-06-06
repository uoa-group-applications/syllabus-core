package nz.ac.auckland.syllabus.errors

import groovy.transform.CompileStatic
import nz.ac.auckland.common.stereotypes.UniversityComponent
import nz.ac.auckland.common.testrunner.BatheCommandLine
import nz.ac.auckland.common.testrunner.SimpleSpringRunner
import nz.ac.auckland.syllabus.payload.ErrorResponse
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration

import javax.inject.Inject

/**
 * User: marnix
 * Date: 26/03/13
 * Time: 1:42 PM
 */
@CompileStatic
@BatheCommandLine(["-Pclasspath:/test.properties"])
@RunWith(SimpleSpringRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class ErrorHandlerCollectionTest {

	@Inject
	ApplicationContext applicationContext


	@Test
	public void sortsHandlerPropertly() {
		ErrorHandlerCollection ec = applicationContext.getBean(ErrorHandlerCollection.class)

		assert ec.handlers.size() == 5
		List<Class<?>> handlers = ec.handlers.collect({return it.getClass()})
		assert handlers.contains(C3Handler)
		assert handlers.contains(B2Handler)
		assert handlers.contains(A1Handler)
		assert handlers.contains(D2Handler)
		assert handlers.contains(UncaughtExceptionHandler)
	}

	@Test
	public void testExactMatch() {
		ErrorHandlerCollection ec = applicationContext.getBean(ErrorHandlerCollection.class)

		// test exact match, and implicitly matching the inheritance matching
		assert ec.getHandlerFor(new C3Exception()) instanceof C3Handler
		assert ec.getHandlerFor(new B2Exception()) instanceof B2Handler
		assert ec.getHandlerFor(new D2Exception()) instanceof D2Handler
		assert ec.getHandlerFor(new A1Exception()) instanceof A1Handler


	}

	// ----------------------------------
	//  some fake exceptions
	// ----------------------------------

	static class A1Exception extends Exception {}          // main
	static class B2Exception extends A1Exception {}        // extends A1
	static class C3Exception extends B2Exception {}        // extends B2
	static class D2Exception extends A1Exception {}        // extends A1

	// ----------------------------------
	//  some fake components
	// ----------------------------------

	@UniversityComponent
	static class A1Handler implements SyllabusExceptionHandler<A1Exception> {

		ErrorResponse handleError(A1Exception exception) { return null }
	}

	@UniversityComponent
	static class B2Handler implements SyllabusExceptionHandler<B2Exception> {
		ErrorResponse handleError(B2Exception exception) { return null }
	}

	@UniversityComponent
	static class C3Handler implements SyllabusExceptionHandler<C3Exception> {

		ErrorResponse handleError(C3Exception exception) { return null }
	}

	@UniversityComponent
	static class D2Handler implements SyllabusExceptionHandler<D2Exception> {

		ErrorResponse handleError(D2Exception exception) { return null }
	}


}
