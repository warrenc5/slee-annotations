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
@Target(value = {ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LibraryRef {

    /*
     * The library-name element contains the name of a library component.
     *
     */
    /**
     * 
     * @return
     */
    String name();
    /*
     * The library-vendor element contains the vendor of a library component.
     *
     */

    /**
     * 
     * @return
     */
    String vendor();

    /*
     * The library-version element contains the version of a library component.
     * The version number typically should have the general form
     * "major-version.minor-version".
     *
     */
    /**
     * 
     * @return
     */
    String version();
}
