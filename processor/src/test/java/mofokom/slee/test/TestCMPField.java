/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mofokom.slee.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.annotation.processing.Processor;
import javax.tools.DiagnosticCollector;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import javax.xml.parsers.ParserConfigurationException;
import mofokom.slee.SleeAnnotationProcessor;
import org.apache.xml.resolver.CatalogException;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.mobicents.slee.annotations.examples.sbb.CMPSbb1;

/**
 *
 * @author wozza
 */
public class TestCMPField {

    @Test
    public void test() throws IOException, CatalogException, ParserConfigurationException {

        javax.tools.JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        assertNotNull(compiler);

        StandardJavaFileManager jfm = ToolProvider.getSystemJavaCompiler().getStandardFileManager(null, Locale.getDefault(), Charset.defaultCharset());

        DiagnosticListener dl = new DiagnosticCollector();
        List<String> options = new ArrayList<>();
        List<String> proc = new ArrayList<>();
        List<Processor> procs = new ArrayList<>();
        procs.add(new SleeAnnotationProcessor());
        List<JavaFileObject> cl = new ArrayList<>();

        cl.add(jfm.getJavaFileForInput(StandardLocation.CLASS_OUTPUT, CMPSbb1.class.getName(), JavaFileObject.Kind.CLASS));

        JavaCompiler.CompilationTask task = compiler.getTask(new PrintWriter(System.err), jfm, dl, options, proc, cl);
        task.setProcessors(procs);
        Boolean call = task.call();
        assertTrue(call);
    }
}
