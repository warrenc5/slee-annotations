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

package org.mobicents.slee.annotations.examples.resource;

import javax.annotation.Resource;
import javax.slee.Address;
import javax.slee.facilities.Tracer;
import javax.slee.resource.ResourceAdaptorContext;

import mobi.mofokom.javax.slee.annotation.ConfigProperty;
import mobi.mofokom.javax.slee.annotation.event.EventTypeRef;
import mobi.mofokom.javax.slee.annotation.LibraryRef;
import mobi.mofokom.javax.slee.annotation.ProfileSpecRef;
import mobi.mofokom.javax.slee.annotation.ResourceAdaptor;
import mobi.mofokom.javax.slee.annotation.ResourceAdaptorType;
import mobi.mofokom.javax.slee.annotation.ResourceAdaptorTypeRef;
import javax.slee.resource.*;
import org.mobicents.slee.annotations.examples.ExampleUsageParametersInterface;
import org.mobicents.slee.annotations.examples.event.ExampleEvent;

@ResourceAdaptorType(name="ExampleAnnotatedResourceAdaptorType",vendor="ISV1",version="1.0",
	activityType={ExampleActivity.class},
	eventTypeRef={@EventTypeRef(name=ExampleEvent.EVENT_TYPE_NAME,vendor=ExampleEvent.EVENT_TYPE_VENDOR,version=ExampleEvent.EVENT_TYPE_VERSION)},
	aciFactory=ExampleActivityContextInterfaceFactory.class,
	raInterface=ExampleResourceAdaptorSbbInterface.class,
	libraryRefs={@LibraryRef(name="ExampleLibrary",vendor="ISV1",version="1.0")})

@ResourceAdaptor(name="ExampleAnnotatedResourceAdaptor",vendor="ISV1",version="1.0",
	typeRefs={@ResourceAdaptorTypeRef(name="ExampleAnnotatedResourceAdaptorType",vendor="ISV1",version="1.0")},
	usageParametersInterface=ExampleUsageParametersInterface.class,
	profileSpecRefs={@ProfileSpecRef(name="SimpleExampleProfile",vendor="ISV1",version="1.0",alias="profileSpec")},
	libraryRefs={@LibraryRef(name="ExampleLibrary",vendor="ISV1",version="1.0")},
	securityPermissions=ResourceAdaptor.ALL_PERMISSIONS
    )
public class ExampleAnnotatedResourceAdaptor implements javax.slee.resource.ResourceAdaptor{

    @ConfigProperty(defaultValue="Default")
    private String someProperty;
    
	@Resource(description="RA Tracer")
	private Tracer tracer;
	
	@Resource
	private ResourceAdaptorContext raContext;
	
	@Resource
	private ConfigProperties configProperties;
	
	@Resource
	private ResourceAdaptorContext resourceAdaptorContext;

	@Resource
	private ExampleUsageParametersInterface defaultUsageParameters;
	
	// RA logic ...

    @Override
    public void setResourceAdaptorContext(ResourceAdaptorContext context) {
    }

    @Override
    public void unsetResourceAdaptorContext() {
    }

    @Override
    public void raConfigure(ConfigProperties properties) {
    }

    @Override
    public void raUnconfigure() {
    }

    @Override
    public void raActive() {
        checkNull(this.configProperties);
        checkNull(this.defaultUsageParameters);
        checkNull(this.raContext);
        checkNull(this.resourceAdaptorContext);
        checkNull(this.tracer);
        checkNull(this.someProperty);
    }

    @Override
    public void raStopping() {
    }

    @Override
    public void raInactive() {
    }

    @Override
    public void raVerifyConfiguration(ConfigProperties properties) throws InvalidConfigurationException {
    }

    @Override
    public void raConfigurationUpdate(ConfigProperties properties) {
    }

    @Override
    public Object getResourceAdaptorInterface(String className) {
        return this;
    }

    @Override
    public Marshaler getMarshaler() {
        return null;
    }

    @Override
    public void serviceActive(ReceivableService serviceInfo) {
    }

    @Override
    public void serviceStopping(ReceivableService serviceInfo) {
    }

    @Override
    public void serviceInactive(ReceivableService serviceInfo) {
    }

    @Override
    public void queryLiveness(ActivityHandle handle) {
    }

    @Override
    public Object getActivity(ActivityHandle handle) {
        return null;
    }

    @Override
    public ActivityHandle getActivityHandle(Object activity) {
        return null;
    }

    @Override
    public void administrativeRemove(ActivityHandle handle) {
    }

    @Override
    public void eventProcessingSuccessful(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags) {
    }

    @Override
    public void eventProcessingFailed(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags, FailureReason reason) {
    }

    @Override
    public void eventUnreferenced(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags) {
    }

    @Override
    public void activityEnded(ActivityHandle handle) {
    }

    @Override
    public void activityUnreferenced(ActivityHandle handle) {
    }

    private void checkNull(Object o) {
        if( o == null)
            throw new NullPointerException();
    }
}