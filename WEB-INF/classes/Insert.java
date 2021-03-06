import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import oracle.jdbc.driver.*;

public class Insert extends HttpServlet {
	Connection conn = null;
	String tableType = "";
	String response_message;
	PrintWriter out;
    public void doGet(HttpServletRequest request,HttpServletResponse res) throws ServletException, IOException {
    	HttpSession session = request.getSession(true);
		if (session.getAttribute("id") == null) {
			res.sendRedirect("/radiology-proj/login");
			return;
		}
    	
    	res.setContentType("text/html");
		out = res.getWriter();
		
	  	if(request.getParameter("bSubmit")!=null){
	  		tableType = request.getParameter("tableType");
			if(tableType.equals("users")){
				usersFormat(request);
			}
			else if(tableType.equals("persons")){
				personsFormat(request);
			}
			else if(tableType.equals("family_doctor")){
				doctorFormat(request);
			}
	  	}
	  	else{
		
			//User selected
	  		if(request.getParameter("submitQueryUser")!=null){
	  			try{
					Database.dbConnection newDB = new Database.dbConnection();
					Connection conn = newDB.connection();
				
		  			String classType = request.getParameter("classType");
					String personID = request.getParameter("personID");
					String username = request.getParameter("username");
					String password = request.getParameter("pass");
					
					if (password.equals(""))
						throw new Exception("Password cannot be empty!");
						
					//out.println(personID);
					//out.println("Users   ");
					//out.println(classType);
					//users(user_name,password,class,person_id,date_registered)
					String sql = "INSERT INTO users (user_name,password,class,person_id,date_registered) VALUES" +
									"(?,?,?,?,SYSDATE)";
					PreparedStatement stmt = conn.prepareStatement(sql);
					stmt.setString(1,username);
					stmt.setString(2,password);
					stmt.setString(3,classType);
					stmt.setInt(4,Integer.valueOf(personID));
					//stmt.setTimestamp(5,new java.sql.Timestamp(today.getTime()));
					stmt.executeUpdate();
					conn.commit();
					stmt.close();
					conn.close();
					response_message = "Insert OK!";
				}catch(Exception ex){
					response_message = ex.getMessage();
					if (response_message.startsWith("ORA-01400"))
						response_message = "Please make sure all fields are filled!";
					else if (response_message.startsWith("ORA-12899"))
						response_message = "Please choose a class for the user!";
				}
				out.println(
					"<HTML>\n" +
					"<HEAD><TITLE>Insert Message</TITLE></HEAD>\n" +
					"<BODY>\n" +
					"<H1>" +
					response_message +
					"</H1>\n" +
					"<p><a href=\"/radiology-proj/home\">Return to home</a></p>"+
					"</BODY></HTML>");
				return;
	  		}		
			//Person selected
	  		if(request.getParameter("submitQueryPerson")!=null){
	  			try{
					Database.dbConnection newDB = new Database.dbConnection();
					Connection conn = newDB.connection();
					
		  			String firstname = request.getParameter("firstname");
					String lastname = request.getParameter("lastname");
					String address = request.getParameter("address");
					String email = request.getParameter("email");
					String phone = request.getParameter("phone");

					//Generate ID
					int id = -1;
					String sql1 = "SELECT MAX(person_id) as id FROM PERSONS";
					Statement stmt = conn.createStatement();
					ResultSet results = stmt.executeQuery(sql1);
				    while(results.next()){
						id = (results.getInt("id"));
					}
					id ++;

					String idee = String.valueOf(id);

					String sql = "INSERT INTO persons (person_id,first_name,last_name,address,email,phone) "+
								"VALUES('"+idee+"', '"+firstname+"', '"+lastname+"', '"+address+"', '"+email+"', '"+phone+"')";
					stmt.executeUpdate(sql);
					conn.commit();
					stmt.close();
					conn.close();
					response_message = "Insert OK with ID of "+idee+"!";
				}catch(Exception ex){
					response_message = ex.getMessage();
				}
				out.println(
					"<HTML>\n" +
					"<HEAD><TITLE>Insert Message</TITLE></HEAD>\n" +
					"<BODY>\n" +
					"<H1>" +
					response_message +
					"</H1>\n" +
					"<p><a href=\"/radiology-proj/home\">Return to home</a></p>"+
					"</BODY></HTML>");
				return;
	  		}
			//Doctor selected
	  		if(request.getParameter("submitQueryDoctor")!=null){
	  			try{
					Database.dbConnection newDB = new Database.dbConnection();
					Connection conn = newDB.connection();
				
	  				String docID = request.getParameter("docID");
					String patID = request.getParameter("patientID");
					String sql = "INSERT INTO family_doctor (doctor_id,patient_id) VALUES" +
									"(?,?)";
					// out.println("DocID: " + String.valueOf(docID));
					// out.println("PatID: " + String.valueOf(patID));
					PreparedStatement stmt = conn.prepareStatement(sql);
					stmt.setString(1,docID);
					stmt.setString(2,patID);
					stmt.executeUpdate();
					conn.commit();
					stmt.close();
					conn.close();
				response_message = "Insert OK!";
				}catch(Exception ex){
					response_message = ex.getMessage();
					if (response_message.startsWith("ORA-00001"))
						response_message = "Assignment already exists!";
				}
				out.println(
					"<HTML>\n" +
					"<HEAD><TITLE>Insert Message</TITLE></HEAD>\n" +
					"<BODY>\n" +
					"<H1>" +
					response_message +
					"</H1>\n" +
					"<p><a href=\"/radiology-proj/home\">Return to home</a></p>"+
					"</BODY></HTML>");
				return;
	  		}
			//Default page when nothing submitted
			out.println("<HTML><HEAD><TITLE>Insert query</TITLE></HEAD><BODY>");
			out.println("<H1><CENTER>Insert Queries</CENTER></H1>");
			out.println("<FORM method=GET action=insert name=tableForm>");
		    out.println("<select name=tableType id=dropdown>");
		    out.println("<option value=dropdown>Select Table</option>");
		    out.println("<option value=users>User</option>");
			out.println("<option value=persons>Persons</option>");
		    out.println("<option value=family_doctor>Family Doctor</option>");
		    out.println("</select>");
		    out.println("<input type=submit value=Enter NAME=bSubmit>");
			out.println("</FORM>");
			out.println("<BR><p><a href=\"/radiology-proj/home\">Return to home</a></p>");
			out.println("<HR>");
			out.println("<p align=right><a href=\"/radiology-proj/help.html\">Help</a></p>");
			out.println("</BODY></HTML>");
	  	}
	} 
	//Creates the page for when Users is selected
	public void usersFormat(HttpServletRequest request){
		out.println("<HTML><HEAD><TITLE>Insert query</TITLE></HEAD><BODY>");
		out.println("<H1><CENTER>Insert New User</CENTER></H1>");
		out.println("<P>Insert a new username and password and select a class for the user. " + 
		"The person must exist in the database before they can be registered as a user. </P>");

        out.println("<FORM METHOD=GET ACTION=insert>");
		out.println("<TABLE>");

        out.println("<TR VALIGN=TOP ALIGN=LEFT>");
        out.println("<TD><B>Username: </B></TD>");
        out.println("<TD><INPUT TYPE=text NAME=username><BR></TD></TR>");

		out.println("<TR VALIGN=TOP ALIGN=LEFT>");
        out.println("<TD><B>Password: </B></TD>");
        out.println("<TD><INPUT TYPE=text NAME=pass><BR></TD></TR>");

		out.println("<TR VALIGN=TOP ALIGN=LEFT>");
		out.println("Class Type: ");
        out.println("<select name=classType id=dropdown>");
    	out.println("<option value=dropdown>Select Class</option>");
    	out.println("<option value=a>Admin</option>");
   	 	out.println("<option value=p>Patient</option>");
    	out.println("<option value=d>Doctor</option>");
    	out.println("<option value=r>Radiologist</option>");
    	out.println("</select></TR><BR>");


		out.println("<TR VALIGN=TOP ALIGN=LEFT>");
		out.println("Person ID: ");
        out.println("<select name=personID id=dropdown>");
		ArrayList<String> model = getIDs("users");
		for(String m: model){
    		out.println("<option value=" + m + ">" + m +"</option>");
    	}
    	out.println("</select></TR>");

		out.println("</TABLE>");
		out.println("<INPUT TYPE=submit NAME=submitQueryUser VALUE=Submit>");

        out.println("</FORM>");
		out.println("<BR><p><a href=\"/radiology-proj/home\">Return to home</a></p>");
		out.println("<HR>");
		out.println("<p align=right><a href=\"/radiology-proj/help.html\">Help</a></p>");
		out.println("</BODY></HTML>");
	}
	
