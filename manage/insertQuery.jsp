<HTML>
<HEAD>
<TITLE>Insert Query</TITLE>
</HEAD>
<BODY>
<%@ page import="java.sql.*,Database.dbConnection,java.util.*" %>
<%! String tableType; %>
<%
if(request.getParameter("bSubmit") != null){

	
	String [] monfs = {"Jan","Feb","Mar","Apr","May","Jun","Jul",
										"Aug","Sept","Oct","Nov","Dec"};

	ArrayList<String> list1 = new ArrayList<String>();

	for(String s: monfs){
		if(s.equals("Apr") || s.equals("Jun") || s.equals("Sept") || s.equals("Nov")){
			list1.add("30");
		}
		else if(s.equals("Feb")){
			list1.add("28");
		}
		else{
			list1.add("31");
		}
		//out.println(s);
	}

	//Month
	out.println("<select name=month>");
	for(int i =0;i<monfs.length;i++){
		out.println("<option value="+list1.get(i)+ ">"+monfs[i]+"</option>");
	}
	out.println("</select>");
	//


	//End
    tableType = request.getParameter("tableType");

    //Establish Connection wif the DB
    dbConnection newDB = new dbConnection();
    Connection conn = newDB.connection();

    //users(user_name,password,class,person_id,date_registered)
    if(tableType.equals("users")){
		out.println("<H1><CENTER>Insert New User</CENTER></H1>");
		out.println("<P>Insert a new Username and Password and select a Class</P>");

        out.println("<FORM METHOD=post ACTION=insertQuery.jsp>");
		out.println("<TABLE>");

        out.println("<TR VALIGN=TOP ALIGN=LEFT>");
        out.println("<TD><B> Username: </B></TD>");
        out.println("<TD><INPUT TYPE=text NAME=username><BR></TD></TR>");

		out.println("<TR VALIGN=TOP ALIGN=LEFT>");
        out.println("<TD><B> Password: </B></TD>");
        out.println("<TD><INPUT TYPE=text NAME=pass><BR></TD></TR>");

		out.println("<TR VALIGN=TOP ALIGN=LEFT>");
        out.println("<select name=tableType id=dropdown>");
    	out.println("<option value=dropdown>Select Class</option>");
    	out.println("<option value=a>Admin</option>");
   	 	out.println("<option value=p>Patient</option>");
    	out.println("<option value=d>Doctor</option>");
    	out.println("<option value=r>Radiologist</option>");
    	out.println("</select></TR>");

		out.println("</TABLE>");
		out.println("<INPUT TYPE=submit NAME=submitQuery VALUE=Submit>");

        out.println("</FORM>");
    }

	//persons(person_id,first_name,last_name,address,email,phone)
    else if(tableType.equals("persons")){
		out.println("<H1><CENTER>Insert New Person</CENTER></H1>");
		out.println("<P>Please Fill Out All The Fields</P>");

		out.println("<FORM METHOD=post ACTION=insertQuery.jsp>");
		out.println("<TABLE>");

        out.println("<TR VALIGN=TOP ALIGN=LEFT>");
        out.println("<TD><B> First Name: </B></TD>");
        out.println("<TD><INPUT TYPE=text NAME=firstname><BR></TD></TR>");

		out.println("<TR VALIGN=TOP ALIGN=LEFT>");
        out.println("<TD><B> Last Name: </B></TD>");
        out.println("<TD><INPUT TYPE=text NAME=lastname><BR></TD></TR>");

		out.println("<TR VALIGN=TOP ALIGN=LEFT>");
        out.println("<TD><B> Address </B></TD>");
        out.println("<TD><INPUT TYPE=text NAME=address><BR></TD></TR>");

		out.println("<TR VALIGN=TOP ALIGN=LEFT>");
        out.println("<TD><B> Email </B></TD>");
        out.println("<TD><INPUT TYPE=text NAME=email><BR></TD></TR>");


		out.println("<TR VALIGN=TOP ALIGN=LEFT>");
        out.println("<TD><B> Phone </B></TD>");
        out.println("<TD><INPUT TYPE=text NAME=phone><BR></TD></TR>");

		out.println("</TABLE>");
		out.println("<INPUT TYPE=submit NAME=submitQuery VALUE=Submit>");

        out.println("</FORM>");
	
	}
    
    //family_doctor(doctor_id,patient_id)
    else if(tableType.equals("family_doctor")){

    	out.println("<H1><CENTER>Insert New Doctor</CENTER></H1>");
		out.println("<P>Please Fill Out All The Fields</P>");

		out.println("<FORM METHOD=post ACTION=insertQuery.jsp>");
		out.println("<TABLE>");

        out.println("<TR VALIGN=TOP ALIGN=LEFT>");
        out.println("<TD><B> Doctor ID: </B></TD>");
        out.println("<TD><INPUT TYPE=text NAME=docID><BR></TD></TR>");

		out.println("<TR VALIGN=TOP ALIGN=LEFT>");
        out.println("<TD><B> Patient ID </B></TD>");
        out.println("<TD><INPUT TYPE=text NAME=patientID><BR></TD></TR>");
		out.println("</TABLE>");

		out.println("<INPUT TYPE=submit NAME=submitQuery VALUE=Submit>");
        out.println("</FORM>");
    }
}

else{

	out.println("<H1><CENTER>Insert Queries</CENTER></H1>");
	out.println("<FORM method=post action=insertQuery.jsp name=tableForm>");
    out.println("<select name=tableType id=dropdown>");
    out.println("<option value=dropdown>Select Table</option>");
    out.println("<option value=users>User</option>");
    out.println("<option value=persons>Persons</option>");
    out.println("<option value=family_doctor>Family Doctor</option>");

    out.println("</select>");
    out.println("<input type=submit value=Enter NAME=bSubmit>");

	out.println("</FORM>");
}
%>

</BODY>
</HTML>