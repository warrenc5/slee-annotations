package javax.slee.annotation;

import javax.slee.annotation.event.EventTypeRef;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The resource-adaptor-type element defines a resource adaptor type component.
 * It contains an optional description about the resource adaptor type being
 * defined, the name, vendor, and version of the resource adaptor type, zero or
 * more references to any required library components, mandatory information
 * about the resource adaptor type's classes and interfaces, and zero or more
 * references to the event types that can be produced by resource adaptors of
 * this resource adaptor type.
 *
 * @author wozza
 * @author martins
 */
@Documented
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceAdaptorType {

    
    String id() default "";
    /**
     * The description element may contain any descriptive text about the parent
     * element.
     *
     * @return
     */
    String description() default "";

    /**
     * The resource-adaptor-type-name element contains the name of the resource
     * adaptor type component.
     *
     * @return
     */
    String name();

    /**
     * The resource-adaptor-type-vendor element contains the vendor of the
     * resource adaptor type component.
     *
     *
     * @return
     */
    String vendor();

    /**
     * The resource-adaptor-type-version element contains the version of the
     * resource adaptor type component. The version number typically should have
     * the general form "major-version.minor-version".
     *
     *
     * @return
     */
    String version();

    /**
     * The library-ref element declares a reference to a library component
     * containing common classes that the resource adaptor type requires. It
     * contains an optional description and the name, vendor, and version of the
     * library component.
     *
     *
     * @return
     */
    LibraryRef[] libraryRefs() default {};

    /**
     * The activity-type-name element contains the fully-qualified name of the
     * class or interface implemented by the activity object.
     *
     *
     * @return
     */
    Class<?>[] activityType() default {};

    /**
     * The activity-context-interface-factory-interface-name element contains
     * the fully-qualified name of the activity context interface factory for
     * the resource adaptor type.
     *
     *
     * @return
     */
    Class<?> aciFactory() default Object.class;

    /**
     * The resource-adaptor-interface-name element contains the fully-qualified
     * name of the resource adaptor interface for the resource adaptor type.
     *
     *
     * @return
     */
    Class<?> raInterface() default Object.class;

    /**
     * The event-type-ref element identifies an event type that may be generated
     * by resource adaptors of the resource adaptor type. It contains the name,
     * vendor, and version of the event type.
     *
     *
     * @return
     */
    EventTypeRef[] eventTypeRef() default {};
}
