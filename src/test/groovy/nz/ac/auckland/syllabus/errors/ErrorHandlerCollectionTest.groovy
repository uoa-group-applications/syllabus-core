package nz.ac.auckland.syllabus.errors

import groovy.transform.CompileStatic
import nz.ac.auckland.common.stereotypes.UniversityComponent
import nz.ac.auckland.syllabus.payload.ErrorResponse
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import javax.inject.Inject

/**
 * User: marnix
 * Date: 26/03/13
 * Time: 1:42 PM
 */
@CompileStatic
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class ErrorHandlerCollectionTest {

	@Inject
	ApplicationContext applicationContext


	@Test
	public void sortsHandlerPropertly() {
		ErrorHandlerCollection ec = applicationContext.getBean(ErrorHandlerCollection.class)

		assert ec.handlers.size() == 5
		assert ec.handlers[0] instanceof C3Handler;
		assert ec.handlers[1] instanceof B2Handler;
		assert ec.handlers[2] instanceof D2Handler;
		assert ec.handlers[3] instanceof A1Handler;
		assert ec.handlers[4] instanceof UncaughtExceptionHandler;
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
	static class A1Handler implements SyllabusExceptionHandler<Exception> {
		Class<? extends Exception> respondsTo() { A1Exception.class }

		ErrorResponse handleError(Exception exception) { return null }
	}

	@UniversityComponent
	static class B2Handler implements SyllabusExceptionHandler<Exception> {
		Class<? extends Exception> respondsTo() { B2Exception.class }

		ErrorResponse handleError(Exception exception) { return null }
	}

	@UniversityComponent
	static class C3Handler implements SyllabusExceptionHandler<Exception> {
		Class<? extends Exception> respondsTo() { C3Exception.class }

		ErrorResponse handleError(Exception exception) { return null }
	}

	@UniversityComponent
	static class D2Handler implements SyllabusExceptionHandler<Exception> {
		Class<? extends Exception> respondsTo() { D2Exception.class }

		ErrorResponse handleError(Exception exception) { return null }
	}


}
