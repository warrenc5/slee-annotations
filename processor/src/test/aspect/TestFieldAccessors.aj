package test.aspects;
import javax.slee.facilities.ActivityContextNamingFacility;
import javax.slee.facilities.AlarmFacility;
import javax.slee.facilities.AlarmLevel;
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


public privileged aspect TestFieldAccessors issingleton() {


pointcut gettimerFieldN66475(mobi.mofokom.javax.slee.annotations.examples.sbb.CMPSbb2 object) :target(object) && get(String mobi.mofokom.javax.slee.annotations.examples.sbb.CMPSbb2.timer) && !within(test.aspects.TestFieldAccessors) && !within(mobi.mofokom.javax.slee.test.TestCMPField) ;

String around(mobi.mofokom.javax.slee.annotations.examples.sbb.CMPSbb2 object) : gettimerFieldN66475(object) {
return (object.timer = object.getTimer());
}

public abstract String mobi.mofokom.javax.slee.annotations.examples.sbb.CMPSbb2.getTimer();


}
