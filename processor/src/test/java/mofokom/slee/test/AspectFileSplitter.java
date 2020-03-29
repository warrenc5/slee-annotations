package mofokom.slee.test;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.logging.Logger;
import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.JavaFileManager.Location;
import javax.tools.StandardLocation;
import mofokom.slee.SleeAnnotationProcessor;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author wozza
 */
public class AspectFileSplitter {

    @Mock
    SleeAnnotationProcessor proc;

    @Mock
    Filer filer;

    @Mock
    FileObject resource;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testQuery1() throws Exception {
        InputStream in = this.getClass().getResourceAsStream("/test-split.aj");
        assertNotNull(in);
        Reader reader = new InputStreamReader(in);

        doReturn(filer).when(proc).getFiler();
        doReturn(resource).when(filer).createResource(any(Location.class), anyString(), anyString(), any());
        StringWriter sw = new StringWriter();
        doReturn(sw).when(resource).openWriter();

        doCallRealMethod().when(proc).doSplitAspects(any(Reader.class));
        proc.logger = Logger.getLogger(this.getClass().getName());

        proc.doSplitAspects(reader);

        System.out.println("**" + sw.getBuffer().toString());

        verify(filer, Mockito.times(3)).createResource(eq(StandardLocation.SOURCE_OUTPUT), any(), any(), any());
        verify(filer, Mockito.atLeastOnce()).createResource(eq(StandardLocation.SOURCE_OUTPUT), eq("testDir"), eq("testName.aj"), any());
    }
}
