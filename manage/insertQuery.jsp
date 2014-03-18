<HTML>
<HEAD>
<TITLE>Insert Query</TITLE>
</HEAD>
<BODY>
<%@ page import="java.sql.*,Database.dbConnection,java.util.*" %>
<%! String tableType; %>

<%
 tableType = request.getParameter("tableType");
if(request.getParameter("bSubmit") != null){
   	//persons(person_id,first_name,last_name,address,email,phone)
    if(tableType.equals("persons")){
    	out.println(tableType);
	    	out.println("<H1><CENTER>Insert New Person</CENTER></H1>");
			out.println("<P>Please Fill Out All The Fields</P>");

			out.println("<FORM METHOD=POST ACTION=insertQuery.jsp>");
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
			out.println("<INPUT TYPE=submit NAME=submitQueryPerson VALUE=Submit>");

	        out.println("</FORM>");
    }
	 //users(user_name,password,class,person_id,date_registered)
    else if(tableType.equals("users")){

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
        out.println("<select name=classType id=dropdown>");
    	out.println("<option value=dropdown>Select Class</option>");
    	out.println("<option value=a>Admin</option>");
   	 	out.println("<option value=p>Patient</option>");
    	out.println("<option value=d>Doctor</option>");
    	out.println("<option value=r>Radiologist</option>");
    	out.println("</select></TR>");

		out.println("</TABLE>");
		out.println("<INPUT TYPE=submit NAME=submitQueryUser VALUE=Submit>");

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

		out.println("<INPUT TYPE=submit NAME=submitQueryDoctor VALUE=Submit>");
        out.println("</FORM>");
    }
}
else{

	dbConnection newDB = new dbConnection();
    Connection conn = newDB.connection();
    //persons(person_id,first_name,last_name,address,email,phone)
	if(request.getParameter("submitQueryPerson")!= null){
			out.println("Persons");
			String firstname = request.getParameter("firstname");
			String lastname = request.getParameter("lastname");
			String address = request.getParameter("address");
			String email = request.getParameter("email");
			String phone = request.getParameter("phone");
			/*
			String sql = "INSERT INTO persons (person_id,first_name,last_name,address,email,phone) VALUES" +
						"(?,?,?,?,?,?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1,239);
			stmt.setString(2,firstname);
			stmt.setString(3,lastname);
			stmt.setString(4,address);
			stmt.setString(5,email);
			stmt.setString(6,phone);
			stmt.executeUpdate();
			conn.commit();
			stmt.close();
			conn.close();
			response.sendRedirect("/radiology-proj/home.jsp");
			*/
	}
	if(request.getParameter("submitQueryUser")!= null){
		String classType = request.getParameter("classType");
		out.println("Users   ");
		out.println(classType);
		String username = request.getParameter("username");
		String password = request.getParameter("pass");

		//users(user_name,password,class,person_id,date_registered)
		String sql = "INSERT INTO users (user_name,password,class,person_id,date_registered) VALUES" +
						"(?,?,?,?,?)";
		/*
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1,username);
		stmt.setString(2,password);
		stmt.setString(3,class);
		stmt.setInt(4,id);
		stmt.setString(5, new Date());
		stmt.executeUpdate();
		conn.commit();
		stmt.close();
		conn.close();
		*/
		response.sendRedirect("/radiology-proj/home.jsp");
	}
	if(request.getParameter("submitQueryDoctor")!= null){
		out.println("doctors");
		int docID = Integer.valueOf(request.getParameter("docID"));
		int patID = Integer.valueOf(request.getParameter("patientID"));
		String sql = "INSERT INTO family_doctor (doctor_id,patient_id) VALUES" +
						"(?,?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1,docID);
		stmt.setInt(2,patID);
		stmt.executeUpdate();
		conn.commit();
		stmt.close();
		conn.close();
		response.sendRedirect("/radiology-proj/home.jsp");
	}
	else{
		out.println("<H1><CENTER>Insert Queries</CENTER></H1>");
		out.println("<FORM method=post action=insertQuery.jsp name=tableForm>");
	    out.println("<select name=tableType id=dropdown>");
	    out.println("<option value=dropdown>Select Table</option>");
	    out.println("<option value=persons>Persons</option>");
	    out.println("<option value=users>User</option>");
	    out.println("<option value=family_doctor>Family Doctor</option>");
	    out.println("</select>");
	    out.println("<input type=submit value=Enter NAME=bSubmit>");
		out.println("</FORM>");
	}
}
%>
</BODY>
</HTML>