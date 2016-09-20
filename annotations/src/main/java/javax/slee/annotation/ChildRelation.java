package javax.slee.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The get-child-relation-method element declares a get-child-relation method
 * defined in the SBB abstract class. It contains an optional description, a
 * reference to the SBB component type of the child SBB, the name of the
 * get-child-relation method, and a default priority for the child SBB.
 *
 *
 * @author wozza
 * @author martins
 */
@Documented
@Target(value = {ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.CLASS)
public @interface ChildRelation {

    /**
     * The description element may contain any descriptive text about the parent
     * element.
     *
     * @return
     */
    String description() default "";

    /**
     * The sbb-alias-ref element contains the name of an SBB component that has
     * been aliased elsewhere in the enclosing sbb element in the deployment
     * descriptor.
     *
     *
     * @return
     */
    String sbbAliasRef();

    /**
     * The default-priority element contains the default priority of a child
     * SBB. The default priority must lie in the range -128 thru 127.
     *
     *
     * @return
     */
    int defaultPriority() default 0;
}
