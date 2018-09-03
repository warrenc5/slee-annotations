package mofokom.slee.aspect;

import javax.slee.facilities.ActivityContextNamingFacility;
import javax.slee.facilities.AlarmFacility;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.Tracer;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;
import javax.slee.profile.ProfileFacility;
import javax.slee.profile.ProfileTableActivityContextInterfaceFactory;
import javax.slee.serviceactivity.ServiceActivityContextInterfaceFactory;
import javax.slee.serviceactivity.ServiceActivityFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public privileged aspect SbbAspect issingleton() {

//declare parents : org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb implements javax.slee.Sbb;

private InitialContext org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb.ic = null;

private Context org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb.env = null;
        public void org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb.setSbbContext(javax.slee.SbbContext context) {
        this.sbbContext = context;
        this.tracer = context.getTracer("Aspect Tracer");
try{
            ic = new InitialContext();
            env = (Context) ic.lookup("java:comp/env");

            this.alarmFacility = (AlarmFacility) ic.lookup(AlarmFacility.JNDI_NAME);
            this.nullAciFactory = (NullActivityContextInterfaceFactory) ic.lookup(NullActivityContextInterfaceFactory.JNDI_NAME);
            this.acNamingFacility = (ActivityContextNamingFacility) ic.lookup(ActivityContextNamingFacility.JNDI_NAME);
            this.nullActivityFactory = (NullActivityFactory) ic.lookup(NullActivityFactory.JNDI_NAME);
            this.profileFacility = (ProfileFacility) ic.lookup(ProfileFacility.JNDI_NAME);
            this.profileTableACIFactory = (ProfileTableActivityContextInterfaceFactory) ic.lookup(ProfileTableActivityContextInterfaceFactory.JNDI_NAME);
            this.serviceActivityACIFactory = (ServiceActivityContextInterfaceFactory) ic.lookup(ServiceActivityContextInterfaceFactory.JNDI_NAME);
            this.serviceActivityFactory = (ServiceActivityFactory) ic.lookup(ServiceActivityFactory.JNDI_NAME);
            this.timerFacility = (TimerFacility) ic.lookup(TimerFacility.JNDI_NAME);
 
}catch(javax.naming.NamingException x){
    context.getTracer("GENERROR").severe(x.getMessage(),x);
}

        System.err.println("on aspect " + this.sbbContext.getSbb());
        
    }
    /* 
    */
        public void org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb.unsetSbbContext() {
    }

        public void org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb.sbbCreate() throws javax.slee.CreateException {
    }

        public void org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb.sbbPostCreate() throws javax.slee.CreateException {
    }

        public void org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb.sbbActivate() {
    }

        public void org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb.sbbPassivate() {
    }

        public void org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb.sbbLoad() {
    }

        public void org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb.sbbStore() {
    }

        public void org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb.sbbRemove() {
    }

        public void org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb.sbbExceptionThrown(Exception exception, Object event, javax.slee.ActivityContextInterface aci) {
    }

        public void org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb.sbbRolledBack(javax.slee.RolledBackContext context) {
    }
        public abstract String org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb.getAnotherCMPField() ;
        public abstract void org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb.setAnotherCMPField(String s) ;
/*
        public Long org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb.getStartTime() {
System.err.println("^^^^ getting " + this.startTime);
return this.startTime;
}
        public void org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb.setStartTime(Long l) {
System.err.println("^^^^ setting "  + l);
        this.startTime = l ;
}
*/

        public abstract Long org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb.getStartTime() ;
        public abstract void org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb.setStartTime(Long l) ;

        public abstract String org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb.getAnotherCMPField() ;
        public abstract void org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb.setAnotherCMPField(String l) ;
        public abstract String org.mobicents.slee.annotations.examples.sbb.CompleteExampleAnnotatedSbb.getAnotherCMPField() ;
        public abstract void org.mobicents.slee.annotations.examples.sbb.CompleteExampleAnnotatedSbb.setAnotherCMPField(String l) ;
/*

pointcut setContextPC(javax.slee.SbbContext context1) :this(org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb)  && execution(void org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb.setContext(javax.slee.SbbContext)) && args(context1);

//pointcut setContextPC() : && execution(void org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb.setContext(javax.slee.SbbContext));

void around(javax.slee.SbbContext context) : setContextPC(context) {
       //((org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb)thisJoinPoint.getTarget()).sbbContext = context;

    System.err.println("advice ###### setContext");
    proceed(context);
}


static privileged aspect TracerAspect pertarget (  {

        public void org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb.initializeTracer(){

            System.err.println("=========== setting  tracer ======== " + this.toString());

            if(tracer == null)
                tracer = sbbContext.getTracer("name");
    }
}
*/

    
/*
    javax.slee.facilities.Tracer around() : get(javax.slee.facilities.Tracer *.*)  { 
            
            System.err.println("=========== setting  tracer ======== " + thisJoinPoint.toString());
proceed();
        //return tracer;
    }
        * 
        */
            
//&& && @target(javax.annotation.Resource) {
       //((org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb)thisJoinPoint.getTarget()).initializeTracer();
       //sbb.initializeTracer();
        
/*
        after() : execution( void org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb.setSbbContext(javax.slee.SbbContext)) {
       ((org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb)thisJoinPoint.getTarget()).initializeTracer();
    }
    * 
    */
}

