<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:import href="resource:common.xslt"/>

    <xsl:output method="xml" indent="yes" doctype-public="-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor Type 1.1//EN" doctype-system="http://java.sun.com/dtd/slee-resource-adaptor-type-jar_1_1.dtd"/>

    <xsl:strip-space elements="."/>

    <xsl:template match="/">
        <resource-adaptor-type-jar>
            <xsl:apply-templates select="node()/element[@kind='CLASS']/annotation[@name='javax.slee.annotation.ResourceAdaptorType']"/>
        </resource-adaptor-type-jar>

    </xsl:template>

    <xsl:template match="annotation[@name='javax.slee.annotation.ResourceAdaptorType']">
        <resource-adaptor-type>

            <xsl:if test="element[@name='id']/@value">
                    <xsl:attribute name="id">
                        <xsl:value-of select="element[@name='id']/@value"/>
                    </xsl:attribute>
            </xsl:if>

            <description>Resource Adaptor Definitions Auto-Generated <xsl:value-of select="/process/@generatedTime"/>
            </description>
            <resource-adaptor-type-name>
                <xsl:value-of select="element[@name='name']/@value"/>
            </resource-adaptor-type-name>
            <resource-adaptor-type-vendor> 
                <xsl:value-of select="element[@name='vendor']/@value"/>
            </resource-adaptor-type-vendor>
            <resource-adaptor-type-version> 
                <xsl:value-of select="element[@name='version']/@value"/> 
            </resource-adaptor-type-version>

            <xsl:apply-templates select="element[@name='libraryRefs']/annotation[@name='javax.slee.annotation.LibraryRef']"/>

            <xsl:variable name="resourceAdaptorTypeClass" select="../@name"/>

            <resource-adaptor-type-classes>
                <description>
                </description>
                <xsl:for-each select="//activitycontextinterface[@enclosing=$resourceAdaptorTypeClass]">
                    <activity-type>
                        <description>
                        </description>
                        <activity-type-name>
                            <xsl:value-of select="@activity"/>
                        </activity-type-name>
                    </activity-type>
                </xsl:for-each>

                <xsl:if test="element[@kind='METHOD' and @name='aciFactory']">
                    <activity-context-interface-factory-interface>
                        <description>
                        </description>
                        <activity-context-interface-factory-interface-name>
                            <xsl:value-of select="element[@kind='METHOD' and @name='aciFactory']/@value"/>
                        </activity-context-interface-factory-interface-name>
                    </activity-context-interface-factory-interface>
                </xsl:if>
                <xsl:if test="element[@kind='METHOD' and @name='raInterface']">
                    <resource-adaptor-interface>
                        <description>
                        </description>
                        <resource-adaptor-interface-name>
                            <xsl:value-of select="element[@kind='METHOD' and @name='raInterface']/@value"/>
                        </resource-adaptor-interface-name>
                    </resource-adaptor-interface>
                </xsl:if>
            </resource-adaptor-type-classes>
            
            <xsl:apply-templates select="element[@name='events']/annotation[@name='javax.slee.annotation.event.EventTypeRef']"/>
        </resource-adaptor-type>
    </xsl:template>

</xsl:stylesheet>
