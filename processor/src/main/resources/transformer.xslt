<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes" xalan:indent-amount="2" xmlns:xalan="http://xml.apache.org/xalan" />
	<!--variables -->
  <!--
  <xsl:param name="raentityname"/>
  -->
	<!-- templates -->
	<xsl:template match="/event-jar">
        <xsl:apply-templates select="node()" mode="xml-ra"/>
        <xsl:apply-templates select="node()" mode="code"/>
        <xsl:apply-templates select="node()" mode="xml"/>
        <xsl:apply-templates select="node()" mode="code-ra"/>
        <xsl:apply-templates select="node()" mode="code-ra2"/>
        <xsl:apply-templates select="node()" mode="code-ra3"/>
	</xsl:template>

	<xsl:template match="event-definition" mode="code">
            public void on<xsl:value-of select="translate(normalize-space(event-type-name),' .','')"/> (<xsl:value-of select="translate(normalize-space(event-class-name),' ','')"/> event, ActivityContextInterface aci,EventContext context){ 
              tracer.info("event <xsl:value-of select="normalize-space(event-type-name)"/>" );
            }

	</xsl:template>

  	<xsl:template match="event-definition" mode="code-ra"> eventType<xsl:value-of select="translate(normalize-space(event-type-name),' .','')"/> = super.raContext.getEventLookupFacility().getFireableEventType(new EventTypeID("<xsl:value-of select="normalize-space(event-type-name)"/>" ,super.raContext.getResourceAdaptorTypes()[0].getVendor(), super.raContext.getResourceAdaptorTypes()[0].getVersion())); </xsl:template>

  	<xsl:template match="event-definition" mode="code-ra2"> eventType<xsl:value-of select="translate(normalize-space(event-type-name),' .','')"/>.getEventType(), </xsl:template>

  	<xsl:template match="event-definition" mode="code-ra3">eventType<xsl:value-of select="translate(normalize-space(event-type-name),' .','')"/> ,</xsl:template>

	<xsl:template match="event-definition" mode="xml">
    <event event-direction="Receive" initial-event="False">
            <event-name> <xsl:value-of select="translate(normalize-space(event-type-name),' .','')"/> </event-name>
            <event-type-ref>
                <event-type-name><xsl:value-of select="normalize-space(event-type-name)"/></event-type-name>
                <event-type-vendor><xsl:value-of select="normalize-space(event-type-vendor)"/></event-type-vendor>
                <event-type-version><xsl:value-of select="normalize-space(event-type-version)"/></event-type-version>
            </event-type-ref>
            <!--
            <initial-event-select variable="ActivityContext"/>
            -->
        </event>
	</xsl:template>

	<xsl:template match="event-definition" mode="xml-ra">
            <event-type-ref>
                <event-type-name><xsl:value-of select="normalize-space(event-type-name)"/></event-type-name>
                <event-type-vendor><xsl:value-of select="normalize-space(event-type-vendor)"/></event-type-vendor>
                <event-type-version><xsl:value-of select="normalize-space(event-type-version)"/></event-type-version>
            </event-type-ref>
	</xsl:template>

	<xsl:template match="@*|node()" mode="xml">
    <!--
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
    -->
	</xsl:template>

</xsl:stylesheet>
