/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.facilities.TimerFacility;
import mofokom.slee.testfw.resource.MockResourceAdaptor;
import mofokom.slee.testfw.resource.MockSbb;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.BeforeClass;
import org.mobicents.slee.annotations.examples.ExampleUsageParametersInterface;
import org.mobicents.slee.annotations.examples.sbb.ExampleSbbLocalObject;
import org.mobicents.slee.annotations.examples.sbb.SimpleExampleAnnotatedSbb;

/**
 *
 * @author wozza
 */
public class InjectedCodeTest {

    public InjectedCodeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testExamples() throws InstantiationException, IllegalAccessException, NamingException {
        MockSbb<SimpleExampleAnnotatedSbb, ExampleSbbLocalObject, ExampleUsageParametersInterface> mockSbb = new MockSbb<SimpleExampleAnnotatedSbb, ExampleSbbLocalObject, ExampleUsageParametersInterface>(SimpleExampleAnnotatedSbb.class, ExampleSbbLocalObject.class, ExampleUsageParametersInterface.class);
        mockSbb.init();
        List<String> methodNames = new ArrayList<String>();

        for (Method m : SimpleExampleAnnotatedSbb.class.getDeclaredMethods()) {
            System.out.println(m.getName());
            methodNames.add(m.getName());
        }
        InitialContext ic = new InitialContext();
        Assert.assertNotNull((TimerFacility) ic.lookup(TimerFacility.JNDI_NAME));

        Assert.assertTrue(methodNames.contains("getAnotherCMPField"));

        MockResourceAdaptor.doCallRealMethods(mockSbb.getSbb(), SimpleExampleAnnotatedSbb.class);
        //MockResourceAdaptor.doCallRealMethods(mockSbb.getSbb(), ExampleSbbLocalObject.class);
        Assert.assertTrue(mockSbb.getSbb().doSomethingElse());

//        mockSbb.getSbbLocalObject().blabla();

        mockSbb.getSbb().onServiceStartedEvent(mockSbb.getServiceStartedEvent(), mockSbb.getActivityContextInterface());
      //  mockSbb.getSbb().setStartTime(Long.valueOf(0));

        mockSbb.getSbb().onServiceStartedEvent(mockSbb.getServiceStartedEvent(), mockSbb.getActivityContextInterface());

        mockSbb.deinit();
    }
}
