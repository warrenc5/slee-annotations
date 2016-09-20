package javax.slee.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The sbb-ref element declares a reference to an SBB component type and
 * establishes an alias name to that type. It contains an optional description
 * about the reference, the name, vendor, and version of the referenced SBB, and
 * the alias name for the reference.
 *
 *
 * @author wozza
 * @author martins
 */
@Documented
@Target(value = {ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface SbbRef {

    /**
     * The description element may contain any descriptive text about the parent
     * element.
     *
     * @return
     */
    String description() default "";

    /**
     * The sbb-name element contains the name of the SBB component.
     *
     *
     * @return
     */
    String name();

    /**
     * The sbb-vendor element contains the vendor of the SBB component.
     *
     * @return
     */
    String vendor();

    /**
     * The sbb-version element contains the version of the SBB component. The
     * version number typically should have the general form
     * "major-version.minor-version".
     *
     * @return
     */
    String version();

    /**
     * The sbb-alias element contains an arbitrary name that can be used to
     * reference, elsewhere in the deployment descriptor, the SBB component type
     * being defined by the parent element.
     *
     * @return
     */
    String alias();
}
