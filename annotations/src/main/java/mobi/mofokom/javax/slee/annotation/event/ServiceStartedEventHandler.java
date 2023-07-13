
package mobi.mofokom.javax.slee.annotation.event;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import mobi.mofokom.javax.slee.annotation.event.EventHandler.InitialEventSelect;


/**
 * 
 * @author wozza
 * @author martins
 */
@Documented
@Target(value={ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ServiceStartedEventHandler {

    /**
     * 
     */
    public static String SERVICE_STARTED_EVENT_1_0_NAME = "javax.slee.serviceactivity.ServiceStartedEvent";
    /**
     * 
     */
    public static String SERVICE_STARTED_EVENT_1_0_VENDOR = "javax.slee";
    /**
     * 
     */
    public static String SERVICE_STARTED_EVENT_1_0_VERSION = "1.0";

    /**
     * 
     */
    public static String SERVICE_STARTED_EVENT_1_1_NAME = SERVICE_STARTED_EVENT_1_0_NAME;
    /**
     * 
     */
    public static String SERVICE_STARTED_EVENT_1_1_VENDOR = SERVICE_STARTED_EVENT_1_0_VENDOR;
    /**
     * 
     */
    public static String SERVICE_STARTED_EVENT_1_1_VERSION = "1.1";

    /**
     * 
     * @return
     */
    boolean initialEvent() default true;

    /**
     * 
     * @return
     */
    InitialEventSelect[] initialEventSelect() default { InitialEventSelect.ActivityContext };

    /**
     * 
     * @return
     */
    String initialEventSelectorMethod() default "";

    /**
     * 
     * @return
     */
    String eventResourceOption() default "";

    /**
     * 
     * @return
     */
    boolean maskOnAttach() default false;
    /**
     * 
     * @return
     */
    boolean lastInTransaction() default false;

    /**
     * 
     * @return
     */
    EventTypeRef eventType() default @EventTypeRef(name=SERVICE_STARTED_EVENT_1_1_NAME,vendor=SERVICE_STARTED_EVENT_1_1_VENDOR,version=SERVICE_STARTED_EVENT_1_1_VERSION);

}
