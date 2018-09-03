<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output omit-xml-declaration="yes" method="text" indent="no" />

    <xsl:strip-space elements="node()"/>
    <xsl:key name="unique-field" match="element[@kind='FIELD']" use="concat(@name,@enclosing)"/>

    <xsl:key name="unique-method" match="element[@kind='FIELD' and annotation[@name='javax.annotation.Resource']]" use="@enclosing"/>
    <xsl:key name="unique-aspect" match="element[@package]" use="@package"/>

    <xsl:template match="/process">

        <xsl:apply-templates select="element[@name and annotation[@package]]" mode="aspect"/>
    
    </xsl:template>

    <xsl:template match="element[@name and annotation[@package]]" mode="aspect">
        <xsl:variable name="name" select="@name" />
        <xsl:text>file:</xsl:text><xsl:value-of select="annotation/@package"/>/<xsl:value-of select="annotation/@simple-name"/><xsl:text>$SleeAnnotationsAspect.aj
        </xsl:text> 
        <xsl:text>
        package </xsl:text><xsl:value-of select="annotation/@package"/><xsl:text>;</xsl:text>

        <xsl:text>
        import javax.slee.facilities.ActivityContextNamingFacility;
        import javax.slee.facilities.AlarmFacility;
        import javax.slee.facilities.TimerFacility;
        import javax.slee.facilities.Tracer;
        import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
        import javax.slee.nullactivity.NullActivityFactory;
        import javax.slee.profile.ProfileFacility;
        import javax.slee.profile.ProfileTableActivityContextInterfaceFactory;
        import javax.slee.serviceactivity.ServiceActivityContextInterfaceFactory;
        import javax.slee.serviceactivity.ServiceActivityFactory;
        import javax.naming.Context;
        import javax.naming.InitialContext;
        import javax.naming.NamingException;

            
        public privileged aspect </xsl:text><xsl:value-of select="annotation/@simple-name"/><xsl:text>$SleeAnnotationsAspect issingleton() {

            </xsl:text>
            <xsl:if test="boolean(@implements)">
            <xsl:text> declare parents : </xsl:text><xsl:value-of select="annotation/@simple-name"/><xsl:text> implements </xsl:text><xsl:value-of select="@interface"/><xsl:text>; </xsl:text>
        </xsl:if>
            <xsl:apply-templates select="//*[@enclosing=$name]" />
            <xsl:text>
        }
            </xsl:text>
            <xsl:text>

        privileged aspect </xsl:text><xsl:value-of select="annotation/@simple-name"/><xsl:text>$DebugAspect {
            pointcut debug_class(): within(</xsl:text><xsl:value-of select="annotation/@name"/><xsl:text>) &amp;&amp; !within(</xsl:text><xsl:value-of select="annotation/@package"/>.<xsl:value-of select="annotation/@simple-name"/><xsl:text>$DebugAspect) &amp;&amp; !within(</xsl:text><xsl:value-of select="annotation/@package"/>.<xsl:value-of select="annotation/@simple-name"/><xsl:text>$SleeAnnotationsAspect);

            pointcut debug_constructor(): debug_class() &amp;&amp; execution(new(..));
            pointcut debug_method(): debug_class() &amp;&amp; execution(* *(..));
            pointcut debug_set(): debug_class() &amp;&amp; (set(* *.*));
            pointcut debug_get(): debug_class() &amp;&amp; (get(* *.*));

            before (): debug_constructor() {
                System.err.println("={= " + thisJoinPointStaticPart.getSignature());
            }
            after(): debug_constructor() {
            System.err.println("=}=} " + thisJoinPointStaticPart.getSignature());
            }

            before (): debug_method() {
            System.err.println("={= " + thisJoinPointStaticPart.getSignature());
            }
            after() returning : debug_method() {
            System.err.println("=}= " + thisJoinPointStaticPart.getSignature());
            }
            after() throwing : debug_method() {
            System.err.println("=*= " + thisJoinPointStaticPart.getSignature());
            }
            after(): debug_set() {
            System.err.println("=w= " + thisJoinPointStaticPart.getSignature());
            }
            after(): debug_get() {
            System.err.println("=r= " + thisJoinPointStaticPart.getSignature());
            }

        }
</xsl:text>
    </xsl:template>
    <!--
    <xsl:template match="/process//element/annotation[@name='javax.annotation.Resource']">
        xsl:template match="element/annotation[@name='javax.annotation.Resource' and @type='<xsl:value-of select="@type" />']"
        /xsl:template
    </xsl:template>
    -->
