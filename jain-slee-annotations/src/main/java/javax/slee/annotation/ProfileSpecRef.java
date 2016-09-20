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
@Target(value = {ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface ProfileSpecRef {

    /**
     * The profile-spec-name element contains the name of a profile
     * specification component.
     *
     * @return
     */
    String name();

    /**
     * The profile-spec-version element contains the version of a profile
     * specification component.
     *
     * @return
     */
    String vendor();

    /**
     The profile-spec-version element contains the version of a profile
specification component.

*
     * @return
     */
    String version();

    /**
     The profile-spec-alias element contains an arbitrary name that can be used to
reference, elsewhere in the deployment descriptor, the profile specification
component type identified by the parent element.

*
     * @return
     */
    String alias();
}