privileged aspect ContextAspect {

pointcut setSbbContextPC(org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb sbb,javax.slee.SbbContext context1) :target(sbb) && execution(void org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb.setSbbContext(javax.slee.SbbContext)) && args(context1);

void around(org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb sbb, javax.slee.SbbContext context) : setSbbContextPC(sbb,context) {
    sbb.tracer2 = context.getTracer("Advice Tracer");
    System.err.println("advice ###### setContext");
    proceed(sbb,context);
}
}

privileged aspect FieldResourceAspect {
pointcut setField(org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb sbb,Long l) :target(sbb) && set(* org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb.startTime) && args(l) && !withincode(void org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb.setStartTime(*));

after(org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb sbb, Long ll) : setField(sbb,ll) {
    System.err.println("advice ###### setter " + ll);
    sbb.setStartTime(ll);
}

pointcut getField(org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb sbb) :target(sbb) && get(* org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb.startTime) && !withincode(Long org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb.getStartTime());

before(org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb sbb) : getField(sbb) {
    System.err.println("advice ###### getter ");
    sbb.getStartTime();
}

    public void org.mobicents.slee.annotations.examples.profile.CompleteExampleAnnotatedProfile.profileActivate() {
    }

    public void org.mobicents.slee.annotations.examples.profile.CompleteExampleAnnotatedProfile.profileInitialize() {
    }

    public void org.mobicents.slee.annotations.examples.profile.CompleteExampleAnnotatedProfile.profileLoad() {
    }

    public void org.mobicents.slee.annotations.examples.profile.CompleteExampleAnnotatedProfile.profilePassivate() {
    }

    public void org.mobicents.slee.annotations.examples.profile.CompleteExampleAnnotatedProfile.profilePostCreate() throws javax.slee.CreateException {
    }

    public void org.mobicents.slee.annotations.examples.profile.CompleteExampleAnnotatedProfile.profileRemove() {
    }

    public void org.mobicents.slee.annotations.examples.profile.CompleteExampleAnnotatedProfile.profileStore() {
    }

    public void org.mobicents.slee.annotations.examples.profile.CompleteExampleAnnotatedProfile.profileVerify() throws javax.slee.profile.ProfileVerificationException {
    }

    public void org.mobicents.slee.annotations.examples.profile.CompleteExampleAnnotatedProfile.setProfileContext(javax.slee.profile.ProfileContext context) {
    }

    public void org.mobicents.slee.annotations.examples.profile.CompleteExampleAnnotatedProfile.unsetProfileContext() {
    }
}