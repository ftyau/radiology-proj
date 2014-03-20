<HTML>
<HEAD>
<TITLE>Report Generator</TITLE>
</HEAD>
<BODY>
<%@ page import="java.sql.*,Database.dbConnection, java.util.Date,java.text.*" %>
<%
if(request.getParameter("bSubmit")!=null){
	
	/*
	dbConnection newDB = new dbConnection();
	Connection conn = newDB.connection(); 
	Statement stmt = conn.createStatement();
	*/
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MMM/dd");
	Date date = new Date();

	out.println(dateFormat.format(date));

	/*
SELECT DISTINCT p.FIRST_NAME,p.LAST_NAME,p.ADDRESS,p.PHONE, r.DIAGNOSIS,r.TEST_DATE
FROM PERSONS p, RADIOLOGY_RECORD r
WHERE p.PERSON_ID = r.PATIENT_ID and r.DIAGNOSIS LIKE '%tag';



*/
	
}
else{
	out.println("<H1><CENTER>Generate Report</CENTER></H1>");
    out.println("<P>Enter Diagnosis and/or date of test</P>");

    out.println("<FORM METHOD=post ACTION=generate.jsp>");

    out.println("<TABLE>");

    out.println("<TR VALIGN=TOP ALIGN=LEFT>");
    out.println("<TD><B>Diagnosis: </B></TD>");
    out.println("<TD><INPUT TYPE=text NAME=diagnosis><BR></TD>");
    out.println("</TR>");

    out.println("<TR VALIGN=TOP ALIGN=LEFT>");
    out.println("<TD><B>Test Date: </B></TD>");
    out.println("<TD><INPUT TYPE=text NAME=inputDate><BR></TD>");
    out.println("</TR>");

    out.println("</TABLE>");

    out.println("<INPUT TYPE=submit NAME=bSubmit VALUE=Submit>");
    out.println("</FORM>");
    out.println("<HR>");
}
%>