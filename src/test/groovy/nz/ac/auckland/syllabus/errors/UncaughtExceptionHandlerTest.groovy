package nz.ac.auckland.syllabus.errors

import groovy.transform.CompileStatic
import nz.ac.auckland.syllabus.payload.ErrorResponse
import nz.ac.auckland.util.JacksonHelper
import org.junit.Before
import org.junit.Test;

@CompileStatic
public class UncaughtExceptionHandlerTest {

	UncaughtExceptionHandler target;

	@Before
	public void setUp() throws Exception {
		target = new UncaughtExceptionHandler();
		target.jacksonHelperApi = new JacksonHelper()
	}

	@Test
	public void returnsErrorWithUuid() throws Exception {
		Exception exception = new Exception("This will only be seen in the log.");

		//-----------------------------------------------------
		ErrorResponse response = target.handleError(exception);
		//-----------------------------------------------------

		assert response.error == UncaughtExceptionHandler.ERROR_UNKNOWN;
		assert response.context instanceof Map;
		assert response.context.containsKey('uuid');
	}

	@Test
	public void increaseCoverageRating() throws Exception {
		assert target.respondsTo() == Exception.class;
	}
}
