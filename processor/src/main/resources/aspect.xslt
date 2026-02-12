<?xml version="1.0" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output omit-xml-declaration="yes" method="text" indent="yes" />

    <xsl:strip-space elements="node()" />

    <xsl:key name="unique-field" match="element[@kind='FIELD']" use="concat(@name,@enclosing)" />
    <xsl:key name="unique-method-private" match="element[@kind='METHOD']" use="concat(@name,@enclosing)" />

    <xsl:key name="unique-method" match="element[@kind='FIELD' and annotation[@name=$ANN_RESOURCE]]" use="@enclosing" />
    <xsl:key name="unique-aspect" match="element[@package]" use="@package" />

    <xsl:param name="slee_debug" />

    <xsl:template match="/process">
        <xsl:apply-templates select="element[@name and annotation[@package]]" mode="aspect" />
    </xsl:template>

    <xsl:template match="element[@name and annotation[@package]]" mode="aspect">
        <xsl:variable name="name" select="@name" />
        <xsl:text>file:</xsl:text>
        <xsl:value-of select="annotation/@package" />/<xsl:value-of select="annotation/@simple-name" />
        <xsl:text>$SleeAnnotationsAspect.aj
        </xsl:text>
        <xsl:if test="string-length(annotation/@package)>0">
            <xsl:text>
                package </xsl:text>
            <xsl:value-of select="annotation/@package" />
            <xsl:text>;</xsl:text>
        </xsl:if>

        <xsl:text>
            import javax.slee.facilities.ActivityContextNamingFacility;
            import javax.slee.facilities.AlarmFacility;
            import javax.slee.facilities.AlarmLevel;
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


            public privileged aspect </xsl:text>
        <xsl:value-of select="annotation/@simple-name" />
        <xsl:text>$SleeAnnotationsAspect issingleton() {

        </xsl:text>
        <xsl:apply-templates select="//classtypes[@enclosing = $name]" mode="out" />
        <!--TODO: add this class if abstractClass is missing -->
        <!--
    <xsl:if test="boolean(@implements)">
        <xsl:choose>
            <xsl:when test="annotation/@name=$ANN_PROFILESPEC and annotation/element[@name='abstractClass']/@value = 'javax.slee.profile.Profile'" >
            </xsl:when>
            <xsl:otherwise>
                <xsl:text> declare parents : </xsl:text>
                <xsl:value-of select="annotation/@simple-name"/>
                <xsl:text> implements </xsl:text>
                <xsl:value-of select="@interface"/>
                <xsl:text>; </xsl:text>


            </xsl:otherwise>
        </xsl:choose>
    </xsl:if>
        -->

        <xsl:apply-templates select="//*[@enclosing = $name]" />

        <xsl:text>
            }
        </xsl:text>

        <xsl:if test="$slee_debug = 'true'">

            <xsl:text>file:</xsl:text>
            <xsl:value-of select="annotation/@package" />/<xsl:value-of select="annotation/@simple-name" />
            <xsl:text>$DebugAspect.aj
            </xsl:text>

            <xsl:if test="string-length(annotation/@package)>0">
                <xsl:text>
                    package </xsl:text>
                <xsl:value-of select="annotation/@package" />
                <xsl:text>;</xsl:text>
            </xsl:if>


            <xsl:text>
                privileged aspect </xsl:text>
            <xsl:value-of select="annotation/@simple-name" />
            <xsl:text>$DebugAspect {
                pointcut debug_class(): within(</xsl:text>
            <xsl:value-of select="annotation/@name" />
            <xsl:text>) &amp;&amp; !within(</xsl:text>
            <xsl:value-of select="annotation/@package" />.<xsl:value-of select="annotation/@simple-name" />
            <xsl:text>$DebugAspect) &amp;&amp; !within(</xsl:text>
            <xsl:value-of select="annotation/@package" />.<xsl:value-of select="annotation/@simple-name" />
            <xsl:text>$SleeAnnotationsAspect);

                pointcut debug_constructor(): debug_class() &amp;&amp; execution(new(..));
                pointcut debug_method(): debug_class() &amp;&amp; execution(* *(..));
                pointcut debug_set(): debug_class() &amp;&amp; (set(* *.*));
                pointcut debug_get(): debug_class() &amp;&amp; (get(* *.*));

                before (): debug_constructor() {
                if(Boolean.getBoolean("slee.annotation.debug")) System.err.println("={= " + thisJoinPointStaticPart.getSignature());
                }
                after(): debug_constructor() {
                if(Boolean.getBoolean("slee.annotation.debug")) System.err.println("=}=} " + thisJoinPointStaticPart.getSignature());
                }

                before (): debug_method() {
                if(Boolean.getBoolean("slee.annotation.debug")) System.err.println("={= " + thisJoinPointStaticPart.getSignature());
                }
                after() returning : debug_method() {
                if(Boolean.getBoolean("slee.annotation.debug")) System.err.println("=}= " + thisJoinPointStaticPart.getSignature());
                }
                after() throwing : debug_method() {
                if(Boolean.getBoolean("slee.annotation.debug")) System.err.println("=*= " + thisJoinPointStaticPart.getSignature());
                }
                after(): debug_set() {
                if(Boolean.getBoolean("slee.annotation.debug")) System.err.println("=w= " + thisJoinPointStaticPart.getSignature());
                }
                after(): debug_get() {
                if(Boolean.getBoolean("slee.annotation.debug")) System.err.println("=r= " + thisJoinPointStaticPart.getSignature());
                }

                }
            </xsl:text>
        </xsl:if>
    </xsl:template>
    <!--
    <xsl:template match="/process//element/annotation[@name=$ANN_RESOURCE]">
        xsl:template match="element/annotation[@name=$ANN_RESOURCE and @type='<xsl:value-of select="@type" />']"
        /xsl:template
    </xsl:template>
    -->

    <xsl:template match="node()[@name]" mode="return-name">
        <xsl:text>
            return object.</xsl:text>
        <xsl:value-of select="@name" />
        <xsl:text>;</xsl:text>
        <xsl:text>
        </xsl:text>
    </xsl:template>
    <!-- SLEE Abstract class methods -->

    <xsl:template
        match="element[@kind='CLASS']/annotation[@name=$ANN_SBB or @name=$ANN_PROFILESPEC or @name=$ANN_RESOURCEADAPTOR]"
        mode="method-intercept-inject"
    >
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

    <xsl:template match="element[@kind='CLASS']/annotation[@name=$ANN_SBB]" priority="1">
        /*
        Sbb
        */
        <xsl:text>javax.slee.SbbContext </xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:text>.__context;
        </xsl:text>
        <xsl:text>javax.naming.InitialContext </xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:text>.__iContext;
        </xsl:text>
        <xsl:text>javax.slee.facilities.AlarmFacility </xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:text>.__alarmFacility;
        </xsl:text>

        <xsl:apply-templates select="." mode="method-intercept-pointcut">
            <xsl:with-param name="arg-type" select="'javax.slee.SbbContext'" />
            <xsl:with-param name="method-name" select="'setSbbContext'" />
        </xsl:apply-templates>

        <xsl:variable name="enclosing" select="../@enclosing" />
        <xsl:apply-templates select="//methods[@enclosing = $enclosing]" mode="out" />
        <xsl:apply-templates select="//element[@kind='METHOD' and @enclosing = $enclosing]" mode="out" />

    </xsl:template>

    <xsl:template match="element[@kind='CLASS']/annotation[@name=$ANN_PROFILESPEC]">
        /*
        ProfileSpec
        */
        <xsl:text>javax.slee.profile.ProfileContext </xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:text>.__context;</xsl:text>
        <xsl:text>javax.naming.InitialContext </xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:text>.__iContext;</xsl:text>
        <xsl:text>javax.slee.facilities.AlarmFacility </xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:text>.__alarmFacility;</xsl:text>
        <xsl:apply-templates select="." mode="method-intercept-pointcut">
            <xsl:with-param name="method-name" select="'setProfileContext'" />
            <xsl:with-param name="arg-type" select="'javax.slee.profile.ProfileContext'" />
        </xsl:apply-templates>

        <xsl:variable name="enclosing" select="../@enclosing" />
        <xsl:apply-templates select="//methods[@enclosing = $enclosing]" mode="out" />
    </xsl:template>

    <xsl:template match="element[@kind='CLASS']/annotation[@name=$ANN_RESOURCEADAPTOR]" mode="method-intercept-inject" priority="1">

        <xsl:param name="arg-type" />
        <xsl:param name="method-name" />

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

    <xsl:template match="element[@kind='CLASS']/annotation[@name=$ANN_RESOURCEADAPTOR]" priority="1">

        /*
        RA
        */
        <xsl:text>javax.slee.resource.ResourceAdaptorContext </xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:text>.__context;
        </xsl:text>
        <xsl:text>javax.naming.InitialContext </xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:text>.__iContext;</xsl:text>
        <xsl:text>javax.slee.resource.ConfigProperties </xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:text>.__configProperties;</xsl:text>
        <xsl:text>javax.slee.facilities.AlarmFacility </xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:text>.__alarmFacility;</xsl:text>

        <xsl:apply-templates select="." mode="method-intercept-pointcut">
            <xsl:with-param name="method-name" select="'setResourceAdaptorContext'" />
            <xsl:with-param name="arg-type" select="'javax.slee.resource.ResourceAdaptorContext'" />
        </xsl:apply-templates>

        <xsl:apply-templates select="." mode="method-intercept-pointcut">
            <xsl:with-param name="arg-type" select="'javax.slee.resource.ConfigProperties'" />
            <xsl:with-param name="method-name" select="'raConfigure'" />
        </xsl:apply-templates>

        <xsl:variable name="enclosing" select="../@enclosing" />

    </xsl:template>

    <xsl:template match="element[@kind='FIELD']/annotation[@name=$ANN_CHILDRELATION]" mode="get-field-inject">
        <xsl:variable name="sbbLocalObject">
            <xsl:value-of select="../@type" />
        </xsl:variable>

        <xsl:text>if(object.</xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:text> == null) {
            object.tmp</xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:text> = object.get</xsl:text>
        <xsl:value-of select="@processed-value" />
        <xsl:text>ChildRelation();
        </xsl:text>
        <xsl:choose>
            <xsl:when test="../@type=$TYPE_CHILDRELATION">
                <xsl:text>object.</xsl:text>
                <xsl:value-of select="../@name" />
                <xsl:text> = object.tmp</xsl:text>
                <xsl:value-of select="../@name" />
                <xsl:text>;
                </xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text>try{
                    object.</xsl:text>
                <xsl:value-of select="../@name" />
                <xsl:text> = (</xsl:text>
                <xsl:value-of select="../@type" />
                <xsl:text>)object.tmp</xsl:text>
                <xsl:value-of select="../@name" />
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

        <xsl:apply-templates select=".." mode="return-name" />
    </xsl:template>

    <xsl:template match="element[@kind='FIELD']/annotation[@name=$ANN_CHILDRELATION]">
        <xsl:text>public abstract javax.slee.ChildRelation </xsl:text>
        <xsl:value-of select="../@enclosing" />
        <xsl:text>.get</xsl:text>
        <xsl:value-of select="@processed-value" />
        <xsl:text>ChildRelation();
        </xsl:text>
        <xsl:text>javax.slee.ChildRelation </xsl:text>
        <xsl:value-of select="../@enclosing" />
        <xsl:text>.tmp</xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:text>= null;
        </xsl:text>
        <xsl:apply-templates select="." mode="get-field-pointcut" />
    </xsl:template>

    <!--CMP FIELD -->

    <xsl:template match="element[@kind='FIELD']/annotation[@name=$ANN_CMPFIELD]" mode="set-field-inject">
        <xsl:text>object.set</xsl:text>
        <xsl:value-of select="@processed-value" />
        <xsl:text>(arg1);
        </xsl:text>
    </xsl:template>

    <xsl:template match="element[@kind='FIELD']/annotation[@name=$ANN_CMPFIELD]" mode="get-field-inject">
        <xsl:text>
            return (object.</xsl:text>
        <xsl:value-of select="../@name" /> = <xsl:text>object.get</xsl:text>
        <xsl:value-of select="@processed-value" />
        <xsl:text>());
        </xsl:text>
    </xsl:template>

    <xsl:template match="element[@kind='FIELD']/annotation[@name=$ANN_CMPFIELD]">

        //TODO add debug advice around added abstracts
        <xsl:text>
            public abstract </xsl:text>
        <xsl:value-of select="../@type" />
        <xsl:text> </xsl:text>
        <xsl:value-of select="../@enclosing" />
        <xsl:text>.get</xsl:text>
        <xsl:value-of select="@processed-value" />
        <xsl:text>() ;
        </xsl:text>
        <xsl:text>
            public abstract void </xsl:text>
        <xsl:value-of select="../@enclosing" />
        <xsl:text>.set</xsl:text>
        <xsl:value-of select="@processed-value" />
        <xsl:text>(</xsl:text>
        <xsl:value-of select="../@type" />
        <xsl:text> arg1);
        </xsl:text>

        <xsl:apply-templates select="." mode="get-field-pointcut" />
        <xsl:apply-templates select="." mode="set-field-pointcut" />
    </xsl:template>

    <!-- JINDI &amp; FACILITIES -->

    <xsl:template match="element[@kind='FIELD']/annotation[@name=$ANN_RESOURCE and @type=$TYPE_INITIALCONTEXT]" mode="get-field-inject" priority="1">
        <xsl:text>if(object.</xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:text> == null) {
        </xsl:text>
        <xsl:text>object.</xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:text>=object.__iContext</xsl:text>;
        <xsl:text>
            }
        </xsl:text>

        <xsl:apply-templates select=".." mode="return-name" />
    </xsl:template>

    <xsl:template match="element/annotation[@name=$ANN_RESOURCE and @type=$TYPE_INITIALCONTEXT]" priority="1">
        <xsl:apply-templates select="." mode="get-field-pointcut" />
    </xsl:template>

    <xsl:template match="element/annotation[@name=$ANN_RESOURCE and @type=$TYPE_CONTEXT]" priority="1">
        <xsl:variable name="name">
            <xsl:choose>
                <xsl:when test="element[@name='mappedName' and not(@value='')]">
                    <xsl:value-of select="element[@name='mappedName']/@value" />
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>"java:comp/env"</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <xsl:apply-templates select="." mode="get-field-jndi-pointcut">
            <xsl:with-param name="jndi-name" select="$name" />
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="element[@kind='field']/annotation[@name=$ANN_RESOURCE and @type=$TYPE_ALARMFACILITY]" priority="1">
        <xsl:apply-templates select="." mode="get-field-jndi-pointcut">
            <xsl:with-param name="jndi-name" select="concat(@type,'.JNDI_NAME')" />
        </xsl:apply-templates>
    </xsl:template>


    <xsl:template match="element/annotation[@name=$ANN_RESOURCE and @type=$TYPE_NULLACTIVITYCONTEXTINTERFACEFACTORY]" priority="1">
        <xsl:apply-templates select="." mode="get-field-jndi-pointcut">
            <xsl:with-param name="jndi-name" select="concat(@type,'.JNDI_NAME')" />
        </xsl:apply-templates>
    </xsl:template>


    <xsl:template match="element/annotation[@name=$ANN_RESOURCE and @type=$TYPE_ACTIVITYCONTEXTNAMINGFACILITY]" priority="1">
        <xsl:apply-templates select="." mode="get-field-jndi-pointcut">
            <xsl:with-param name="jndi-name" select="concat(@type,'.JNDI_NAME')" />
        </xsl:apply-templates>
    </xsl:template>


    <xsl:template match="element[@kind='FIELD']/annotation[@name=$ANN_RESOURCE and @type=$TYPE_NULLACTIVITYFACTORY]" priority="1">
        <xsl:apply-templates select="." mode="get-field-jndi-pointcut">
            <xsl:with-param name="jndi-name" select="concat(@type,'.JNDI_NAME')" />
        </xsl:apply-templates>
    </xsl:template>


    <xsl:template match="element[@kind='FIELD']/annotation[@name=$ANN_RESOURCE and @type=$TYPE_PROFILEFACILITY]" priority="1">
        <xsl:apply-templates select="." mode="get-field-jndi-pointcut">
            <xsl:with-param name="jndi-name" select="concat(@type,'.JNDI_NAME')" />
        </xsl:apply-templates>
    </xsl:template>


    <xsl:template match="element[@kind='FIELD']/annotation[@name=$ANN_RESOURCE and @type=$TYPE_PROFILETABLEACTIVITYCONTEXTINTERFACEFACTORY]" priority="1">
        <xsl:apply-templates select="." mode="get-field-jndi-pointcut">
            <xsl:with-param name="jndi-name" select="concat(@type,'.JNDI_NAME')" />
        </xsl:apply-templates>
    </xsl:template>


    <xsl:template match="element[@kind='FIELD']/annotation[@name=$ANN_RESOURCE and @type=$TYPE_SERVICEACTIVITYCONTEXTINTERFACEFACTORY]" priority="1">
        <xsl:apply-templates select="." mode="get-field-jndi-pointcut">
            <xsl:with-param name="jndi-name" select="concat(@type,'.JNDI_NAME')" />
        </xsl:apply-templates>
    </xsl:template>


    <xsl:template match="element[@kind='FIELD']/annotation[@name=$ANN_RESOURCE and @type=$TYPE_SERVICEACTIVITYFACTORY]" priority="1">
        <xsl:apply-templates select="." mode="get-field-jndi-pointcut">
            <xsl:with-param name="jndi-name" select="concat(@type,'.JNDI_NAME')" />
        </xsl:apply-templates>
    </xsl:template>


    <xsl:template match="element[@kind='FIELD']/annotation[@name=$ANN_RESOURCE and @type=$TYPE_TIMERFACILITY]" priority="1">
        <xsl:apply-templates select="." mode="get-field-jndi-pointcut">
            <xsl:with-param name="jndi-name" select="concat(@type,'.JNDI_NAME')" />
        </xsl:apply-templates>
    </xsl:template>

    <!--EJB -->

    <xsl:template match="element/annotation[@name=$ANN_RESOURCE and @type=$TYPE_SOMEEJBHOME]" priority="1">
        <xsl:apply-templates select="." mode="get-field-jndi-pointcut">
            <xsl:with-param name="jndi-name" select="concat('&quot;',element[@name='name']/@value,'&quot;')" />
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="element/annotation[@name=$ANN_RESOURCE and @type=$TYPE_SOMEEJBREMOTE]">
        <!--
        <xsl:text> (</xsl:text>
        <xsl:value-of select="../@type"/><xsl:text> ) lookup home - create();</xsl:text>
        -->
    </xsl:template>

    <!--ENV ENTRIES-->


    <xsl:template match="element/annotation[@name=$ANN_RESOURCE and @type=$TYPE_EXAMPLERESOURCEADAPTORSBBINTERFACE]" priority="1">
        <xsl:apply-templates select="." mode="get-field-jndi-pointcut">
            <xsl:with-param name="jndi-name" select="concat('&quot;',element[@name='name']/@value,'&quot;')" />
        </xsl:apply-templates>
    </xsl:template>


    <!--RA BINDING -->
    <xsl:template match="element/annotation[@name=$ANN_RESOURCE and @type=$TYPE_EXAMPLEACTIVITYCONTEXTINTERFACEFACTORY]" priority="1">
        <xsl:apply-templates select="." mode="get-field-jndi-pointcut">
            <xsl:with-param name="jndi-name" select="concat('&quot;',element[@name='name']/@value,'&quot;')" />
        </xsl:apply-templates>
    </xsl:template>

    <!-- CONTEXT -->
    <xsl:template
        match="element[@kind='FIELD']/annotation[@name=$ANN_RESOURCE and (@type=$TYPE_SBBCONTEXT or @type=$TYPE_RESOURCEADAPTORCONTEXT or @type=$TYPE_PROFILECONTEXT)]"
        mode="get-field-inject"
        priority="1"
    >
        <xsl:text>if(object.</xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:text> == null) {
        </xsl:text>
        <xsl:text>object.</xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:text>=object.__context;</xsl:text>
        <xsl:text>
            }
            return object.__context; //proceed(object)
        </xsl:text>
    </xsl:template>

    <xsl:template
        match="element[@kind='METHOD']/annotation[@name=$ANN_RESOURCE and (@type=$TYPE_SBBCONTEXT or @type=$TYPE_RESOURCEADAPTORCONTEXT or @type=$TYPE_PROFILECONTEXT)]"
        mode="method-intercept-inject"
        priority="1"
    >
        <xsl:param name="fieldName" />
        <xsl:value-of select="$fieldName" />
        <xsl:text>
            return object.__context;
        </xsl:text>

    </xsl:template>


    <xsl:template match="element[@kind='METHOD']/annotation[@name=$ANN_EVENTHANDLER and @abstract='true']" priority="1">
        //event handler call super
        <xsl:value-of select="@modifiers" />
        <xsl:text> </xsl:text>
        <xsl:value-of select="../@type" />
        <xsl:text> </xsl:text>
        <xsl:value-of select="../@enclosing" />.<xsl:value-of select="../@name" />
        <xsl:text>() {
            super. //TODO
            }
        </xsl:text>
    </xsl:template>

    <!-- INITIATORS -->
    <xsl:template match="element[@kind='FIELD']/annotation[@name=$ANN_RESOURCE and @type=$TYPE_SBBCONTEXT]" priority="1">
        //set sbb context
        <xsl:apply-templates select="." mode="get-field-pointcut" />
    </xsl:template>

    <!--TODO  isAbstract -->
    <xsl:template match="element[@kind='METHOD']/annotation[@name=$ANN_RESOURCE and @type=$TYPE_SBBCONTEXT]" priority="1">
        //set sbb context

        <xsl:text>
            public </xsl:text>
        <xsl:value-of select="../@type" />
        <xsl:text> </xsl:text>
        <xsl:value-of select="../@enclosing" />.<xsl:value-of select="../@name" />
        <xsl:text>(){return null;}
        </xsl:text>

        <xsl:apply-templates select="." mode="method-intercept-pointcut-0">
            <xsl:with-param name="method-name" select="../@name" />
            <xsl:with-param name="return-type" select="../@type" />
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="element/annotation[@name=$ANN_RESOURCE and @type=$TYPE_RESOURCEADAPTORCONTEXT]" priority="1">
        //set ra context
        <xsl:apply-templates select="." mode="get-field-pointcut" />
    </xsl:template>

    <xsl:template match="element[@kind='FIELD']/annotation[@name=$ANN_RESOURCE and @type=$TYPE_PROFILECONTEXT]" priority="1">
        //set profile context
        <xsl:apply-templates select="." mode="get-field-pointcut" />
    </xsl:template>

    <xsl:template match="element/annotation[@name=$ANN_RESOURCE and @type=$TYPE_EXAMPLESBBLOCALOBJECT]" priority="1">
        //sbb local object
    </xsl:template>
    <!--TRACER-->

    <xsl:template match="element[@kind='METHOD']/annotation[@name=$ANN_RESOURCE and @type=$TYPE_TRACER]" mode="method-intercept-inject" priority="1">
        <xsl:param name="fieldName" />
        <xsl:value-of select="$fieldName" />

        <xsl:if test="$slee_debug = 'true'">
            <xsl:text
            >if(Boolean.getBoolean("slee.annotation.debug")) System.err.println("getting tracer " + object.toString() + " "+ object.__context);</xsl:text>
        </xsl:if>

        <xsl:text> //TODO cache this tracer?
            return object.__context.getTracer("</xsl:text>
        <xsl:choose>
            <xsl:when test="string-length(element/@value)=0">
                <xsl:value-of select="../@name" />
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="element/@value" />
            </xsl:otherwise>
        </xsl:choose>
        <xsl:text>");
        </xsl:text>
    </xsl:template>

    <xsl:template match="element[@kind='FIELD']/annotation[@name=$ANN_RESOURCE and @type=$TYPE_TRACER]" mode="get-field-inject" priority="1">
        <xsl:text>if(object.</xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:text> == null) {
        </xsl:text>
        <xsl:if test="$slee_debug = 'true'">
            <xsl:text>if(Boolean.getBoolean("slee.annotation.debug")) System.err.println("constructing tracer </xsl:text>
            <xsl:value-of select="../@name" />
            <xsl:text> </xsl:text>
            <xsl:value-of select="element/@value" />
            <xsl:text>");
            </xsl:text>
        </xsl:if>
        <xsl:text>
            object.</xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:text>=object.__context.getTracer("</xsl:text>
        <xsl:choose>
            <xsl:when test="string-length(element/@value)=0">
                <xsl:value-of select="../@name" />
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="element/@value" />
            </xsl:otherwise>
        </xsl:choose>
        <xsl:text>");
            }

            try {
            return object.</xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:text>;
            } finally {
        </xsl:text>

        <xsl:if test="$slee_debug = 'true'">
            <xsl:text>if(Boolean.getBoolean("slee.annotation.debug")) System.err.println("gotted tracer - was null: " + (object.</xsl:text>
            <xsl:value-of select="../@name" />
            <xsl:text> == null)); </xsl:text>
        </xsl:if>
        <xsl:text>}</xsl:text>
    </xsl:template>

    <xsl:template match="element[@kind='FIELD']/annotation[@name=$ANN_RESOURCE and @type=$TYPE_TRACER]" priority="1">
        //is tracer from ra or sbb or profile context
        <xsl:apply-templates select="." mode="get-field-pointcut" />
    </xsl:template>

    <xsl:template match="element[@kind='METHOD']/annotation[@name=$ANN_RESOURCE and @type=$TYPE_TRACER]" priority="1">
        //is tracer from ra or sbb or profile context
        <xsl:text>
            public </xsl:text>
        <xsl:value-of select="../@type" />
        <xsl:text> </xsl:text>
        <xsl:value-of select="../@enclosing" />.<xsl:value-of select="../@name" />
        <xsl:text>(){return null;}
        </xsl:text>

        <xsl:apply-templates select="." mode="method-intercept-pointcut-0">
            <xsl:with-param name="method-name" select="../@name" />
            <xsl:with-param name="return-type" select="../@type" />
        </xsl:apply-templates>
    </xsl:template>

    <!--CALCULATED -->
    <xsl:template match="element/annotation[@name=$ANN_RESOURCE and @type=$TYPE_EXAMPLESBBLOCALOBJECT]" priority="1">
        //profile local object
    </xsl:template>

    <!--CONFIG PROPERTIES -->

    <xsl:template match="element[@kind='FIELD' and @type=$TYPE_CONFIGPROPERTIES]/annotation[@name=$ANN_RESOURCE]" mode="get-field-inject" priority="1">
        <xsl:text>object.</xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:text> = object.__configProperties;
        </xsl:text>

        <xsl:apply-templates select=".." mode="return-name" />
    </xsl:template>

    <xsl:template match="element[@kind='FIELD' and @type=$TYPE_CONFIGPROPERTIES]/annotation[@name=$ANN_RESOURCE]" priority="1">
        <xsl:apply-templates select="." mode="get-field-pointcut" />
    </xsl:template>

    <!-- INJECT GETTER -->

    <!--CONFIG PROPERTY -->

    <xsl:template match="//element[@kind='FIELD']/annotation[@name=$ANN_CONFIGPROPERTY]" mode="get-field-inject" priority="1">
        <!--TODO: null config -->
        <xsl:text>javax.slee.resource.ConfigProperties.Property o = null;
            object.</xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:text> = (</xsl:text>
        <xsl:value-of select="../@type" />
        <xsl:text>)((o = object.__configProperties.getProperty("</xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:text>")) == null? null:o.getValue());</xsl:text>

        <xsl:apply-templates select=".." mode="return-name" />
    </xsl:template>

    <xsl:template match="//element[@kind='FIELD']/annotation[@name=$ANN_CONFIGPROPERTY]" priority="1">
        <xsl:apply-templates select="." mode="get-field-pointcut" />
    </xsl:template>

    <!-- inject from slee or dd -->

    <xsl:template match="element[@kind='FIELD']/annotation[@name=$ANN_RESOURCE]" mode="get-field-inject" priority="0">

        <xsl:variable name="name" select="../@name" />
        <xsl:variable name="type" select="@type" />
        <xsl:variable name="enclosing" select="../@enclosing" />
        <!--USAGE PARAMETERS -->

        <xsl:choose>
            <xsl:when
                test="element[@name ='name' and @value=''] and //element[@name = $enclosing and annotation[(@name=$ANN_SBB or @name=$ANN_PROFILESPEC) and element[@name ='usageParametersInterface' and @value=$type]]]"
            >

                <xsl:text>if(object.</xsl:text>
                <xsl:value-of select="../@name" />
                <xsl:text> == null) {
                </xsl:text>
                <xsl:text>object.</xsl:text>
                <xsl:value-of select="../@name" />
                <xsl:text>=(</xsl:text>
                <xsl:value-of select="../@type" />
                <xsl:text>) object.getDefault</xsl:text>
                <xsl:choose>
                    <xsl:when test="//element[@name = $enclosing]/annotation[@name=$ANN_SBB]">
                        <xsl:text>Sbb</xsl:text>
                    </xsl:when>
                    <!--PROFILE_SPEC no indicator-->
                </xsl:choose>
                <xsl:text>UsageParameterSet();
                    }
                </xsl:text>
                <xsl:apply-templates select=".." mode="return-name" />

            </xsl:when>

            <!--USAGE PARAMETERS -->
            <xsl:when
                test="element[@name ='name' and not(@value ='')] and //element[@name = $enclosing and annotation[(@name=$ANN_SBB or @name=$ANN_PROFILESPEC) and element[@name ='usageParametersInterface' and @value=$type]]]"
            >

                <xsl:text>if(object.</xsl:text>
                <xsl:value-of select="../@name" />
                <xsl:text> == null) {
                    try{
                </xsl:text>
                <xsl:text>object.</xsl:text>
                <xsl:value-of select="../@name" />
                <xsl:text>=(</xsl:text>
                <xsl:value-of select="../@type" />
                <xsl:text>) object.get</xsl:text>
                <xsl:choose>
                    <xsl:when test="//element[@name = $enclosing]/annotation[@name=$ANN_SBB]">
                        <xsl:text>Sbb</xsl:text>
                    </xsl:when>
                </xsl:choose>
                <xsl:text>UsageParameterSet("</xsl:text>
                <xsl:value-of select="element[@name='name']/@value" />
                <xsl:text>");
                    }catch(javax.slee.usage.UnrecognizedUsageParameterSetNameException x){
                    throw new RuntimeException(x);
                    }
                    }
                    </xsl:text>
                    <xsl:apply-templates select=".." mode="return-name" />
            </xsl:when>

            <!--RA -->

            <!--USAGE PARAMETERS -->
            <xsl:when
                test="element[@name ='name' and @value=''] and //element[@name = $enclosing and annotation[@name=$ANN_RESOURCEADAPTOR and element[@name ='usageParametersInterface' and @value=$type]]]"
            >
                <xsl:text>if(object.</xsl:text>
                <xsl:value-of select="../@name" />
                <xsl:text> == null) {
                </xsl:text>
                <xsl:text>object.</xsl:text>
                <xsl:value-of select="../@name" />
                <xsl:text>=(</xsl:text>
                <xsl:value-of select="../@type" />
                <xsl:text>) object.__context.getDefaultUsageParameterSet();
                    }
                </xsl:text>

                <xsl:apply-templates select=".." mode="return-name" />
            </xsl:when>

            <!--USAGE PARAMETERS -->
            <xsl:when
                test="element[@name ='name' and not(@value ='')] and //element[@name = $enclosing and annotation[@name=$ANN_RESOURCEADAPTOR and element[@name ='usageParametersInterface' and @value=$type]]]"
            >
                <xsl:text>if(object.</xsl:text>
                <xsl:value-of select="../@name" />
                <xsl:text> == null) {
                    try{
                </xsl:text>
                <xsl:text>object.</xsl:text>
                <xsl:value-of select="../@name" />
                <xsl:text>=(</xsl:text>
                <xsl:value-of select="../@type" />
                <xsl:text>) object.__context.getUsageParameterSet("</xsl:text>
                <xsl:value-of select="element[@name='name']/@value" />
                <xsl:text>");
                    }catch(javax.slee.usage.UnrecognizedUsageParameterSetNameException x){
                    throw new RuntimeException(x);
                    }
                    }
                </xsl:text>

            </xsl:when>
        </xsl:choose>
    </xsl:template>

    <!-- RESOURCE GETTERS -->

    <xsl:template match="element[@kind='FIELD' or @kind='METHOD']/annotation[@name=$ANN_RESOURCE]">
        <xsl:variable name="type" select="@type" />
        <xsl:variable name="name" select="element[@name='name']/@value" />
        <xsl:variable name="enclosing" select="../@enclosing" />
        <xsl:choose>

            <!-- USAGE PARAMETERS -->
            <xsl:when
                test="element[@name ='name' and @value=''] and //element[@name = $enclosing and annotation[(@name=$ANN_SBB or @name=$ANN_PROFILESPEC) and element[@name ='usageParametersInterface' and @value=$type]]]"
            >
                //get usage parameter method
                <xsl:text>public abstract </xsl:text>
                <xsl:value-of select="../@type" />
                <xsl:text> </xsl:text>
                <xsl:value-of select="../@enclosing" />
                <xsl:text>.getDefault</xsl:text>
                <xsl:choose>
                    <xsl:when test="//element[@name = $enclosing]/annotation[@name=$ANN_SBB]">
                        <xsl:text>Sbb</xsl:text>
                    </xsl:when>
                </xsl:choose>
                <xsl:text>UsageParameterSet();</xsl:text>

                <xsl:apply-templates select="." mode="get-field-pointcut" />

            </xsl:when>

            <xsl:when
                test="element[@name ='name' and not(@value ='')] and //element[@name = $enclosing and annotation[(@name=$ANN_SBB or @name=$ANN_PROFILESPEC) and element[@name ='usageParametersInterface' and @value=$type]]]"
            >
                //get named usage parameter method
                <xsl:text>public abstract </xsl:text>
                <xsl:value-of select="../@type" />
                <xsl:text> </xsl:text>
                <xsl:value-of select="../@enclosing" />
                <xsl:text>.get</xsl:text>
                <xsl:choose>
                    <xsl:when test="//element[@name = $enclosing]/annotation[@name=$ANN_SBB]">
                        <xsl:text>Sbb</xsl:text>
                    </xsl:when>
                    <!--PROFILE_SPEC no indicator-->
                </xsl:choose>
                <xsl:text>UsageParameterSet( String name) throws javax.slee.usage.UnrecognizedUsageParameterSetNameException;
                </xsl:text>
                <xsl:apply-templates select="." mode="get-field-pointcut" />
            </xsl:when>

            <xsl:when
                test="element[@name ='name' and not(@value ='')] and //element[@name = $enclosing and annotation[@name=$ANN_RESOURCEADAPTOR and element[@name ='usageParametersInterface' and @value=$type]]]"
            >
                <xsl:apply-templates select="." mode="get-field-pointcut" />
            </xsl:when>

            <xsl:when
                test="element[@name ='name' and @value=''] and //element[@name = $enclosing and annotation[@name=$ANN_RESOURCEADAPTOR and element[@name ='usageParametersInterface' and @value=$type]]]"
            >
                //ra get up from context
                <xsl:apply-templates select="." mode="get-field-pointcut" />
            </xsl:when>

            <xsl:when
                test="element[@name ='name' and not(@value ='')] and //element[@name = $enclosing and annotation[@name=$ANN_RESOURCEADAPTOR and element[@name ='usageParametersInterface' and @value=$type]]]"
            >
                //ra get named up from context
                <xsl:apply-templates select="." mode="get-field-pointcut" />
            </xsl:when>

            <!-- SBB RA ENTITY LINK -->

            <xsl:when
                test="//element[@name=$enclosing]/annotation[@name =$ANN_SBB]/element[@name='resourceAdaptorTypeBinding']/annotation[@name=$ANN_RESOURCEADAPTORTYPEBINDING and element[@name='raEntityLink' and @value=$name]]"
            >
                //OVER HERE
                <xsl:variable
                    name="ref"
                    select="//element[@enclosing=$enclosing]/annotation[@name =$ANN_SBB]/element[@name='resourceAdaptorTypeBinding']/annotation[@name=$ANN_RESOURCEADAPTORTYPEBINDING and element[@name='raEntityLink' and @value=$name]]"
                />
                <xsl:value-of select="$ref" />
                //THATS IT <xsl:value-of select="../@type" />
                <!--
            <xsl:apply-templates select="." mode="get-field-jndi-pointcut">

            <xsl:with-param name="jndi-name" select="concat('slee/resources/',generate-id(key('raEntityLink',concat(element/annotation[@name=$ANN_RESOURCEADAPTORTYPEREF]/element[@name='name']/@value,element/annotation[@name=$ANN_RESOURCEADAPTORTYPEREF_JAVAX]/element[@name='vendor']/@value,element/annotation[@name=$ANN_RESOURCEADAPTORTYPEREF]/element[@name='version']/@value))))"/>

                        </xsl:apply-templates>
                -->
                <xsl:choose>
                    <xsl:when test="../@kind='FIELD'">
                        <xsl:apply-templates select="." mode="get-field-jndi-pointcut">
                            <!--NAME/MAPPED_NAME-->
                            <xsl:with-param name="jndi-name" select="concat('&quot;',element[@name='name']/@value,'&quot;')" />
                        </xsl:apply-templates>
                    </xsl:when>
                    <xsl:when test="../@kind='METHOD'">
                        <xsl:apply-templates select="." mode="get-method-jndi-insert">
                            <!--NAME/MAPPED_NAME-->
                            <xsl:with-param name="jndi-name" select="concat('&quot;',element[@name='name']/@value,'&quot;')" />
                        </xsl:apply-templates>
                    </xsl:when>
                    <xsl:otherwise>
                        <!-- CANT -->
                    </xsl:otherwise>
                </xsl:choose>

            </xsl:when>
            <!--CONFIG PROPERTIES-->
            <xsl:otherwise>
                <!--JNDI PARAMETERS -->
                //JNDI <xsl:value-of select="../@name" /> - <xsl:value-of select="../@type" />
                <xsl:choose>
                    <xsl:when test="../@kind='FIELD'">
                        <xsl:apply-templates select="." mode="get-field-jndi-pointcut">
                            <!--NAME/MAPPED_NAME-->
                            <xsl:with-param name="jndi-name" select="concat('&quot;',element[@name='name']/@value,'&quot;')" />
                        </xsl:apply-templates>
                    </xsl:when>
                    <xsl:when test="../@kind='METHOD'">
                        <xsl:apply-templates select="." mode="get-method-jndi-insert">
                            <!--NAME/MAPPED_NAME-->
                            <xsl:with-param name="jndi-name" select="concat('&quot;',element[@name='name']/@value,'&quot;')" />
                        </xsl:apply-templates>
                    </xsl:when>
                    <xsl:otherwise>
                        <!-- CANT -->
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!--Alarms & Custom-->

    <!--TODO optimize lookup -->
    <xsl:template match="element[@kind='CLASS']/annotation[@name=$ANN_ROLLBACK]">
        context.setRollbackOnly();
    </xsl:template>

    <xsl:template match="element[@kind='METHOD']/annotation[@name=$ANN_RAISEALARM]">

        <xsl:if test="../@abstract='true'">
            <xsl:text>
                //unabstracted method

            </xsl:text>
            <xsl:value-of select="../@modifiers" />
            <xsl:text> </xsl:text>
            <xsl:value-of select="../@type" />
            <xsl:text> </xsl:text>
            <xsl:value-of select="../@enclosing" />.<xsl:value-of select="../@name" />
            <xsl:text>() {
            </xsl:text>
            <xsl:if test="not(../@type=$TYPE_VOID)">
            <xsl:text>
            return null;
                </xsl:text>
            </xsl:if>
            <xsl:text>
                }
            </xsl:text>
        </xsl:if>
        <xsl:apply-templates select="." mode="method-intercept-pointcut-0">
            <xsl:with-param name="method-name" select="../@name" />
            <xsl:with-param name="return-type" select="../@type" />
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="element[@kind='METHOD']/annotation[@name=$ANN_RAISEALARM]" mode="method-intercept-inject">
        <xsl:text>
            if(object.__alarmFacility == null)  {
            try {
            object.__alarmFacility = (AlarmFacility)((javax.naming.Context)new javax.naming.InitialContext().lookup("java:comp/env")).lookup(AlarmFacility.JNDI_NAME);
            } catch (NamingException ex) {
            throw new RuntimeException(ex);
            }
            }
            String alarmID = object.__alarmFacility.raiseAlarm("</xsl:text>
        <xsl:value-of select="./element[@name='alarmType']/@value" />
        <xsl:text>", "</xsl:text>
        <xsl:value-of select="./element[@name='instanceId']/@value" />
        <xsl:text>", AlarmLevel.fromInt(</xsl:text>
        <xsl:value-of select="./element[@name='alarmLevel']/@value" />
        <xsl:text>), "</xsl:text>
        <xsl:value-of select="./element[@name='alarmMessage']/@value" />"<xsl:text>);
            object.__context.getTracer(</xsl:text>
        <xsl:value-of select="../@enclosing" />
        <xsl:text>.class.getName()).warning(alarmID);
        </xsl:text>
        <xsl:if test="not(../@type=$TYPE_VOID)">
            <xsl:text>
                return alarmID;
            </xsl:text>
        </xsl:if>
    </xsl:template>

    <xsl:template match="element[@kind='METHOD']/annotation[@name=$ANN_CLEARALARM]">

        <xsl:if test="../@abstract='true'">
            <xsl:value-of select="../@modifiers" />
            <xsl:text> </xsl:text>
            <xsl:value-of select="../@type" />
            <xsl:text> </xsl:text>
            <xsl:value-of select="../@enclosing" />.<xsl:value-of select="../@name" />
            <xsl:text>() {
            </xsl:text>
            <xsl:if test="not(../@type=$TYPE_VOID)">
                <xsl:text>
                    return null;
                </xsl:text>
            </xsl:if>
            <xsl:text>
                }
            </xsl:text>
        </xsl:if>
        <xsl:apply-templates select="." mode="method-intercept-pointcut-0">
            <xsl:with-param name="method-name" select="../@name" />
            <xsl:with-param name="return-type" select="../@type" />
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="element[@kind='METHOD']/annotation[@name=$ANN_CLEARALARM]" mode="method-intercept-inject">
        <xsl:text>
            if(object.__alarmFacility == null)  {
            try {
            object.__alarmFacility = (AlarmFacility)((javax.naming.Context)new javax.naming.InitialContext().lookup("java:comp/env")).lookup(AlarmFacility.JNDI_NAME);
            } catch (NamingException ex) {
            throw new RuntimeException(ex);
            }
            }
            //TODO: lookup alarmId in aci naming
            object.__alarmFacility.clearAlarm("alarm");</xsl:text>
    </xsl:template>

    <xsl:template match="classtypes" mode="out">
        <xsl:text>
            declare parents : </xsl:text>
        <xsl:value-of select="@simple-name" />
        <xsl:text> implements </xsl:text>
        <xsl:for-each select="classtype">
            <xsl:value-of select="@name" />
            <xsl:if test="(position( )) != last()">
                <xsl:text>,</xsl:text>
            </xsl:if>
        </xsl:for-each>
        <!-- TODO: move local-interfaces into classtypes -->
        <xsl:if test="../@local-interfaces">
            <xsl:text>,</xsl:text>
            <xsl:value-of select="../@local-interfaces" />
        </xsl:if>
        <xsl:text>;</xsl:text>
    </xsl:template>

    <!--METHODS -->

    <xsl:template match="methods" mode="out">
        <xsl:apply-templates select="node()" mode="out" />
    </xsl:template>

    <xsl:template match="method" mode="out">
        <xsl:value-of select="@name" />
        <xsl:text>
        </xsl:text>
    </xsl:template>

    <!--POINTCUTS -->

    <xsl:template match="annotation" mode="method-intercept-pointcut-0">
        <xsl:param name="method-name" />
        <xsl:param name="return-type" />

        <xsl:text>
        pointcut </xsl:text>
        <xsl:value-of select="$AJC_PC_METHOD" />
        <xsl:value-of select="$method-name" />
        <xsl:value-of select="generate-id(key('unique-method',../@enclosing))" />
        <xsl:text>(</xsl:text>
        <xsl:value-of select="../@enclosing" />
        <xsl:text> object) :target(object) &amp;&amp; execution(</xsl:text>
        <xsl:value-of select="$return-type" />
        <xsl:text> </xsl:text>
        <xsl:value-of select="../@enclosing" />
        <xsl:text>+.</xsl:text>
        <xsl:value-of select="$method-name" />
        <xsl:text>());</xsl:text>
        <xsl:text>
            //TODO !within
        </xsl:text>
        <xsl:value-of select="$return-type" />
        <xsl:text> around(</xsl:text>
        <xsl:value-of select="../@enclosing" />
        <xsl:text> object) : </xsl:text>
        <xsl:value-of select="$AJC_PC_METHOD" />
        <xsl:value-of select="$method-name" />
        <xsl:value-of select="generate-id(key('unique-method',../@enclosing))" />
        <xsl:text>(object) {
        </xsl:text>
        <xsl:if test="$slee_debug = 'true'">
            <xsl:text>if(Boolean.getBoolean("slee.annotation.debug")) System.err.println("advice ###### around-0 </xsl:text>
            <xsl:value-of select="$method-name" />
            <xsl:text>");</xsl:text>
        </xsl:if>

        <xsl:apply-templates select="." mode="method-intercept-inject">
            <xsl:with-param name="method-name" select="$method-name" />
        </xsl:apply-templates>

        <xsl:text>
            //proceed(object);
            }
        </xsl:text>
    </xsl:template>

    <xsl:template match="annotation" mode="method-intercept-pointcut">
        <xsl:param name="arg-type" />
        <xsl:param name="method-name" />

        <xsl:text>
            pointcut </xsl:text>
        <xsl:value-of select="$AJC_PC_METHOD" />
        <xsl:value-of select="$method-name" />
        <xsl:value-of select="generate-id(key('unique-method',../@enclosing))" />
        <xsl:text>(</xsl:text>
        <xsl:value-of select="../@enclosing" />
        <xsl:text> object,</xsl:text>
        <xsl:value-of select="$arg-type" />
        <xsl:text> arg1) :target(object) &amp;&amp; execution(void </xsl:text>
        <xsl:value-of select="../@enclosing" />
        <xsl:text>+.</xsl:text>
        <xsl:value-of select="$method-name" />
        <xsl:text>(</xsl:text>
        <xsl:value-of select="$arg-type" />
        <xsl:text>+)) &amp;&amp; args(arg1);</xsl:text>

        //TODO !within

        <xsl:text>
            void around(
        </xsl:text>
        <xsl:value-of select="../@enclosing" />
        <xsl:text> object, </xsl:text>
        <xsl:value-of select="$arg-type" />
        <xsl:text> arg1) : </xsl:text>
        <xsl:value-of select="$AJC_PC_METHOD" />
        <xsl:value-of select="$method-name" />
        <xsl:value-of select="generate-id(key('unique-method',../@enclosing))" />
        <xsl:text>(object,arg1) { </xsl:text>

        <xsl:if test="$slee_debug = 'true'">
            <xsl:text>if(Boolean.getBoolean("slee.annotation.debug")) System.err.println("advice ###### around " + object.toString() + "</xsl:text>
            <xsl:value-of select="$method-name" />
            <xsl:text>");</xsl:text>
        </xsl:if>

        <xsl:apply-templates select="." mode="method-intercept-inject">
            <xsl:with-param name="method-name" select="$method-name" />
            <xsl:with-param name="arg-type" select="$arg-type" />
        </xsl:apply-templates>

        <xsl:text>
            proceed(object,arg1);
            }
        </xsl:text>
    </xsl:template>

    <xsl:template match="annotation" mode="set-field-pointcut">
        <xsl:text>
            pointcut </xsl:text>
        <xsl:value-of select="$AJC_PC_SET" />
        <xsl:value-of select="../@name" />
        <xsl:value-of select="generate-id(key('unique-field',concat(../@name,../@enclosing)))" />
        <xsl:text>(</xsl:text>
        <xsl:value-of select="../@enclosing" />
        <xsl:text> object,</xsl:text>
        <xsl:value-of select="../@type" />
        <xsl:text> l) :target(object) &amp;&amp; set(</xsl:text>
        <xsl:value-of select="../@type" />
        <xsl:text> </xsl:text>
        <xsl:value-of select="../@enclosing" />
        <xsl:text>.</xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:text>) &amp;&amp; args(l) &amp;&amp; !within(</xsl:text>
        <xsl:value-of select="../@enclosing" />$SleeAnnotationsAspect<xsl:text>)</xsl:text>
        <xsl:if test="$slee_debug = 'true'">
            <xsl:text> &amp;&amp; !within(</xsl:text>
            <xsl:value-of select="../@enclosing" />$DebugAspect<xsl:text>)</xsl:text>
        </xsl:if>
        <xsl:text>;
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
        <xsl:value-of select="../@enclosing" />
        <xsl:text> object, </xsl:text>
        <xsl:value-of select="../@type" />
        <xsl:text> arg1) : </xsl:text>
        <xsl:value-of select="$AJC_PC_SET" />
        <xsl:value-of select="../@name" />
        <xsl:value-of select="generate-id(key('unique-field',concat(../@name,../@enclosing)))" />
        <xsl:text>(object,arg1) {</xsl:text>
        <xsl:if test="$slee_debug = 'true'">
            <xsl:text>
                if(Boolean.getBoolean("slee.annotation.debug")) System.err.println("advice ###### setter </xsl:text>
            <xsl:value-of select="../@name" />
            <xsl:text> " + arg1);
            </xsl:text>
        </xsl:if>

        <xsl:apply-templates select="." mode="set-field-inject" />
        <xsl:text>}
        </xsl:text>
    </xsl:template>

    <xsl:template match="annotation" mode="get-field-pointcut">
        <xsl:text>
            pointcut </xsl:text>
        <xsl:value-of select="$AJC_PC_GET" />
        <xsl:value-of select="../@name" />
        <xsl:value-of select="generate-id(key('unique-field',concat(../@name,../@enclosing)))" />
        <xsl:text>(</xsl:text>
        <xsl:value-of select="../@enclosing" />
        <xsl:text> object) :target(object) &amp;&amp; get(</xsl:text>
        <xsl:value-of select="../@type" />
        <xsl:text> </xsl:text>
        <xsl:value-of select="../@enclosing" />
        <xsl:text>.</xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:if test="@processed-value != '{unknown}'">
            <xsl:text>) &amp;&amp; !within(</xsl:text>
            <xsl:value-of select="../@enclosing" />
            <xsl:text>.get</xsl:text>
            <xsl:value-of select="@processed-value" />
        </xsl:if>
        <xsl:text>) &amp;&amp; !within(</xsl:text>
        <xsl:value-of select="../@enclosing" />$SleeAnnotationsAspect<xsl:text>)</xsl:text>
        <xsl:if test="$slee_debug = 'true'">
            <xsl:text> &amp;&amp; !within(</xsl:text>
            <xsl:value-of select="../@enclosing" />$DebugAspect<xsl:text>)</xsl:text>
        </xsl:if>
        <xsl:text>;
        </xsl:text>

        <xsl:value-of select="../@type" />
        <xsl:text> around(</xsl:text>
        <xsl:value-of select="../@enclosing" />
        <xsl:text> object) : </xsl:text>
        <xsl:value-of select="$AJC_PC_GET" />
        <xsl:value-of select="../@name" />
        <xsl:value-of select="generate-id(key('unique-field',concat(../@name,../@enclosing)))" />
        <xsl:text>(object) {
        </xsl:text>
        <xsl:if test="$slee_debug = 'true'">
            <!--todo can show null or hashcode-->
            <xsl:text>
                if(Boolean.getBoolean("slee.annotation.debug")) System.err.println("advice ###### getter </xsl:text>
            <xsl:value-of select="../@name" />
            <xsl:text> " + object.getClass().toString());</xsl:text>
        </xsl:if>
        <xsl:text>

        </xsl:text>

        <xsl:apply-templates select="." mode="get-field-inject">
            <xsl:with-param name="fieldName">
                <xsl:value-of select="generate-id(key('unique-field',concat(../@name,../@enclosing)))" />
            </xsl:with-param>
        </xsl:apply-templates>
        <xsl:text>
            }
        </xsl:text>
    </xsl:template>

    <xsl:template match="annotation" mode="get-field-jndi-pointcut">
        <xsl:param name="jndi-name" />
        <xsl:text>
        pointcut </xsl:text>
        <xsl:value-of select="$AJC_PC_FIELD_JNDI" />
        <xsl:value-of select="../@name" />
        <xsl:value-of select="generate-id(key('unique-field',concat(../@name,../@enclosing)))" />
        <xsl:text>(</xsl:text>
        <xsl:value-of select="../@enclosing" />
        <xsl:text> object) :target(object) &amp;&amp; get(</xsl:text>
        <xsl:value-of select="../@type" />
        <xsl:text> </xsl:text>
        <xsl:value-of select="../@enclosing" />
        <xsl:text>.</xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:text>) &amp;&amp; !within(</xsl:text>
        <xsl:value-of select="../@enclosing" />$SleeAnnotationsAspect<xsl:text>)</xsl:text>
        <xsl:if test="$slee_debug = 'true'">
            <xsl:text> &amp;&amp; !within(</xsl:text>
            <xsl:value-of select="../@enclosing" />$DebugAspect<xsl:text>)</xsl:text>
        </xsl:if>
        <xsl:text>;
        </xsl:text>
        <xsl:text>
            before( </xsl:text>
        <xsl:value-of select="../@enclosing" />
        <xsl:text> object) : </xsl:text>
        <xsl:value-of select="$AJC_PC_FIELD_JNDI" />
        <xsl:value-of select="../@name" />
        <xsl:value-of select="generate-id(key('unique-field',concat(../@name,../@enclosing)))" />
        <xsl:text>(object) {</xsl:text>
        <xsl:if test="$slee_debug = 'true'">
            <xsl:text>
                if(Boolean.getBoolean("slee.annotation.debug")) System.err.println("advice ###### jndi getter wrapper </xsl:text>
            <xsl:value-of select="../@name" /> " + <xsl:value-of select="$jndi-name" />
            <xsl:text>);
            </xsl:text>
        </xsl:if>
        <xsl:text>if(object.</xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:text> == null) {
            try{
            object.</xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:text>=(</xsl:text>
        <xsl:value-of select="../@type" />
        <xsl:text>)((javax.naming.Context)new javax.naming.InitialContext().lookup("java:comp/env")).lookup(</xsl:text>
        <xsl:value-of select="$jndi-name" />
        <xsl:text>);
            }catch(javax.naming.NamingException x){
            throw new RuntimeException(x);
            }
            }
            }
        </xsl:text>
    </xsl:template>

    <xsl:template match="annotation" mode="get-method-jndi-insert">
        <xsl:param name="jndi-name" />

        <xsl:text>
        </xsl:text>
        <xsl:value-of select="../@enclosing" />
        private <xsl:value-of select="../@type" />
        <xsl:text> </xsl:text>
        <xsl:value-of select="../@enclosing" />
        <xsl:text>.</xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:value-of select="generate-id(key('unique-method-private',concat(../@name,../@enclosing)))" />
        <xsl:text>;
        </xsl:text>

        <xsl:value-of select="@modifiers" />
        <xsl:text> </xsl:text>
        <xsl:value-of select="../@type" />
        <xsl:text> </xsl:text>
        <xsl:value-of select="../@enclosing" />.<xsl:value-of select="../@name" />
        <xsl:text>() {
            if(Boolean.getBoolean("slee.annotation.debug")) System.err.println("advice ###### jndi method wrapper </xsl:text>
        <xsl:value-of select="@name" /> " + <xsl:value-of select="$jndi-name" />
        <xsl:text>);
        </xsl:text>
        <xsl:text>if(this.</xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:value-of select="generate-id(key('unique-method-private',concat(../@name,../@enclosing)))" />
        <xsl:text> == null) {
            try{
            this.</xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:value-of select="generate-id(key('unique-method-private',concat(../@name,../@enclosing)))" />
        <xsl:text>=(</xsl:text>
        <xsl:value-of select="../@type" />
        <xsl:text>)((javax.naming.Context)new javax.naming.InitialContext().lookup("java:comp/env")).lookup(</xsl:text>
        <xsl:value-of select="$jndi-name" />
        <xsl:text>);
            }catch(javax.naming.NamingException x){
            throw new RuntimeException(x);
            }
            }
            return </xsl:text>
        <xsl:value-of select="../@name" />
        <xsl:value-of select="generate-id(key('unique-method-private',concat(../@name,../@enclosing)))" />
        <xsl:text>;
            }</xsl:text>
    </xsl:template>


    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" />
        </xsl:copy>
    </xsl:template>

    <!-- Annotation class name parameters -->
    <xsl:param name="ANN_RESOURCE">jakarta.annotation.Resource</xsl:param>
    <xsl:param name="ANN_SBB">mobi.mofokom.javax.slee.annotation.Sbb</xsl:param>
    <xsl:param name="ANN_PROFILESPEC">mobi.mofokom.javax.slee.annotation.ProfileSpec</xsl:param>
    <xsl:param name="ANN_RESOURCEADAPTOR">mobi.mofokom.javax.slee.annotation.ResourceAdaptor</xsl:param>
    <xsl:param name="ANN_CHILDRELATION">mobi.mofokom.javax.slee.annotation.ChildRelation</xsl:param>
    <xsl:param name="ANN_CMPFIELD">mobi.mofokom.javax.slee.annotation.CMPField</xsl:param>
    <xsl:param name="ANN_EVENTHANDLER">mobi.mofokom.javax.slee.annotation.event.EventHandler</xsl:param>
    <xsl:param name="ANN_CONFIGPROPERTY">mobi.mofokom.javax.slee.annotation.ConfigProperty</xsl:param>
    <xsl:param name="ANN_ROLLBACK">mobi.mofokom.javax.slee.annotation.Rollback</xsl:param>
    <xsl:param name="ANN_RAISEALARM">mobi.mofokom.javax.slee.annotation.RaiseAlarm</xsl:param>
    <xsl:param name="ANN_CLEARALARM">mobi.mofokom.javax.slee.annotation.ClearAlarm</xsl:param>
    <xsl:param name="ANN_RESOURCEADAPTORTYPEBINDING">javax.slee.annotation.ResourceAdaptorTypeBinding</xsl:param>
    <xsl:param name="ANN_RESOURCEADAPTORTYPEREF">mobi.mofokom.javax.slee.annotation.ResourceAdaptorTypeRef</xsl:param>
    <xsl:param name="ANN_RESOURCEADAPTORTYPEREF_JAVAX">javax.slee.annotation.ResourceAdaptorTypeRef</xsl:param>

    <!-- Type class name parameters -->
    <xsl:param name="TYPE_INITIALCONTEXT">javax.naming.InitialContext</xsl:param>
    <xsl:param name="TYPE_CONTEXT">javax.naming.Context</xsl:param>
    <xsl:param name="TYPE_CHILDRELATION">javax.slee.ChildRelation</xsl:param>
    <xsl:param name="TYPE_SBBCONTEXT">javax.slee.SbbContext</xsl:param>
    <xsl:param name="TYPE_RESOURCEADAPTORCONTEXT">javax.slee.resource.ResourceAdaptorContext</xsl:param>
    <xsl:param name="TYPE_PROFILECONTEXT">javax.slee.profile.ProfileContext</xsl:param>
    <xsl:param name="TYPE_ALARMFACILITY">javax.slee.facilities.AlarmFacility</xsl:param>
    <xsl:param name="TYPE_NULLACTIVITYCONTEXTINTERFACEFACTORY">javax.slee.nullactivity.NullActivityContextInterfaceFactory</xsl:param>
    <xsl:param name="TYPE_ACTIVITYCONTEXTNAMINGFACILITY">javax.slee.facilities.ActivityContextNamingFacility</xsl:param>
    <xsl:param name="TYPE_NULLACTIVITYFACTORY">javax.slee.nullactivity.NullActivityFactory</xsl:param>
    <xsl:param name="TYPE_PROFILEFACILITY">javax.slee.profile.ProfileFacility</xsl:param>
    <xsl:param name="TYPE_PROFILETABLEACTIVITYCONTEXTINTERFACEFACTORY">javax.slee.profile.ProfileTableActivityContextInterfaceFactory</xsl:param>
    <xsl:param name="TYPE_SERVICEACTIVITYCONTEXTINTERFACEFACTORY">javax.slee.serviceactivity.ServiceActivityContextInterfaceFactory</xsl:param>
    <xsl:param name="TYPE_SERVICEACTIVITYFACTORY">javax.slee.serviceactivity.ServiceActivityFactory</xsl:param>
    <xsl:param name="TYPE_TIMERFACILITY">javax.slee.facilities.TimerFacility</xsl:param>
    <xsl:param name="TYPE_TRACER">javax.slee.facilities.Tracer</xsl:param>
    <xsl:param name="TYPE_CONFIGPROPERTIES">javax.slee.resource.ConfigProperties</xsl:param>
    <xsl:param name="TYPE_SOMEEJBHOME">org.mobicents.slee.annotations.examples.ejb.SomeEJBHome</xsl:param>
    <xsl:param name="TYPE_SOMEEJBREMOTE">org.mobicents.slee.annotations.examples.ejb.SomeEJBRemote</xsl:param>
    <xsl:param name="TYPE_EXAMPLERESOURCEADAPTORSBBINTERFACE">org.mobicents.slee.annotations.examples.resource.ExampleResourceAdaptorSbbInterface</xsl:param>
    <xsl:param
        name="TYPE_EXAMPLEACTIVITYCONTEXTINTERFACEFACTORY"
    >org.mobicents.slee.annotations.examples.resource.ExampleActivityContextInterfaceFactory</xsl:param>
    <xsl:param name="TYPE_EXAMPLESBBLOCALOBJECT">org.mobicents.slee.annotations.examples.sbb.ExampleSbbLocalObject</xsl:param>
    <xsl:param name="TYPE_VOID">void</xsl:param>
    <xsl:param name="AJC_PC">_ajc_pc_</xsl:param>
    <xsl:param name="AJC_PC_FIELD_JNDI">_ajc_pc_jndi_</xsl:param>
    <xsl:param name="AJC_PC_GET">_ajc_pc_get_</xsl:param>
    <xsl:param name="AJC_PC_SET">_ajc_pc_set_</xsl:param>
    <xsl:param name="AJC_PC_METHOD">_ajc_pc_method_</xsl:param>

</xsl:stylesheet>
