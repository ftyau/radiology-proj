<HTML>
<HEAD>
<TITLE>Report Generator</TITLE>
</HEAD>
<BODY>
<%@ page import="java.sql.*,Database.dbConnection, java.util.*" %>
<%
if(request.getParameter("bSubmit")!=null){
	dbConnection newDB = new dbConnection();
	Connection conn = newDB.connection();

	ArrayList<ArrayList<String>> outer = new ArrayList<ArrayList<String>>();
	
	String diagnosis = request.getParameter("diagnosis");
	out.println(diagnosis);
	
	ResultSet results = null;
	try{

		String sql = "SELECT DISTINCT FIRST_NAME,LAST_NAME,ADDRESS,PHONE FROM PERSONS p, RADIOLOGY_RECORD r " 
					+"WHERE p.PERSON_ID = r.PATIENT_ID  AND r.DIAGNOSIS LIKE" + "'%" + diagnosis + "%'" ;

       	Statement stmt = conn.createStatement();
        results = stmt.executeQuery(sql);

        while(results != null && results.next()){
    		String firstname = results.getString("FIRST_NAME");
    		String lastname  = results.getString("LAST_NAME");
    		String address = results.getString("ADDRESS");
    		String phone = results.getString("PHONE");
    		
    		ArrayList<String> inner = new ArrayList<String>();
    		inner.add(firstname);
    		inner.add(lastname);
    		inner.add(address);
    		inner.add(phone);

    		outer.add(inner);
	
    		out.println("firstname: " + firstname);
    		out.println("lastname: " + lastname);
    		out.println("address: " + address);
    		out.println("phone: " + phone);
    
    	}
		stmt.close();
        conn.close();
    }catch(SQLException e){
    	e.printStackTrace();
    }
	//Print data out into a table
	out.println("<H1><CENTER>Report</CENTER></H1>");
	out.println("<table border=1>");
	out.println("<TR>");
	out.println("<TH>First Name</TH>");
	out.println("<TH>Last Name</TH>");
	out.println("<TH>Address</TH>");
	out.println("<TH>Phone Number</TH>");
	out.println("</TR>");
	out.println("<BR>");

	for(int i =0; i<outer.size();i++){
		out.println("<TR>");
		out.println("<TH>" + outer.get(i).get(0)+ "</TH>"); //First Name
		out.println("<TH>" + outer.get(i).get(1)+ "</TH>"); //Last Name
		out.println("<TH>" + outer.get(i).get(2)+ "</TH>"); //Address
		out.println("<TH>" + outer.get(i).get(3)+ "</TH>"); //Phone
		out.println("</TR>");
	}
	out.println("</TABLE>");
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