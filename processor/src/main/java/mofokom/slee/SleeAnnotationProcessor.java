//TODO allow @Resource on abstract methods - create field and return 
//TODO call super if the method is there
//TODO warn if alarm method does not return string
//TODO: replace Classname strings with class.getName()
package mofokom.slee;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.text.FieldPosition;
import java.text.MessageFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import static javax.lang.model.type.TypeKind.NONE;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.ElementKindVisitor6;
import javax.slee.annotation.Collator;
import javax.slee.annotation.ProfileCMPField;
import javax.slee.annotation.ProfileSpec;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.xml.resolver.CatalogException;
import org.apache.xml.resolver.CatalogManager;
import org.apache.xml.resolver.tools.CatalogResolver;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.*;

/**
 *
 * @author wozza
 */
@SupportedSourceVersion(SourceVersion.RELEASE_6)
//@SupportedOptions
@SupportedAnnotationTypes({
    "javax.annotation.Resource",
    "javax.slee.annotation.ActivityContextAttributeAlias",
    "javax.slee.annotation.CMPField",
    "javax.slee.annotation.ChildRelation",
    "javax.slee.annotation.ClearAlarm",
    "javax.slee.annotation.ConfigProperty",
    "javax.slee.annotation.Collation",
    "javax.slee.annotation.EnvEntry",
    "javax.slee.annotation.EJBRef",
    "javax.slee.annotation.LibraryRef",
    "javax.slee.annotation.ProfileCMP",
    "javax.slee.annotation.ProfileCMPField",
    "javax.slee.annotation.ProfileSpec",
    "javax.slee.annotation.ProfileSpecCollator",
    "javax.slee.annotation.ProfileSpecRef",
    "javax.slee.annotation.RaiseAlarm",
    "javax.slee.annotation.Reentarant",
    "javax.slee.annotation.ResourceAdaptor",
    "javax.slee.annotation.ResourceAdaptorTypeRef",
    "javax.slee.annotation.ResourceAdaptorType",
    "javax.slee.annotation.ResourceAdaptorTypeBinding",
    "javax.slee.annotation.Rollback",
    "javax.slee.annotation.Sbb",
    "javax.slee.annotation.SbbActivityContextFactory",
    "javax.slee.annotation.SbbRef",
    "javax.slee.annotation.SbbResourceAdaptorInterface",
    "javax.slee.annotation.Service",
    "javax.slee.annotation.SerivecConfigProperties",
    "javax.slee.annotation.StaticQuery",
    "javax.slee.annotation.UsageParameter",
    "javax.slee.annotation.UsageParametersInterface",
    "javax.slee.annotation.event.ActivityEndEventHandler",
    "javax.slee.annotation.event.EventFiring",
    "javax.slee.annotation.event.EventHandler",
    "javax.slee.annotation.event.EventType",
    "javax.slee.annotation.event.EventTypeRef",
    "javax.slee.annotation.event.InitialEventSelect",
    "javax.slee.annotation.event.InitialEventSelectorMethod",
    "javax.slee.annotation.event.ProfileAddedEventHandler",
    "javax.slee.annotation.event.ProfileRemovedEventHandler",
    "javax.slee.annotation.event.ProfileUpdatedEventHandler",
    "javax.slee.annotation.event.ServiceStartedEventHandler",
    "javax.slee.annotation.event.TimerEventHandler"})
public class SleeAnnotationProcessor extends AbstractProcessor {

    private org.w3c.dom.Element rootNode;
    private RoundEnvironment roundEnv;
    private DocumentBuilder db;
    public static Logger log = Logger.getLogger(SleeAnnotationProcessor.class.getName());
    private Document doc;
    private Map<String, Set<String>> processedAnnotation = new HashMap<String, Set<String>>();
    private Set<String> processedElement = new HashSet<String>();
    private boolean binary;
    private Map<String, String> pubmap = new HashMap<String, String>();
    private Map<String, String> sysmap = new HashMap<String, String>();
    private Map<String, String> options;
    private boolean ajCompile;
    private List<URI> aspects;
    private boolean debugOutput = true;
    private CatalogResolver cr;
    private TransformerFactory tf = null;

    public SleeAnnotationProcessor() throws IOException, CatalogException, ParserConfigurationException, ParserConfigurationException {
        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
        this.configureCatalogResolver();
        this.createDocument();
    }

    private void createDocument() throws ParserConfigurationException {
        doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(true);
        db = dbf.newDocumentBuilder();
        db.setEntityResolver(cr);
        db.setErrorHandler(new ErrorHandler() {

            @Override
            public void warning(SAXParseException exception) throws SAXException {
                log(exception);
            }

            @Override
            public void error(SAXParseException exception) throws SAXException {
                log(exception);
            }

            @Override
            public void fatalError(SAXParseException exception) throws SAXException {
                log(exception);
            }

            private void log(SAXParseException exception) {
                log.warning(exception.getPublicId() + " " + exception.getSystemId() + " @" + exception.getLineNumber() + ":" + exception.getColumnNumber() + " " + exception.getMessage());
            }
        });

        rootNode = doc.createElement("process");
        rootNode.setAttribute("generatedTime", new Date().toString());
        doc.appendChild(rootNode);
    }

