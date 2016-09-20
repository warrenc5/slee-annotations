package javax.slee.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The resource-adaptor-type-ref element declares the resource adaptor type the
 * SBB is binding to. It contains an optional description and the name, vendor,
 * and version of the resource adaptor type.
 *
 * @author wozza
 * @author martins
 */
@Documented
@Target(value = {ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface ResourceAdaptorTypeRef {

    /**
     * The resource-adaptor-type-name element contains the name of a resource
     * adaptor type component.
     *
     * @return
     */
    String name();

    /**
     *
     * The resource-adaptor-type-vendor element contains the vendor of a
     * resource adaptor type component.
     *
     *
     * @return
     */
    String vendor();

    /**
     * The resource-adaptor-type-version element contains the version of a
     * resource adaptor type component.
     *
     * @return
     */
    String version();
}
