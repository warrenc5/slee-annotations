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

import javax.slee.annotation.Service;
import javax.slee.annotation.LibraryRef;
import javax.slee.annotation.SbbRef;
import javax.slee.annotation.ClearAlarm;
import javax.slee.annotation.ProfileSpecRef;
import javax.slee.annotation.RaiseAlarm;
import javax.slee.annotation.Reentrant;
import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.slee.*;
import javax.slee.annotation.ResourceAdaptorTypeRef;
import javax.slee.annotation.event.EventFiring;
import javax.slee.annotation.ProfileCMP;
import javax.slee.annotation.event.EventHandler;
import javax.slee.annotation.event.InitialEventSelectorMethod;
import javax.slee.annotation.CMPField;
import javax.slee.annotation.event.EventTypeRef;
import javax.slee.annotation.event.ServiceStartedEventHandler;
import javax.slee.annotation.event.TimerEventHandler;
import javax.slee.annotation.ChildRelation;
import javax.slee.annotation.*;
import javax.slee.annotation.Sbb;
import javax.slee.facilities.*;
import javax.slee.profile.*;
import javax.slee.serviceactivity.*;

import org.mobicents.slee.annotations.examples.ExampleUsageParametersInterface;
import org.mobicents.slee.annotations.examples.ejb.SomeEJBHome;
import org.mobicents.slee.annotations.examples.ejb.SomeEJBRemote;
import org.mobicents.slee.annotations.examples.event.*;
import org.mobicents.slee.annotations.examples.profile.ExampleProfileCMPInterface;
import org.mobicents.slee.annotations.examples.resource.*;

@Service(name = "CompleteExampleService", vendor = "ISV1", version = "1.0", rootSbb = CompleteExampleAnnotatedSbb.class)
@Reentrant
@Sbb(id = "N67689", name = "CompleteExampleAnnotatedSbb", vendor = "ISV1", version = "1.0",
localInterface = ExampleSbbLocalObject.class,
activityContextInterface = ExampleSbbActivityContextInterface.class,
sbbRefs = {
    @SbbRef(name = "SimpleExampleAnnotatedSbb", vendor = "ISV1", version = "1.0", alias = "SimpleSbb")},
libraryRefs = {
    @LibraryRef(name = "ExampleLibrary", vendor = "ISV1", version = "1.0")},
profileSpecRefs = {
    @ProfileSpecRef(name = "CompleteExampleAnnotatedProfile", vendor = "ISV1", version = "1.0", alias = "profileSpec")},
    usageParametersInterface = ExampleUsageParametersInterface.class,
    resourceAdaptorTypeBinding = {
    @ResourceAdaptorTypeBinding(description = "",
    raTypeRef =
    @ResourceAdaptorTypeRef(name = "ExampleAnnotatedResourceAdaptorType", vendor = "ISV1", version = "1.0"),
    aciFactoryName = "slee/resources/http/activitycontextinterfacefactory",
    raEntityLink = "ExampleAnnotatedResourceAdaptor Entity Link")},
    /*
ejbRef = {
    @EJBRef(name = "myejb", type = EJBRef.Type.ENTITY, home = SomeEJBHome.class, remote = SomeEJBRemote.class)},
    * 
    */
securityPermissions = Sbb.ALL_PERMISSIONS)
public abstract class CompleteExampleAnnotatedSbb implements javax.slee.Sbb {

    @Resource(name = "Sbb Tracer")
    private Tracer tracer;
    @Resource(name = "Env Entry Name")
    @EnvEntry(value = "")
    private static String envEntry = "value";

    @Resource
    @EnvEntry(value = "99")
    private Integer anotherEnvEntry;

    @EnvEntry(value = "99")
    private Integer someOtherEnvEntry;

    @Resource
    private SbbContext sbbContext;
    @Resource(name = "ExampleAnnotatedResourceAdaptor Entity Link")
    private ExampleResourceAdaptorSbbInterface raSbbInterface;
    @Resource(mappedName = "slee/resources/http/activitycontextinterfacefactory")
    private ExampleActivityContextInterfaceFactory activityContextInterfaceFactory;
    @Resource
    private InitialContext context;

    /*
    @Resource
    public abstract Integer getAnotherEnvEntry();
    * 
    */
    
    @Resource(name = "java:comp/env")
    private Context envContext;

    @CMPField
    public abstract String getCMPField();

    public abstract void setCMPField(String value);

    @CMPField
    private String anotherCMPField;

