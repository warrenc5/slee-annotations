
package javax.slee.annotation.event;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.slee.annotation.event.EventHandler.InitialEventSelect;

import javax.slee.profile.ProfileRemovedEvent;

/**
 * 
 * @author martins
 * @author wozza
 */
@Documented
@Target(value = { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ProfileRemovedEventHandler {

    /**
     * 
     */
    public static String PROFILE_REMOVED_EVENT_NAME = "javax.slee.profile.ProfileRemovedEvent";
    /**
     * 
     */
    public static String PROFILE_REMOVED_EVENT_VENDOR = "javax.slee";
    /**
     * 
     */
    public static String PROFILE_REMOVED_EVENT_VERSION = "1.0";

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
    EventTypeRef eventType() default @EventTypeRef(name=PROFILE_REMOVED_EVENT_NAME,vendor=PROFILE_REMOVED_EVENT_VENDOR,version=PROFILE_REMOVED_EVENT_VERSION);
}
