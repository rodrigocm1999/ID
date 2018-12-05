<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:template match="/">
      <html>

      <body>
         <h2>Brasão dos Municípios</h2>
         <table border="1">
            <xsl:for-each select="municipios/municipio">
               <tr>
                  <td><img src="{brasao}" /></td>
               </tr>
            </xsl:for-each>
         </table>
      </body>

      </html>
   </xsl:template>
</xsl:stylesheet>
