package nz.ac.auckland.syllabus.events

import groovy.transform.CompileStatic
import nz.ac.auckland.syllabus.payload.EventRequestBase
import nz.ac.auckland.syllabus.payload.EventResponseBase
import org.junit.Test

/**
 * Author: Marnix
 *
 * This test was introduced to make sure the marshalling types get cached
 * after they have been first found out. This seems currently not be happening
 */
@CompileStatic
class MarshallingTypesNotCached {

	/**
	 * Make sure they get stored.
	 */
	@Test
	public void shouldStoreMarshallingTypeDeterminations() {
		EventDispatcher dispatcher = new EventDispatcher();
		EventHandler handler = new TestHandler()
		List<Class> classes = dispatcher.getMarshallingTypes(handler);

		assert classes[0] == NormalRequest
		assert classes[1] == NormalResponse

		assert dispatcher.marshallingTypes[handler] == classes

	}

	// -------------------------------------------------------------------------------------
	//      Data structures
	// -------------------------------------------------------------------------------------

	/**
	 * A payload that contains a list of something
	 *
	 * @param < T >  is the type of the list elements
	 */
	public static class NormalRequest extends EventRequestBase {
		List<String> items;
	}

	/**
	 * Response
	 */
	public static class NormalResponse extends EventResponseBase {
		int code;
	}

	/**
	 * Test handler implementation
	 */
	public static class TestHandler implements EventHandler<NormalRequest, NormalResponse> {

		@Override
		NormalResponse handleEvent(NormalRequest payload) throws Exception {
			return new NormalResponse(code: 200);
		}
	}


}
