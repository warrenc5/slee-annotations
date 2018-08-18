
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.mobicents.slee.annotations.examples.sbb.NoInterfaceSbb;

public class InterfaceTest {
    @Test
    public void testSbbImplementsInterface(){
        assertTrue(javax.slee.Sbb.class.isAssignableFrom(NoInterfaceSbb.class));
    }
    
}
