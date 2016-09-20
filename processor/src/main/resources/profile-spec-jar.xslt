<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" indent="yes" doctype-public="-//Sun Microsystems, Inc.//DTD JAIN SLEE Profile Specification 1.1//EN" doctype-system="http://java.sun.com/dtd/slee-profile-spec-jar_1_1.dtd"/>

    <xsl:import href="resource:common.xslt"/>

    <xsl:strip-space elements="."/>

    <xsl:key name="usageKey" match="/process/element[@kind='METHOD']/annotation[@name='javax.slee.annotation.UsageParameter']" use="concat(@processed-value,../@enclosing)"/>
    <xsl:template match="/">
        <profile-spec-jar>
            <description><xsl:text>Profile Spec Definitions Auto-Generated </xsl:text><xsl:value-of select="/process/@generatedTime"/></description>
         
            <xsl:apply-templates select="node()/element[@kind='CLASS' or @kind='INTERFACE']/annotation[@name='javax.slee.annotation.ProfileSpec']"/>
            <xsl:apply-templates select="node()/element[@kind='CLASS' or @kind='INTERFACE']/annotation[@name='javax.slee.annotation.ProfileSpec']/element[@name='securityPermissions']"/>
        </profile-spec-jar>
    </xsl:template>

    <xsl:template match="annotation[@name='javax.slee.annotation.ProfileSpec']">
        <xsl:variable name="profileSpecClass" select="@value"/>
        <xsl:variable name="profileTableInterface" select="element[@name='tableInterface']/@value"/>
        <xsl:variable name="cmpInterface" select="element[@name='cmpInterface']/@value"/>

        <profile-spec>
            <xsl:if test="element[@name='id']/@value">
                    <xsl:attribute name="id">
                        <xsl:value-of select="element[@name='id']/@value"/>
                    </xsl:attribute>
            </xsl:if>

            <xsl:if test="element[@name='readOnly']">
                <xsl:attribute name="profile-read-only">
                    <xsl:value-of select="element[@name='readOnly']/@value"/>
                </xsl:attribute>
            </xsl:if>

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
            
            <xsl:apply-templates select="element[@name='libraryRefs']/annotation[@name='javax.slee.annotation.LibraryRef']"/>

            <xsl:apply-templates select="element[@name='profileSpecRefs']/annotation[@name='javax.slee.annotation.ProfileSpecRef']"/>
            <xsl:for-each select="element/annotation[@name='javax.slee.annotation.Collator']">
                <collator>
                    <xsl:attribute name="strength">
                        <xsl:value-of select="element[@name='strength']/@value"/>
                    </xsl:attribute>
                    <xsl:attribute name="decomposition">
                        <xsl:value-of select="element[@name='decomposition']/@value"/>
                    </xsl:attribute>
                    <description>
                        <xsl:value-of select="element[@name='description']/@value"/>
                    </description>
                    <collator-alias>
                        <xsl:value-of select="element[@name='collatorAlias']/@value"/>
                    </collator-alias>

                    <locale-language>
                        <xsl:value-of select="element[@name='localeLanguage']/@value"/>
                    </locale-language>
                    <xsl:if test="element[@name='localeCountry' and not(@value='')]">
                        <locale-country>
                            <xsl:value-of select="element[@name='localeCountry']/@value"/>
                        </locale-country>
                    </xsl:if>
                    <xsl:if test="element[@name='localeVariant' and not(@value='')]">
                        <locale-variant>
                            <xsl:value-of select="element[@name='localeVariant']/@value"/>
                        </locale-variant>
                    </xsl:if>
                </collator>
            </xsl:for-each>

            <profile-classes>
                <profile-cmp-interface>
                    <profile-cmp-interface-name>
                        <xsl:value-of select="$cmpInterface"/>
                    </profile-cmp-interface-name>
                    <xsl:for-each select="/process/element[@kind='METHOD' and @enclosing=$cmpInterface]/annotation[@name='javax.slee.annotation.ProfileCMPField']">
                        <cmp-field>
                            <xsl:attribute name="unique">
                                <xsl:value-of select="element[@name='unique']/@value"/>
                            </xsl:attribute>
                            <cmp-field-name>
                                <xsl:value-of select="@processed-value"/>
                            </cmp-field-name>

                            <xsl:if test="element[@name='indexHint' and not(@value='NONE')]">
                                <index-hint>
                                    <xsl:attribute name="query-operator">
                                        <xsl:value-of select="element[@name='indexHint']/@value"/>
                                    </xsl:attribute>
                                </index-hint>
                            </xsl:if>
                        </cmp-field>
                    </xsl:for-each>
                </profile-cmp-interface>
                <xsl:if test="element[@name='localInterface' and not(@value='javax.slee.profile.ProfileLocalObject')]">
                    <profile-local-interface>
                        <xsl:attribute name="isolate-security-permissions">
                            <xsl:value-of select="element[@name='isolateSecurityPermissions']/@value"/>
                        </xsl:attribute>
                        <profile-local-interface-name> 
                            <xsl:value-of select="element[@name='localInterface']/@value"/>
                        </profile-local-interface-name>
                    </profile-local-interface>
                </xsl:if>
                <xsl:if test="element[@name='managementInterface' and not(@value='java.lang.Object')]/@value">
                    <profile-management-interface>
                        <profile-management-interface-name>
                            <xsl:value-of select="element[@name='managementInterface']/@value"/>
                        </profile-management-interface-name>
                    </profile-management-interface>
                </xsl:if>

                <xsl:if test="element[@name='abstractClass' and not(@value='javax.slee.profile.Profile')]">
                    <profile-abstract-class>
                        <xsl:if test="../annotation[@name='javax.slee.annotation.Reentrant']">
                            <xsl:attribute name="reentrant">True</xsl:attribute>
                        </xsl:if>
                        <profile-abstract-class-name>
                            <xsl:value-of select="element[@name='abstractClass']/@value"/>
                        </profile-abstract-class-name>
                    </profile-abstract-class>
                </xsl:if>
                <xsl:if test="element[@name='tableInterface' and not(@value='javax.slee.profile.ProfileTable')]">
                    <profile-table-interface>
                        <profile-table-interface-name>
                            <xsl:value-of select="element[@name='tableInterface']/@value"/>
                        </profile-table-interface-name>
                    </profile-table-interface>
                </xsl:if>
        
                <xsl:if test="element[@name='usageParametersInterface' and not(@value='java.lang.Object')]">
                    <xsl:variable name="usageParametersInterface" select="element[@name='usageParametersInterface']/@value"/>
                    <profile-usage-parameters-interface>
                        <profile-usage-parameters-interface-name>
                            <xsl:value-of select="$usageParametersInterface"/> 
                        </profile-usage-parameters-interface-name>

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
                    </profile-usage-parameters-interface>
                </xsl:if>
            </profile-classes>

            <xsl:apply-templates select="//element[@enclosing=$profileSpecClass]/annotation[@name='javax.slee.annotation.EnvEntry']"/>

            <xsl:for-each select="/process/element[@enclosing=$profileSpecClass or @enclosing=$profileTableInterface]/query">
                <xsl:copy-of select="."/>
            </xsl:for-each>

            <profile-hints>
                <xsl:attribute name="single-profile">
                    <xsl:value-of select="element[@name='singleProfileHint']/@value"/>
                </xsl:attribute>
            </profile-hints> 
        </profile-spec>
    </xsl:template>

</xsl:stylesheet>
