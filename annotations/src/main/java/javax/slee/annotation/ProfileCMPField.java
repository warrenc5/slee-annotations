package javax.slee.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author wozza
 * @author martins
 */
@Documented
@Target(value = {ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProfileCMPField {
    /**
     * The description element may contain any descriptive text about the parent
     * element.
     * 
     * @return
     */
    String description() default "";


    /**
     *
     * @return
     */
    boolean unique() default false;

    /**
     *
     * @return
     */
    Collator uniqueCollator() default @Collator(collatorAlias = "{generated}", localeLanguage = "{generated}");

    /**
     * The optional collator-ref attribute can only be specified if the Java
     * type of the CMP field is java.lang.String. This attribute references a
     * collator by its collator-alias and is used to determine equality and
     * ordering of string values in place of String.equals(Object) and
     * String.compareTo(String).
     *
     *
     * @return
     */
    Collator indexCollator() default @Collator(collatorAlias = "{generated}", localeLanguage = "{generated}");

    /**
     * The index-hint element is used to provide information to the SLEE about a
     * type of query that may be executed against profiles in a profile table
     * created from this profile specification. The SLEE can use this
     * information when deciding about what indexes it might create for the
     * profile table.
     *
     *
     * @return
     */
    QueryOperator indexHint() default QueryOperator.NONE;

    /**
     * The query-operator attribute specifies a query term or operator that may
     * be applied to the CMP field.
     *
     */
    enum QueryOperator {

        /**
         *
         */
        NONE,
        /**
         *
         */
        EQUALS,
        /**
         *
         */
        NOT_EQUALS,
        /**
         *
         */
        LESS_THAN,
        /**
         *
         */
        LESS_THAN_OR_EQUALS,
        /**
         *
         */
        GREATER_THAN,
        /**
         *
         */
        GREATER_THAN_OR_EQUALS,
        /**
         *
         */
        RANGE_MATCH,
        /**
         *
         */
        LONGEST_PREFIX_MATCH,
        /**
         *
         */
        HAS_PREFIX
    };

    /**
     * Should the annotation processor generate a setter for this field
     *
     * @return
     */
    boolean setter() default false;

}
