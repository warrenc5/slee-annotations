package mobi.mofokom.javax.slee.annotation;

/**
 * Used with @Resource on fields to optimize synchronization and access 

 * @author wozza
 */
public @interface SynchronizationStrategy {

    SynchronizationType value();

    static enum SynchronizationType {

        /**
        Use the sbbCreate/sbbActivate/sbbPassivate/sbbLoad/sbbStore methods (once) to create/initialize/access the field.
         */
        SBB_LIFECYCLE,
        /**
        Use the abstract/generated method every time to access the field.
         */
        ABSTRACT_GENERATED;
    }
}
