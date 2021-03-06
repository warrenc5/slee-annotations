package javax.slee.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * The cmp-field element declares a cmp field defined in the SBB abstract class.
 * It contains an optional description, the name of the cmp field, and an
 * optional SBB alias reference. The SBB alias reference is required if the cmp
 * field stores SBB local object references.
 *
 *
 * @author wozza
 * @author martins
 */
@Documented
@Target(value = {ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CMPField {

    /**
     * The sbb-alias-ref element contains the name of an SBB component that has
     * been aliased elsewhere in the enclosing sbb element in the deployment
     * descriptor.
     *
     * @return
     */
    String sbbAliasRef() default "";
}
