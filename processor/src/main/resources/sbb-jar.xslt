<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" indent="yes" doctype-public="-//Sun Microsystems, Inc.//DTD JAIN SLEE SBB 1.1//EN" doctype-system="http://java.sun.com/dtd/slee-sbb-jar_1_1.dtd" xalan:indent-amount="2" xmlns:xalan="http://xml.apache.org/xalan" />

    <xsl:include href="resource:common.xslt"/>

    <xsl:key name="usageKey" match="/process/element[@kind='METHOD']/annotation[@name='javax.slee.annotation.UsageParameter']" use="concat(@processed-value,../@enclosing)"/>

    <xsl:key name="eventKey" match="/process/element[@kind='METHOD']/annotation[@name='javax.slee.annotation.event.EventHandler']//annotation[@name='javax.slee.annotation.event.EventTypeRef']" use="concat(element[@name='name']/@value,element[@name='vendor']/@value,element[@name='version']/@value)"/>

    <xsl:key name="eventTypeRefKey" match="annotation[@name='javax.slee.annotation.event.EventTypeRef']" use="concat(element[@name='name']/@value,element[@name='vendor']/@value,element[@name='version']/@value)"/>

    <xsl:key name="raEntityLink" match="//element[@name='raTypeRef']/annotation" use="concat(element[@name='name']/@value,element[@name='vendor']/@value,element[@name='version']/@value)"/>

    <xsl:template match="/">
        <sbb-jar>
         
            <description>SBB Definitions Auto-Generated <xsl:value-of select="/process/@generatedTime"/> 
            </description>
            <xsl:apply-templates select="node()/element[@kind='CLASS']/annotation[@name='javax.slee.annotation.Sbb']"/>
            <xsl:apply-templates select="node()/element[@kind='CLASS']/annotation[@name='javax.slee.annotation.Sbb']/element[@name='securityPermissions']"/>
        </sbb-jar>
    </xsl:template>

    <xsl:template match="annotation[@name='javax.slee.annotation.event.EventHandler' 
