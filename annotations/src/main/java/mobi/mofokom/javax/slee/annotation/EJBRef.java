package mobi.mofokom.javax.slee.annotation;

/**
 * The ejb-ref element allows an SBB to reference an Enterprise Java Bean. It
 * contains an optional description, the JNDI name where the home interface of
 * the EJB will be bound in the SBB's environment, the type of the EJB, and the
 * home and remote interface types.
 *
 *
 * @author wozza
 * @author martins
 */
public @interface EJBRef {

    /**
     *
     * @return
     */
    String description() default "";

    /**
     *
     * The ejb-ref-name element contains the namespace path, relative to the
     * java:comp/env context, where the resource adaptor entity's resource
     * adaptor interface object should be bound in the SBB's JNDI environment.
     *
     *
     * @return
     */
    String name() default "";

    /**
     * The ejb-ref-type element contains the exptected type of the referenced
     * Enterprise Java Bean. Valid values are Entity or Session.
     *
     *
     * @return
     */
    Type type();

    /**
     *
     * @return
     */
    Class<? extends javax.ejb.EJBHome> home() default javax.ejb.EJBHome.class;

    /**
     * The home element contains the fully-qualified name of a referenced
     * Enterprise Java Bean's remote home interface.
     *
     * @return
     */
    Class<? extends javax.ejb.EJBObject> remote() default javax.ejb.EJBObject.class;

    /**
     *
     * The remote element contains the fully-qualified name of a referenced
     * Enterprise Java Bean's remote component interface.
     *
     *
     */
    enum Type {

        /**
         *
         */
        SESSION,
        /**
         *
         */
        ENTITY;
    }
}
