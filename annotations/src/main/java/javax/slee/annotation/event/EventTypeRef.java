
package javax.slee.annotation.event;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author wozza
 * @author martins
 */
@Documented
@Target(value={ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface EventTypeRef {

    /**
     * The event-type-name element contains the name of an event type.


     * @return
     */
    String name();

    /**
     * The event-type-vendor element contains the vendor of an event type.


     * @return
     */
    String vendor();

    /**
     * The event-type-version element contains the version of an event type.


     * @return
     */
    String version();

}
