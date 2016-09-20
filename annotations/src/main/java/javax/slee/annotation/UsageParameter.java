package javax.slee.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The usage-parameter element allows various properties of a usage parameter to
 * be specified at deployment time. The name attribute of this element contains
 * the name of a usage parameter defined in the usage parameters interface.
 *
 * @author wozza
 * @author martins
 */
@Documented
@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface UsageParameter {

    /**
     * The notifications-enabled attribute allows the initial state of the
     * notification generation flag for the usage parameter in each usage
     * parameter set it appears in to be specified.
     *
     * @return
     */
    boolean notificationsEnabled() default false;
}
