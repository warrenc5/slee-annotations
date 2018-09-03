/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.mobicents.slee.annotations.examples.sbb;

import javax.annotation.Resource;
import javax.slee.ActivityContextInterface;
import javax.slee.SbbContext;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.Tracer;
import javax.slee.serviceactivity.ServiceStartedEvent;

import javax.slee.annotation.CMPField;
import javax.slee.annotation.Sbb;
import javax.slee.annotation.Service;
import javax.slee.annotation.event.ServiceStartedEventHandler;
import javax.slee.annotation.event.TimerEventHandler;
import javax.slee.facilities.*;
import javax.slee.nullactivity.NullActivity;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;
import javax.slee.profile.ProfileFacility;
import javax.slee.profile.ProfileTableActivityContextInterfaceFactory;
import javax.slee.serviceactivity.ServiceActivityContextInterfaceFactory;
import javax.slee.serviceactivity.ServiceActivityFactory;

/**
 * Simple example for an annotated Sbb, on service start fires a 1 sec timer and
 * on timer event calculates real delay. Examples CMP Field and facilities
 * injection too.
 *
 * A service is defined by applying the {@link Service} annotation too.
 *
 * Regarding field injection: - "default" tracer and SbbContextExt injected
 * before setSbbContext() - by applying CMPField annotation to field, value is
 * injected before sbbLoad(), and persisted after sbbStore(), in case applied to
 * method it identifies one of the CMP Field accessors.
 *
 * A few more notes about event handler method annotations: - initialEventSelect
 * is { InitialEventSelect.ActivityContext } by default - initialEvent is false
 * by default
 *
 * @author Eduardo Martins
 *
 */
@Sbb(name = "SimpleExampleAnnotatedSbb", vendor = "ISV1", version = "1.0", alias = "SimpleSbb",localInterface=SimpleExampleSbbLocalObject.class)

public abstract class SimpleExampleAnnotatedSbb implements javax.slee.Sbb {

    private static final long TIMER_DURATION = 1000;
    @Resource
    private AlarmFacility alarmFacility;
    @Resource
    private NullActivityContextInterfaceFactory nullAciFactory;
    @Resource
    private ActivityContextNamingFacility acNamingFacility;
    @Resource
    private NullActivityFactory nullActivityFactory;
    @Resource
    private ProfileFacility profileFacility;
    @Resource
    private ProfileTableActivityContextInterfaceFactory profileTableACIFactory;
    @Resource
    private ServiceActivityContextInterfaceFactory serviceActivityACIFactory;
    @Resource
    private ServiceActivityFactory serviceActivityFactory;
    @Resource
    private TimerFacility timerFacility;
    @Resource(name = "MyTracer")
    private Tracer tracer;
    @Resource(name = "MyTracer")
    private Tracer tracer2;
    @Resource
    private SbbContext sbbContext;
    @CMPField
    private Long startTime = 100L;

    @ServiceStartedEventHandler
    public void onServiceStartedEvent(ServiceStartedEvent event,
            ActivityContextInterface aci) {
        tracer.info("service started");
        timerFacility.setTimer(aci, null, TIMER_DURATION,
                new TimerOptions());
        if(startTime == null || startTime == 0)
            startTime = Long.valueOf(System.currentTimeMillis());
    }

    @TimerEventHandler
    public void onTimerEvent(TimerEvent event, ActivityContextInterface aci) {
        long delay = (System.currentTimeMillis() - TIMER_DURATION) - startTime;
        tracer.info("timer expired, delay = " + delay + " ms.");
    }

    public boolean doSomethingElse() {
        NullActivity nullActivity = nullActivityFactory.createNullActivity();
        nullAciFactory.getActivityContextInterface(nullActivity);
        tracer.info("HELLO " + sbbContext.getSbb().toString());
        tracer2.info("HELLO 2 ");
        return true;
    }

    /*
    public void setSbbContext(javax.slee.SbbContext context) {
        InitialContext ic;
        try {
            ic = new InitialContext();
            Context env = (Context) ic.lookup("java:comp/env");

       } catch (NamingException ex) {
            Logger.getLogger(SimpleExampleAnnotatedSbb.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        System.err.println("!!! setSbbContext");
    }
    * 
    */
}