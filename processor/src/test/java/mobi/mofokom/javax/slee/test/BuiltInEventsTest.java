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
package mobi.mofokom.javax.slee.test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Logger;
import javax.slee.ActivityContextInterface;
import javax.slee.serviceactivity.ServiceStartedEvent;
import mobi.mofokom.javax.slee.annotation.Sbb;
import mobi.mofokom.javax.slee.annotation.event.ServiceStartedEventHandler;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.mobicents.slee.annotations.examples.sbb.SimpleExampleSbbLocalObject;

public class BuiltInEventsTest {

    Logger log = Logger.getLogger(this.getClass().getName());

    @Test
    public void testServiceStartedEvent() {
        Optional<Method> m = Arrays.asList(BuiltInEventsSbb.class.getMethods()).stream().filter(m1->m1.getName().equals("onServiceStartedEvent")).findFirst();
        log.info(m.toString());
        assertNotNull(m);
        assertTrue(m.isPresent());
        assertFalse(m.get().getAnnotations().length == 0);
        assertFalse(m.get().getAnnotationsByType(ServiceStartedEventHandler.class).length == 0);
    }
}

@Sbb(name = "SimpleExampleAnnotatedSbb", vendor = "ISV1", version = "1.0", alias = "SimpleSbb", localInterface = SimpleExampleSbbLocalObject.class)

abstract class BuiltInEventsSbb {

    @ServiceStartedEventHandler
    public void onServiceStartedEvent(ServiceStartedEvent event, ActivityContextInterface aci) {
    }
}