    @ChildRelation(sbbAliasRef = "SimpleSbb")
    public abstract javax.slee.ChildRelation getChildRelation();
    @ChildRelation(sbbAliasRef = "SimpleSbb")
    private javax.slee.ChildRelation anotherChildRelation;
    @ChildRelation(sbbAliasRef = "SimpleSbb")
    private SimpleExampleSbbLocalObject exampleChildRelation;

    @Resource
    private ExampleSbbLocalObject anotherSbbLocal;

    @ProfileCMP(profileAliasRef = "profileSpec")
    public abstract ExampleProfileCMPInterface getExampleProfile(ProfileID profileID) throws UnrecognizedProfileTableNameException, UnrecognizedProfileNameException;
    
    @Resource
    private ExampleUsageParametersInterface defaultSbbUsageParameterSet;
    @Resource(name = "someUsageParametersSet")
    private ExampleUsageParametersInterface someSbbUsageParameterSet;
    //throws UnrecognizedUsageParameterSetNameException;

    public abstract org.mobicents.slee.annotations.examples.sbb.ExampleSbbActivityContextInterface asSbbActivityContextInterface(javax.slee.ActivityContextInterface aci);
    
    @ServiceStartedEventHandler
    public void onServiceStartedEvent(ServiceStartedEvent event, ActivityContextInterface aci) {
    }

    @TimerEventHandler
    public void onTimerEvent(TimerEvent event,
            ActivityContextInterface aci) {
        // ...
    }

    @InitialEventSelectorMethod({
        @EventTypeRef(name = ExampleEvent.EVENT_TYPE_NAME,
        vendor = ExampleEvent.EVENT_TYPE_VENDOR,
        version = ExampleEvent.EVENT_TYPE_VERSION)})
    public InitialEventSelector ies(InitialEventSelector ies) {
        return ies;
    }

    @EventHandler(initialEvent = true, eventType =
    @EventTypeRef(name = ExampleEvent.EVENT_TYPE_NAME,
    vendor = ExampleEvent.EVENT_TYPE_VENDOR,
    version = ExampleEvent.EVENT_TYPE_VERSION))
    public void onExampleEvent(ExampleEvent event,
            ActivityContextInterface aci) {
        // ...
    }

    @EventFiring(
    @EventTypeRef(name = ExampleEvent.EVENT_TYPE_NAME,
    vendor = ExampleEvent.EVENT_TYPE_VENDOR,
    version = ExampleEvent.EVENT_TYPE_VERSION))
    public abstract void fireExampleEvent(ExampleEvent event,
            ActivityContextInterface aci, Address address);

    @EventFiring(
    @EventTypeRef(name = ExampleSecondEvent.EVENT_TYPE_NAME,
    vendor = ExampleSecondEvent.EVENT_TYPE_VENDOR,
    version = ExampleSecondEvent.EVENT_TYPE_VERSION))
    public abstract void fireSecondExampleEvent(ExampleSecondEvent event,
            ActivityContextInterface aci, Address address);

    @RaiseAlarm(alarmType = "AlarmType", instanceId = "1", alarmLevel = AlarmLevel.LEVEL_WARNING, alarmMessage = "Something Went Wrong", catchException = true)
    @Rollback
    private void someThingWentWrong() throws Exception {
    }

    @ClearAlarm(alarmType = "AlarmType", instanceId = "1", alarmLevel = AlarmLevel.LEVEL_WARNING)
    private void everythingIsOkNow() {
    }

    @ClearAlarm
    private void clearAllAlarms() {
    }
    @Resource
    private SomeEJBHome ejbHome;
    @Resource
    private SomeEJBRemote ejbRemote;

    public boolean doSomething() {
        return true;
    }

    ///////////////////
    @Override
    public void sbbActivate() {
    }

    @Override
    public void sbbCreate() throws CreateException {
    }

    @Override
    public void sbbExceptionThrown(Exception exception, Object event, ActivityContextInterface aci) {
    }

    @Override
    public void sbbLoad() {
    }

    @Override
    public void sbbPassivate() {
    }

    @Override
    public void sbbPostCreate() throws CreateException {
    }

    @Override
    public void sbbRemove() {
    }

    @Override
    public void sbbRolledBack(RolledBackContext context) {
    }

    @Override
    public void sbbStore() {
    }

    @Override
    public void setSbbContext(SbbContext context) {
    }

    @Override
    public void unsetSbbContext() {
    }
}