package javax.slee.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * The query element declares a static query. It contains an optional
 * description, optional query options, zero or more query parameters, and a
 * query search term.
 *
 * The name attribute of this element identifies the name of the query. *The
 * compare element is used in a query expression to compare a profile attribute
 * value to the value of a constant or query parameter.
 *
 * The attribute-name attribute of this element identifies the name of the
 * profile attribute to compare. The op attribute specifies the relational
 * operator to apply. The value attribute specifies a constant value to compare
 * the profile attribute to, while the parameter attribute specifies the name of
 * a query parameter whose value should be compared. One and only one of the
 * value attribute or parameter attribute must be specified.
 *
 * The following relational operators are supported: - equals - not-equals -
 * less-than - less-than-or-equals - greater-than - greater-than-or-equals
 *
 * The collator-ref attribute of this element is optional. It can only be
 * specified if the type of the profile attribute is java.lang.String. If
 * specified it references a collator defined in a collator element of the
 * enclosing profile-spec deployment descriptor element. The collator is used to
 * perform locale-sensitive comparisons on the profile attribute values.
 *
 *
 * @author wozza
 * @author martins
 */
@Documented
@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StaticQuery {

    String id() default "";
    /**
     * The description element may contain any descriptive text about the parent
     * element.
     * 
     * @return
     */
    String description() default "";


    /**
     * The read-only attribute of this element indicates to the SLEE whether a
     * SLEE component executing this query will interact with the profiles
     * obtained via the query in a read-only or read-write manner within the
     * same transaction the query is executed. This is akin to SQL's SELECT ...
     * vs. SELECT ... FOR UPDATE. If pessimistic locking is being used by the
     * SLEE for the profile table, a read-only query allows concurrent access to
     * the same profiles via a shared reader lock, whereas a read-write query
     * would require the SLEE to obtain an exclusive writer lock on the
     * profiles, prohibiting concurrent access by SLEE components executing in
     * different threads. If this option is not specified for a query, the
     * default value is equal to the value of the profile-read-only attribute of
     * the enclosing profile-spec element.
     *
     * @return
     */
    public String readOnly();

    /**
     * The max-matches attribute of this element specifies the maximum number of
     * matching profiles that can be returned by the SLEE when the query is
     * executed. This option allows smaller manageable sets of data to be
     * returned from a query if it is expected that the query may return a large
     * number of results. If this option is not specified for a query, the SLEE
     * will return all matching profiles when the query is executed.
     *
     * @return
     */
    public String maxMatches();

    /**
     *
     * @return
     */
    public String query();
}
