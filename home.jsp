<HTML>
<HEAD>
<TITLE>HomeScreen</TITLE>
</HEAD>

<BODY>
<!--This is the login page-->
<H1><CENTER>Welcome</CENTER></H1>
<TABLE ALIGN="RIGHT">
    <TABLE  BORDER="1">
       <TR>
          <TH COLSPAN="2">
             <H4><BR>Navigation</H4>
       </TR>

       <!--Personal Info -->
       <TR>
       	<TD><a href="/radiology-proj/login/user.jsp">Personal Info</a></TD>
        <% String tempID = (String)(session.getAttribute("id"));%>
        <% session.setAttribute("id",tempID);%>
       </TR>

       <!-- User Management -->
       <TR>
       	<TD><a href="">System Admin Panel</a></TD>
       </TR>
        
        <!-- Report Generator -->
       <TR>
       	<TD><a href="">Report Generator</a></TD>
       </TR>

        <!-- Upload Picture Module -->
       <TR>
       	<TD><a href="/radiology-proj/images/upload.html">Upload Pictures</a></TD>
       </TR>
	   
	   <!-- View Images -->
	   <TR>
		<TD><a href="/radiology-proj/images/images">View Pictures</a></TD>
	   </TR>

        <!-- Search Module -->
       <TR>
        <TD><a href="">Search Database</a></TD>
       </TR>

   </TABLE>
</TABLE>
</BODY>
</HTML>
