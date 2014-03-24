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
       	<TD><a href="/radiology-proj/Personal">Personal Info</a></TD>
        <% String tempID = (String)(session.getAttribute("id"));%>
        <% session.setAttribute("id",tempID);%>
       </TR>

       <!-- User Management -->
       <TR>
       	<TD><a href="/radiology-proj/AdminPanel">System Admin Panel</a></TD>
       </TR>
        
		<!-- Upload Picture Module -->
        <TR>
      		<TD><a href="/radiology-proj/upload">Upload Pictures</a></TD>
        </TR>
		
	   <!-- View Images (unneeded for project)
		<TR>
			<TD><a href="/radiology-proj/images/images">View Pictures</a></TD>
		</TR>
		-->
		
       <!-- Search Module -->
        <TR>
        	<TD><a href="/radiology-proj/search">Search Database</a></TD>
        </TR> 

		<% String user_class = (String) session.getAttribute("class");%>
		<% if (user_class.equals("a")) {%>
		<TR>
			<TD><a href="/radiology-proj/olap">Data Analysis</a></TD>
		</TR>
		<% }%>
     </TABLE>
</TABLE>
</BODY>
</HTML>
