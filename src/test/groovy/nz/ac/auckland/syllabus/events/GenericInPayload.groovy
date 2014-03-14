package nz.ac.auckland.syllabus.events

import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import nz.ac.auckland.syllabus.payload.EventRequestBase
import nz.ac.auckland.syllabus.payload.EventResponseBase
import nz.ac.auckland.util.JacksonHelper
import org.junit.Test

/**
 * Author: Marnix
 *
 * Detected by Mr. Bygrave. When wrapping the payload in a subclass, or something that
 * has a generic in it, the servlet is unable to properly parse it. This test tries to mimick
 * that behavior. We'll try to make it pass.
 */
@CompileStatic
class GenericInPayload {

	/**
	 * Test that the event construction works
	 */
	private static final String CURRENT_VERSION = '1'

	@Test
	public void shouldPassWithDirectCall() {
		Payload<String> payload = new Payload<>(items: ['1', '2', '3'] as List<String>);
		EventHandler<Payload<String>, NormalResponse> handler = new TestHandler();
		NormalResponse response = handler.handleEvent(payload);

		assert response?.code == 200;
	}

	/**
	 * Test that the deserialization with a generic type works
	 */
	@Test
	@CompileStatic(TypeCheckingMode.SKIP)
	public void shouldPassWhenParsedFromJson() {
		EventDispatcher dispatcher = new EventDispatcher()

		// actually dispatch
		List<Class> classes = dispatcher.getMarshallingTypes(new TestHandler());

		assert classes[0] == Payload
		assert classes[1] == NormalResponse

		String json = '{ "items" : ["1", "2", "3"] }';
		Payload<String> str = JacksonHelper.deserialize(json, classes[0]) as Payload<String>;
		assert str
	}

	/**
	 * Test that the deserialization with a generic type works
	 */
	@Test
	public void shouldPassMultilevelWhenParsedFromJson() {
		EventDispatcher dispatcher = new EventDispatcher()

		// actually dispatch
		List<Class> classes = dispatcher.getMarshallingTypes(new TestHandler2());

		assert classes[0] == Payload
		assert classes[1] == NormalResponse
	}

	// -------------------------------------------------------------------------------------
	//      Data structures
	// -------------------------------------------------------------------------------------

	/**
	 * A payload that contains a list of something
	 *
	 * @param < T >  is the type of the list elements
	 */
	public static class Payload<T> extends EventRequestBase {
		List<T> items;
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
	public static class TestHandler2 implements EventHandler<Payload<Payload<String>>, NormalResponse> {

		@Override
		NormalResponse handleEvent(Payload<Payload<String>> payload) throws Exception {
			return new NormalResponse(code: 200);
		}
	}

	/**
	 * Test handler implementation
	 */
	public static class TestHandler implements EventHandler<Payload<String>, NormalResponse> {

		@Override
		NormalResponse handleEvent(Payload<String> payload) throws Exception {
			return new NormalResponse(code: 200);
		}
	}

}
