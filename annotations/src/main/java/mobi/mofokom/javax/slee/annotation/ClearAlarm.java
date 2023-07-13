package mobi.mofokom.javax.slee.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;

/**
 * Calls to methods annotated with Rollback are equivalent to calling
 * AlarmFacility.raiseAlarm
 *
 * @author wozza
 * @author martins
 */
@Documented
@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ClearAlarm {

    /**
     *
     * @return
     */
    public String alarmType() default "";

    /**
     *
     * @return
     */
    public String instanceId() default "";

    /**
     *
     * @return
     */
    public int alarmLevel() default 0;
}
