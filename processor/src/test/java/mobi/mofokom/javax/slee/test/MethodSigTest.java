package mobi.mofokom.javax.slee.test;


import java.text.MessageFormat;
import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.BeforeClass;

public class MethodSigTest {

    public MethodSigTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void doIt() {
        String s = "CompleteExampleAnnotatedSbb.sbbExceptionThrown(java.lang.Exception, java.lang.Object, javax.slee.ActivityContextInterface)";
        s = s.replaceAll(",", " {},").replaceFirst("\\)", " {})");

        for (int i = 0; i < 3; i++) {
            s = s.replaceFirst("\\{\\}", "{" + i + "}");
        }

        System.err.println(s);

        MessageFormat f = new MessageFormat(s);

        StringBuffer buffer = new StringBuffer();

        f.format(new Object[]{"arg0", "arg1", "arg2"}, buffer, null);
        System.err.println(buffer.toString());

        String o = "CompleteExampleAnnotatedSbb.sbbExceptionThrown(java.lang.Exception arg0, java.lang.Object arg1, javax.slee.ActivityContextInterface arg2)";

        System.out.print(o);
        System.out.print(buffer.toString());
        Assert.assertEquals(o, buffer.toString());
    }

}
