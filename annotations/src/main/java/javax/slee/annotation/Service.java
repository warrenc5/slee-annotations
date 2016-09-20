package javax.slee.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.slee.Sbb;

/**
 * The service element defines a service component. It contains an optional
 * description about the service being defined, the name, vendor, and version of
 * the service, a reference to the root SBB type, a default priority for the
 * root SBB, and the optional name of an address profile table used by the
 * service.
 *
 *
 * @author wozza
 * @author martins
 */
@Documented
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface Service {

    String id() default "";

    /**
     * The description element may contain any descriptive text about the parent
     * element.
     *
     * @return
     */
    String description() default "";

    /**
     * The service-name element contains the name of the service component.
     *
     * @return
     */
    String name();

    /**
     * The service-vendor element contains the vendor of the service component.
     *
     * @return
     */
    String vendor();

    /**
     * The service-version element contains the version of the service
     * component. The version number typically should have the general form
     * "major-version.minor-version".
     *
     *
     * @return
     */
    String version();

    /**
     * The root-sbb element defines the SBB component type of the service's root
     * SBB. It contains an optional description about the root SBB, and the
     * name, vendor, and version of the root SBB.
     *
     *
     * @return
     */
    Class<? extends Sbb> rootSbb();

    /**
     * The default-priority element contains the default priority of the root
     * SBB. The default priority must lie in the range -128 thru 127.
     *
     *
     * @return
     */
    byte defaultPriority() default 0;

    /**
     * The address-profile-table element contains the name of the address
     * profile table used by the service.
     *
     * @return
     */
    String addressProfileTable() default "";
}
