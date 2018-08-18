<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output xalan:indent-amount="2" xmlns:xalan="http://xml.apache.org/xalan" />

    <xsl:template match="element/annotation[@name='javax.slee.annotation.EJBRef']">
        <ejb-ref>
            <description>
                <xsl:value-of select="element[@name='description']/@value"/>
            </description>
            <ejb-ref-name>
                <xsl:value-of select="element[@name='name']/@value"/>
            </ejb-ref-name>
            <ejb-ref-type>
                <xsl:value-of select="element[@name='type']/@value"/>
            </ejb-ref-type>
            <home>
                <xsl:value-of select="element[@name='home']/@value"/> 
            </home>
            <remote>
                <xsl:value-of select="element[@name='remote']/@value"/> 
            </remote>
        </ejb-ref>
    </xsl:template>

    <xsl:template match="annotation[@name='javax.slee.annotation.EnvEntry']">
        <env-entry>
            <description>
                <xsl:value-of select="element[@name='description']/@value"/>
            </description>
            <env-entry-name>
                <xsl:value-of select="../@name"/>
            </env-entry-name>
            <env-entry-type>
                <xsl:value-of select="../@type"/>
            </env-entry-type>
            <env-entry-value>
                <xsl:choose>
                    <xsl:when test="@processed-value"> 
                        <xsl:value-of select="@processed-value"/> 
                    </xsl:when>
                    <xsl:when test="element[@name='value' and not(@value='')]"> 
                        <xsl:value-of select="element[@name='value']/@value"/> 
                    </xsl:when>
                </xsl:choose>
            </env-entry-value>
        </env-entry>
    </xsl:template>

    <xsl:template match="annotation[@name='javax.slee.annotation.event.EventTypeRef']"> 
        <event-type-ref>
            <event-type-name>
                <xsl:value-of select="element[@name='name']/@value"/>
            </event-type-name>
            <event-type-vendor>
                <xsl:value-of select="element[@name='vendor']/@value"/>
            </event-type-vendor>
            <event-type-version>
                <xsl:value-of select="element[@name='version']/@value"/> 
            </event-type-version>
        </event-type-ref>
    </xsl:template>

    <xsl:template match="annotation[@name='javax.slee.annotation.LibraryRef']">
        <library-ref>
            <description> 
                <xsl:value-of select="element[@name='description']/@value"/> 
            </description>
            <library-name>
                <xsl:value-of select="element[@name='name']/@value"/>
            </library-name>
            <library-vendor>
                <xsl:value-of select="element[@name='vendor']/@value"/>
            </library-vendor>
            <library-version>
                <xsl:value-of select="element[@name='version']/@value"/>
            </library-version>
        </library-ref>
    </xsl:template>

    <xsl:template match="annotation[@name='javax.slee.annotation.SbbRef']">
        <sbb-ref>
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
            <sbb-alias>
                <xsl:value-of select="element[@name='alias']/@value"/>
            </sbb-alias>
        </sbb-ref>
    </xsl:template>
    <xsl:template match="annotation[@name='javax.slee.annotation.ProfileSpecRef']" mode="sbb">
        <profile-spec-ref>
            <description> 
                <xsl:value-of select="element[@name='description']/@value"/> 
            </description>
            <profile-spec-name>
                <xsl:value-of select="element[@name='name']/@value"/>
            </profile-spec-name>
            <profile-spec-vendor>
                <xsl:value-of select="element[@name='vendor']/@value"/>
            </profile-spec-vendor>
            <profile-spec-version>
                <xsl:value-of select="element[@name='version']/@value"/>
            </profile-spec-version>
            <profile-spec-alias>
                <xsl:value-of select="element[@name='alias']/@value"/>
            </profile-spec-alias>
        </profile-spec-ref>
    </xsl:template>
    <xsl:template match="annotation[@name='javax.slee.annotation.ProfileSpecRef']">
        <profile-spec-ref>
            <description> 
                <xsl:value-of select="element[@name='description']/@value"/> 
            </description>
            <profile-spec-name>
                <xsl:value-of select="element[@name='name']/@value"/>
            </profile-spec-name>
            <profile-spec-vendor>
                <xsl:value-of select="element[@name='vendor']/@value"/>
            </profile-spec-vendor>
            <profile-spec-version>
                <xsl:value-of select="element[@name='version']/@value"/>
            </profile-spec-version>
            <!--
            <profile-spec-alias>
                <xsl:value-of select="element[@name='alias']/@value"/>
            </profile-spec-alias>
            -->
        </profile-spec-ref>
    </xsl:template>
    <xsl:template match="annotation[@name='javax.slee.annotation.ResourceAdaptorTypeRef']">
        <resource-adaptor-type-ref>
            <!--  FIXME: only for ra 
            <description> 
                <xsl:value-of select="element[@name='description']/@value"/> 
            </description>
            -->
            <resource-adaptor-type-name>
                <xsl:value-of select="element[@name='name']/@value"/>
            </resource-adaptor-type-name>
            <resource-adaptor-type-vendor>
                <xsl:value-of select="element[@name='vendor']/@value"/>
            </resource-adaptor-type-vendor>
            <resource-adaptor-type-version>
                <xsl:value-of select="element[@name='version']/@value"/>
            </resource-adaptor-type-version>
        </resource-adaptor-type-ref>
    </xsl:template>
    <xsl:template match="element[@name='securityPermissions']">
        <xsl:if test="not(@value='')">
            <security-permissions>
                <description>
                </description>
                <security-permission-spec>
                    <xsl:value-of select="@value"/>
                </security-permission-spec>
            </security-permissions>
        </xsl:if>
    </xsl:template>
         
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