	//Creates the page for when Person is selected
	public String personsFormat(HttpServletRequest request){
		out.println("<HTML><HEAD><TITLE>Insert query</TITLE></HEAD><BODY>");
		out.println("<H1><CENTER>Insert New Person</CENTER></H1>");
		out.println("<P>Enter the details for the new person</P>");

		out.println("<FORM METHOD=GET ACTION=insert>");
		out.println("<TABLE>");

        out.println("<TR VALIGN=TOP ALIGN=LEFT>");
        out.println("<TD><B> First Name: </B></TD>");
        out.println("<TD><INPUT TYPE=text NAME=firstname><BR></TD></TR>");

		out.println("<TR VALIGN=TOP ALIGN=LEFT>");
        out.println("<TD><B> Last Name: </B></TD>");
        out.println("<TD><INPUT TYPE=text NAME=lastname><BR></TD></TR>");

		out.println("<TR VALIGN=TOP ALIGN=LEFT>");
        out.println("<TD><B> Address: </B></TD>");
        out.println("<TD><INPUT TYPE=text NAME=address><BR></TD></TR>");

		out.println("<TR VALIGN=TOP ALIGN=LEFT>");
        out.println("<TD><B> Email: </B></TD>");
        out.println("<TD><INPUT TYPE=text NAME=email><BR></TD></TR>");


		out.println("<TR VALIGN=TOP ALIGN=LEFT>");
        out.println("<TD><B> Phone: </B></TD>");
        out.println("<TD><INPUT TYPE=text NAME=phone><BR></TD></TR>");

		out.println("</TABLE>");
		out.println("<INPUT TYPE=submit NAME=submitQueryPerson VALUE=Submit>");

        out.println("</FORM>");
        out.println("<BR><p><a href=\"/radiology-proj/home\">Return to home</a></p>");
		out.println("<HR>");
		out.println("<p align=right><a href=\"/radiology-proj/help.html\">Help</a></p>");
		out.println("</BODY></HTML>");
        return request.getParameter("submitQueryPerson");
	}
	
