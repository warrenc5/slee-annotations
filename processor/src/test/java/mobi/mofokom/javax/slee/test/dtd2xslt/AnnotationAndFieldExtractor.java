/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mobi.mofokom.javax.slee.test.dtd2xslt;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import static java.util.stream.Collectors.toList;
import org.junit.Test;

/**
 *
 * @author wozza
 */
public class AnnotationAndFieldExtractor {

    String[] anCls = new String[]{
        "mobi.mofokom.javax.slee.annotation.ActivityContextAttributeAlias",
        "mobi.mofokom.javax.slee.annotation.CMPField",
        "mobi.mofokom.javax.slee.annotation.ChildRelation",
        "mobi.mofokom.javax.slee.annotation.ClearAlarm",
        "mobi.mofokom.javax.slee.annotation.ConfigProperty",
        "mobi.mofokom.javax.slee.annotation.Collator",
        "mobi.mofokom.javax.slee.annotation.EnvEntry",
        "mobi.mofokom.javax.slee.annotation.EJBRef",
        "mobi.mofokom.javax.slee.annotation.LibraryRef",
        "mobi.mofokom.javax.slee.annotation.ProfileCMP",
        "mobi.mofokom.javax.slee.annotation.ProfileCMPField",
        "mobi.mofokom.javax.slee.annotation.ProfileSpec",
        //"mobi.mofokom.javax.slee.annotation.ProfileSpecCollator",
        "mobi.mofokom.javax.slee.annotation.ProfileSpecRef",
        "mobi.mofokom.javax.slee.annotation.RaiseAlarm",
        "mobi.mofokom.javax.slee.annotation.Reentrant",
        "mobi.mofokom.javax.slee.annotation.ResourceAdaptor",
        "mobi.mofokom.javax.slee.annotation.ResourceAdaptorTypeRef",
        "mobi.mofokom.javax.slee.annotation.ResourceAdaptorType",
        "mobi.mofokom.javax.slee.annotation.ResourceAdaptorTypeBinding",
        "mobi.mofokom.javax.slee.annotation.Rollback",
        "mobi.mofokom.javax.slee.annotation.Sbb",
        "mobi.mofokom.javax.slee.annotation.SbbActivityContextFactory",
        "mobi.mofokom.javax.slee.annotation.SbbRef",
        //"mobi.mofokom.javax.slee.annotation.SbbResourceAdaptorInterface",
        "mobi.mofokom.javax.slee.annotation.Service",
        //"mobi.mofokom.javax.slee.annotation.ServiceConfigProperties",
        "mobi.mofokom.javax.slee.annotation.StaticQuery",
        "mobi.mofokom.javax.slee.annotation.UsageParameter",
        "mobi.mofokom.javax.slee.annotation.UsageParametersInterface",
        "mobi.mofokom.javax.slee.annotation.event.ActivityEndEventHandler",
        "mobi.mofokom.javax.slee.annotation.event.EventFiring",
        "mobi.mofokom.javax.slee.annotation.event.EventHandler",
        "mobi.mofokom.javax.slee.annotation.event.EventType",
        "mobi.mofokom.javax.slee.annotation.event.EventTypeRef",
        //"mobi.mofokom.javax.slee.annotation.event.InitialEventSelect",
        "mobi.mofokom.javax.slee.annotation.event.InitialEventSelectorMethod",
        "mobi.mofokom.javax.slee.annotation.event.ProfileAddedEventHandler",
        "mobi.mofokom.javax.slee.annotation.event.ProfileRemovedEventHandler",
        "mobi.mofokom.javax.slee.annotation.event.ProfileUpdatedEventHandler",
        "mobi.mofokom.javax.slee.annotation.event.ServiceStartedEventHandler",
        "mobi.mofokom.javax.slee.annotation.event.TimerEventHandler"};

    @Test
    public void testExtractTest() throws ClassNotFoundException {
        Set<String> res = new HashSet<>();

        for (String s : anCls) {
            Class c = Class.forName(s);

            res.add(c.getSimpleName());
            for (Method f : c.getDeclaredMethods()) {
                res.add(f.getName());
            }
        }
        System.out.println(res.stream().sorted().collect(toList()));
    }
}
