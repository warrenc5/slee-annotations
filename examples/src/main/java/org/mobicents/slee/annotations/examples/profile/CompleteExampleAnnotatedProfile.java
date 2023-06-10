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
package org.mobicents.slee.annotations.examples.profile;

import mobi.mofokom.javax.slee.annotation.ProfileSpec;
import mobi.mofokom.javax.slee.annotation.EnvEntry;
import mobi.mofokom.javax.slee.annotation.LibraryRef;
import mobi.mofokom.javax.slee.annotation.Collator;
import mobi.mofokom.javax.slee.annotation.Reentrant;
import java.util.Collection;
import javax.annotation.Resource;
import javax.slee.CreateException;
import javax.slee.facilities.Tracer;
import javax.slee.profile.Profile;
import javax.slee.profile.ProfileContext;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.ProfileVerificationException;

import org.mobicents.slee.annotations.examples.ExampleUsageParametersInterface;

@Reentrant
@ProfileSpec(name = "CompleteExampleAnnotatedProfile", vendor = "ISV1", version = "1.0",
abstractClass = CompleteExampleAnnotatedProfile.class,
cmpInterface = ExampleProfileCMPInterface.class,
localInterface = ExampleProfileLocalInterface.class,
tableInterface = ExampleProfileTableInterface.class,
managementInterface = ExampleProfileManagementInterface.class,
libraryRefs = {
    @LibraryRef(name = "ExampleLibrary", vendor = "ISV1", version = "1.0")
},
usageParametersInterface = ExampleUsageParametersInterface.class,
collators = {
    @Collator(collatorAlias = "oneCollator", localeLanguage = "en", decomposition = Collator.Decomposition.FULL, strength = Collator.Strength.IDENTICAL)},
/*
 * profileSpecRefs={
 * @ProfileSpecRef(name="SimpleExampleProfile",vendor="ISV1",version="1.0",alias="profileSpec")
 * },
 *
 */
readOnly = true,
singleProfileHint = true,
securityPermissions = ProfileSpec.ALL_PERMISSIONS)
public abstract class CompleteExampleAnnotatedProfile implements Profile{

    @EnvEntry(value = "Hello")
    private final String envEntry="YeSSS IT WORKS";
    @Resource
    private Tracer tracer;
    @Resource
    private ProfileContext profileContext;

    @Resource
    private ExampleUsageParametersInterface defaultUsageParameterSet;

    @Resource(name = "setName")
    private ExampleUsageParametersInterface anotherNamedUsageParameterSet;

    public void expunge() {
        Collection all = this.profileContext.getProfileTable().findAll();
        for (Object o : all) {
            ProfileLocalObject local = (ProfileLocalObject) o;
            this.profileContext.getProfileTable().remove(local.getProfileName());
        }
    }

    @Override
    public void setProfileContext(ProfileContext context) {
    }

    @Override
    public void unsetProfileContext() {
    }

    @Override
    public void profileInitialize() {
    }

    @Override
    public void profilePostCreate() throws CreateException {
    }

    @Override
    public void profileActivate() {
    }

    @Override
    public void profilePassivate() {
    }

    @Override
    public void profileLoad() {
    }

    @Override
    public void profileStore() {
    }

    @Override
    public void profileRemove() {
    }

    @Override
    public void profileVerify() throws ProfileVerificationException {
    }
    
}