	//Creates a page for when Doctor is selected
	public void doctorFormat(HttpServletRequest request){
		out.println("<HTML><HEAD><TITLE>Insert query</TITLE></HEAD><BODY>");
    	out.println("<H1><CENTER>Insert New Doctor</CENTER></H1>");
		out.println("<P>Assign a doctor to a patient</P>");

		out.println("<FORM METHOD=GET ACTION=insert>");
		out.println("<TABLE>");

		out.println("<TR VALIGN=TOP ALIGN=LEFT>");
		out.println("Doctor ID: ");
        out.println("<select name=docID id=dropdown>");
		ArrayList<String> docArray = getIDs("doctors");
		for(String m: docArray){
    		out.println("<option value=" + m + ">" + m +"</option>");
    	}
    	out.println("</select></TR><BR>");

    	out.println("<TR VALIGN=TOP ALIGN=LEFT>");
		out.println("Patient ID: ");
        out.println("<select name=patientID id=dropdown>");
		ArrayList<String> patArray = getIDs("patient");
		for(String m: patArray){
    		out.println("<option value=" + m + ">" + m +"</option>");
    	}
    	out.println("</select></TR><BR>");

		out.println("<INPUT TYPE=submit NAME=submitQueryDoctor VALUE=Submit>");
        out.println("</FORM>");
		out.println("<BR><p><a href=\"/radiology-proj/home\">Return to home</a></p>");
		out.println("<HR>");
		out.println("<p align=right><a href=\"/radiology-proj/help.html\">Help</a></p>");
		out.println("</BODY></HTML>");

	}
	//Gets IDs
	public ArrayList<String> getIDs(String str){
		ArrayList<String> array = new ArrayList<String>();
		Database.dbConnection newDB = new Database.dbConnection();
		Connection conn = newDB.connection();
		
		String sql = "";

		if(str.equals("users")){
			//sql = "SELECT person_id FROM persons minus select person_id from users";
			sql = "SELECT person_id FROM persons";
		}

		else if(str.equals("doctors")){
			//sql = "SELECT doctor_id FROM family_doctor";
			sql = "SELECT u.person_id FROM persons p, users u WHERE p.person_id = u.person_id AND u.class = 'd'";
		}

		else if(str.equals("patient")){
			//sql = "SELECT patient_id FROM family_doctor";
			sql = "SELECT u.person_id FROM persons p, users u WHERE p.person_id = u.person_id AND u.class = 'p'";
		}

		try{
			Statement stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery(sql);

		    while(results != null && results.next()){
		    	String id = id = (results.getString("person_id"));
			    array.add(id);
	    	}
	        results.close();
	    	conn.close();
	    }
		catch(SQLException e){
			e.printStackTrace();
		}
    return array;
}
}
