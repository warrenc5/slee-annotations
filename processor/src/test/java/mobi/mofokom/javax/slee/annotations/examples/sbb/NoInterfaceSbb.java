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
package mobi.mofokom.javax.slee.annotations.examples.sbb;

import javax.annotation.Resource;
import javax.slee.SbbContext;
import javax.slee.facilities.Tracer;
import mobi.mofokom.javax.slee.annotation.Sbb;
import mobi.mofokom.javax.slee.annotation.Service;
import org.mobicents.slee.annotations.examples.sbb.SimpleExampleSbbLocalObject;

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
@Sbb(name = "SimpleExampleAnnotatedSbb", vendor = "ISV1", version = "1.0", alias = "SimpleSbb", localInterface = SimpleExampleSbbLocalObject.class)

public abstract class NoInterfaceSbb {

    @Resource(name = "MyTracer")
    public Tracer tracer;

    @Resource
    public SbbContext sbbContextField;

    @Resource
    public abstract SbbContext sbbContextMethod();

    @Resource(name="Bullshit")
    public abstract Tracer someTracer();

}
