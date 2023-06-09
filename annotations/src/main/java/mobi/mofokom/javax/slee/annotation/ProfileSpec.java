package mobi.mofokom.javax.slee.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.slee.profile.Profile;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.ProfileTable;

/**
 * The profile-spec element defines a profile specification. It contains an
 * optional description about the profile specification being defined, the name,
 * vendor, and version of the profile specification, zero or more references to
 * any required library components, zero or more references to other profile
 * specifications used by this profile specification, mandatory information
 * about the profile specification's interfaces and classes, zero or more JNDI
 * environment entry definitions, zero or more collators for use in static
 * queries or indexes, optional profile attribute indexing information and
 * hints, zero or more static query definitions, and optional hints to the SLEE
 * about the use of the profile specification.
 *
 * @author wozza
 * @author martins
 */
@Documented
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProfileSpec {

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
     * The profile-spec-name element contains the name of the profile
     * specification component.
     *
     *
     * @return
     */
    String name();

    /**
     * The profile-spec-vendor element contains the vendor of the profile
     * specification component.
     *
     * @return
     */
    String vendor();

    /**
     * The profile-spec-version element contains the version of the profile
     * specification component. The version number typically should have the
     * general form "major-version.minor-version".
     *
     * @return
     */
    String version();

    /**
     * The profile-cmp-interface-name element contains the fully-qualified name
     * of the profile specification's profile cmp interface.
     *
     * @return
     */
    Class<?> cmpInterface();

    /**
     * The profile-local-interface element contains an optional description
     * about the profile specification's profile local interface, and the name
     * of the profile local interface.
     *
     *
     * @return
     */
    Class<? extends ProfileLocalObject> localInterface() default ProfileLocalObject.class;

    /**
     * The profile-management-interface element contains an optional description
     * about the profile specification's profile management interface, and the
     * name of the profile management interface.
     *
     *
     * @return
     */
    Class<?> managementInterface() default Object.class;

    /**
     * The profile-abstract-class element contains an optional description about
     * the profile specification's profile abstract class, and the name of the
     * profile abstract class.
     *
     * @return
     */
    Class<? extends Profile> abstractClass() default Profile.class;

    /**
     * The profile-table-interface element contains an optional description
     * about the profile specification's profile table interface, and the name
     * of the profile table interface.
     *
     * @return
     */
    Class<? extends ProfileTable> tableInterface() default ProfileTable.class;

    /**
     * The profile-usage-parameters-interface element declares a usage
     * parameters interface for the profile specification. It contains an
     * optional description, the name of the usage parameters interface, and
     * properties for zero or more usage parameters in the usage parameters
     * interface.
     *
     * @return
     */
    public Class usageParametersInterface() default Object.class;

    /**
     * The library-ref element declares a reference to a library component
     * containing common classes that the profile specification requires. It
     * contains an optional description and the name, vendor, and version of the
     * library component.
     *
     *
     * @return
     */
    LibraryRef[] libraryRefs() default {};

    /**
     * The profile-spec-ref element declares a reference to a profile
     * specification component type. It contains an optional description about
     * the reference and the name, vendor, and version of the referenced profile
     * specification.
     *
     * @return
     */
    ProfileSpecRef[] profileSpecRefs() default {};

    /**
     *
     * The security-permission-spec element specifies security permissions based
     * on the security policy file syntax. Refer to the following URL for
     * definition of Sun's security policy file syntax:
     *
     * http://java.sun.com/j2se/1.4.2/docs/guide/security/PolicyFiles.html#FileSyntax
     *
     * Note: The codeBase argument of any grant entries in the security
     * permission specification is ignored. The SLEE assumes the code base to be
     * profile specification component jar file.
     *
     * The security permissions specified here are granted to classes loaded
     * from the profile specification jar file only. They are not granted to
     * classes loaded from any other dependent jar required by profile
     * specification defined in the profile specification component jar's
     * deployment descriptor, nor to any dependent library jars.
     *
     * @return
     */
    String securityPermissions() default "";

    /**
     * The profile-read-only attribute of this element specifies whether the
     * SLEE Component object view of profiles created from this profile
     * specification is read-only or read-write. If the value of this attribute
     * is True then SLEE components may not create, remove or modify profiles
     * created from this profile specification. If the value of this attribute
     * is False, SLEE Components are permitted to create, remove, or modify
     * profiles created from this profile specification.
     *
     * @return
     */
    boolean readOnly() default false;

    /**
     * The profile-events-enabled attribute of this element specifies whether
     * profile events will be fired on the profile table activities of profile
     * tables created from this profile specification when profiles are added
     * to, updated in, or removed from these profile table.
     *
     * @return
     */
    boolean eventsEnabled() default false;

    /**
     * The profile-hints element contains no text but defines a single-profile
     * attribute. This attribute, if set to "True", implies that a profile table
     * created from this profile specification will only ever contain at most
     * one profile. The SLEE may be able to use this information to provide a
     * better implementation of the profile specification. The default value of
     * this attribute is "False".
     *
     * @return
     */
    boolean singleProfileHint() default false;

    /**
     * The query element declares a static query. It contains an optional
     * description, optional query options, zero or more query parameters, and a
     * query search term.
     *
     * The name attribute of this element identifies the name of the query.
     *
     *
     * @return
     */
    StaticQuery[] queries() default {};

    /**
     * The collator element defines a collator that can be used for
     * locale-sensitive comparisons of profile attributes.
     *
     * @return
     */
    Collator[] collators() default {};

    /**
     *
     * The isolate-security-permissions attribute of this element controls
     * whether or not security permissions of other classes in the call stack
     * are propagated to the profile when a method on the Profile local
     * interface is invoked. If the value of this attribute is False, then a
     * business method invoked on a Profile object as a result of an Profile
     * local interface method invocation runs with an access control context
     * that includes the protection domain(s) of the Profile Specification as
     * well as the protection domains of any other classes in the call stack,
     * such as the SLEE component that invoked the Profile local interface
     * method. If the value of this attribute is True, the SLEE automatically
     * wraps the Profile local interface method invocation in an
     * AccessController.doPrivileged() block in order to isolate the security
     * permissions of the invoked Profile, i.e. the security permissions of
     * other classes in the call stack do not affect the invoked Profile. The
     * default value of this attribute is False.
     *
     *
     * @return
     */
    boolean isolateSecurityPermissions() default false;
}
