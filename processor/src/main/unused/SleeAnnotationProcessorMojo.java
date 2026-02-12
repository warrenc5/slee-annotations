//TODO: set sensible defaults
//TODO: include aspectj rt system scope
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "process", defaultPhase = LifecyclePhase.PROCESS_CLASSES)
public class SleeAnnotationProcessorMojo
        extends org.codehaus.mojo.aspectj.AjcCompileMojo {

    /*****
    <forceAjcCompile>false</forceAjcCompile>
                            <source>1.8</source>
                            <target>1.8</target>
                            <complianceLevel>1.8</complianceLevel>
                            <showWeaveInfo>false</showWeaveInfo>
                            <verbose>false</verbose>
                            <XnoInline>true</XnoInline>
                            <proc>none</proc>
                            <additionalCompilerArgs>-Anobinary=false, -Adebug=true, -Anofail</additionalCompilerArgs>
    @throws MojoExecutionException
     */
    public void execute()
            throws MojoExecutionException {
        super.execute();
        Thread.dumpStack();
    }
}
