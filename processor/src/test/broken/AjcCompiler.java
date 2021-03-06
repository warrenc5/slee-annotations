

/**
 * The MIT License
 *
 * Copyright 2005-2006 The Codehaus.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xalan.xsltc.runtime.MessageHandler;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.MessageHandler;
import org.aspectj.tools.ajc.Main;


public class AjcCompiler {

    /**
     * List of ant-style patterns used to specify the aspects that should be
     * included when compiling. When none specified all .java and .aj files in
     * the project source directories, or directories specified by the
     * ajdtDefFile property are included.
     *
     * @parameter
     */
    protected String[] includes;
    /**
     * List of ant-style patterns used to specify the aspects that should be
     * excluded when compiling. When none specified all .java and .aj files in
     * the project source directories, or directories specified by the
     * ajdtDefFile property are included.
     *
     * @parameter
     */
    protected String[] excludes;
    /**
     * Where to find the ajdt build definition file. <i>If set this will
     * override the use of project sourcedirs</i>.
     *
     * @parameter
     */
    protected String ajdtBuildDefFile;
    /**
     * Generate aop.xml file for load-time weaving with default name
     * (/META-INF/aop.xml).
     *
     * @parameter
     */
    protected boolean outxml;
    /**
     * Generate aop.xml file for load-time weaving with custom name.
     *
     * @parameter
     */
    protected String outxmlfile;
    /**
     * Generate .ajesym symbol files for emacs support.
     *
     * @parameter
     */
    protected boolean emacssym;
    /**
     * Set default level for messages about potential programming mistakes in
     * crosscutting code. {level} may be ignore, warning, or error. This
     * overrides entries in org/aspectj/weaver/XlintDefault.properties from
     * aspectjtools.jar.
     *
     * @parameter
     */
    protected String Xlint;
    /**
     * Enables the compiler to support hasmethod(method_pattern) and
     * hasfield(field_pattern) type patterns, but only within declare
     * statements. It's experimental and undocumented because it may change, and
     * because it doesn't yet take into account ITDs.
     *
     * @parameter
     *
     * @since 1.3
     */
    protected boolean XhasMember;
    /**
     * Specify classfile target setting (1.1 to 1.6) default is 1.2
     *
     * @parameter default-value="${project.build.java.target}"
     */
    protected String target;
    /**
     * Toggle assertions (1.3, 1.4, or 1.6 - default is 1.4). When using -source
     * 1.3, an assert() statement valid under Java 1.4 will result in a compiler
     * error. When using -source 1.4, treat assert as a keyword and implement
     * assertions according to the 1.4 language spec. When using -source 1.5 or
     * higher, Java 5 language features are permitted.
     *
     * @parameter default-value="${mojo.java.target}"
     */
    protected String source;
    /**
     * Specify compiler compliance setting (1.3 to 1.6) default is 1.4
     *
     * @parameter
     */
    protected String complianceLevel;
    /**
     * Toggle warningmessages on deprecations
     *
     * @parameter
     */
    protected boolean deprecation;
    /**
     * Emit no errors for unresolved imports;
     *
     * @parameter
     */
    protected boolean noImportError;
    /**
     * Keep compiling after error, dumping class files with problem methods
     *
     * @parameter
     */
    protected boolean proceedOnError;
    /**
     * Preserve all local variables during code generation (to
     * facilitate.fineging).
     *
     * @parameter
     */
    protected boolean preserveAllLocals;
    /**
     * Compute reference information.
     *
     * @parameter
     */
    protected boolean referenceInfo;
    /**
     * Specify default source encoding format.
     *
     * @parameter expression="${project.build.sourceEncoding}"
     */
    protected String encoding;
    /**
     * Emit messages about accessed/processed compilation units
     *
     * @parameter
     */
    protected boolean verbose;
    /**
     * Emit messages about weaving
     *
     * @parameter
     */
    protected boolean showWeaveInfo;
    /**
     * Repeat compilation process N times (typically to do performance
     * analysis).
     *
     * @parameter
     */
    protected int repeat;
    /**
     * (Experimental) runs weaver in reweavable mode which causes it to create
     * woven classes that can be rewoven, subject to the restriction that on
     * attempting a reweave all the types that advised the woven type must be
     * accessible.
     *
     * @parameter
     */
    protected boolean Xreweavable;
    /**
     * (Experimental) do not inline around advice
     *
     * @parameter
     */
    protected boolean XnoInline;
    /**
     * (Experimental) Normally it is an error to declare aspects {@link Serializable}.
     * This option removes that restriction.
     *
     * @parameter
     */
    protected boolean XserializableAspects;
    /**
     * Causes the compiler to calculate and add the SerialVersionUID field to
     * any type implementing {@link Serializable} that is affected by an aspect.
     * The field is calculated based on the class before weaving has taken
     * place.
     *
     * @parameter
     */
    protected boolean XaddSerialVersionUID;
    /**
     * Causes compiler to terminate before weaving
     *
     * @parameter
     */
    protected boolean XterminateAfterCompilation;
    /**
     * Override location of VM's bootclasspath for purposes of evaluating types
     * when compiling. Path is a single argument containing a list of paths to
     * zip files or directories, delimited by the platform-specific path
     * delimiter.
     *
     * @parameter
     */
    protected String bootclasspath;
    /**
     * Emit warnings for any instances of the comma-delimited list of
     * questionable code (e.g. 'unusedLocals,deprecation'): see
     * http://www.eclipse.org/aspectj/doc/released/devguide/ajc-ref.html#ajc for
     * available settings
     *
     * @parameter
     */
    protected String warn;
    /**
     * The filename to store build configuration in. This file will be placed in
     * the project build output directory, and will contain all the arguments
     * passed to the compiler in the last run, and also all the filenames
     * included in the build. Aspects as well as java files.
     *
     * @parameter default-value="builddef.lst"
     */
    protected String argumentFileName = "builddef.lst";
    /**
     * Forces re-compilation, regardless of whether the compiler arguments or
     * the sources have changed.
     *
     * @parameter
     */
    protected boolean forceAjcCompile;
    /**
     * Holder for ajc compiler options
     */
    protected List ajcOptions = new ArrayList();
    /**
     * Holds all files found using the includes, excludes parameters.
     */
    protected Set resolvedIncludes;
    /**
     * The directory for compiled classes.
     *
     * @parameter default-value="${project.build.outputDirectory}" @required
     * @readonly
     */
    private File outputDirectory;
    /**
     * The basedir of the project.
     *
     * @parameter default-value="${basedir}" @required @readonly
     */
    protected File basedir;

    protected File getOutputDirectory() {
        return outputDirectory;
    }

    /**
     *
     */
    protected List getClasspathDirectories() {
        return Collections.singletonList(outputDirectory);
    }

    /**
     *
     */
    protected List getSourceDirectories() {
        return null;
    }

    protected String getAdditionalAspectPaths() {
        return null;
    }
    /**
     * List of of directories with .class files to weave (into target
     * directory). Corresponds to
     * <code>ajc -inpath</code> option .
     *
     * @parameter
     *
     * @since 1.4
     */
    protected String[] weaveDirectories;
    /**
     * Lock for the call to the AspectJ compiler to make it thread-safe.
     */
    private static final Object BIG_ASPECTJ_LOCK = new Object();

    /**
     * Do the AspectJ compiling.
     *
     * @throws Exception
     */
    public void execute()
            throws Exception {
        assembleArguments();

        if (!forceAjcCompile && !hasSourcesToCompile()) {
            getLog().warning("No sources found skipping aspectJ compile");
            return;
        }

        if (!forceAjcCompile && !isBuildNeeded()) {
            getLog().info("No modifications found skipping aspectJ compile");
            return;
        }

        if (getLog().isLoggable(Level.INFO)) {
            String command = "Running : ajc";
            Iterator iter = ajcOptions.iterator();
            while (iter.hasNext()) {
                command += (" " + iter.next());
            }
            getLog().info(command);
        }
        try {
            getLog().fine("Compiling and weaving " + resolvedIncludes.size() + " sources to "
                    + getOutputDirectory());
            writeBuildConfigToFile(ajcOptions, argumentFileName, getOutputDirectory());
            getLog().fine("Argumentsfile written : "
                    + new File(getOutputDirectory(), argumentFileName).getAbsolutePath());
        } catch (IOException e) {
            throw new Exception("Could not write arguments file to the target area", e);
        }

        Main main = new Main();
        MavenMessageHandler mavenMessageHandler = new MavenMessageHandler(getLog());
        main.setHolder(mavenMessageHandler);

        synchronized (BIG_ASPECTJ_LOCK) {
            main.runMain((String[]) ajcOptions.toArray(new String[ajcOptions.size()]), false);
        }

        IMessage[] errors = mavenMessageHandler.getMessages(IMessage.ERROR, true);
        if (!proceedOnError && errors.length > 0)
            throw new Exception(Arrays.asList(errors).toString());
    }

    /**
     * Assembles a complete ajc compiler arguments list.
     *
     * @throws Exception error in configuration
     */
    protected void assembleArguments()
            throws Exception {
        if (XhasMember)
            ajcOptions.add("-XhasMember");

        // Add classpath
        ajcOptions.add("-classpath");
        ajcOptions.add(createClassPath(getClasspathDirectories()));

        // Add boot classpath
        if (null != bootclasspath) {
            ajcOptions.add("-bootclasspath");
            ajcOptions.add(bootclasspath);
        }

        // Add warn option
        if (null != warn)
            ajcOptions.add("-warn:" + warn);

        // Add artifacts or directories to weave
        String joinedWeaveDirectories = null;
        if (weaveDirectories != null)
            joinedWeaveDirectories = join(weaveDirectories, File.pathSeparator);
        //addModulesArgument( "-inpath", ajcOptions, weaveDependencies, joinedWeaveDirectories, "dependencies and/or directories to weave" );

        // Add library artifacts
        /*
         * addModulesArgument( "-aspectpath", ajcOptions, aspectLibraries,
         * getAdditionalAspectPaths(), "an aspect library" );
         *
         */

        // add target dir argument
        ajcOptions.add("-d");
        ajcOptions.add(getOutputDirectory().getAbsolutePath());

        // Add all the files to be included in the build,
        if (null != ajdtBuildDefFile)
            resolvedIncludes = getBuildFilesForAjdtFile(ajdtBuildDefFile, basedir);
        else
            resolvedIncludes = getIncludedSources();
        ajcOptions.addAll(resolvedIncludes);
    }

    /**
     * Finds all artifacts in the weavemodule property, and adds them to the ajc
     * options.
     *
     * @param argument
     * @param arguments
     * @param modules
     * @param aditionalpath
     * @param role
     * @throws Exception private void addModulesArgument( String argument, List
     * arguments, Module[] modules, String aditionalpath, String role ) throws
     * Exception { StringBuffer buf = new StringBuffer();
     *
     * if ( null != aditionalpath ) { arguments.add( argument ); buf.append(
     * aditionalpath ); } if ( modules != null && modules.length > 0 ) { if (
     * !arguments.contains( argument ) ) { arguments.add( argument ); }
     *
     * for ( int i = 0; i < modules.length; ++i ) { Module module = modules[i];
     * // String key = ArtifactUtils.versionlessKey( module.getGroupId(),
     * module.getArtifactId() ); // Artifact artifact = (Artifact)
     * project.getArtifactMap().get( key ); Artifact artifact = null; Set
     * allArtifacts = project.getArtifacts(); for ( Iterator iterator =
     * allArtifacts.iterator(); iterator.hasNext(); ) { Artifact art =
     * (Artifact) iterator.next(); if ( art.getGroupId().equals(
     * module.getGroupId() ) && art.getArtifactId().equals(
     * module.getArtifactId() ) && StringUtils.defaultString(
     * module.getClassifier() ).equals( StringUtils.defaultString(
     * art.getClassifier() ) ) && StringUtils.defaultString( module.getType(),
     * "jar" ).equals( StringUtils.defaultString( art.getType() ) ) ) { artifact
     * = art; break; } } if ( artifact == null ) { throw new Exception( "The
     * artifact " + module.toString() + " referenced in aspectj plugin as " +
     * role + ", is not found the project dependencies" ); } if ( buf.length()
     * != 0 ) { buf.append( File.pathSeparatorChar ); } buf.append(
     * artifact.getFile().getPath() ); } } if ( buf.length() > 0 ) { String
     * pathString = buf.toString(); arguments.add( pathString ); getLog().fine(
     * "Adding " + argument + ": " + pathString ); } }
     */

    /**
     * Checks modifications that would make us need a build
     *
     * @return
     * <code>true</code> if build is needed, otherwise
     * <code>false</code>
     * @throws Exception
     */
    protected boolean isBuildNeeded()
            throws Exception {
        File outDir = getOutputDirectory();
        return hasNoPreviousBuild(outDir) || hasArgumentsChanged(outDir)
                || hasSourcesChanged(outDir) || hasNonWeavedClassesChanged(outDir);

    }

    private boolean hasNoPreviousBuild(File outDir) {
        return !resolveFile(outDir, argumentFileName).exists();
    }

    private boolean hasArgumentsChanged(File outDir)
            throws Exception {
        return (!ajcOptions.equals(readBuildConfigFile(argumentFileName, outDir)));
    }

    /**
     * Not entirely safe, assembleArguments() must be run
     */
    private boolean hasSourcesToCompile() {
        return resolvedIncludes.size() > 0;
    }

    private boolean hasSourcesChanged(File outDir) {
        Iterator sourceIter = resolvedIncludes.iterator();
        long lastBuild = new File(outDir, argumentFileName).lastModified();
        while (sourceIter.hasNext()) {
            File sourceFile = new File((String) sourceIter.next());
            long sourceModified = sourceFile.lastModified();
            if (sourceModified >= lastBuild)
                return true;

        }
        return false;
    }

    private boolean hasNonWeavedClassesChanged(File outDir)
            throws Exception {
        if (weaveDirectories != null && weaveDirectories.length > 0) {
            Set weaveSources = getWeaveSourceFiles(weaveDirectories);
            Iterator sourceIter = weaveSources.iterator();
            long lastBuild = new File(outDir, argumentFileName).lastModified();
            while (sourceIter.hasNext()) {
                File sourceFile = new File((String) sourceIter.next());
                long sourceModified = sourceFile.lastModified();
                if (sourceModified >= lastBuild)
                    return true;

            }
        }
        return false;
    }

    /**
     * Setters which when called sets compiler arguments
     *
     * @param complianceLevel the complianceLevel
     */
    public void setComplianceLevel(String complianceLevel) {
        if (complianceLevel.equals("1.3") || complianceLevel.equals("1.4") || complianceLevel.equals("1.5")
                || complianceLevel.equals("1.6"))
            ajcOptions.add("-" + complianceLevel);

    }

    public void setDeprecation(boolean deprecation) {
        if (deprecation)
            ajcOptions.add("-deprecation");
    }

    public void setEmacssym(boolean emacssym) {
        if (emacssym)
            ajcOptions.add("-emacssym");

    }

    public void setEncoding(String encoding) {
        ajcOptions.add("-encoding");
        ajcOptions.add(encoding);
    }

    public void setNoImportError(boolean noImportError) {
        if (noImportError)
            ajcOptions.add("-noImportError");

    }

    public void setOutxml(boolean outxml) {
        if (outxml)
            ajcOptions.add("-outxml");

    }

    public void setOutxmlfile(String outxmlfile) {
        ajcOptions.add("-outxmlfile");
        ajcOptions.add(outxmlfile);
    }

    public void setPreserveAllLocals(boolean preserveAllLocals) {
        if (preserveAllLocals)
            ajcOptions.add("-preserveAllLocals");

    }

    public void setProceedOnError(boolean proceedOnError) {
        if (proceedOnError)
            ajcOptions.add("-proceedOnError");
        this.proceedOnError = proceedOnError;
    }

    public void setReferenceInfo(boolean referenceInfo) {
        if (referenceInfo)
            ajcOptions.add("-referenceInfo");

    }

    public void setRepeat(int repeat) {
        ajcOptions.add("-repeat");
        ajcOptions.add("" + repeat);
    }

    public void setShowWeaveInfo(boolean showWeaveInfo) {
        if (showWeaveInfo)
            ajcOptions.add("-showWeaveInfo");

    }

    public void setTarget(String target) {
        ajcOptions.add("-target");
        ajcOptions.add(target);
    }

    public void setSource(String source) {
        ajcOptions.add("-source");
        ajcOptions.add(source);
    }

    public void setVerbose(boolean verbose) {
        if (verbose)
            ajcOptions.add("-verbose");
    }

    public void setXhasMember(boolean xhasMember) {
        XhasMember = xhasMember;
    }

    public void setXlint(String xlint) {
        ajcOptions.add("-Xlint:" + xlint);
    }

    public void setXnoInline(boolean xnoInline) {
        if (xnoInline)
            ajcOptions.add("-XnoInline");

    }

    public void setXreweavable(boolean xreweavable) {
        if (xreweavable)
            ajcOptions.add("-Xreweavable");

    }

    public void setXserializableAspects(boolean xserializableAspects) {
        if (xserializableAspects)
            ajcOptions.add("-XserializableAspects");
    }

    public void setXaddSerialVersionUID(boolean xaddSerialVersionUID) {
        if (xaddSerialVersionUID)
            ajcOptions.add("-XaddSerialVersionUID");
    }

    public void setXterminateAfterCompilation(boolean xterminateAfterCompilation) {
        if (xterminateAfterCompilation)
            ajcOptions.add("-XterminateAfterCompilation");
    }

    public void setBootClassPath(String bootclasspath) {
        this.bootclasspath = bootclasspath;
    }

    public void setWarn(String warn) {
        this.warn = warn;
    }

    public void setArgumentFileName(String argumentFileName) {
        this.argumentFileName = argumentFileName;

    }

    private Logger getLog() {
        Logger log = Logger.getLogger("spawn ajc");
        log.setLevel(Level.ALL);
        return log;

    }

    private String join(Object[] names, String pathSeparator) {
        StringBuilder bob = new StringBuilder();
        for (int i = 0; i < names.length; i++) {
            bob.append(names[i].toString());
            if (i < names.length)
                bob.append(pathSeparator);
        }
        return bob.toString();
    }

    public void setAjdtBuildDefFile(String ajdtBuildDefFile) {
        this.ajdtBuildDefFile = ajdtBuildDefFile;
    }

    public void setBasedir(File basedir) {
        this.basedir = basedir;
    }

    public void setExcludes(String[] excludes) {
        this.excludes = excludes;
    }

    public void setForceAjcCompile(boolean forceAjcCompile) {
        this.forceAjcCompile = forceAjcCompile;
    }

    public void setIncludes(String[] includes) {
        this.includes = includes;
    }

    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public void setResolvedIncludes(Set resolvedIncludes) {
        this.resolvedIncludes = resolvedIncludes;
    }

    public void setWeaveDirectories(String[] weaveDirectories) {
        this.weaveDirectories = weaveDirectories;
    }

    private File resolveFile(File outDir, String argumentFileName) {
        return new File(outDir, argumentFileName);
    }

    private Set getWeaveSourceFiles(String[] weaveDirectories) {
        return new HashSet();
    }

    private String createClassPath(List classpathDirectories) {
        if(classpathDirectories.size() ==0 )
            return "none";

        return join(classpathDirectories.toArray(new File[classpathDirectories.size()]),File.pathSeparator);
    }

    private Set getIncludedSources() {
        Set includeSources = new HashSet();
        for(String s : includes)
            includeSources.add(s);
            
        return includeSources;
    }

    private void writeBuildConfigToFile(List ajcOptions, String argumentFileName, File outputDirectory) throws IOException {
    }

    private Set getBuildFilesForAjdtFile(String ajdtBuildDefFile, File basedir) {
        return new HashSet();
    }

    private Object readBuildConfigFile(String argumentFileName, File outDir) {
        return "";
    }
}
class MavenMessageHandler
        extends MessageHandler {

    Logger log;

    /**
     * Constructs a MessageHandler with a Maven plugin logger.
     *
     * @param log
     */
    public MavenMessageHandler(Logger log) {
        super();
        this.log = log;
    }

    /**
     * Hook into the maven logger.
     */
    public boolean handleMessage(IMessage message) {
        if (message.getKind().equals(IMessage.WARNING) && !isIgnoring(IMessage.WARNING))
            log.warning(message.getMessage());
        else if (message.getKind().equals(IMessage.DEBUG) && !isIgnoring(IMessage.DEBUG))
            log.fine(message.getMessage());
        else if (message.getKind().equals(IMessage.ERROR) && !isIgnoring(IMessage.ERROR))
            log.severe(message.getMessage());
        else if (message.getKind().equals(IMessage.ABORT) && !isIgnoring(IMessage.ABORT))
            log.severe(message.getMessage());
        else if (message.getKind().equals(IMessage.FAIL) && !isIgnoring(IMessage.FAIL))
            log.severe(message.getMessage());
        else if (message.getKind().equals(IMessage.INFO) && !isIgnoring(IMessage.INFO))
            log.fine(message.getMessage());
        else if (message.getKind().equals(IMessage.WEAVEINFO) && !isIgnoring(IMessage.WEAVEINFO))
            log.info(message.getMessage());
        else if (message.getKind().equals(IMessage.TASKTAG) && !isIgnoring(IMessage.TASKTAG))
            log.fine(message.getMessage());
        return super.handleMessage(message);
    }

}
