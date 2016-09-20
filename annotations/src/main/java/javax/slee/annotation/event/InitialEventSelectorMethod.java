package javax.slee.annotation.event;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The initial-event-selector-method-name contains the name of an initial event
 * selector method defined in the SBB abstract class.
 *
 *
 * @author martins
 * @author wozza
 */
@Documented
@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface InitialEventSelectorMethod {

    /**
     * The event-type-ref element identifies the event type received or fired by
     * the SBB. It contains the name, vendor, and version of the event type.
     *
     *
     * @return
     */
    EventTypeRef[] value();
}
