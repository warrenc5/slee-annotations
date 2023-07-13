package mobi.mofokom.javax.slee.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import mobi.mofokom.javax.slee.annotation.SynchronizationStrategy.SynchronizationType;

/**
 *
 * The following are the legal values of the env-entry-type element:
 * java.lang.Integer, java.lang.Double, java.lang.Boolean, java.lang.Character,
 * java.lang.Byte, java.lang.Short, java.lang.Long, and java.lang.Float,
 * java.lang.String.
 *
 * @author wozza
 * @author martins
 */
@Documented
@Target(value = {ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EnvEntry {

    /**
     * The description element may contain any descriptive text about the parent
     * element.
     *
     * @return
     */
    String description() default "";

    /**
     *
     * The env-entry-value element contains the value of an SBB's environment
     * entry contant. The value must be a string that is valid for the
     * constructor of the environment entry's declared type that takes a single
     * java.lang.String parameter, or for java.lang.Character environment entry
     * types, a single character.
     *
     *
     * @return
     */
    String value();

    /**
     * The strategy the generated code shoud use to synchronize the instance variable
     * @return
     */
    SynchronizationType synchronizationStrategy() default SynchronizationType.ABSTRACT_GENERATED;
}
