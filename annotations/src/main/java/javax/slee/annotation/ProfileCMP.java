package javax.slee.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The get-profile-cmp-method declares a get-profile-cmp method defined in the
 * SBB abstract class. It contains an optional description, a reference to the
 * profile specification component type expected to be returned from the method,
 * and the name of the get-profile-cmp method.
 *
 * @author wozza
 * @author martins
 */
@Documented
@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface ProfileCMP {

    /**
     * The description element may contain any descriptive text about the parent
     * element.
     *
     * @return
     */
    String description() default "";

    /**
     * The profile-spec-alias-ref element contains the name of a profile
     * specification component alias that has been defined in a profile-spec
     * element elsewhere in the enclosing sbb element in the deployment
     * descriptor.
     *
     *
     * @return
     */
    String profileAliasRef();
}
