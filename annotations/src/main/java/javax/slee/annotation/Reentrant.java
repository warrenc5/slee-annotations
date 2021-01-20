package javax.slee.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The reentrant attribute of this element determines whether reentrant
 * callbacks on the SBB/Profile abstract class are permitted. A reentrant
 * callback occurs when an SBB entity/Profile invokes a method on an SBB/Profile
 * local interface object which causes, either directly or indirectly, a method
 * on the original SBB entity/Profile to be invoked in the same transaction. The
 * default value of this attribute is False.
 *
 * @author martins
 * @author wozza
 */
@Documented
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Reentrant {
}
