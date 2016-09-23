Provides a specification of java based annotations to generate deployment descriptors for the JAIN SLEE JSR240.

Using annotations rapidly speeds up development times of JAIN SLEE resource adaptors and services. They can be supported by the JAIN SLEE container explicitly or you can use a post processor to generate the deployment descriptors.

In this technical article I will show you how to use the annotations and compile your project using the Mofokom standalone annotation processor to generate deployment descriptor.

Later I will show you how to use the AspectJ compiler to modify your compiled classes to get member field injection of contexts, jndi, env entries, config properties, child relations and annotation based alarm raising and clearing and transaction rolback.

First step is to add the correct dependencies and plugins to your maven project and configure your container.

 
<dependency>
    <groupId>javax.slee</groupId>
    <artifactId>jain-slee-annotations</artifactId>
    <version>1.0</version>
</dependency>
<dependency>
    <groupId>mofokom</groupId>
    <artifactId>slee-annotation-processor</artifactId>
    <version>1.0</version>
</dependency>

...
<repositories>
  <repository>
      <id>mofokom-public</id>
      <name>mofokom-public</name>
      <url>http://www.mofokom.mobi/maven/releases-public</url>
  </repository>
</repositories>

Then include the generated deployment descriptors to the projects target classes directory.

<build>
    <resources>
        <resource>
            <filtering>true</filtering>
                <directory>${basedir}/target/generated-sources/annotations</directory>
         </resource>
    <resource>
        <filtering>true</filtering>
            <directory>${basedir}/src/main/resources</directory>
    </resource>
</resources>

The plugin is just the standard maven compiler plugin which executes the Javac annotation processor. Note that this one uses the xalan xslt libraries to avoid problems with different versions of java and xml apis.

 
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>2.3.2</version>
    <executions>
        <execution>
            <id>generate</id>
            <phase>generate-resources</phase>
            <goals>
                <goal>compile</goal> 
            </goals>
        </execution>
    </executions>
    <configuration>
        <source>1.6</source>
        <target>1.6</target>
        <verbose>true</verbose>
        <annotationProcessors>
            <annotationProcessor>mofokom.slee.SLEEAnnotationProcessor</annotationProcessor>
        </annotationProcessors>
    </configuration>
</plugin>

