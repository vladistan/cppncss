<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/">
      <html>
        <head>
          <title>CppNcss Measurement Results</title>
        </head>
      <body>
        <xsl:apply-templates/>
      </body>
      </html>
    </xsl:template>

    <xsl:template match="/cppncss">

      <h1>Summary</h1>
      <table class="bodyTable">
        <xsl:for-each select="measure/sum">
        <tr>
          <xsl:call-template name="alternated-row"/>
          <th><xsl:value-of select="@label"/> total</th>
          <td><xsl:value-of select="@value"/></td>
        </tr>
        </xsl:for-each>
      </table>

      <xsl:for-each select="measure">

        <h1><xsl:value-of select="@type"/>s</h1>
        <h2>Top
        <xsl:call-template name='to-lower'>
            <xsl:with-param name='string' select='@type'/>
        </xsl:call-template>s containing the most NCSS</h2>
        <table class="bodyTable">
          <tr class="a">
            <xsl:for-each select="labels/label">
            <th><xsl:value-of select="."/></th>
            </xsl:for-each>
            <th><xsl:value-of select="@type"/></th>
          </tr>
          <xsl:for-each select="item">
          <tr>
            <xsl:call-template name="alternated-row"/>
            <xsl:for-each select="value">
            <td><xsl:value-of select="."/></td>
            </xsl:for-each>
            <td><xsl:value-of select="@name"/></td>
          </tr>
          </xsl:for-each>
        </table>

        <h2>Averages</h2>
        <table class="bodyTable">
          <xsl:for-each select="average">
          <tr>
            <xsl:call-template name="alternated-row"/>
            <th>Average <xsl:value-of select="@label"/></th>
            <td><xsl:value-of select="@value"/></td>
          </tr>
          </xsl:for-each>
        </table>

      </xsl:for-each>

    </xsl:template>

    <xsl:template name='to-lower'>
        <xsl:param name='string'/>
        <xsl:variable name="lcletters">abcdefghijklmnopqrstuvwxyz</xsl:variable>
        <xsl:variable name="ucletters">ABCDEFGHIJKLMNOPQRSTUVWXYZ</xsl:variable>
        <xsl:value-of select="translate($string,$ucletters,$lcletters)"/>
    </xsl:template>

    <xsl:template name="alternated-row">
      <xsl:attribute name="class">
        <xsl:if test="position() mod 2 = 1">a</xsl:if>
        <xsl:if test="position() mod 2 = 0">b</xsl:if>
      </xsl:attribute>
    </xsl:template>

</xsl:stylesheet>
