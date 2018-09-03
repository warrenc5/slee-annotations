
import javax.slee.SbbContext;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.mobicents.slee.annotations.examples.sbb.NoInterfaceSbb;
import static org.mockito.Matchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;

public class InterfaceTest {

    @Test
    public void testSbbImplementsInterface() {
        assertTrue(javax.slee.Sbb.class.isAssignableFrom(NoInterfaceSbb.class));

    }

    @Test
    public void testSbbSubclassCall() {
        MySubClass mock = mock(MySubClass.class);

        Mockito.doCallRealMethod().when(mock).setSbbContext(any(SbbContext.class));
        Mockito.doCallRealMethod().when(mock).check();

        mock.setSbbContext(mock(SbbContext.class));
        assertNotNull(mock.check());
    }

    public class MySubClass extends NoInterfaceSbb {

        public void setSbbContext(SbbContext context) {
        }

        public SbbContext check() {
            return this.sbbContext;
        }
    }

}