Then we can get to coding with annotations for example to create a JAIN SLEE event type.

 @EventType(name=ExampleEvent.EVENT_TYPE_NAME,
 vendor=ExampleEvent.EVENT_TYPE_VENDOR,
 version=ExampleEvent.EVENT_TYPE_VERSION,
 libraryRefs={@LibraryRef(name="ExampleLibrary",vendor="ISV1",version="1.0"),   @LibraryRef(name="ExampleLibrary2",vendor="ISV1",version="1.0")}) 
 public class ExampleEvent {

And to have a sbb which will handle the event

 @Service(name = "CompleteExampleService", vendor = "ISV1", version = "1.0", rootSbb = CompleteExampleAnnotatedSbb.class)
 @Reentrant
 @Sbb(id = "mysbb", name = "CompleteExampleAnnotatedSbb", vendor = "ISV1", version = "1.0",
 localInterface = ExampleSbbLocalObject.class,
 activityContextInterface = ExampleSbbActivityContextInterface.class,
 sbbRefs = {
 @SbbRef(name = "SimpleExampleAnnotatedSbb", vendor = "ISV1", version = "1.0", alias = "SimpleSbb")},
 SecurityPermissions = Sbb.ALL_PERMISSIONS)
 public abstract class CompleteExampleAnnotatedSbb implements javax.slee.Sbb {

It could be handled by an initial event selector method.

 @InitialEventSelectorMethod({
 @EventTypeRef(name = ExampleEvent.EVENT_TYPE_NAME,
 vendor = ExampleEvent.EVENT_TYPE_VENDOR,
 version = ExampleEvent.EVENT_TYPE_VERSION)}) 
 public InitialEventSelector ies(InitialEventSelector ies) {
    return ies;
 }

or a standard event handler method

 @EventHandler(initialEvent = true, eventType =
 @EventTypeRef(name = ExampleEvent.EVENT_TYPE_NAME,
 vendor = ExampleEvent.EVENT_TYPE_VENDOR,
 version = ExampleEvent.EVENT_TYPE_VERSION)) 
 public void onExampleEvent(ExampleEvent event,
 ActivityContextInterface aci) {

Once you compile inspect the plugins output in

 target/generated-sources/annotations/META-INF/event-jar.xml

and

 target/generated-sources/annotations/META-INF/sbb-jar.xml

Notice how each element has an id as per xml spec. This is for container specific references or further processing.

-

Now to go one step further “inject” fields (well it's really only compile time weaving) into your SLEE component use the standard @javax.Resource annotation.

Depending on the container you are using you need to add the aspectj-rt to the mobicents - copy the aspectj-rt-1.7.jar into the server/default/mobicents/lib directory

opencloud - copy the aspectj-rt-1.7.jar into the RhinoSDK/lib/ext

For example you can now inject some SLEE particles

 @Resource(name = "Sbb Tracer")
 private Tracer tracer;

Or inject other JNDI resources

 @Resource
 private SbbContext sbbContext;
 @Resource(name = "ExampleAnnotatedResourceAdaptor Entity Link")
 private ExampleResourceAdaptorSbbInterface raSbbInterface;
 @Resource(mappedName = "slee/resources/http/activitycontextinterfacefactory")
 private ExampleActivityContextInterfaceFactory activityContextInterfaceFactory;
 @Resource
 private InitialContext context;

Then compile with the AspectJ plugin in your maven pom.

 
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>aspectj-maven-plugin</artifactId>
    <version>1.4</version>
    <configuration>
        <forceAjcCompile>true</forceAjcCompile>
        <source>1.6</source>
        <target>1.6</target>
        <showWeaveInfo>true</showWeaveInfo>
        <verbose>true</verbose>
        <aspectDirectory>target/generated-sources/annotations</aspectDirectory>
        <!--skip>true</skip-->
        <XnoInline>true</XnoInline>
        <Xreweavable>true</Xreweavable>
        <!--addSafePrefix>true</addSafePrefix-->
    </configuration>
    <executions>
        <execution>
            <phase>compile</phase>
            <goals>
                <goal>compile</goal>
            </goals>
        </execution>
    </executions>
</plugin>

Inspect the generated aspects to see what code modifications were made to your classes.

 ./examples/target/generated-sources/annotations/ExampleAnnotatedResourceAdaptor$SleeAnnotationsAspect.aj

 ./examples/target/generated-sources/annotations/SimpleExampleAnnotatedSbb$SleeAnnotationsAspect.aj

 ./examples/target/generated-sources/annotations/CompleteExampleAnnotatedSbb$SleeAnnotationsAspect.aj

 ./examples/target/generated-sources/annotations/CompleteExampleAnnotatedProfile$SleeAnnotationsAspect.aj

-

For further documentation see the following.

http://java.net/projects/slee-annotations

sources are available here

https://svn.java.net/svn/slee-annotations~jain-slee-annotations-svn

mailto:commits@slee-annotations.java.net

mailto:jain-slee-annotations@slee-annotations.java.net

Maven artifacts i.e sources,binaries, javadoc hosted here

http://mofokom.mobi/site/jain-slee-annotations

Mofokom slee annotation processor is here

http://www.mofokom.mobi/artifactory/releases-public/mofokom/slee-annotation-processor/1.0/

Full example source code (initially from mobicents project) utilizing all known annotations and injections - please report bugs with this test case.

http://www.mofokom.mobi/artifactory/snapshots-public/org/mobicents/servers/jainslee/api/jain-slee-11-ext/1.0.0-SNAPSHOT/
