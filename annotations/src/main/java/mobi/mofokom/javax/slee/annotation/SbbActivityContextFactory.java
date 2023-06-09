
package mobi.mofokom.javax.slee.annotation;

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
@Target(value={ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SbbActivityContextFactory {

    /**
     * 
     * @return
     */
    ResourceAdaptorTypeRef value();
}
