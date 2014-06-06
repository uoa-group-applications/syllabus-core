package nz.notpackage

import groovy.transform.CompileStatic
import nz.ac.auckland.syllabus.events.Event

/**
 *
 * This one is outside the normal package scan space, so it doesn't get picked up. We put it in by hand and ensure that the
 * app won't start.
 */
@CompileStatic
@Event(name = "hasNoInterface")
class DontPickThisOne {
}
