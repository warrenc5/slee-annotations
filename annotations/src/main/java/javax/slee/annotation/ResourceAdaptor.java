package javax.slee.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The resource-adaptor element defines a resource adaptor component. It
 * contains an optional description about the resource adaptor being defined,
 * the name, vendor, and version of the resource adaptor, one or more references
 * to the resource adaptor types implemented by the resource adaptor, zero or
 * more references to any required library components, zero or more references
 * to any required profile specifications, mandatory information about the
 * resource adaptor classes, and zero or more optional configuration properties
 * defined by the resource adaptor.
 *
 * @author wozza
 * @author martins
 */
@Documented
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceAdaptor {

    /**
     *
     */
    public static final String ALL_PERMISSIONS = Sbb.ALL_PERMISSIONS;

    String id() default "";
    /**
     * The description element may contain any descriptive text about the parent
     * element.
     *
     * @return
     */
    String description() default "";

    /**
     * The resource-adaptor-name element contains the name of the resource
     * adaptor component.
     *
     * @return
     */
    String name();

    /**
     *
     * The resource-adaptor-vendor element contains the vendor of the resource
     * adaptor component.
     *
     * @return
     */
    String vendor();

    /**
     * The resource-adaptor-version element contains the version of the resource
     * adaptor component. The version number typically should have the general
     * form "major-version.minor-version".
     *
     * @return
     */
    String version();

    /**
     * The ignore-ra-type-event-type-check attribute of this element determines
     * whether or not the SLEE places event type limitations on the resource
     * adaptor. If the value of this attribute is False, then the SLEE will only
     * allow the resource adaptor to fire events of event types referenced by
     * the resource adaptor types implemented by the resource adaptor, and
     * similarly will only provide service receivable event type information to
     * the resource adaptor concerning the same limited set of event types. If
     * the value of this attribute is True, then the SLEE will provide the
     * resource adaptor with information about all event types received by all
     * services, and will allow the resource adaptor to fire events of any event
     * type. The default value of this attribute is False.
     *
     *
     * @return
     */
    boolean ignoreRATypeEventTypeCheck() default false;

    /**
     *
     * The supports-active-reconfiguration attribute of this element determines
     * when resource adaptor entities may be reconfigured with new configuration
     * properties. If the value of the supports-active-reconfiguration attribute
     * is True then the resource adaptor entities may be reconfigured when in
     * any valid state. If the value of this attribute is False, then the
     * resource adaptor entities may only be reconfigured when in the Inactive
     * state. The default value of this attribute is False.
     *
     * @return
     */
    boolean supportsActiveReconfiguration() default false;

    /**
     *
     * @return
     */
    Class<?> usageParametersInterface() default Object.class;

    /**
     *
     * @return
     */
    ConfigProperty[] properties() default {};

    /**
     * The resource-adaptor-type-ref element declares a resource adaptor type
     * the resource adaptor implements. It contains an optional description and
     * the name, vendor, and version of the resource adaptor type.
     *
     *
     * @return
     */
    ResourceAdaptorTypeRef[] typeRefs();

    /**
     * The library-ref element declares a reference to a library component
     * containing common classes that the resource adaptor requires. It contains
     * an optional description and the name, vendor, and version of the library
     * component.
     *
     *
     * @return
     */
    LibraryRef[] libraryRefs() default {};

    /**
     * The profile-spec-ref element declares a reference to a profile
     * specification component type used by the resource adaptor. It contains an
     * optional description about the reference, and the name, vendor, and
     * version of the referenced profile specification.
     *
     * @return
     */
    ProfileSpecRef[] profileSpecRefs() default {};

    /**
     * The security-permission-spec element specifies security permissions based
     * on the security policy file syntax. Refer to the following URL for
     * definition of Sun's security policy file syntax:
     *
     * http://java.sun.com/j2se/1.4.2/docs/guide/security/PolicyFiles.html#FileSyntax
     *
     * Note: The codeBase argument of any grant entries in the security
     * permission specification is ignored. The SLEE assumes the code base to be
     * resource adaptor component jar file.
     *
     * The security permissions specified here are granted to classes loaded
     * from the resource adaptor component jar file only. They are not granted
     * to classes loaded from any other dependent jar required by resource
     * adaptors defined in the resource adaptor component jar's deployment
     * descriptor, nor to any dependent library jars.
     *
     *
     * @return
     */
    String securityPermissions() default "";
}
