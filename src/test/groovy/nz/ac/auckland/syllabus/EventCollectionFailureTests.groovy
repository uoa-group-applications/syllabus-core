package nz.ac.auckland.syllabus

import groovy.transform.CompileStatic
import nz.ac.auckland.common.testrunner.BatheCommandLine
import nz.ac.auckland.common.testrunner.SimpleSpringRunner
import nz.ac.auckland.common.testrunner.SimpleTestRunner
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.test.context.ContextConfiguration

/**
 *
 * @author: Richard Vowles - https://plus.google.com/+RichardVowles
 */
@BatheCommandLine(["-Pclasspath:/test.properties"])
@RunWith(SimpleTestRunner.class)
class EventCollectionFailureTests {
	final shouldFail = new GroovyTestCase().&shouldFail

	@Test
	public void shouldBlow() {
		shouldFail(RuntimeException) {
			new ClassPathXmlApplicationContext("/event-failure.xml")
		}
	}
}
