package mobi.mofokom.javax.slee.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The collator element defines a collator that can be used for locale-sensitive
 * comparisons of profile attributes. It contains an optional description, a
 * mandatory alias for referencing the collator elsewhere in the deployment
 * descriptor, the mandatory language for the collator's locale, and an optional
 * country and variant for the language.
 *
 * If the locale-variant element is specified, the locale-country element must
 * also be specified.
 *
 * See java.text.Collator for details on the use of collators.
 *
 * @author wozza
 * @author martins
 */
@Documented
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Collator {
    /**
     * The description element may contain any descriptive text about the parent
     * element.
     * 
     * @return
     */
    String description() default "";

    /**
     *The collator-alias element contains an arbitrary name that can be used to
reference, elsewhere in the deployment descriptor, the collator being defined
by the parent element.


     * @return
     */
    String collatorAlias();

    /**
     *The locale-language element specifies an ISO language code that identifies the
language of the locale.  These codes are the lower-case, two-letter codes as
defined by ISO-639.


     * @return
     */
    String localeLanguage();

    /**
     *The locale-country element specifies an ISO country code that identifies a
specific country for the locale language. These codes are the upper-case,
two-letter codes as defined by ISO-3166.


     * @return
     */
    String localeCountry() default "";

    /**
     *The locale-variant element specifies a vendor or browser-specific variant to
a locale language.


     * @return
     */
    String localeVariant() default "";

    /**
     * The optional strength attribute of this element specifies the strength
     * property of the collator.
     *
     * @return
     */
    Strength strength() default Strength.IDENTICAL;

    /**
     * The optional decompostion attribute specifies the decomposition mode of
     * the collator. If either of these attributes are not specified the default
     * properties of the collator for the specified locale are used.
     *
     *
     * @return
     */
    Decomposition decomposition() default Decomposition.NONE;

    /**
     *
     */
    public enum Strength {

        /**
         *
         */
        PRIMARY,
        /**
         *
         */
        SECONDARY,
        /**
         *
         */
        TERTIARY,
        /**
         *
         */
        IDENTICAL
    }

    /**
     *
     */
    enum Decomposition {

        /**
         *
         */
        FULL,
        /**
         *
         */
        CANONICAL,
        /**
         *
         */
        NONE
    }
}
