
import javax.slee.SbbContext;
import javax.slee.facilities.Tracer;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.mobicents.slee.annotations.examples.profile.SimpleExampleProfileCMPInterface;
import org.mobicents.slee.annotations.examples.sbb.NoInterfaceSbb;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class InterfaceTest {

    @Test
    public void testSbbImplementsInterface() {
        assertTrue(javax.slee.Sbb.class.isAssignableFrom(NoInterfaceSbb.class));

    }

    @Test
    public void testProfileCMPNOImplementsInterface() {
        assertFalse(javax.slee.profile.Profile.class.isAssignableFrom(SimpleExampleProfileCMPInterface.class));
    }

    @Test
    public void testSbbSubclassCall() {
        System.setProperty("debug", "true");
        MySubClass mock = spy(MySubClass.class);
        SbbContext context;

        mock.setSbbContext(context = mock(SbbContext.class));
        doReturn(mock(Tracer.class)).when(context).getTracer(anyString());
        //Tracer k = mock.tracer;
        mock.sbbContextMethod();
        Tracer k2 = mock.someTracer();
        //assertNotNull(mock.tracer);
        assertNotNull(mock.sbbContextField);
        assertNotNull(mock.sbbContextMethod());
        assertNotNull(mock.someTracer());
    }

    static class MySubClass extends NoInterfaceSbb {

        public MySubClass() {
        }

        public void setSbbContext(SbbContext context) {
        }

        @Override
        public SbbContext sbbContextMethod() {
            return this.sbbContextField;
        }

        @Override
        public Tracer someTracer() {
            return null;
        }
    }

}