or @name='javax.slee.annotation.event.EventFiring'
or @name='javax.slee.annotation.event.ActivityEndEventHandler'
or @name='javax.slee.annotation.event.ProfileAddedEventHandler'
or @name='javax.slee.annotation.event.ProfileRemovedEventHandler'
or @name='javax.slee.annotation.event.ProfileUpdatedEventHandler'
or @name='javax.slee.annotation.event.ServiceStartedEventHandler'
or @name='javax.slee.annotation.event.TimerEventHandler'
    ]"> 
        <event>
         
            <xsl:variable name="eventKey" select="generate-id(key('eventKey',concat(.//annotation[@name='javax.slee.annotation.event.EventTypeRef']/element[@name='name']/@value,.//annotation[@name='javax.slee.annotation.event.EventTypeRef']/element[@name='vendor']/@value,.//annotation[@name='javax.slee.annotation.event.EventTypeRef']/element[@name='version']/@value)))"/>

            <xsl:choose>
                <xsl:when test="@name='javax.slee.annotation.event.EventFiring'">
                    <xsl:attribute name="event-direction">Fire</xsl:attribute>
                </xsl:when>
                <xsl:otherwise> 
                    <xsl:choose>
                        <xsl:when test="@name='javax.slee.annotation.event.EventHandler' and not($eventKey='')">
                            <xsl:attribute name="event-direction">FireAndReceive</xsl:attribute>
                        </xsl:when>
                        <xsl:otherwise> 
                            <xsl:attribute name="event-direction">Receive</xsl:attribute>
                        </xsl:otherwise>
                    </xsl:choose>
                    <xsl:attribute name="mask-on-attach">
                        <xsl:value-of select="element[@name='maskOnAttach']/@value"/>
                    </xsl:attribute>
                    <xsl:attribute name="initial-event">
                        <xsl:value-of select="element[@name='initialEvent']/@value"/>
                    </xsl:attribute>
                    <xsl:attribute name="last-in-transaction">
                        <xsl:value-of select="element[@name='lastInTransaction']/@value"/>
                    </xsl:attribute>
                </xsl:otherwise>
            </xsl:choose>
            <event-name>
         
                <xsl:value-of select="@processed-value"/>
            </event-name>

            <xsl:apply-templates select=".//annotation[@name='javax.slee.annotation.event.EventTypeRef']"/>

            <xsl:choose>
                <xsl:when test="not(@name='javax.slee.annotation.event.EventFiring')">
                    <xsl:if test="boolean(element[@name='initialEvent']/@value)"> 
                        <xsl:for-each select="element[@name='initialEventSelect']">
                            <initial-event-select>
                                <xsl:attribute name="variable">
                                    <xsl:value-of select="value/@name"/>
                                </xsl:attribute>
                            </initial-event-select>
                        </xsl:for-each>
                    </xsl:if>
                    <xsl:if test="element[@name='initialEventSelectorMethod' and @value='']">
                        <xsl:variable name="event" select=".//annotation[@name='javax.slee.annotation.event.EventTypeRef']"/> 
                        <xsl:variable name="iesm" select="/process/element[@kind='METHOD' and @type='javax.slee.InitialEventSelector' and annotation[@name='javax.slee.annotation.event.InitialEventSelectorMethod' and element/annotation[@name='javax.slee.annotation.event.EventTypeRef' and generate-id(key('eventTypeRefKey',concat(element[@name='name']/@value,element[@name='vendor']/@value,element[@name='version']/@value))) = generate-id(key('eventTypeRefKey',concat($event/element[@name='name']/@value,$event/element[@name='vendor']/@value,$event/element[@name='version']/@value)))]]]/@name"/> 

                        <xsl:if test="$iesm">
                            <initial-event-selector-method-name>
                                <xsl:value-of select="$iesm"/>
                            </initial-event-selector-method-name>
                        </xsl:if>
                    </xsl:if>

                    <xsl:if test="element[@name='initialEventSelectorMethod' and not(@value='')]"> 
                        <initial-event-selector-method-name>
                            <xsl:value-of select="element[@name='initialEventSelectorMethod']/@value"/>
                        </initial-event-selector-method-name>
                    </xsl:if>

                    <xsl:if test="element[@name='eventResourceOption' and not(@value='')]"> 
                        <event-resource-option>
         
                            <xsl:value-of select="element[@name='eventResourceOption']/@value"/>
                        </event-resource-option>
                    </xsl:if>
                </xsl:when>
            </xsl:choose>
        </event>
    </xsl:template>
    <xsl:template match="annotation[@name='javax.slee.annotation.Sbb']">
        <xsl:variable name="sbbClass" select="../@name"/>
        <xsl:variable name="sbbACI" select="element[@name='activityContextInterface']/@value"/>
        <sbb>
            <xsl:if test="element[@name='id']/@value">
                    <xsl:attribute name="id">
                        <xsl:value-of select="element[@name='id']/@value"/>
                    </xsl:attribute>
            </xsl:if>

            <description> 
         
                <xsl:value-of select="element[@name='description']/@value"/> 
            </description>

            <sbb-name>
         
                <xsl:value-of select="element[@name='name']/@value"/>
            </sbb-name>
            <sbb-vendor> 
         
                <xsl:value-of select="element[@name='vendor']/@value"/>
            </sbb-vendor>
            <sbb-version> 
         
                <xsl:value-of select="element[@name='version']/@value"/> 
            </sbb-version>

            <xsl:if test="element[@name='alias' and not(@value='')]">
                <sbb-alias> 
         
                    <xsl:value-of select="element[@name='alias']/@value"/> 
                </sbb-alias>
            </xsl:if>

            <xsl:apply-templates select="element[@name='libraryRefs']/annotation[@name='javax.slee.annotation.LibraryRef']"/>

            <xsl:apply-templates select="element[@name='sbbRefs']/annotation[@name='javax.slee.annotation.SbbRef']"/>

            <xsl:apply-templates select="element[@name='profileSpecRefs']/annotation[@name='javax.slee.annotation.ProfileSpecRef']" mode="sbb"/>

            <sbb-classes>
         
                <sbb-abstract-class>
         
                    <xsl:if test="../annotation[@name='javax.slee.annotation.Reentrant']">
                        <xsl:attribute name="reentrant">True</xsl:attribute>
                    </xsl:if>
                    <sbb-abstract-class-name> 
         
                        <xsl:value-of select="../@name"/> 
                    </sbb-abstract-class-name>

                    <xsl:for-each select="/process/element[@enclosing=$sbbClass]/annotation[@name='javax.slee.annotation.CMPField']">
                        <cmp-field>
         
                            <cmp-field-name>
                                <xsl:choose>
                                    <xsl:when test="../@kind = 'METHOD'">
                                        <xsl:value-of select="@processed-value"/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="../@name"/>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </cmp-field-name>
                            <xsl:for-each select="element[@name='sbbAliasRef' and not(@value='')]">
                                <sbb-alias-ref>
         
                                    <xsl:value-of select="@name"/> 
                                </sbb-alias-ref>
                            </xsl:for-each>
                        </cmp-field>
                    </xsl:for-each>

                    <xsl:for-each select="/process/element[@enclosing=$sbbClass]/annotation[@name='javax.slee.annotation.ChildRelation']">
                        <get-child-relation-method>
         
                            <description>
         
                                <xsl:value-of select="element[@name='description']"/> 
                            </description> 
                            <xsl:if test="element[@name='sbbAliasRef' and not(@value='')]"> 
                                <sbb-alias-ref>
         
                                    <xsl:value-of select="element[@name='sbbAliasRef']/@value"/> 
                                </sbb-alias-ref>
                            </xsl:if>
                            <get-child-relation-method-name>
                                <xsl:choose>
                                    <xsl:when test="../@kind = 'FIELD'">
                                        get<xsl:value-of select="@processed-value"/>ChildRelation
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="../@name"/>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </get-child-relation-method-name>
                            <default-priority>
         
                                <xsl:value-of select="element[@name='defaultPriority']/@value"/> 
                            </default-priority>
                        </get-child-relation-method> 
                    </xsl:for-each>
                    
                    <xsl:for-each select="/process/element[@enclosing=$sbbClass]/annotation[@name='javax.slee.annotation.ProfileCMP']">
                        <get-profile-cmp-method>
         
                            <description>
         
                                <xsl:value-of select="element[@name='description']/@value"/> 
                            </description> 
                            <profile-spec-alias-ref>
         
                                <xsl:value-of select="element[@name='profileAliasRef']/@value"/> 
                            </profile-spec-alias-ref>
                            <get-profile-cmp-method-name>
         
                                <xsl:value-of select="../@name"/> 
                            </get-profile-cmp-method-name>
                        </get-profile-cmp-method> 
                    </xsl:for-each>
                </sbb-abstract-class>

                <xsl:if test="element[@name='localInterface' and not(@value='javax.slee.SbbLocalObject')]">
                    <sbb-local-interface>
         
                        <xsl:attribute name="isolate-security-permissions">
                            <xsl:value-of select="element[@name='isolateSecurityPermissions']/@value"/>
                        </xsl:attribute>
                        <sbb-local-interface-name>
         
                            <xsl:value-of select="element[@name='localInterface']/@value"/> 
                        </sbb-local-interface-name>
                    </sbb-local-interface>
                </xsl:if>

                <xsl:if test="element[@name='activityContextInterface' and not(@value='javax.slee.ActivityContextInterface')]">
                    <sbb-activity-context-interface>
         
                        <sbb-activity-context-interface-name>
         
                            <xsl:value-of select="$sbbACI"/> 
                        </sbb-activity-context-interface-name>
                    </sbb-activity-context-interface>
                </xsl:if>

                <xsl:if test="element[@name='usageParametersInterface' and not(@value='java.lang.Object')]">
                    <xsl:variable name="usageParametersInterface" select="element[@name='usageParametersInterface']/@value"/>
                    <sbb-usage-parameters-interface>
         
                        <sbb-usage-parameters-interface-name>
         
                            <xsl:value-of select="$usageParametersInterface"/> 
                        </sbb-usage-parameters-interface-name>

                        <xsl:variable name="set1" select="/process/element[@kind='INTERFACE']/annotation[@name='javax.slee.annotation.UsageParametersInterface']/element[@kind='METHOD' and @enclosing=$usageParametersInterface]/annotation[@name='javax.slee.annotation.UsageParameter' and generate-id(key('usageKey',concat(@processed-value,../@enclosing)))='']"/>

                        <xsl:variable name="set2" select="/process/element[@kind='METHOD' and @enclosing=$usageParametersInterface]/annotation[@name='javax.slee.annotation.UsageParameter']"/>

                        <xsl:for-each select="$set1 | $set2">
                            <xsl:sort select="@processed-value" />
                            <usage-parameter>
         
                                <xsl:attribute name="name">
                                    <xsl:value-of select="@processed-value"/>
                                </xsl:attribute>
                                <xsl:if test="element[@name='notificationsEnabled']">
                                    <xsl:attribute name="notifications-enabled">
                                        <xsl:value-of select="element[@name='notificationsEnabled']/@value"/>
                                    </xsl:attribute>
                                </xsl:if>
                            </usage-parameter>
                        </xsl:for-each>
                    </sbb-usage-parameters-interface>
                </xsl:if>
            </sbb-classes>

            <xsl:if test="element[@name='addressProfileSpecAliasRef' and not(@value='')]">
                <address-profile-spec-alias-ref>
         
                    <xsl:value-of select="element[@name='addressProfileSpecAliasRef']/@value"/>
                </address-profile-spec-alias-ref>
            </xsl:if>

            <xsl:apply-templates select="/process/element[@enclosing=$sbbClass]/annotation[@name='javax.slee.annotation.event.EventFiring' and generate-id(key('eventKey',concat(.//annotation[@name='javax.slee.annotation.event.EventTypeRef']/element[@name='name']/@value,.//annotation[@name='javax.slee.annotation.event.EventTypeRef']/element[@name='vendor']/@value,.//annotation[@name='javax.slee.annotation.event.EventTypeRef']/element[@name='version']/@value)))='']"/>

            <xsl:apply-templates select="/process/element[@enclosing=$sbbClass]/annotation[@name='javax.slee.annotation.event.EventHandler']"/>
            <xsl:apply-templates select="/process/element[@enclosing=$sbbClass]/annotation[@name='javax.slee.annotation.event.ActivityEndEventHandler']"/>
            <xsl:apply-templates select="/process/element[@enclosing=$sbbClass]/annotation[@name='javax.slee.annotation.event.TimerEventHandler']"/>
            <xsl:apply-templates select="/process/element[@enclosing=$sbbClass]/annotation[@name='javax.slee.annotation.event.ServiceStartedEventHandler']"/>
            <xsl:apply-templates select="/process/element[@enclosing=$sbbClass]/annotation[@name='javax.slee.annotation.event.ProfileAddedEventHandler']"/>
            <xsl:apply-templates select="/process/element[@enclosing=$sbbClass]/annotation[@name='javax.slee.annotation.event.ProfileUpdatedEventHandler']"/>
            <xsl:apply-templates select="/process/element[@enclosing=$sbbClass]/annotation[@name='javax.slee.annotation.event.ProfileRemovedEventFiring']"/>

            <xsl:for-each select="//element[@enclosing=$sbbACI]/annotation[@name='javax.slee.annotation.ActivityContextAttributeAlias' and count(@attribute-name)=0 ]">
                <activity-context-attribute-alias>
         
                    <description>
         
                        <xsl:value-of select="element[@name='description']/@value"/>
                    </description>
                    <attribute-alias-name>
         
                        <xsl:value-of select="@processed-value"/>
                    </attribute-alias-name>
                    <xsl:variable name="aliasName" select="@processed-value"/>
                    <xsl:for-each select="//element[@enclosing=$sbbACI]/annotation[@name='javax.slee.annotation.ActivityContextAttributeAlias' and @attribute-name=$aliasName]">
                        <sbb-activity-context-attribute-name>
         
                            <xsl:value-of select="@processed-value"/>
                        </sbb-activity-context-attribute-name>
                    </xsl:for-each>
                </activity-context-attribute-alias>
            </xsl:for-each>
 
            <xsl:apply-templates select="//element[@enclosing=$sbbClass]/annotation[@name='javax.slee.annotation.EnvEntry']"/>
            <xsl:if test="element/annotation[@name='javax.slee.annotation.ResourceAdaptorTypeBinding']">
                <xsl:for-each select="element/annotation[@name='javax.slee.annotation.ResourceAdaptorTypeBinding']">
                    <resource-adaptor-type-binding>

                        <description/>
                        <xsl:apply-templates select="element/annotation[@name='javax.slee.annotation.ResourceAdaptorTypeRef']"/>
                        <xsl:if test="element[@name='aciFactoryName']">
                            <activity-context-interface-factory-name> 
         
                                <xsl:value-of select="element[@name='aciFactoryName']/@value"/>
                            </activity-context-interface-factory-name>
                        </xsl:if>
                        <xsl:if test="element[@name='raEntityLink']">
                            <resource-adaptor-entity-binding>
                                <resource-adaptor-object-name> 
                                    <xsl:value-of select="element[@name='raObjectName']/@value"/>
                                    <!--
                                    <xsl:text>slee/resources/</xsl:text>
                                        <xsl:value-of select="generate-id(key('raEntityLink',concat(element/annotation[@name='javax.slee.annotation.ResourceAdaptorTypeRef']/element[@name='name']/@value,element/annotation[@name='javax.slee.annotation.ResourceAdaptorTypeRef']/element[@name='vendor']/@value,element/annotation[@name='javax.slee.annotation.ResourceAdaptorTypeRef']/element[@name='version']/@value)))"/>
                                        TODO make this automatic-->
                                </resource-adaptor-object-name>
                                <resource-adaptor-entity-link>
                                    <xsl:value-of select="element[@name='raEntityLink']/@value"/>
                                </resource-adaptor-entity-link>
                            </resource-adaptor-entity-binding>
                        </xsl:if>
                    </resource-adaptor-type-binding>
                </xsl:for-each>
            </xsl:if>
            <xsl:apply-templates select="element/annotation[@name='javax.slee.annotation.EJBRef']"/>
        </sbb>
    </xsl:template>
</xsl:stylesheet>