<!-- SLEE Abstract class methods -->

    <xsl:template match="element[@kind='CLASS']/annotation[@name='javax.slee.annotation.Sbb' or @name='javax.slee.annotation.ProfileSpec' or @name='javax.slee.annotation.ResourceAdaptor']" mode="method-intercept-inject">
        <xsl:text>object.__context=arg1;
        </xsl:text>
        <xsl:text>
            try{
            object.__iContext=new javax.naming.InitialContext();
            }catch(javax.naming.NamingException x){
            throw new RuntimeException(x);
            }
        </xsl:text>
        
    </xsl:template>

    <xsl:template match="element[@kind='CLASS']/annotation[@name='javax.slee.annotation.Sbb']">
        /*
        Sbb 
        */
        <xsl:text>javax.slee.SbbContext </xsl:text><xsl:value-of select="../@name"/><xsl:text>.__context;
        </xsl:text>
        <xsl:text>javax.naming.InitialContext </xsl:text><xsl:value-of select="../@name"/><xsl:text>.__iContext;
        </xsl:text>

        <xsl:apply-templates select="." mode="method-intercept-pointcut">
            <xsl:with-param name="arg-type" select="'javax.slee.SbbContext'"/>
            <xsl:with-param name="method-name" select="'setSbbContext'"/>
        </xsl:apply-templates>

        <xsl:variable name="enclosing" select="../@enclosing" />
        <xsl:apply-templates select="//methods[@enclosing = $enclosing]" />

    </xsl:template>

    <xsl:template match="element[@kind='CLASS']/annotation[@name='javax.slee.annotation.ProfileSpec']">
        /*
        ProfileSpec 
        */
        <xsl:text>javax.slee.profile.ProfileContext </xsl:text><xsl:value-of select="../@name"/><xsl:text>.__context;
        </xsl:text>
        <xsl:text>javax.naming.InitialContext </xsl:text><xsl:value-of select="../@name"/><xsl:text>.__iContext;
        </xsl:text>
        <xsl:apply-templates select="." mode="method-intercept-pointcut">
            <xsl:with-param name="method-name" select="'setProfileContext'"/>
            <xsl:with-param name="arg-type" select="'javax.slee.profile.ProfileContext'"/>
        </xsl:apply-templates>

        <xsl:variable name="enclosing" select="../@enclosing" />

    </xsl:template>

    <xsl:template match="element[@kind='CLASS']/annotation[@name='javax.slee.annotation.ResourceAdaptor']" mode="method-intercept-inject" priority="1">

        <xsl:param name="arg-type"/>
        <xsl:param name="method-name"/>

        <xsl:text>object.</xsl:text>
        <xsl:if test="$arg-type = 'javax.slee.resource.ConfigProperties'">
        <xsl:text>__configProperties</xsl:text>
        </xsl:if>
        <xsl:if test="$arg-type = 'javax.slee.resource.ResourceAdaptorContext'">
        <xsl:text>__context</xsl:text>
        </xsl:if>
        <xsl:text> = arg1;
        </xsl:text>
    </xsl:template>

    <xsl:template match="element[@kind='CLASS']/annotation[@name='javax.slee.annotation.ResourceAdaptor']" priority="1">

        /*
        RA 
        */
        <xsl:text>javax.slee.resource.ResourceAdaptorContext </xsl:text><xsl:value-of select="../@name"/><xsl:text>.__context;
        </xsl:text>
        <xsl:text>javax.naming.InitialContext </xsl:text><xsl:value-of select="../@name"/><xsl:text>.__iContext;
        </xsl:text>

        <xsl:text>javax.slee.resource.ConfigProperties </xsl:text><xsl:value-of select="../@name"/><xsl:text>.__configProperties;
        </xsl:text>

        <xsl:apply-templates select="." mode="method-intercept-pointcut">
            <xsl:with-param name="method-name" select="'setResourceAdaptorContext'"/>
            <xsl:with-param name="arg-type" select="'javax.slee.resource.ResourceAdaptorContext'"/>
        </xsl:apply-templates>

        <xsl:apply-templates select="." mode="method-intercept-pointcut">
            <xsl:with-param name="arg-type" select="'javax.slee.resource.ConfigProperties'"/>
            <xsl:with-param name="method-name" select="'raConfigure'"/>
        </xsl:apply-templates>

        <xsl:variable name="enclosing" select="../@enclosing" />

    </xsl:template>

    <xsl:template match="element[@kind='FIELD']/annotation[@name='javax.slee.annotation.ChildRelation']" mode="get-field-inject">
        <xsl:variable name="sbbLocalObject">
            <xsl:value-of select="../@type"/>
        </xsl:variable>

        <xsl:text>if(object.</xsl:text>
        <xsl:value-of select="../@name"/>
        <xsl:text> == null) {
            object.tmp</xsl:text>
        <xsl:value-of select="../@name"/>
        <xsl:text> = object.get</xsl:text>
        <xsl:value-of select="@processed-value"/>
        <xsl:text>ChildRelation();
        </xsl:text>
        <xsl:choose>
            <xsl:when test="../element[@type='javax.slee.ChildRelation']">
                <xsl:text>object.</xsl:text>
                <xsl:value-of select="../@name"/>
                <xsl:text> = tmp</xsl:text>
                <xsl:value-of select="../@name"/>
                <xsl:text>;
                </xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text>try{
                object.</xsl:text>
                <xsl:value-of select="../@name"/>
                <xsl:text> = (</xsl:text>
                <xsl:value-of select="../@type"/>
                <xsl:text>)object.tmp</xsl:text>
                <xsl:value-of select="../@name"/>
                <xsl:text>.create();
                }catch(javax.slee.CreateException x){
                    throw new RuntimeException(x);
                }
                </xsl:text>

            </xsl:otherwise>
        </xsl:choose>
        <xsl:text>
            }
        </xsl:text>

    </xsl:template>

    <xsl:template match="element[@kind='FIELD']/annotation[@name='javax.slee.annotation.ChildRelation']">
        <xsl:text>public abstract javax.slee.ChildRelation </xsl:text> 
        <xsl:value-of select="../@enclosing"/>
        <xsl:text>.get</xsl:text>
        <xsl:value-of select="@processed-value"/>
        <xsl:text>ChildRelation();
        </xsl:text>
        <xsl:text>javax.slee.ChildRelation </xsl:text> 
        <xsl:value-of select="../@enclosing"/>
        <xsl:text>.tmp</xsl:text>
        <xsl:value-of select="../@name"/>
        <xsl:text>= null;
        </xsl:text>
        <xsl:apply-templates select="." mode="get-field-pointcut"/>
    </xsl:template>