    private void configureCatalogResolver() throws IOException, CatalogException {

        pubmap.put("event-jar.xslt", "-//Sun Microsystems, Inc.//DTD JAIN SLEE Event 1.1//EN");
        sysmap.put("event-jar.xslt", "http://java.sun.com/dtd/slee-event-jar_1_1.dtd");
        pubmap.put("profile-spec-jar.xslt", "-//Sun Microsystems, Inc.//DTD JAIN SLEE Profile Specification 1.1//EN");
        sysmap.put("profile-spec-jar.xslt", "http://java.sun.com/dtd/slee-profile-spec-jar_1_1.dtd");
        pubmap.put("resource-adaptor-type-jar.xslt", "-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor Type 1.1//EN");
        sysmap.put("resource-adaptor-type-jar.xslt", "http://java.sun.com/dtd/slee-resource-adaptor-type-jar_1_1.dtd");
        pubmap.put("resource-adaptor-jar.xslt", "-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor 1.1//EN");
        sysmap.put("resource-adaptor-jar.xslt", "http://java.sun.com/dtd/slee-resource-adaptor-jar_1_1.dtd");
        pubmap.put("sbb-jar.xslt", "-//Sun Microsystems, Inc.//DTD JAIN SLEE SBB 1.1//EN");
        sysmap.put("sbb-jar.xslt", "http://java.sun.com/dtd/slee-sbb-jar_1_1.dtd");
        pubmap.put("service.xslt", "-//Sun Microsystems, Inc.//DTD JAIN SLEE Service 1.1//EN");
        sysmap.put("service.xslt", "http://java.sun.com/dtd/slee-service_1_1.dtd");

        CatalogManager m = new CatalogManager();
        m.setIgnoreMissingProperties(true);
        m.setPreferPublic(true);
        m.setVerbosity(3);

        cr = new CatalogResolver(m) {

            @Override
            public InputSource resolveEntity(String publicId, String systemId) {
                String resolvedEntity = super.getResolvedEntity(publicId, systemId);
                System.out.println("locating " + resolvedEntity);
                if (resolvedEntity.startsWith("resource:")) {
                    return resolveResource(resolvedEntity.substring(9));
                }
                return super.resolveEntity(publicId, systemId);
            }

            public Source resolve(String href, String base) throws TransformerException {
                log.info("resolve " + href);
                if (href.startsWith("resource:")) {
                    return new StreamSource(resolveResource(href.substring(9)).getByteStream());
                }

                return super.resolve(href, base);
            }

            private InputSource resolveResource(String location) {

                URL resource = Thread.currentThread().getContextClassLoader().getResource(location);
                if (resource == null) {
                    log.warning(location + " not found");
                }
                System.out.println("located " + resource.toExternalForm());

                InputStream r = null;
                try {
                    r = resource.openStream();
                } catch (IOException ex) {
                    log.log(Level.WARNING, location + " " + ex.getMessage(), ex);
                }
                InputSource inputSource = new InputSource(r);
                inputSource.setPublicId(location);
                inputSource.setSystemId(resource.toExternalForm());
                return inputSource;

            }

        };

        cr.validating = true;

        cr.getCatalog().parseCatalog("application/xml", this.getClass().getClassLoader().getResourceAsStream("slee-catalog.xml"));
    }

