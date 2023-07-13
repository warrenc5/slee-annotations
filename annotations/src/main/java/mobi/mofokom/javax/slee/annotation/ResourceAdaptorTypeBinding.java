package mobi.mofokom.javax.slee.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The resource-adaptor-type-binding element declares bindings between an SBB
 * and external resources. It contains an optional description, a mandatory
 * reference to a resource adaptor type, an optional name where the resource
 * adaptor type's activity context interface object will be bound in the SBB's
 * JNDI environment, and zero or more resource adaptor entity bindings.
 *
 *
 * @author wozza
 * @author martins
 */
@Documented
@Target(value = {ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceAdaptorTypeBinding {

    /**
     *
     * @return
     */
    public String description() default "";

    /**
     * The resource-adaptor-type-ref element declares the resource adaptor type
     * the SBB is binding to. It contains an optional description and the name,
     * vendor, and version of the resource adaptor type.
     *
     *
     * @return
     */
    ResourceAdaptorTypeRef raTypeRef();

    /**
     * The activity-context-interface-factory-name element contains the
     * namespace path, relative to the java:comp/env context, where the activity
     * context interface factory object of the corresponding resource adaptor
     * type should be bound in the SBB's JNDI environment.
     *
     *
     * @return
     */
    String aciFactoryName() default "";

    /**
     * The resource-adaptor-object-name element contains the namespace path,
     * relative to the java:comp/env context, where the resource adaptor
     * entity's resource adaptor interface object should be bound in the SBB's
     * JNDI environment.
     *
     *
     * @return
     */
    String raObjectName() default "";

    /**
     * The resource-adaptor-entity-link element contains an arbitrary string
     * that the SLEE can use to locate the correct resource adaptor entity to
     * bind into the SBB's JNDI environment.
     *
     *
     *
     * @return
     */
    String raEntityLink() default "";
}
