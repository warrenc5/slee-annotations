
package javax.slee.annotation;

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
@Target(value={ElementType.ANNOTATION_TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.CLASS)
public @interface ConfigProperty {

    /**
     * 
     * @return
     */
    String defaultValue() default "";

}
