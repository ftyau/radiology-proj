<HTML>
<HEAD>
<TITLE>Execute Queries</TITLE>
</HEAD>
<BODY>
<%@ page import="java.sql.*" %>


<H1><CENTER>Queries</CENTER></H1>


<select>
  <option value="Users">Users</option>
  <option value="Persons">Persons</option>
  <option value="familyDoctor">Family Doctor</option>
</select>

<form name="query" method="GET">
<TABLE ALIGN="CENTER">
  <TR>
    <TH>SQL Statement: </TH>
    <TD><input name="SQLStatement" type="textfield" size="30"></input></TD>
  </TR>
  <TR>
    <TD ALIGN=CENTER COLSPAN="2"><input type="submit" name=".submit" value="Enter"></TD>
  </TR>
  
</TABLE>
</BODY>
</HTML>