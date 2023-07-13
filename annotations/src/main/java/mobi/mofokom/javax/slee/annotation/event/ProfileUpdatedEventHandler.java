
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
@Target(value = { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ProfileUpdatedEventHandler {

    /**
     * 
     */
    public static String PROFILE_UPDATED_EVENT_NAME = "javax.slee.profile.ProfileUpdatedEvent";
    /**
     * 
     */
    public static String PROFILE_UPDATED_EVENT_VENDOR = "javax.slee";
    /**
     * 
     */
    public static String PROFILE_UPDATED_EVENT_VERSION = "1.0";

    /**
     * 
     * @return
     */
    boolean initialEvent() default false;

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
    EventTypeRef eventType() default @EventTypeRef(name=PROFILE_UPDATED_EVENT_NAME,vendor=PROFILE_UPDATED_EVENT_VENDOR,version=PROFILE_UPDATED_EVENT_VERSION);
}
