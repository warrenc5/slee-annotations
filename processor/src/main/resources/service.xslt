<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" indent="yes" doctype-public="-//Sun Microsystems, Inc.//DTD JAIN SLEE Service 1.1//EN" doctype-system="http://java.sun.com/dtd/slee-service_1_1.dtd" xalan:indent-amount="2" xmlns:xalan="http://xml.apache.org/xalan" />

    <xsl:template match="/">
        <service-xml>
            <description><xsl:text>Service Auto-Generated </xsl:text><xsl:value-of select="/process/@generatedTime"/></description>
         
            <xsl:apply-templates select="node()/element[@kind='CLASS']/annotation[@name='mobi.mofokom.javax.slee.annotation.Service']"/>
        </service-xml>
    </xsl:template>
    
    <xsl:template match="annotation[@name='mobi.mofokom.javax.slee.annotation.Service']">
        <service>
            <xsl:if test="element[@name='id']/@value">
                    <xsl:attribute name="id">
                        <xsl:value-of select="element[@name='id']/@value"/>
                    </xsl:attribute>
            </xsl:if>

                <description>
                    <xsl:value-of select="element[@name='description']/@value"/>
                </description>
         
            <service-name>
         
                <xsl:value-of select="element[@name='name']/@value"/>
            </service-name>
            <service-vendor>
         
                <xsl:value-of select="element[@name='vendor']/@value"/>
            </service-vendor>
            <service-version>
         
                <xsl:value-of select="element[@name='version']/@value"/> 
            </service-version>
            <xsl:variable name="name" select="element[@name='rootSbb']/@value"/>
            <xsl:apply-templates select="/process/element[@kind='CLASS' and @name=$name]/annotation[@name='mobi.mofokom.javax.slee.annotation.Sbb']"/>
            <default-priority>
         
                <xsl:value-of select="element[@name='defaultPriority']/@value"/> 
            </default-priority>
            <xsl:if test="element[@name='addressProfileTable' and not(@value='')]">
                <address-profile-table> 
         
                    <xsl:value-of select="element[@name='addressProfileTable']/@value"/> 
                </address-profile-table>
            </xsl:if>
        </service>
    </xsl:template>
    <xsl:template match="annotation[@name='mobi.mofokom.javax.slee.annotation.Sbb']">
        <root-sbb>
            <sbb-name>
                <xsl:value-of select="element[@name='name']/@value"/>
            </sbb-name>
            <sbb-vendor> 
         
                <xsl:value-of select="element[@name='vendor']/@value"/>
            </sbb-vendor>
            <sbb-version> 
         
                <xsl:value-of select="element[@name='version']/@value"/> 
            </sbb-version>
        </root-sbb>
    </xsl:template>
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>
