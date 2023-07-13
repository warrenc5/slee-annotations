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
 * @author wozza
 * @author martins
 */
@Documented
@Target(value = {ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ConfigProperty {

    /**
     * 
     * @return
     */
    String defaultValue() default "";

    /**
     * The strategy the generated code shoud use to synchronize the instance variable
     * @return
     */
    SynchronizationType synchronizationStrategy() default SynchronizationType.ABSTRACT_GENERATED;
}
