package nz.ac.auckland.syllabus.actions

import groovy.transform.CompileStatic
import nz.ac.auckland.syllabus.events.Event

/**
 * User: marnix
 * Date: 25/03/13
 * Time: 12:29 PM
 *
 * This one should is scanned but filter out by the dispatcher as it does not implement the EventHandler interface
 */
@CompileStatic
@Event(name = "hasNoInterface")
class DontPickThisOne {
}
