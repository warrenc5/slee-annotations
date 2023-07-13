package mobi.mofokom.javax.slee.annotation.event;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The event element declares an event received, fired, or both, by the SBB. It
 * contains an optional description, the name of the event as used by the SBB, a
 * reference to the event type of the event, zero or more initial event selector
 * options, an optional initial event selector method name, and an optional
 * resource option for the event.
 *
 * The event-direction attribute of this element declares the direction of the
 * event to and/or from the SBB. If the event is received by the SBB, the SBB
 * abstract class must have an event-handler method for it. If the event is
 * fired by the SBB, the SBB abstract class must declare an abstract fire-event
 * method for it. The initial-event-select, intitial-event-selector-method-name,
 * and event-resource-option sub-elements may only be included in the event
 * element if the event-direction attribute indicates that the event is received
 * by the SBB. A value for this attribute must be specified in the deployment
 * descriptor. There is no default value.
 *
 *
 * @author wozza
 * @author martins
 */
@Documented
@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EventHandler {
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
    EventTypeRef eventType();

    /**
     * The initial-event attribute of this element is only relevant if the
     * event-direction attribute indicates that the event is recieved by the
     * SBB. It declares whether the event is an initial event for the SBB or
     * not. The initial-event-select and initial-event-selector-method-name
     * sub-elements may only be included in the event element if the
     * initial-event attribute is set to True. The default value is False.
     *
     *
     * @return
     */
    boolean initialEvent() default false;

    /**
     * The initial-event-select element contains no text but defines a variable
     * attribute. The variable attribute declares an initial event selector
     * variable that should be included in the convergence name calculation
     * performed by the SLEE when considering an initial event for a service's
     * root SBB.
     *
     * @return
     */
    InitialEventSelect[] initialEventSelect() default {InitialEventSelect.ActivityContext};

    /**
     * The initial-event-selector-method-name contains the name of an initial event
     * selector method defined in the SBB abstract class.
     * @return
     */
    String initialEventSelectorMethod() default "";

    /**
     * The mask-on-attach attribute of this element is only relevant if the
     * event-direction attribute indicates that the event is received by the
     * SBB. It declares whether the receipt of the event by the SBB is initially
     * masked on an activity when the SBB attaches to it. The default value is
     * False.
     *
     *
     * @return
     */
    boolean maskOnAttach() default false;

    /**
     * The last-in-transaction attribute of this element is only relevant if the
     * event-direction attribute indicates that the event is received by the
     * SBB. It declares if it is possible for the SLEE to invoke multiple
     * event-handler methods in the same transaction. If set to True, the SLEE
     * must complete the enclosing transaction after the SBB's event handler
     * method has been invoked before invoking the event-handler method for the
     * next lower-priority SBB for the same event. If set to False, the SLEE
     * may, at its discretion, invoke the event-handler method for the next
     * lower-priority SBB interested in the event in the same transaction as it
     * invoked this event-handler method. The default value for this attribute
     * is True.
     *
     *
     * @return
     */
    boolean lastInTransaction() default true;

    /**
     * The event-resource-option element contains arbitrary event handling
     * options that the SLEE can forward to resource adaptor entities that emit
     * the enclosing event element's event type. The format of the element
     * contents is typically specified by a resource adaptor vendor.
     *
     *
     * @return
     */
    String eventResourceOption() default "";

    /**
     *
     */
    public enum InitialEventSelect {

        /**
         *
         */
        ActivityContext,
        /**
         *
         */
        AddressProfile,
        /**
         *
         */
        Address,
        /**
         *
         */
        EventType,
        /**
         *
         */
        Event
    }
}