<!--CMP FIELD -->
    
    <xsl:template match="element[@kind='FIELD']/annotation[@name='javax.slee.annotation.CMPField']" mode="set-field-inject">
        <xsl:text>object.set</xsl:text>
        <xsl:value-of select="@processed-value"/>
        <xsl:text>(arg1); 
        </xsl:text>
    </xsl:template>

    <xsl:template match="element[@kind='FIELD']/annotation[@name='javax.slee.annotation.CMPField']" mode="get-field-inject">
    </xsl:template>

    <xsl:template match="element[@kind='FIELD']/annotation[@name='javax.slee.annotation.CMPField']">
        <xsl:apply-templates select="." mode="get-field-pointcut"/>
        <xsl:apply-templates select="." mode="set-field-pointcut"/>

        <xsl:text>
            public abstract </xsl:text>
        <xsl:value-of select="../@type"/>
        <xsl:text> </xsl:text> 
        <xsl:value-of select="../@enclosing"/>
        <xsl:text>.get</xsl:text>
        <xsl:value-of select="@processed-value"/>
        <xsl:text>();
        </xsl:text>
        <xsl:text>
            public abstract void </xsl:text>
        <xsl:value-of select="../@enclosing"/>
        <xsl:text>.set</xsl:text>
        <xsl:value-of select="@processed-value"/>
        <xsl:text>(</xsl:text>
        <xsl:value-of select="../@type"/>
        <xsl:text> arg1);
        </xsl:text>
    </xsl:template>
   
    <!-- JINDI &amp; FACILITIES -->

    <xsl:template match="element[@kind='FIELD']/annotation[@name='javax.annotation.Resource' and @type='javax.naming.InitialContext']" mode="get-field-inject" priority="1">
        <xsl:text>if(object.</xsl:text>
        <xsl:value-of select="../@name"/>
        <xsl:text> == null) {
        </xsl:text> 
        <xsl:text>object.</xsl:text><xsl:value-of select="../@name"/>
        <xsl:text>=object.__iContext</xsl:text>;
        <xsl:text>
            }
        </xsl:text>
    </xsl:template>

    <xsl:template match="element/annotation[@name='javax.annotation.Resource' and @type='javax.naming.InitialContext']" priority="1">
        <xsl:apply-templates select="." mode="get-field-pointcut"/>
    </xsl:template>

    <xsl:template match="element/annotation[@name='javax.annotation.Resource' and @type='javax.naming.Context']" priority="1">
        <xsl:variable name="name">
        <xsl:choose>
            <xsl:when test="element[@name='mappedName' and not(@value='')]">
                <xsl:value-of select="element[@name='mappedName']/@value"/>
            </xsl:when>
            <xsl:otherwise><xsl:text>"java:comp/env"</xsl:text></xsl:otherwise>
        </xsl:choose>
        </xsl:variable>

        <xsl:apply-templates select="." mode="get-field-jndi-pointcut">
            <xsl:with-param name="jndi-name" select="$name"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="element[@kind='field']/annotation[@name='javax.annotation.Resource' and @type='javax.slee.facilities.AlarmFacility']" priority="1">
        <xsl:apply-templates select="." mode="get-field-jndi-pointcut">
            <xsl:with-param name="jndi-name" select="concat(@type,'.JNDI_NAME')"/>
        </xsl:apply-templates>
    </xsl:template>
    

    <xsl:template match="element/annotation[@name='javax.annotation.Resource' and @type='javax.slee.nullactivity.NullActivityContextInterfaceFactory']" priority="1">
        <xsl:apply-templates select="." mode="get-field-jndi-pointcut">
            <xsl:with-param name="jndi-name" select="concat(@type,'.JNDI_NAME')"/>
        </xsl:apply-templates>
    </xsl:template>
    

    <xsl:template match="element/annotation[@name='javax.annotation.Resource' and @type='javax.slee.facilities.ActivityContextNamingFacility']" priority="1">
        <xsl:apply-templates select="." mode="get-field-jndi-pointcut">
            <xsl:with-param name="jndi-name" select="concat(@type,'.JNDI_NAME')"/>
        </xsl:apply-templates>
    </xsl:template>
    

    <xsl:template match="element[@kind='field']/annotation[@name='javax.annotation.Resource' and @type='javax.slee.nullactivity.NullActivityFactory']" priority="1">
        <xsl:apply-templates select="." mode="get-field-jndi-pointcut">
            <xsl:with-param name="jndi-name" select="concat(@type,'.JNDI_NAME')"/>
        </xsl:apply-templates>
    </xsl:template>
    

    <xsl:template match="element[@kind='field']/annotation[@name='javax.annotation.Resource' and @type='javax.slee.profile.ProfileFacility']" priority="1">
        <xsl:apply-templates select="." mode="get-field-jndi-pointcut">
            <xsl:with-param name="jndi-name" select="concat(@type,'.JNDI_NAME')"/>
        </xsl:apply-templates>
    </xsl:template>
    

    <xsl:template match="element[@kind='field']/annotation[@name='javax.annotation.Resource' and @type='javax.slee.profile.ProfileTableActivityContextInterfaceFactory']" priority="1">
        <xsl:apply-templates select="." mode="get-field-jndi-pointcut">
            <xsl:with-param name="jndi-name" select="concat(@type,'.JNDI_NAME')"/>
        </xsl:apply-templates>
    </xsl:template>
    

    <xsl:template match="element[@kind='field']/annotation[@name='javax.annotation.Resource' and @type='javax.slee.serviceactivity.ServiceActivityContextInterfaceFactory']" priority="1">
        <xsl:apply-templates select="." mode="get-field-jndi-pointcut">
            <xsl:with-param name="jndi-name" select="concat(@type,'.JNDI_NAME')"/>
        </xsl:apply-templates>
    </xsl:template>
    

    <xsl:template match="element[@kind='field']/annotation[@name='javax.annotation.Resource' and @type='javax.slee.serviceactivity.ServiceActivityFactory']" priority="1">
        <xsl:apply-templates select="." mode="get-field-jndi-pointcut">
            <xsl:with-param name="jndi-name" select="concat(@type,'.JNDI_NAME')"/>
        </xsl:apply-templates>
    </xsl:template>
    

    <xsl:template match="element[@kind='field']/annotation[@name='javax.annotation.Resource' and @type='javax.slee.facilities.TimerFacility']" priority="1">
        <xsl:apply-templates select="." mode="get-field-jndi-pointcut">
            <xsl:with-param name="jndi-name" select="concat(@type,'.JNDI_NAME')"/>
        </xsl:apply-templates>
    </xsl:template>
    
    <!--EJB -->

    <xsl:template match="element/annotation[@name='javax.annotation.Resource' and @type='org.mobicents.slee.annotations.examples.ejb.SomeEJBHome']" priority="1">
        <xsl:apply-templates select="." mode="get-field-jndi-pointcut">
            <xsl:with-param name="jndi-name" select="concat('&quot;',element[@name='name']/@value,'&quot;')"/>
        </xsl:apply-templates>
    </xsl:template>
 
    <xsl:template match="element/annotation[@name='javax.annotation.Resource' and @type='org.mobicents.slee.annotations.examples.ejb.SomeEJBRemote']">
        <!--
       <xsl:text> (</xsl:text>
       <xsl:value-of select="../@type"/><xsl:text> ) lookup home - create();</xsl:text>
       -->
    </xsl:template>

    <!--ENV ENTRIES-->

    
    <xsl:template match="element/annotation[@name='javax.annotation.Resource' and @type='org.mobicents.slee.annotations.examples.resource.ExampleResourceAdaptorSbbInterface']" priority="1">
        <xsl:apply-templates select="." mode="get-field-jndi-pointcut">
            <xsl:with-param name="jndi-name" select="concat('&quot;',element[@name='name']/@value,'&quot;')"/>
        </xsl:apply-templates>
    </xsl:template>
    

    <!--RA BINDING -->
    <xsl:template match="element/annotation[@name='javax.annotation.Resource' and @type='org.mobicents.slee.annotations.examples.resource.ExampleActivityContextInterfaceFactory']" priority="1">
        <xsl:apply-templates select="." mode="get-field-jndi-pointcut">
            <xsl:with-param name="jndi-name" select="concat('&quot;',element[@name='name']/@value,'&quot;')"/>
        </xsl:apply-templates>
    </xsl:template>
 
    <!-- CONTEXT -->
    <xsl:template match="element[@kind='FIELD']/annotation[@name='javax.annotation.Resource' and (@type='javax.slee.SbbContext' or @type='javax.slee.resource.ResourceAdaptorContext' or @type='javax.slee.profile.ProfileContext')]" mode="get-field-inject" priority="1">
     <xsl:text>if(object.</xsl:text>
        <xsl:value-of select="../@name"/>
        <xsl:text> == null) { </xsl:text> 
        <xsl:text>object.</xsl:text><xsl:value-of select="../@name"/>
        <xsl:text>=object.__context;</xsl:text>
        <xsl:text>
            }
        </xsl:text>
        
    </xsl:template>

    <xsl:template match="element[@kind='FIELD']/annotation[@name='javax.annotation.Resource' and @type='javax.slee.SbbContext']" priority="1">
        //set sbb context
        <xsl:apply-templates select="." mode="get-field-pointcut"/>
    </xsl:template>
    
    <xsl:template match="element/annotation[@name='javax.annotation.Resource' and @type='javax.slee.resource.ResourceAdaptorContext']" priority="1">
        //set ra context
        <xsl:apply-templates select="." mode="get-field-pointcut"/>
    </xsl:template>

    <xsl:template match="element[@kind='FIELD']/annotation[@name='javax.annotation.Resource' and @type='javax.slee.profile.ProfileContext']" priority="1">
        //set profile context
        <xsl:apply-templates select="." mode="get-field-pointcut"/>
    </xsl:template>

    <xsl:template match="element/annotation[@name='javax.annotation.Resource' and @type='org.mobicents.slee.annotations.examples.sbb.ExampleSbbLocalObject']" priority="1">
        //sbb local object
    </xsl:template>
    <!--TRACER-->

    <xsl:template match="element[@kind='FIELD']/annotation[@name='javax.annotation.Resource' and @type='javax.slee.facilities.Tracer']" mode="get-field-inject" priority="1">
        <xsl:text>if(object.</xsl:text>
        <xsl:value-of select="../@name"/>
        <xsl:text> == null) {
        System.err.println("constructing tracer");
        </xsl:text> 
        <xsl:text>object.</xsl:text><xsl:value-of select="../@name"/>
        <xsl:text>=object.__context.getTracer("</xsl:text>
        <xsl:value-of select="element/@name"/>");
        <xsl:text>
            }
        </xsl:text>
    </xsl:template>
    <xsl:template match="element[@kind='FIELD']/annotation[@name='javax.annotation.Resource' and @type='javax.slee.facilities.Tracer']" priority="1">
        //is tracer from ra or sbb or profile context
        <xsl:apply-templates select="." mode="get-field-pointcut"/>
    </xsl:template>

<!--CALCULATED -->
    <xsl:template match="element/annotation[@name='javax.annotation.Resource' and @type='org.mobicents.slee.annotations.examples.sbb.ExampleSbbLocalObject']" priority="1">
        //profile local object
    </xsl:template>

<!--CONFIG PROPERTIES -->

    <xsl:template match="element[@kind='FIELD' and @type='javax.slee.resource.ConfigProperties']/annotation[@name='javax.annotation.Resource']" mode="get-field-inject" priority="1">
        <xsl:text>object.</xsl:text>
        <xsl:value-of select="../@name"/>
        <xsl:text> = object.__configProperties;
        </xsl:text>
    </xsl:template>

    <xsl:template match="element[@kind='FIELD' and @type='javax.slee.resource.ConfigProperties']/annotation[@name='javax.annotation.Resource']" priority="1">
        <xsl:apply-templates select="." mode="get-field-pointcut"/>
    </xsl:template>
 
<!-- INJECT GETTER -->

<!--CONFIG PROPERTY -->

    <xsl:template match="//element[@kind='FIELD']/annotation[@name='javax.slee.annotation.ConfigProperty']" mode="get-field-inject" priority="1">
        <xsl:text>object.</xsl:text>
        <xsl:value-of select="../@name"/>
        <xsl:text> = (</xsl:text>
        <xsl:value-of select="../@type"/>
        <xsl:text>)object.__configProperties.getProperty("</xsl:text>
        <xsl:value-of select="../@name"/><xsl:text>").getValue();</xsl:text>
    </xsl:template>

    <xsl:template match="//element[@kind='FIELD']/annotation[@name='javax.slee.annotation.ConfigProperty']" priority="1">
        <xsl:apply-templates select="." mode="get-field-pointcut"/>
    </xsl:template>

    <!-- inject from slee or dd -->
    
    <xsl:template match="element[@kind='FIELD']/annotation[@name='javax.annotation.Resource']" mode="get-field-inject" priority="0">

        <xsl:variable name="name" select="../@name" />
        <xsl:variable name="type" select="@type" />
        <xsl:variable name="enclosing" select="../@enclosing" />
    <!--USAGE PARAMETERS -->

        <xsl:choose>
        <xsl:when test="element[@name ='name' and @value=''] and //element[@name = $enclosing and annotation[(@name='javax.slee.annotation.Sbb' or @name='javax.slee.annotation.ProfileSpec') and element[@name ='usageParametersInterface' and @value=$type]]]">

        <xsl:text>if(object.</xsl:text>
        <xsl:value-of select="../@name"/>
        <xsl:text> == null) {
        </xsl:text> 
        <xsl:text>object.</xsl:text><xsl:value-of select="../@name"/>
        <xsl:text>=(</xsl:text>
        <xsl:value-of select="../@type"/>
        <xsl:text>) object.getDefault</xsl:text>
       <xsl:choose>
            <xsl:when test="//element[@name = $enclosing]/annotation[@name='javax.slee.annotation.Sbb']">
<xsl:text>Sbb</xsl:text>
            </xsl:when>
        </xsl:choose><xsl:text>UsageParameterSet();
            }
        </xsl:text>
        </xsl:when>

    <!--USAGE PARAMETERS -->
        <xsl:when test="element[@name ='name' and not(@value ='')] and //element[@name = $enclosing and annotation[(@name='javax.slee.annotation.Sbb' or @name='javax.slee.annotation.ProfileSpec') and element[@name ='usageParametersInterface' and @value=$type]]]">
        
        <xsl:text>if(object.</xsl:text>
        <xsl:value-of select="../@name"/>
        <xsl:text> == null) {
        try{
        </xsl:text> 
        <xsl:text>object.</xsl:text><xsl:value-of select="../@name"/>
        <xsl:text>=(</xsl:text>
        <xsl:value-of select="../@type"/>
        <xsl:text>) object.get</xsl:text>
       <xsl:choose>
            <xsl:when test="//element[@name = $enclosing]/annotation[@name='javax.slee.annotation.Sbb']">
<xsl:text>Sbb</xsl:text>
            </xsl:when>
        </xsl:choose><xsl:text>UsageParameterSet("</xsl:text>
        <xsl:value-of select="element[@name='name']/@value"/><xsl:text>");
        }catch(javax.slee.usage.UnrecognizedUsageParameterSetNameException x){
        throw new RuntimeException(x);
        }
            }
        </xsl:text>
        </xsl:when>

        <!--RA -->

    <!--USAGE PARAMETERS -->
        <xsl:when test="element[@name ='name' and @value=''] and //element[@name = $enclosing and annotation[@name='javax.slee.annotation.ResourceAdaptor' and element[@name ='usageParametersInterface' and @value=$type]]]">
        <xsl:text>if(object.</xsl:text>
        <xsl:value-of select="../@name"/>
        <xsl:text> == null) {
        </xsl:text> 
        <xsl:text>object.</xsl:text><xsl:value-of select="../@name"/>
        <xsl:text>=(</xsl:text>
        <xsl:value-of select="../@type"/>
        <xsl:text>) object.__context.getDefaultUsageParameterSet();
            }
        </xsl:text>
        </xsl:when>

    <!--USAGE PARAMETERS -->
        <xsl:when test="element[@name ='name' and not(@value ='')] and //element[@name = $enclosing and annotation[@name='javax.slee.annotation.ResourceAdaptor' and element[@name ='usageParametersInterface' and @value=$type]]]">
        <xsl:text>if(object.</xsl:text>
        <xsl:value-of select="../@name"/>
        <xsl:text> == null) {
        try{
        </xsl:text> 
        <xsl:text>object.</xsl:text><xsl:value-of select="../@name"/>
        <xsl:text>=(</xsl:text>
        <xsl:value-of select="../@type"/>
        <xsl:text>) object.__context.getUsageParameterSet("</xsl:text>
        <xsl:value-of select="element[@name='name']/@value"/><xsl:text>");
        }catch(javax.slee.usage.UnrecognizedUsageParameterSetNameException x){
        throw new RuntimeException(x);
        }
            }
        </xsl:text>
        
        </xsl:when>    
        </xsl:choose>
    </xsl:template>

<!-- RESOURCE GETTERS -->
    
    <xsl:template match="element[@kind='FIELD']/annotation[@name='javax.annotation.Resource']" >
        <xsl:variable name="type" select="@type" />
        <xsl:variable name="name" select="element[@name='name']/@value" />
        <xsl:variable name="enclosing" select="../@enclosing" />
        <xsl:choose>

<!-- USAGE PARAMETERS -->
        <xsl:when test="element[@name ='name' and @value=''] and //element[@name = $enclosing and annotation[(@name='javax.slee.annotation.Sbb' or @name='javax.slee.annotation.ProfileSpec') and element[@name ='usageParametersInterface' and @value=$type]]]">
        //get usage parameter method
        <xsl:text>public abstract </xsl:text>
        <xsl:value-of select="../@type"/><xsl:text> </xsl:text> 
        <xsl:value-of select="../@enclosing"/><xsl:text>.getDefault</xsl:text>
        <xsl:choose>
            <xsl:when test="//element[@name = $enclosing]/annotation[@name='javax.slee.annotation.Sbb']">
<xsl:text>Sbb</xsl:text>
            </xsl:when>
        </xsl:choose><xsl:text>UsageParameterSet();</xsl:text>

        <xsl:apply-templates select="." mode="get-field-pointcut"/>
    </xsl:when>

    <xsl:when test="element[@name ='name' and not(@value ='')] and //element[@name = $enclosing and annotation[(@name='javax.slee.annotation.Sbb' or @name='javax.slee.annotation.ProfileSpec') and element[@name ='usageParametersInterface' and @value=$type]]]">
        //get named usage parameter method
        <xsl:text>public abstract </xsl:text>
        <xsl:value-of select="../@type"/><xsl:text> </xsl:text> 
        <xsl:value-of select="../@enclosing"/><xsl:text>.get</xsl:text>
        <xsl:choose>
            <xsl:when test="//element[@name = $enclosing]/annotation[@name='javax.slee.annotation.Sbb']">
<xsl:text>Sbb</xsl:text>
            </xsl:when>
        </xsl:choose><xsl:text>UsageParameterSet( String name) throws javax.slee.usage.UnrecognizedUsageParameterSetNameException;
        </xsl:text>
        <xsl:apply-templates select="." mode="get-field-pointcut"/>
    </xsl:when>

    <xsl:when test="element[@name ='name' and not(@value ='')] and //element[@name = $enclosing and annotation[@name='javax.slee.annotation.ResourceAdaptor' and element[@name ='usageParametersInterface' and @value=$type]]]">
        <xsl:apply-templates select="." mode="get-field-pointcut"/>
    </xsl:when>

    <xsl:when test="element[@name ='name' and @value=''] and //element[@name = $enclosing and annotation[@name='javax.slee.annotation.ResourceAdaptor' and element[@name ='usageParametersInterface' and @value=$type]]]">
        //ra get up from context
        <xsl:apply-templates select="." mode="get-field-pointcut"/>
    </xsl:when>

    <xsl:when test="element[@name ='name' and not(@value ='')] and //element[@name = $enclosing and annotation[@name='javax.slee.annotation.ResourceAdaptor' and element[@name ='usageParametersInterface' and @value=$type]]]">
        //ra get named up from context
            <xsl:apply-templates select="." mode="get-field-pointcut"/>
    </xsl:when>

<!-- SBB RA ENTITY LINK -->

    <xsl:when test="//element[@name=$enclosing]/annotation[@name ='javax.slee.annotation.Sbb']/element[@name='resourceAdaptorTypeBinding']/annotation[@name='javax.slee.annotation.ResourceAdaptorTypeBinding' and element[@name='raEntityLink' and @value=$name]]">
            //OVER HERE
            <xsl:variable name="ref" select="//element[@enclosing=$enclosing]/annotation[@name ='javax.slee.annotation.Sbb']/element[@name='resourceAdaptorTypeBinding']/annotation[@name='javax.slee.annotation.ResourceAdaptorTypeBinding' and element[@name='raEntityLink' and @value=$name]]" />
            <xsl:value-of select="$ref"/>
            //THATS IT
<!--
            <xsl:apply-templates select="." mode="get-field-jndi-pointcut">

            <xsl:with-param name="jndi-name" select="concat('slee/resources/',generate-id(key('raEntityLink',concat(element/annotation[@name='javax.slee.annotation.ResourceAdaptorTypeRef']/element[@name='name']/@value,element/annotation[@name='javax.slee.annotation.ResourceAdaptorTypeRef']/element[@name='vendor']/@value,element/annotation[@name='javax.slee.annotation.ResourceAdaptorTypeRef']/element[@name='version']/@value))))"/>
            
                        </xsl:apply-templates>
                -->

            </xsl:when>
            <!--CONFIG PROPERTIES-->
            <xsl:otherwise>

                <!--JNDI PARAMETERS -->
                <xsl:apply-templates select="." mode="get-field-jndi-pointcut">
                    <!--NAME/MAPPED_NAME-->
                    <xsl:with-param name="jndi-name" select="concat('&quot;',element[@name='name']/@value,'&quot;')"/>
                </xsl:apply-templates>
            </xsl:otherwise>

        </xsl:choose>
    </xsl:template>

    <!--Alarms & Custom-->

    <xsl:template match="element[@kind='CLASS']/annotation[@name='javax.slee.annotation.Rollback']">
        context.setRollbackOnly();
    </xsl:template>

    <xsl:template match="element[@kind='METHOD']/annotation[@name='javax.slee.annotation.RaiseAlarm']">
    </xsl:template>

    <xsl:template match="element[@kind='METHOD']/annotation[@name='javax.slee.annotation.ClearAlarm']">
    </xsl:template>

    <!--METHODS -->
    <xsl:template match="methods" >
        <xsl:apply-templates select="node()" />
    </xsl:template>

    <xsl:template match="method" >
        <xsl:value-of select="@name"/>
        <xsl:text> 
        </xsl:text>
    </xsl:template>















    <!--POINTCUTS -->

    <xsl:template match="annotation" mode="method-intercept-pointcut">
        <xsl:param name="arg-type"/>
        <xsl:param name="method-name"/>

        <xsl:text>
            pointcut method_</xsl:text>
        <xsl:value-of select="generate-id(key('unique-method',../@enclosing))"/>
        <xsl:value-of select="$method-name"/>
        <xsl:text>(</xsl:text>
        <xsl:value-of select="../@enclosing"/>
        <xsl:text> object,</xsl:text>
        <xsl:value-of select="$arg-type"/>
        <xsl:text> arg1) :target(object) &amp;&amp; execution(void </xsl:text>
        <xsl:value-of select="../@enclosing"/>
        <xsl:text>+.</xsl:text>
        <xsl:value-of select="$method-name"/>
        <xsl:text>(</xsl:text>
        <xsl:value-of select="$arg-type"/>
        <xsl:text>+)) &amp;&amp; args(arg1);</xsl:text>

        <xsl:text>
            void around( 
        </xsl:text>
        <xsl:value-of select="../@enclosing"/>
        <xsl:text> object, </xsl:text>
        <xsl:value-of select="$arg-type"/>
        <xsl:text> arg1) : method_</xsl:text>
        <xsl:value-of select="generate-id(key('unique-method',../@enclosing))"/>
        <xsl:value-of select="$method-name"/>
        <xsl:text>(object,arg1) { </xsl:text>
        System.err.println("advice ###### around ");
        <xsl:apply-templates select="." mode="method-intercept-inject">
            <xsl:with-param name="method-name" select="$method-name"/>
            <xsl:with-param name="arg-type" select="$arg-type"/>
        </xsl:apply-templates>

        <xsl:text>
            proceed(object,arg1);
            }
        </xsl:text>
    </xsl:template>

    <xsl:template match="annotation" mode="set-field-pointcut">
        <xsl:text>
            pointcut set</xsl:text>
        <xsl:value-of select="../@name"/>
        <xsl:value-of select="generate-id(key('unique-field',concat(../@name,../@enclosing)))"/>
        <xsl:text>Field(</xsl:text>
        <xsl:value-of select="../@enclosing"/>
        <xsl:text> object,</xsl:text>
        <xsl:value-of select="../@type"/>
        <xsl:text> l) :target(object) &amp;&amp; set(</xsl:text>
        <xsl:value-of select="../@type"/>
        <xsl:text> </xsl:text> 
        <xsl:value-of select="../@enclosing"/>
        <xsl:text>.</xsl:text>
        <xsl:value-of select="../@name"/>
        <xsl:text>) &amp;&amp; args(l) &amp;&amp; !within(</xsl:text>
        <xsl:value-of select="../@enclosing"/>$SleeAnnotationsAspect<xsl:text>) &amp;&amp; !within(</xsl:text>
        <xsl:value-of select="../@enclosing"/>$DebugAspect<xsl:text>);
        </xsl:text>
        <!--
        &amp;&amp; !withincode(void </xsl:text>
        <xsl:value-of select="../@enclosing"/>
        <xsl:text>.set</xsl:text>
        <xsl:value-of select="../@name"/>
        <xsl:text>Field(</xsl:text>
        <xsl:value-of select="../@type"/>
        <xsl:text>));
        -->

        <xsl:text>
            after( </xsl:text>
        <xsl:value-of select="../@enclosing"/>
        <xsl:text> object, </xsl:text>
        <xsl:value-of select="../@type"/>
        <xsl:text> arg1) : set</xsl:text>
        <xsl:value-of select="../@name"/>
        <xsl:value-of select="generate-id(key('unique-field',concat(../@name,../@enclosing)))"/>
        <xsl:text>Field(object,arg1) {</xsl:text>
        <xsl:text>
            //System.err.println("advice ###### setter " + arg1);
        </xsl:text>
        <xsl:apply-templates select="." mode="set-field-inject" />
        <xsl:text>}
        </xsl:text>
    </xsl:template>

    <xsl:template match="annotation" mode="get-field-pointcut">
        <xsl:text>
            pointcut get</xsl:text>
        <xsl:value-of select="generate-id(key('unique-field',concat(../@name,../@enclosing)))"/>
        <xsl:value-of select="../@name"/>
        <xsl:text>Field(</xsl:text>
        <xsl:value-of select="../@enclosing"/>
        <xsl:text> object) :target(object) &amp;&amp; get(</xsl:text>
        <xsl:value-of select="../@type"/>
        <xsl:text> </xsl:text>
        <xsl:value-of select="../@enclosing"/>
        <xsl:text>.</xsl:text>
        <xsl:value-of select="../@name"/>
        <xsl:text>) &amp;&amp; !within(</xsl:text>
        <xsl:value-of select="../@enclosing"/>$SleeAnnotationsAspect<xsl:text>) &amp;&amp; !within(</xsl:text>
        <xsl:value-of select="../@enclosing"/>$DebugAspect<xsl:text>);
        </xsl:text>

        <xsl:text>
            before( </xsl:text>
        <xsl:value-of select="../@enclosing"/>
        <xsl:text> object) : get</xsl:text>
        <xsl:value-of select="generate-id(key('unique-field',concat(../@name,../@enclosing)))"/>
        <xsl:value-of select="../@name"/>
        <xsl:text>Field(object) {</xsl:text>
        <xsl:text>
            //System.err.println("advice ###### getter ");
        </xsl:text>
        <xsl:apply-templates select="." mode="get-field-inject" />
        <xsl:text>
            }
        </xsl:text>
    </xsl:template>

    <xsl:template match="annotation" mode="get-field-jndi-pointcut">
        <xsl:param name="jndi-name"/>
        <xsl:text>
            pointcut get</xsl:text>
        <xsl:value-of select="generate-id(key('unique-field',concat(../@name,../@enclosing)))"/>
        <xsl:value-of select="../@name"/>
        <xsl:text>FieldJNDI(</xsl:text>
        <xsl:value-of select="../@enclosing"/>
        <xsl:text> object) :target(object) &amp;&amp; get(</xsl:text>
        <xsl:value-of select="../@type"/>
        <xsl:text> </xsl:text>
        <xsl:value-of select="../@enclosing"/>
        <xsl:text>.</xsl:text>
        <xsl:value-of select="../@name"/>
        <xsl:text>);
        </xsl:text>

        <xsl:text>
            before( </xsl:text>
        <xsl:value-of select="../@enclosing"/>
        <xsl:text> object) : get</xsl:text>
        <xsl:value-of select="generate-id(key('unique-field',concat(../@name,../@enclosing)))"/>
        <xsl:value-of select="../@name"/>
        <xsl:text>FieldJNDI(object) {</xsl:text>
        <xsl:text>
            //System.err.println("advice ###### jndi getter wrapper");
        </xsl:text>
        <xsl:text>if(object.</xsl:text>
        <xsl:value-of select="../@name"/>
        <xsl:text> == null) {
            try{
            object.</xsl:text>
        <xsl:value-of select="../@name"/>
        <xsl:text>=(</xsl:text>
        <xsl:value-of select="../@type"/>
        <xsl:text>)new javax.naming.InitialContext().lookup(</xsl:text>
        <xsl:value-of select="$jndi-name"/>
        <xsl:text>);
            }catch(javax.naming.NamingException x){
            throw new RuntimeException(x);
            }
            }
            }
        </xsl:text>
    </xsl:template>

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
