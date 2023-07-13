package mobi.mofokom.javax.slee.annotation.event;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author wozza
 * @author martins
 */
@Documented
@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EventFiring {

    /**
     * The description element may contain any descriptive text about the parent
     * element.
     *
     * @return
     */
    String description() default "";

    /**
     * The event-type-ref element identifies the event type received or fired by
     * the SBB. It contains the name, vendor, and version of the event type.
     *
     * @return
     */
    EventTypeRef value();
}