    private String transformerFactoryClass;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.configureTransformer(processingEnv.getOptions().get("transformerFactoryClass"));
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        options = this.processingEnv.getOptions();
        log.info(roundEnv.getRootElements().stream().map(r -> r.getSimpleName()).collect(toList()).toString());
        try {
            return innerProcess(annotations, roundEnv);
        } catch (Throwable t) {
            t.printStackTrace();

            if (!options.containsKey("nofail")) {
                throw new RuntimeException(t);
            }
        }
        return true;
    }

    public boolean innerProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) throws Exception {
        this.roundEnv = roundEnv;

        if (roundEnv.processingOver()) {

            processOutput(doc, "annotations.xslt", "annotations.xml", "");
            generateDescriptors();

            processOutput(doc, "aspect.xslt", "SleeAnnotationsAspect.aj", "");

            if (options.containsKey("addInterfaces")) {
                //AddInterfaces.addInterfaces(roundEnv);
            }
            if (options.containsKey("injectResource")) {
                ajCompile = true;
                runAjcCompiler();

            }
        }

//      doc.getDocumentElement().appendChild(rootNode);
        Filer filer = this.processingEnv.getFiler();
        for (TypeElement e : annotations) {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(e);
            for (Element e2 : elements) {
                if (processed(e2)) {
                    continue;
                }
                List<? extends AnnotationMirror> annotationMirrors = super.processingEnv.getElementUtils().getAllAnnotationMirrors(e2);
                Node elementNode = rootNode.appendChild(createNode(e2));
                for (AnnotationMirror a : annotationMirrors) {
                    if (processed(e2, a)) {
                        continue;
                    }
                    elementNode.appendChild(createNode(e2, a));
                    //testForMissingMethods((org.w3c.dom.Element) elementNode, e2, a,base);
                    if (a.getAnnotationType().toString().equals(javax.slee.annotation.ResourceAdaptorType.class.getName())) {
                        doResourceAdaptorACI(e2, a);
                    }
                }
            }
        }

        return true;
    }

    private org.w3c.dom.Element createNode(Element e2, TypeElement type) {

        final org.w3c.dom.Element node = doc.createElement("classtype");

        node.setAttribute("name", type.getQualifiedName().toString());
        node.setAttribute("interface", ((Boolean) type.getKind().isInterface()).toString());
        node.setAttribute("enclosing", e2.toString());
        node.setAttribute("simple-name", e2.getSimpleName().toString());
        return node;
    }

    private Node createNode(Element e2, Method m) {
        final org.w3c.dom.Element node = doc.createElement("method");

        node.setAttribute("name", m.getName().trim());
        node.setAttribute("enclosing", e2.toString());
        return node;
    }

    private Node createNode(Element e) {
        final org.w3c.dom.Element node = doc.createElement("element");

        node.setAttribute("kind", e.getKind().toString());
        e.accept(new ElementKindVisitor6<Node, Element>() {

            @Override
            public Node visitType(TypeElement e, Element p) {
                node.setAttribute("name", e.toString().trim());
                node.setAttribute("enclosing", e.getQualifiedName().toString());
                return null;
            }

            @Override
            public Node visitVariableAsField(VariableElement e, Element p) {
                node.setAttribute("name", e.getSimpleName().toString().trim());
                node.setAttribute("enclosing", e.getEnclosingElement().toString());
                node.setAttribute("type", e.asType().toString());
                return null;
            }

            @Override
            public Node visitExecutable(ExecutableElement e, Element p) {
                node.setAttribute("name", e.getSimpleName().toString().trim());
                node.setAttribute("enclosing", e.getEnclosingElement().toString());
                node.setAttribute("type", e.getReturnType().toString());
                return null;
            }
        }, e);
        return node;
    }
    private List<String> eventTypeLibraryRefs = new ArrayList<String>();

    private Node createNode(Element e2, AnnotationMirror a) throws ClassNotFoundException, NoSuchMethodException {
        org.w3c.dom.Element node = doc.createElement("annotation");
        String name = a.getAnnotationType().asElement().asType().toString();
        node.setAttribute("name", name.trim());
        String query = null;

        for (Entry<? extends ExecutableElement, ? extends AnnotationValue> e : this.processingEnv.getElementUtils().getElementValuesWithDefaults(a).entrySet()) {
            Node n = createNode(e.getKey());
            node.appendChild(n);
            //&& o.getClass().getComponentType().isAssignableFrom(AnnotationMirror.class))

            Object o = e.getValue().getValue();
            if (e.getKey().getSimpleName().toString().equals("query")) {
                query = o.toString();
            }

            if (o instanceof AnnotationMirror) {
                n.appendChild(createNode(e.getKey(), (AnnotationMirror) o));
            } else if (o instanceof List) {
                for (Object m : (List) o) {
                    if (m instanceof AnnotationMirror) {
                        if (name.equals(javax.slee.annotation.event.EventType.class.getName())) {
                            SleeAnnotationProcessor.this.log.fine(m.toString());
                            if (eventTypeLibraryRefs.contains(m.toString()))
                                ; else {
                                eventTypeLibraryRefs.add(m.toString());
                                n.appendChild(createNode(e.getKey(), (AnnotationMirror) m));
                            }
                        } else {
                            n.appendChild(createNode(e.getKey(), (AnnotationMirror) m));
                        }
                    } else if (m instanceof AnnotationValue) {
                        org.w3c.dom.Element n2 = doc.createElement("value");
                        Object av = ((AnnotationValue) m).getValue();
                        n2.setAttribute("name", av.toString());
                        n.appendChild(n2);
                    }
                }
            } else if (o instanceof Boolean) {
                ((org.w3c.dom.Element) n).setAttribute("value", o.toString().substring(0, 1).toUpperCase() + o.toString().substring(1));

            } else if (e.getKey().asType().toString().endsWith(Collator.Strength.class
                    .getSimpleName().toString())
                    | e.getKey().asType().toString().endsWith(Collator.Decomposition.class
                            .getSimpleName().toString())) {
                ((org.w3c.dom.Element) n).setAttribute("value", o.toString().charAt(0) + o.toString().toLowerCase().substring(1));
                ((org.w3c.dom.Element) n).setAttribute("type", e.getKey().asType().toString());
            } else if (e.getKey().asType().toString().endsWith(ProfileCMPField.QueryOperator.class.getSimpleName())) {
                ((org.w3c.dom.Element) n).setAttribute("processed-value", kebabCase(o.toString()));
                ((org.w3c.dom.Element) n).setAttribute("value", o.toString());
            } else {
                ((org.w3c.dom.Element) n).setAttribute("value", o.toString());
            }
            if (e.getKey().getDefaultValue() != null) {
                ((org.w3c.dom.Element) n).setAttribute("default", e.getKey().getDefaultValue().toString());
            }

        }

        //PROCESSED ELEMENTS 
        processPackage(node, a, e2);
        switch (name) {
            case "javax.slee.annotation.EnvEntry":
                break;
            default:
        }
        if (name.equals(javax.slee.annotation.EnvEntry.class.getName())) {
            //TODO handle final or value
            if (!((VariableElement) e2).getModifiers().contains(Modifier.FINAL)) {
                log.warning("env entry " + e2.getSimpleName() + " not final");
            }

            Object constantValue = ((VariableElement) e2).getConstantValue();

            if (constantValue != null) {
                ((org.w3c.dom.Element) node).setAttribute("processed-value", constantValue.toString());
            }
        }
        if (name.equals(javax.annotation.Resource.class.getName())) {
            getResourceName(node, a, e2);
        }

        if (name.equals(javax.slee.annotation.ActivityContextAttributeAlias.class.getName())) {
            ((org.w3c.dom.Element) node).setAttribute("processed-value", deBeanifyCamelCase(e2.getSimpleName().toString(), "get"));
            //attribute name
            for (Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : a.getElementValues().entrySet()) {
                if (entry.getKey().getSimpleName().toString().equals("attributeName")) {
                    ((org.w3c.dom.Element) node).setAttribute("attribute-name", deBeanifyCamelCase(entry.getValue().getValue().toString(), "get"));
                }

            }
        }
        if (name.equals(javax.slee.annotation.ChildRelation.class.getName())) {
            if (e2.getKind().isField()) {
                ((org.w3c.dom.Element) node).setAttribute("processed-value", BeanifySentenceCase(e2.getSimpleName().toString().replaceAll("ChildRelation$", "")));
            }
        }
        if (name.equals(javax.slee.annotation.CMPField.class.getName())) {
            if (e2.getKind().isField()) {
                ((org.w3c.dom.Element) node).setAttribute("processed-value", BeanifySentenceCase(e2.getSimpleName().toString()));
            } else {
                String mname = e2.getSimpleName().toString();

                ((org.w3c.dom.Element) node).setAttribute("processed-value", deBeanifyCamelCase(e2.getSimpleName().toString(), mname.substring(0, 3)));
            }
        }
        if (name.equals(javax.slee.annotation.ProfileCMPField.class.getName())) {
            ((org.w3c.dom.Element) node).setAttribute("processed-value", deBeanifyCamelCase(e2.getSimpleName().toString(), "get", "set"));
        }
        if (name.equals(javax.slee.annotation.UsageParameter.class.getName())) {
            if (e2.getSimpleName().toString().startsWith("increment") || e2.getSimpleName().toString().startsWith("sample")) {
                ((org.w3c.dom.Element) node).setAttribute("processed-value", formatUsageParameter(e2.getSimpleName().toString()));
            }
        }
        if (name.equals(javax.slee.annotation.UsageParametersInterface.class.getName())) //TODO CALCULATE ALL UsageParameters on super interfaces not annotated with the above annotation.
        {
            //TODO add counter methods for usageparameter annotated methods.
            calculateUsageParameterSet(e2, a, (org.w3c.dom.Element) node);
        }

        if (name.equals(javax.slee.annotation.event.EventFiring.class.getName()) || name.matches("^javax\\.slee\\.annotation\\.event\\..*EventHandler$")) //TODO CALCULATE ALL UsageParameters on super interfaces not annotated with the above annotation.
        {

            //addModifiers(e2, node);
            ((org.w3c.dom.Element) node).setAttribute("processed-value", deBeanifySentenceCase(e2.getSimpleName().toString(), "on", "fire"));
        }
        TypeElement base = null;

        if (a.getAnnotationType().asElement().toString().equals(javax.slee.annotation.Sbb.class.getName())) {
            base = super.processingEnv.getElementUtils().getTypeElement(javax.slee.Sbb.class.getName());

        }

        if (a.getAnnotationType().asElement().toString().equals(javax.slee.annotation.ProfileSpec.class.getName())) {
            if (e2.getKind().isClass()) {
                base = super.processingEnv.getElementUtils().getTypeElement(javax.slee.profile.Profile.class.getName());
            } else {
                //TODO don't implement if only has interface i.e. no profile abstract class section 3.3.4
            }
        }

        if (a.getAnnotationType().asElement().toString().equals(javax.slee.annotation.ResourceAdaptor.class.getName())) {
            base = super.processingEnv.getElementUtils().getTypeElement(javax.slee.resource.ResourceAdaptor.class.getName());
        }

        if (base != null) {
            this.addMissingInterfaces(node, e2, base);
            this.testForMissingMethods((org.w3c.dom.Element) node, e2, a, base);
            List<? extends TypeMirror> interfaces = ((TypeElement) e2).getInterfaces();
            log.info("interfaces " + interfaces.toString());
            //this.addMissingInterfaces(node, e2, interfaces.to);
        }

        if (name.equals(javax.slee.annotation.ProfileSpec.class.getName())) {
            ((org.w3c.dom.Element) node).setAttribute("processed-value", formatUsageParameter(e2.getSimpleName().toString()));

            Optional<String> cmpInterface = this.getElementValue(a, "cmpInterface");
            if (cmpInterface.isPresent()) {
                if (!hasImplements((TypeElement) e2, this.getTypeElement(cmpInterface.get()).getQualifiedName().toString())) {
                    this.addMissingInterfaces(node, e2, base = this.getTypeElement(cmpInterface.get()));
                    //this.testForMissingMethods((org.w3c.dom.Element) node, e2, a, base);
                }
            }

            cmpInterface = this.getElementValue(a, "managementInterface");
            if (cmpInterface.isPresent()) {
                if (!hasImplements((TypeElement) e2, this.getTypeElement(cmpInterface.get()).getQualifiedName().toString())) {
                    this.addMissingInterfaces(node, e2, this.getTypeElement(cmpInterface.get()));
                }
            }
        }

        if (name.equals(javax.slee.annotation.StaticQuery.class.getName())) {
            List<? extends VariableElement> parameters = ((ExecutableElement) e2).getParameters();

            String queryName = e2.getSimpleName().toString();
            List<String> attributeNames = getAllAttributeNames((TypeElement) e2.getEnclosingElement());
            log.fine(attributeNames.toString());
            //new ArrayList<String>();
            List<String> parameterNames = new ArrayList<String>();
            org.w3c.dom.Element n = null, oNode = node;
            node.appendChild(node = doc.createElement("query"));
            node.setAttribute("name", deBeanifyCamelCase(queryName, "query"));

            for (VariableElement v : parameters) {
                n = doc.createElement("query-parameter");
                n.setAttribute("type", v.asType().toString());
                n.setAttribute("name", v.getSimpleName().toString());
                node.appendChild(n);
                parameterNames.add(v.getSimpleName().toString());

            }
            try {
                node.appendChild(parse(query,
                        attributeNames, 0,
                        parameterNames));
            } catch (Exception x) {
                log.warning(x.getMessage() + " " + query);
                node = oNode;
            }
        }

        return node;
    }

    private Node createNode(AnnotationValue v) {
        org.w3c.dom.Element node = doc.createElement("annotation-value");
        String name = v.getValue().toString();
        node.setAttribute("value", name);
        return node;

    }

    private Node createNode(TypeElement e, ExecutableElement ee) {
        org.w3c.dom.Element node = doc.createElement("method");
        node.setAttribute("name", formatMethodImpl(e, ee));
        return node;
    }

    @SuppressWarnings("All")
    private void addMissingInterfaces(final org.w3c.dom.Element n1, final Element e2, TypeElement... a) throws ClassNotFoundException, NoSuchMethodException {
        org.w3c.dom.Element node = doc.createElement("classtypes");
        NodeList nl = null;
        if ((nl = n1.getElementsByTagName("classtypes")).getLength() > 0) {
            node = (org.w3c.dom.Element) nl.item(0);
        } else {
            node = doc.createElement("classtypes");
            node.setAttribute("enclosing", e2.toString());
            node.setAttribute("simple-name", e2.getSimpleName().toString());
            n1.appendChild(node);
        }

        if (a != null) {
            for (TypeElement aa : a) {
                if (aa != null) {
                    node.appendChild(this.createNode(e2, aa));
                }
            }
        }
    }

    @SuppressWarnings("All")
    private void testForMissingMethods(final org.w3c.dom.Element n1, final Element e2, AnnotationMirror a, TypeElement base) throws ClassNotFoundException, NoSuchMethodException {
        final org.w3c.dom.Element node = doc.createElement("methods");
        node.setAttribute("enclosing", e2.toString());

        e2.accept(new ElementKindVisitor6<Void, TypeElement>() {

            @Override
            public Void visitTypeAsClass(TypeElement e, TypeElement p) {
                final List<? extends Element> existing = processingEnv.getElementUtils().getAllMembers((TypeElement) e);
                final List<String> ex = new ArrayList<String>();
                for (Element ie : existing) {
                    if (!ie.getModifiers().contains(Modifier.ABSTRACT)) {
                        ex.add(ie.toString());
                    }
                }
                //System.err.println(e.toString() + " "+ ex.toString());

                p.accept(new ElementKindVisitor6<Void, Void>() {

                    @Override
                    public Void visitTypeAsInterface(TypeElement e, Void p) {
                        List<? extends Element> elements = processingEnv.getElementUtils().getAllMembers((TypeElement) e);
                        for (Element ee : elements) {
                            if (!ex.contains(ee.toString())) {
                                Node n = createNode((TypeElement) e2, (ExecutableElement) ee);
                                node.appendChild(n);
                            }
                        }

                        return null;
                    }
                }, null);

                return null;
            }
        }, base);

        addModifiers(e2, n1);
        n1.appendChild(node);
    }

    private boolean hasImplements(TypeElement e2, String name) throws ClassNotFoundException, NoSuchMethodException {
        log.fine("hasImplements " + e2.toString() + " " + name + " " + e2.getInterfaces());
        for (TypeMirror i : e2.getInterfaces()) {
            if (i.toString().equals(name)) {
                return true;
            }
        }

        if (!e2.getSuperclass().getKind().equals(NONE)) {
            TypeElement e3 = (TypeElement) ((DeclaredType) e2.getSuperclass()).asElement();
            return hasImplements(e3, name);
        }

        return false;
    }

    private void processOutput(Document doc, String transformFile, String fileName, String filePath) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, ParserConfigurationException, SAXException {

        FileObject resource;
        Filer filer = super.processingEnv.getFiler();
        resource = filer.createResource(StandardLocation.SOURCE_OUTPUT, filePath, fileName, null);
        log.info("creating resource " + resource.toUri().toString());

        OutputStream out = null;
        PipedInputStream pis = null;

        if (transformFile.equals("aspect.xslt")) {
            out = new PipedOutputStream(pis = new PipedInputStream(1000000));
        } else {
            out = resource.openOutputStream();
        }

        StreamResult result;
        Transformer t = doTransform(new DOMSource(doc), transformFile, result = new StreamResult(out), pubmap.get(transformFile), sysmap.get(transformFile));

        out.flush();
        out.close();

        if (transformFile != null && !transformFile.startsWith("annotations") && !transformFile.startsWith("aspect")) {
            DOMSource source = null;

            log.log(Level.INFO, "validating {0}", resource.toUri().toString());
            source = new DOMSource(db.parse(resource.toUri().toString()));

            out = new FileOutputStream(new File(resource.toUri().getPath()));

            doTransform(source, "id.xslt", new StreamResult(out), t.getOutputProperty(OutputKeys.DOCTYPE_PUBLIC), t.getOutputProperty(OutputKeys.DOCTYPE_SYSTEM));

            out.flush();
            out.close();
        } else if (transformFile.startsWith("aspect")) {
            doSplitAspects(new InputStreamReader(pis));
        }
    }

    public void doSplitAspects(Reader reader) throws IOException {
        aspects = new ArrayList<>();

        BufferedReader r2 = new BufferedReader(reader);
        String s2;
        BufferedWriter writer = null;
        Scanner scanner = new Scanner(r2);

        Pattern p = Pattern.compile("file:(.*)/(.*.aj)");

        while (scanner.hasNext()
                && ((s2 = scanner.findInLine(p)) != null
                || (s2 = scanner.nextLine()) != null)) {

            Matcher matcher = p.matcher(s2);

            if (matcher.matches()) {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }

                FileObject outResource = getFiler().createResource(StandardLocation.SOURCE_OUTPUT, matcher.group(1), matcher.group(2), null);
                log.info("writing " + matcher.group(1) + " " + matcher.group(2));
                aspects.add(outResource.toUri());
                writer = new BufferedWriter(outResource.openWriter());
            } else if (writer != null) {
                writer.write(s2);
                writer.newLine();
            }
        }
        log.log(Level.INFO, "creating " + aspects.toString());
        if (writer != null) {
            writer.flush();
            writer.close();
        }
    }

    public void configureTransformer(String transformerFactoryClass) {
        if (transformerFactoryClass == null) {
            transformerFactoryClass = "org.apache.xalan.xsltc.trax.TransformerFactoryImpl";
        }

        if (transformerFactoryClass != null) {
            tf = TransformerFactory.newInstance(transformerFactoryClass, getClass().getClassLoader());
        } else {
            tf = TransformerFactory.newInstance();
        }
        tf.setErrorListener(new ErrorListener() {
            public void warning(TransformerException exception) throws TransformerException {
                log.log(Level.WARNING, message(exception));
            }

            public void error(TransformerException exception) throws TransformerException {
                log.log(Level.SEVERE, message(exception));
            }

            public void fatalError(TransformerException exception) throws TransformerException {
                log.log(Level.SEVERE, message(exception));
            }

            private String message(TransformerException exception) {
                return String.format("%1$s %2$s", exception.getLocator().getSystemId(), exception.getMessageAndLocation());
            }
        });

        try {
            binary = null != Class.forName("mofokom.transform.annotations");
        } catch (ClassNotFoundException x) {
            log.warning(x.getMessage() + " non-binary transforms only");
        }
        if (binary) {
            try {
                tf.setAttribute("use-classpath", Boolean.TRUE);
            } catch (IllegalArgumentException x) {
                binary = false;
                log.warning(x.getMessage());
            }
        }

        tf.setURIResolver(cr);
        log.info("Initialized " + transformerFactoryClass);
    }

    private Transformer doTransform(Source source, String transformFile, Result result, String publicId, String systemId) {
        Transformer transform = null;
        Templates template = null;
        InputStream transformStream = this.getClass().getClassLoader().getResourceAsStream(transformFile);
        try {
            if (transformFile == null) {
                transform = tf.newTransformer();
            } else if (binary) {
                tf.setAttribute("use-classpath", Boolean.TRUE);
                transformFile = transformFile.replace('-', '_').substring(0, transformFile.length() - 5);
                tf.setAttribute("translet-name", transformFile);
                tf.setAttribute("package-name", "mofokom.transform");

                template = tf.newTemplates(new StreamSource(transformStream));
                transform = template.newTransformer();
            } else {
                transform = tf.newTransformer(new StreamSource(transformStream));
            }

            if (systemId != null) {
                transform.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, systemId);
            }
            if (publicId != null) {
                transform.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, publicId);
            }

            transform.setOutputProperty(OutputKeys.INDENT, "yes");
            transform.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
            transform.setURIResolver(cr);
            DOMResult d = null;

            transform.transform(source, result);

        } catch (Exception ex) {
            log.log(Level.WARNING, ex.getMessage(), ex);
        }
        return transform;
    }

    private boolean processed(Element e2, AnnotationMirror a) {
        if (processedAnnotation.containsKey(e2.getEnclosingElement().toString() + e2.toString())) {
            if (processedAnnotation.get(e2.getEnclosingElement().toString() + e2.toString()).contains(a.getAnnotationType().toString())) {
                return true;
            } else {
                processedAnnotation.get(e2.getEnclosingElement().toString() + e2.toString()).add(a.getAnnotationType().toString());
            }
        } else {
            processedAnnotation.put(e2.getEnclosingElement().toString() + e2.toString(), new HashSet<String>());
        }
        return false;
    }

    private boolean processed(Element e2) {
        if (processedElement.contains(e2.getEnclosingElement().toString() + e2.toString())) {
            return true;
        }
        processedElement.add(e2.getEnclosingElement().toString() + e2.toString());
        return false;
    }

    private String formatUsageParameter(String v) {
        return deBeanifyCamelCase(v, "increment", "sample");
    }

    private String deBeanifyCamelCase(String v, String... prefix) {
        log.fine(v);
        for (String p : prefix) {
            if (v.startsWith(p)) {
                v = v.substring(p.length());
                break;
            }
        }
        return v.toLowerCase().charAt(0) + v.substring(1);
    }

    private String deBeanifySentenceCase(String v, String... prefix) {
        log.fine(v);
        for (String p : prefix) {
            if (v.startsWith(p)) {
                v = v.substring(p.length());
                break;
            }
        }
        return v;
    }

    private List<String> getAllAttributeNames(final TypeElement e2) {
        final List<String> attributes = new ArrayList<String>();
        //find profile having this table interface
        Optional<TypeElement> cmp = roundEnv.getElementsAnnotatedWith(ProfileSpec.class)
                .stream()
                .filter(
                        e -> e.getAnnotationMirrors().stream().anyMatch(a -> {
                            Optional<String> tableInterface = this.getElementValue(a, "tableInterface");
                            if (!tableInterface.isPresent()) {
                                return false;
                            }
                            return e2.getQualifiedName().toString().equals(tableInterface.get());
                        })
                ).flatMap(
                        e -> e.getAnnotationMirrors().stream()
                                .map(a -> this.getElementValue(a, "cmpInterface"))
                                .filter(o -> o.isPresent()).collect(toList()).stream()
                //FIXME use optional.stream
                )
                .map(n -> getTypeElement(n.get()))
                .findAny();

        TypeElement e3 = cmp.get();
        TypeMirror m;
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ProfileCMPField.class)
                .stream().
                filter(
                        t -> ((TypeElement) t.getEnclosingElement()).getQualifiedName().toString().equals(e3.getQualifiedName().toString())
                        || e3.getInterfaces().contains(((TypeElement) t.getEnclosingElement()).asType())
                )
                .collect(toSet());

        for (Element e : elements) {
            e.accept(new ElementKindVisitor6<Void, Element>() {

                @Override
                public Void visitExecutableAsMethod(ExecutableElement e, Element p) {
                    attributes.add(deBeanifySentenceCase(e.getSimpleName().toString(), "get", "set"));
                    return null;
                }

                @Override
                public Void visitVariableAsField(VariableElement e, Element p) {
                    attributes.add(e.getSimpleName().toString());
                    return null;
                }
            }, e3);
        }
        return attributes;
    }

    public org.w3c.dom.Node parse(String query, List<String> attributes, int iiii, List<String> parameters) throws Exception {

        DocumentFragment e = doc.createDocumentFragment();
        org.w3c.dom.Node cn = e;
        org.w3c.dom.Element tn = doc.createElement("tmp");

        StringTokenizer st = new StringTokenizer(query, " #()", true);
        List<String> ops = Arrays.asList(new String[]{"equals", "not-equals", "less-than", "less-than-or-equals", "greater-than", "greater-than-or-equals"});
        List<String> cons = Arrays.asList(new String[]{"and", "or", "not"});
        List<String> fun = Arrays.asList(new String[]{"range-match", "longest-prefix-match", "has-prefix"});
        boolean collator = false;

        for (int i = 0; i < attributes.size(); i++) {
            attributes.set(i, attributes.get(i).toLowerCase());
        }
        while (st.hasMoreElements()) {
            String t = st.nextToken();
            log.fine(t);

            if (t.equals("#")) {
                collator = true;
            } else if (collator == true) {
                tn.setAttribute("collator-ref", t);
                collator = false;
            } else if (attributes.contains(t.toLowerCase())) {
                tn.setAttribute("attribute-name", t);
            } else if (parameters.contains(t)) {
                tn.setAttribute(calculatePrefix((org.w3c.dom.Element) cn) + "parameter", t);
            } else if (ops.contains(t)) {
                cn.appendChild(cn = doc.createElement("compare"));
                copyTempAttrs((org.w3c.dom.Element) cn, tn);
                ((org.w3c.dom.Element) cn).setAttribute("op", t);
            } else if (cons.contains(t)) {
                copyTempAttrs((org.w3c.dom.Element) cn, tn);
                //log.fine(cn.getParentNode().getNodeName());
                org.w3c.dom.Element qn;
                Node on = cn.getParentNode().replaceChild(qn = doc.createElement(t), cn);
                qn.appendChild(on);
                cn = qn;
            } else if (fun.contains(t)) {
                cn.appendChild(cn = doc.createElement(t));
                copyTempAttrs((org.w3c.dom.Element) cn, tn);
            } else if (t.equals("(")) {
            } else if (t.equals(")")) {
                cn = (org.w3c.dom.Element) cn.getParentNode();
            } else if (t.equals(" ")) {
            } else //constant
            //FIX BUG HERE WHEN NO ATTRIBUTENAMES
            {
                if (cn instanceof org.w3c.dom.Element) {
                    ((org.w3c.dom.Element) cn).setAttribute(calculatePrefix((org.w3c.dom.Element) cn) + "value", t);
                } else {
                    throw new Exception("no attribute-name attribute on query (missing CMP parameter?)");
                }
            }

        }

        copyTempAttrs((org.w3c.dom.Element) cn, tn);

        return e;
    }

    private String calculatePrefix(org.w3c.dom.Element cn) {
        if (cn.getNodeName().equals("range-match")
                && (cn.getAttributeNode("from-value") == null & cn.getAttributeNode("from-parameter") == null)) {
            return "from-";
        } else if (cn.getNodeName().equals("range-match")
                && (cn.getAttributeNode("to-value") == null & cn.getAttributeNode("to-parameter") == null)) {
            return "to-";
        }
        return "";
    }

    private void copyTempAttrs(org.w3c.dom.Element cn, org.w3c.dom.Element tn) {
        NamedNodeMap attributes = tn.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Attr item = (Attr) attributes.item(i);
            attributes.removeNamedItem(item.getName());
            cn.setAttributeNode(item);
        }
    }

    private Node calculateUsageParameterSet(Element e2, AnnotationMirror a, org.w3c.dom.Element node) {
        DocumentFragment f = doc.createDocumentFragment();
        List<ExecutableElement> ee = ElementFilter.methodsIn(this.processingEnv.getElementUtils().getAllMembers((TypeElement) e2));
        org.w3c.dom.Element n = null;

        final org.w3c.dom.Element mnode = doc.createElement("methods");
        mnode.setAttribute("enclosing", e2.toString());
        node.appendChild(mnode);
        MessageFormat mf = new MessageFormat("public {0} {1} ();");

        for (ExecutableElement m : ee) {

            if (m.getSimpleName().toString().startsWith("increment") || m.getSimpleName().toString().startsWith("sample")) {
                String ename = ((TypeElement) m.getEnclosingElement()).getQualifiedName().toString();

                if (!ename.equals(Object.class
                        .getName())) {
                    f.appendChild(n = (org.w3c.dom.Element) this.createNode(m));
                    n.appendChild(n = doc.createElement("annotation"));

                    ((org.w3c.dom.Element) n).setAttribute("name", javax.slee.annotation.UsageParameter.class.getName());
                    final String name = formatUsageParameter(m.getSimpleName().toString());
                    ((org.w3c.dom.Element) n).setAttribute("processed-value", name);

                    if (!ee.stream().filter(m2 -> !m.equals(m2)).anyMatch(
                            m2 -> m2.getSimpleName().toString().equals("get" + this.capitalizeFirst(name))
                    )) {
                        ((org.w3c.dom.Element) n).setAttribute("missing-counter", Boolean.TRUE.toString());

                        final org.w3c.dom.Element me = doc.createElement("method");
                        String r = m.getSimpleName().toString().startsWith("sample") ? "javax.slee.usage.SampleStatistics" : "long";

                        me.setAttribute("name", mf.format(new Object[]{r, "get" + this.capitalizeFirst(name)}, new StringBuffer(), new FieldPosition(0)).toString());
                        me.setAttribute("enclosing", e2.toString());
                        mnode.appendChild(me);
                    }
                }
            } else {
            }
        }

        node.appendChild(f);
        return f;
    }

    private void getResourceName(org.w3c.dom.Element node, AnnotationMirror a, Element e2) {
        a.getElementValues();

        e2.accept(new ElementKindVisitor6<Node, org.w3c.dom.Element>() {

            @Override
            public Node visitType(TypeElement e, org.w3c.dom.Element p) {
                p.setAttribute("processed-value",
                        e.getModifiers().toString() + " " + e.toString().trim());
                return null;
            }

            @Override
            public Node visitVariableAsField(VariableElement e, org.w3c.dom.Element p) {
                p.setAttribute("type", e.asType().toString());
                p.setAttribute("processed-value", getSleeJndiName(e.asType().toString()));
                return null;
            }

            @Override
            public Node visitExecutable(ExecutableElement e, org.w3c.dom.Element p) {
                p.setAttribute("type", e.getReturnType().toString());
                p.setAttribute("processed-value", e.toString().trim());
                String s = e.getModifiers().stream().filter(m -> !m.equals(Modifier.ABSTRACT)).map(m -> m.toString()).collect(Collectors.joining(" ")).toString();

                p.setAttribute("modifiers", s);
                return null;
            }

            private String getSleeJndiName(String cn) {
                if (cn.startsWith("javax.slee")) {
                    try {
                        return Class.forName(cn).getField("JNDI_NAME").get(Class.forName(cn)).toString();
                    } catch (IllegalArgumentException ex) {
                    } catch (IllegalAccessException ex) {
                    } catch (NoSuchFieldException ex) {
                    } catch (SecurityException ex) {
                    } catch (ClassNotFoundException ex) {
                    }
                }
                return "{unknown}";
            }
        }, node);
    }

    private void setIdAttributes(Node node) {
    }

    private String BeanifySentenceCase(String v) {
        return v.substring(0, 1).toUpperCase() + v.substring(1);
    }

    private void doResourceAdaptorACI(final Element e2, AnnotationMirror a) {
        for (Entry<? extends ExecutableElement, ? extends AnnotationValue> e : this.processingEnv.getElementUtils().getElementValuesWithDefaults(a).entrySet()) {
            if (e.getKey().getSimpleName().toString().equals("aciFactory")) {
                Object o = e.getValue().getValue();
                TypeElement aci = super.processingEnv.getElementUtils().getTypeElement(o.toString());

                for (Element m : processingEnv.getElementUtils().getAllMembers(aci)) {
                    m.accept(new ElementKindVisitor6<Boolean, Object>() {

                        @Override
                        public Boolean visitExecutableAsMethod(ExecutableElement e, Object p) {
                            if (e.getReturnType().toString().equals(javax.slee.ActivityContextInterface.class.getName())) {
                                org.w3c.dom.Element n;
                                n = doc.createElement("activitycontextinterface");
                                n.setAttribute("activity", e.getParameters().get(0).asType().toString());
                                n.setAttribute("enclosing", e2.toString());
                                rootNode.appendChild(n);
                            }
                            return Boolean.TRUE;
                        }
                    }, o);
                }
            }
        }
    }

    private String formatMethodImpl(TypeElement e, ExecutableElement m) {
        StringBuffer buffy = new StringBuffer();

        int i = 0;

        MessageFormat f = new MessageFormat("public {0} {1}.{2} {3} '{' {4} '}'");

        String mm = m.toString();
        String mb = "";
        String mt = "";

        if (m.getParameters().size() > 0) {
            mm = mm.replaceAll(",", " {},").replaceFirst("\\)", " {})");
            for (Object o : m.getParameters()) {
                mm = mm.replaceFirst("\\{\\}", "arg" + i++);
            }
        }

        if (!m.getReturnType().toString().equals("void")) {
            mb = "return null;";
        }

        if (m.getThrownTypes().size() > 0) {
            mt = "throws " + m.getThrownTypes().toString();
        }

        return f.format(new Object[]{m.getReturnType().toString(), e.toString(), mm, mt, mb}, buffy, null).toString();

    }

    private void processPackage(org.w3c.dom.Element node, AnnotationMirror a, Element e2) {

        if (a.getAnnotationType().asElement().toString().equals(javax.slee.annotation.Sbb.class.getName())
                || a.getAnnotationType().asElement().toString().equals(javax.slee.annotation.ProfileSpec.class.getName())
                || a.getAnnotationType().asElement().toString().equals(javax.slee.annotation.ResourceAdaptor.class.getName())) {
            TypeElement clazz = super.processingEnv.getElementUtils().getTypeElement(e2.toString());
            node.setAttribute("package", ((PackageElement) clazz.getEnclosingElement()).getQualifiedName().toString());
            node.setAttribute("simple-name", clazz.getSimpleName().toString());
        }
    }

    private void addMethods(Element e2, TypeElement base) {
    }

    private void generateDescriptors() throws XPathExpressionException, InstantiationException, IllegalAccessException, IOException, ParserConfigurationException, SAXException, ClassNotFoundException {

        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        DOMSource domDoc = new DOMSource(doc.getDocumentElement().getFirstChild());

        if (xpath.compile("count(/process/element[@kind='CLASS']/annotation[@name='javax.slee.annotation.event.EventType'])>0").evaluate(doc.getDocumentElement(), XPathConstants.BOOLEAN).equals(Boolean.TRUE)) {
            processOutput(doc, "event-jar.xslt", "META-INF/event-jar.xml", "");
        }

        if (xpath.compile("count(/process/element[@kind='CLASS']/annotation[@name='javax.slee.annotation.Service'])>0").evaluate(doc.getDocumentElement(), XPathConstants.BOOLEAN).equals(Boolean.TRUE)) {
            processOutput(doc, "service.xslt", "service.xml", "");
        }

        if (xpath.compile("count(/process/element[@kind='CLASS']/annotation[@name='javax.slee.annotation.ResourceAdaptor'])>0").evaluate(doc.getDocumentElement(), XPathConstants.BOOLEAN).equals(Boolean.TRUE)) {
            processOutput(doc, "resource-adaptor-jar.xslt", "META-INF/resource-adaptor-jar.xml", "");
        }
        if (xpath.compile("count(/process/element[@kind='CLASS']/annotation[@name='javax.slee.annotation.ResourceAdaptorType'])>0").evaluate(doc.getDocumentElement(), XPathConstants.BOOLEAN).equals(Boolean.TRUE)) {
            processOutput(doc, "resource-adaptor-type-jar.xslt", "META-INF/resource-adaptor-type-jar.xml", "");
        }
        if (xpath.compile("count(/process/element[@kind='CLASS' or @kind='INTERFACE']/annotation[@name='javax.slee.annotation.ProfileSpec'])>0").evaluate(doc.getDocumentElement(), XPathConstants.BOOLEAN).equals(Boolean.TRUE)) {
            processOutput(doc, "profile-spec-jar.xslt", "META-INF/profile-spec-jar.xml", "");
        }
        if (xpath.compile("count(/process/element[@kind='CLASS']/annotation[@name='javax.slee.annotation.Sbb'])>0").evaluate(doc.getDocumentElement(), XPathConstants.BOOLEAN).equals(Boolean.TRUE)) {
            processOutput(doc, "sbb-jar.xslt", "META-INF/sbb-jar.xml", "");
        }

    }

    private void runAjcCompiler() {
        /*
             * Filer filer = super.processingEnv.getFiler();
             * String pi = "notused.package-info";
             * JavaFileObject testFile = filer.createClassFile(pi);
             * File baseDir = new File(testFile.toUri().getPath());
             * baseDir = baseDir.getParentFile().getParentFile();
             * logger.info(Arrays.toString(baseDir.list()));
             * testFile.delete();
             *
             * AjcCompiler ajcCompiler = new AjcCompiler();
             * ajcCompiler.setOutputDirectory(baseDir);
             * ajcCompiler.setBasedir(baseDir);
             * switch (this.processingEnv.getSourceVersion()) {
             * case RELEASE_3:
             * ajcCompiler.setSource("1.3");
             * break;
             * case RELEASE_4:
             * ajcCompiler.setSource("1.4");
             * break;
             * case RELEASE_5:
             * ajcCompiler.setSource("1.5");
             * break;
             * case RELEASE_6:
             * ajcCompiler.setSource("1.6");
             * break;
             * default:
             * ajcCompiler.setSource("1.5");
             *
             * }
             *
             * //ajcCompiler.setWeaveDirectories(new String[]{baseDir.toString()});
             * ajcCompiler.setVerbose(true);
             * ajcCompiler.setForceAjcCompile(true);
             * ajcCompiler.setIncludes(aspects.toArray(new String[aspects.size()]));
             *
             * String cp = "";
             * for (URL u : ((URLClassLoader) this.getClass().getClassLoader()).getURLs()) {
             * cp += u.getPath();
             * cp += File.pathSeparatorChar;
             * }
             *
             * for (URL u : ((URLClassLoader) ClassLoader.getSystemClassLoader()).getURLs()) {
             * cp += u.getPath();
             * cp += File.pathSeparatorChar;
             * }
             * cp += System.getProperty("sun.boot.class.path");
             * cp += File.pathSeparatorChar;
             * cp += baseDir.toString();
             *
             * ajcCompiler.setBootClassPath(cp);
             * ajcCompiler.execute();
             *
         */
    }

    private void addModifiers(Element e2, org.w3c.dom.Element node) {
        String s = e2.getModifiers().stream().filter(m -> !m.equals(Modifier.ABSTRACT)).map(m -> m.toString()).collect(Collectors.joining(" ")).toString();

        ((org.w3c.dom.Element) node).setAttribute("modifiers", s);
        Boolean isAbstract = e2.getModifiers().contains(Modifier.ABSTRACT);
        if (isAbstract) {
            ((org.w3c.dom.Element) node).setAttribute("abstract", isAbstract.toString());
        }
    }

    public Filer getFiler() {
        return super.processingEnv.getFiler();
    }

    private Optional<? extends Element> getElement(String name, Element e) {
        return e.getEnclosedElements().stream().filter(e2 -> name.equals(e2.getSimpleName())).findFirst();
    }

    private TypeElement getTypeElement(String name) {
        return super.processingEnv.getElementUtils().getTypeElement(name);
    }

    private Optional<String> getElementValue(AnnotationMirror a, String name) {
        return a.getElementValues().entrySet().stream().filter(e -> name.equals(e.getKey().getSimpleName().toString())).map(e -> e.getValue().getValue().toString()).findFirst();
    }

    private String capitalizeFirst(String name) {
        StringBuilder bob = new StringBuilder(name);
        bob.setCharAt(0, Character.toUpperCase(bob.charAt(0)));
        return bob.toString();
    }

    private String kebabCase(String snakeCase) {
        return snakeCase.toLowerCase().replaceAll("_", "-");
    }

}
