<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:include href="resource:common.xslt"/>
    <xsl:output method="xml" indent="yes" doctype-public="-//Sun Microsystems, Inc.//DTD JAIN SLEE Event 1.1//EN" doctype-system="http://java.sun.com/dtd/slee-event-jar_1_1.dtd"/>
    <xsl:strip-space elements="."/>
    <xsl:template match="/">
        <event-jar>
            <description>
                <xsl:text>Event Definitions Auto-Generated </xsl:text>
                <xsl:value-of select="process/@generatedTime"/>
            </description>
            <xsl:apply-templates select="node()/element[@kind='CLASS']/annotation[@name='javax.slee.annotation.event.EventType']/element[@name='libraryRefs']/annotation[@name='javax.slee.annotation.LibraryRef']"/>
            <xsl:apply-templates select="node()/element[@kind='CLASS']/annotation[@name='javax.slee.annotation.event.EventType']"/>
        </event-jar>
    </xsl:template>
    <xsl:template match="annotation[@name='javax.slee.annotation.event.EventType']">
        <event-definition>
            <xsl:if test="element[@name='id']/@value">
                    <xsl:attribute name="id">
                        <xsl:value-of select="element[@name='id']/@value"/>
                    </xsl:attribute>
            </xsl:if>

            <description>
                <xsl:value-of select="element[@name='description']/@value"/>
            </description>
         
            <event-type-name>
                <xsl:value-of select="element[@name='name']/@value"/>
            </event-type-name>
            <event-type-vendor>
         
                <xsl:value-of select="element[@name='vendor']/@value"/>
            </event-type-vendor>
            <event-type-version>
                <xsl:value-of select="element[@name='version']/@value"/> 
            </event-type-version>
            <event-class-name>
                <xsl:choose>
                    <xsl:when test="element[@name='eventClass']/@value = 'java.lang.Void'">
                        <xsl:value-of select="ancestor::element/@name"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="element[@name='eventClass']/@value"/> 
                    </xsl:otherwise>
                </xsl:choose>
            </event-class-name>
        </event-definition>
    </xsl:template>
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>
