package mobi.mofokom.javax.slee.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Calls to methods annotated with RainAlarm are equivalent to calling
 * AlarmFacility.raiseAlarm
 *
 * @author wozza
 * @author martins
 */
@Documented
@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RaiseAlarm {

    /**
     *
     * @return
     */
    public String alarmType();

    /**
     *
     * @return
     */
    public String instanceId();

    /**
     *
     * @return
     */
    public int alarmLevel();

    /**
     *
     * @return
     */
    public String alarmMessage() default "";

    /**
     *
     * @return
     */
    public boolean catchException() default false;
}
