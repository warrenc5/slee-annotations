package mobi.mofokom.javax.slee.annotation.event;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import mobi.mofokom.javax.slee.annotation.LibraryRef;

/**
 *
 * @author martins
 * @author wozza
 */
@Documented
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventType {

    String id() default "";

    /*
     *
     * The description element may contain any descriptive text about the parent
     * element.
     *
     *
     */
    String description() default "";

    /**
     * The library-ref element declares a reference to a library component
     * containing common classes that the event types in this component jar
     * require. It contains an optional description and the name, vendor, and
     * version of the library component.
     *
     *
     *
     * @return
     */
    LibraryRef[] libraryRefs() default {};

    /**
     * The event-type-name element contains the name of the event type
     * component.
     *
     *
     * @return
     */
    String name();

    /**
     *
     * The event-type-vendor element contains the vendor of the event type
     * component.
     *
     *
     * @return
     */
    String vendor();

    /**
     *
     * The event-type-version element contains the version of the event type
     * component. The version number typically should have the general form
     * "major-version.minor-version".
     *
     *
     * @return
     */
    String version();

    /**
     * The event-class-name element contains the fully-qualified name of the
     * event type's class or interface.
     *
     * Used in: event-definition
     *
     * Example:
     * &lt;event-class-name&gt;javax.slee.ActivityEndEvent&lt;/event-class-name&gt;
     */
    Class eventClass() default Void.class;
}
