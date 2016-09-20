<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:import href="resource:common.xslt"/>

    <xsl:output method="xml" indent="yes" doctype-public="-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor 1.1//EN" doctype-system="http://java.sun.com/dtd/slee-resource-adaptor-jar_1_1.dtd"/>

    <xsl:strip-space elements="."/>

    <xsl:key name="usageKey" match="/process/element[@kind='METHOD']/annotation[@name='javax.slee.annotation.UsageParameter']" use="concat(@processed-value,../@enclosing)"/>
    
    <xsl:template match="/">
        <resource-adaptor-jar>
            <description><xsl:text>Resource Adaptor Definitions Auto-Generated</xsl:text><xsl:value-of select="/process/@generatedTime"/></description>
                <xsl:apply-templates select="node()/element[@kind='CLASS']/annotation[@name='javax.slee.annotation.ResourceAdaptor']"/>
                <xsl:apply-templates select="node()/element[@kind='CLASS']/annotation[@name='javax.slee.annotation.ResourceAdaptor']/element[@name='securityPermissions']"/>
            </resource-adaptor-jar>

        </xsl:template>

    
        <xsl:template match="annotation[@name='javax.slee.annotation.ResourceAdaptor']">
            <resource-adaptor>
            <xsl:if test="element[@name='id']/@value">
                    <xsl:attribute name="id">
                        <xsl:value-of select="element[@name='id']/@value"/>
                    </xsl:attribute>
            </xsl:if>

                <description>
                    <xsl:value-of select="element[@name='description']/@value"/>
                </description>
                <resource-adaptor-name>
                    <xsl:value-of select="element[@name='name']/@value"/>
                </resource-adaptor-name>
                <resource-adaptor-vendor> 
                    <xsl:value-of select="element[@name='vendor']/@value"/>
                </resource-adaptor-vendor>
                <resource-adaptor-version> 
                    <xsl:value-of select="element[@name='version']/@value"/> 
                </resource-adaptor-version>

                <xsl:apply-templates select="element[@name='typeRefs']/annotation[@name='javax.slee.annotation.ResourceAdaptorTypeRef']"/>
                <xsl:apply-templates select="element[@name='libraryRefs']/annotation[@name='javax.slee.annotation.LibraryRef']"/>
                <xsl:apply-templates select="element[@name='profileSpecRefs']/annotation[@name='javax.slee.annotation.ProfileSpecRef']"/>

                <xsl:variable name="resourceAdaptorClass" select="../@name"/>

                <resource-adaptor-classes>
                    <resource-adaptor-class>
                        <resource-adaptor-class-name>
                            <xsl:value-of select="$resourceAdaptorClass"/> 
                        </resource-adaptor-class-name>
                    </resource-adaptor-class>

                    <xsl:if test="element[@name='usageParametersInterface' and not(@value='java.lang.Object')]">
                        <xsl:variable name="usageParametersInterface" select="element[@name='usageParametersInterface']/@value"/>
                        <resource-adaptor-usage-parameters-interface>
                            <resource-adaptor-usage-parameters-interface-name>
                                <xsl:value-of select="$usageParametersInterface"/> 
                            </resource-adaptor-usage-parameters-interface-name>

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
                        </resource-adaptor-usage-parameters-interface>
                    </xsl:if>
                </resource-adaptor-classes>
                <xsl:if test="/process/element[@kind='FIELD' and @enclosing=$resourceAdaptorClass]/annotation[@name='javax.slee.annotation.ConfigProperty']">
                    <config-property>
                        <xsl:for-each select="/process/element[@kind='FIELD' and @enclosing=$resourceAdaptorClass]/annotation[@name='javax.slee.annotation.ConfigProperty']">
                            <config-property-name>
                                <xsl:value-of select="../@name"/>
                            </config-property-name>
                            <config-property-type>
                                <xsl:value-of select="../@type"/>
                            </config-property-type>
                            <config-property-value>
                                <xsl:if test="element[@name='defaultValue']">
                                    <xsl:value-of select="element[@name='defaultValue']/@value"/>
                                </xsl:if>
                            </config-property-value>
                        </xsl:for-each>
                    </config-property>
                </xsl:if>
            </resource-adaptor>

        </xsl:template>
    </xsl:stylesheet>
