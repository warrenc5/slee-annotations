package mobi.mofokom.javax.slee.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The activity-context-attribute-alias element allows an SBB to alias activity
 * context attributes. Aliasing an attribute causes it to become available in a
 * global namespace public to all SBBs. The element contains an optional
 * description, the attribute alias name, and the names of zero or more
 * attributes in the SBB's activity context interface that should be aliased to
 * the given name.
 *
 * @author wozza
 * @author martins
 */
@Documented
@Target(value = {ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityContextAttributeAlias {

    /**
     *
     * @return
     */
    String description() default "";

    /**
     * The sbb-activity-context-attribute-name element contains the name of an
     * attribute in the SBB's activity context interface.
     *
     * The name of the referent attribute or otherwise empty if this is the refering alias .
     *
     * @return
     */
    String attributeName() default "";
}
