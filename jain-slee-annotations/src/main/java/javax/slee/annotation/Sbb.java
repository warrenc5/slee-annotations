package javax.slee.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.slee.ActivityContextInterface;
import javax.slee.SbbLocalObject;

/*
 * The sbb element defines an SBB. It contains an optional description about the
 * SBB being defined, the name, vendor, and version of the SBB being defined, an
 * optional alias for referencing the SBB elsewhere in the deployment
 * descriptor, zero or more references to any required library components, zero
 * or more child SBB references, zero or more profile specification references,
 * mandatory information about the SBB's interfaces and classes, an optional
 * reference to a profile specification to be used for the SBB's address profile
 * table, zero or more event entries, zero or more activity context attribute
 * alias definitions, zero or more JNDI environment entry definitions, zero or
 * more resource adaptor type bindings, and zero or more EJB references.
 *
 * @author martins
 * @author wozza
 */
@Documented
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface Sbb {

    /**
     *
     */
    public static final String ALL_PERMISSIONS = "grant { permission java.security.AllPermission ; };";

    /**
     * The description element may contain any descriptive text about the parent
     * element.
     *
     * @return
     */
    String description() default "";

    String id() default "";

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
    String alias() default "";

    /**
     * The library-ref element declares a reference to a library component
     * containing common classes that the SBB requires. It contains an optional
     * description and the name, vendor, and version of the library component.
     *
     * @return
     */
    LibraryRef[] libraryRefs() default {};

    /**
     * The sbb-ref element declares a reference to an SBB component type and
     * establishes an alias name to that type. It contains an optional
     * description about the reference, the name, vendor, and version of the
     * referenced SBB, and the alias name for the reference.
     *
     * @return
     */
    SbbRef[] sbbRefs() default {};

    /**
     * The profile-spec-ref element declares a reference to a profile
     * specification component type and optionally establishes an alias name to
     * that type. It contains an optional description about the reference, the
     * name, vendor, and version of the referenced profile specification, and
     * the optional alias name for the reference.
     *
     *
     *
     * @return
     */
    ProfileSpecRef[] profileSpecRefs() default {};

    /**
     * The sbb-local-interface element contains an optional description about
     * the SBB local interface and the name of the SBB local interface.
     *
     *
     * @return
     */
    Class<? extends SbbLocalObject> localInterface() default SbbLocalObject.class;

    /**
     * The isolate-security-permissions attribute of this element controls
     * whether or not security permissions of other SBBs in the call stack are
     * propagated to the SBB when a method on the SBB local interface is
     * invoked. If the value of this attribute is False, then a business method
     * invoked on an SBB object as a result of an SBB local interface method
     * invocation runs with an access control context that includes the
     * protection domain(s) of the SBB as well as the protection domains of any
     * other SBBs in the call stack, such as the SBB that invoked the SBB local
     * interface method. If the value of this attribute is True, the SLEE
     * automatically wraps the SBB local interface method invocation in an
     * AccessController.doPrivileged() block in order to isolate the security
     * permissions of the invoked SBB, i.e. the security permissions of other
     * SBBs in the call stack do not affect the invoked SBB. The default value
     * of this attribute is False.
     *
     * @return
     */
    boolean isolateSecurityPermissions() default false;

    /**
     *
     * The sbb-activity-context-interface-name element contains the
     * fully-qualified name of the SBB activity context interface.
     *
     *
     * @return
     */
    Class<? extends ActivityContextInterface> activityContextInterface() default ActivityContextInterface.class;

    /**
     * The sbb-usage-parameters-interface-name element contains the
     * fully-qualified name of the SBB's usage parameters interface.
     *
     * @return
     */
    Class usageParametersInterface() default Object.class;

    /**
     *
     * The address-profile-spec-alias-ref element contains the name of a profile
     * specification component alias that has been defined in a profile-spec-ref
     * element elsewhere in the enclosing sbb element in the deployment
     * descriptor. It is used to declare the profile specification of the
     * address profile table used by the SBB.
     *
     *
     * @return
     */
    String addressProfileSpecAliasRef() default "";

    /**
     * The resource-adaptor-type-binding element declares bindings between an
     * SBB and external resources. It contains an optional description, a
     * mandatory reference to a resource adaptor type, an optional name where
     * the resource adaptor type's activity context interface object will be
     * bound in the SBB's JNDI environment, and zero or more resource adaptor
     * entity bindings.
     *
     * @return
     */
    ResourceAdaptorTypeBinding[] resourceAdaptorTypeBinding() default {};

    /**
     * The ejb-ref element allows an SBB to reference an Enterprise Java Bean.
     * It contains an optional description, the JNDI name where the home
     * interface of the EJB will be bound in the SBB's environment, the type of
     * the EJB, and the home and remote interface types.
     *
     *
     * @return
     */
    EJBRef[] ejbRef() default {};

    /**
     * The security-permission-spec element specifies security permissions based
     * on the security policy file syntax. Refer to the following URL for
     * definition of Sun's security policy file syntax:
     *
     * http://java.sun.com/j2se/1.4.2/docs/guide/security/PolicyFiles.html#FileSyntax
     *
     * Note: The codeBase argument of any grant entries in the security
     * permission specification is ignored. The SLEE assumes the code base to be
     * SBB component jar file.
     *
     * The security permissions specified here are granted to classes loaded
     * from the SBB jar file only. They are not granted to classes loaded from
     * any other dependent jar required by SBB defined in the SBB component
     * jar's deployment descriptor, nor to any dependent library jars.
     *
     *
     * @return
     */
    String securityPermissions() default "";
}
