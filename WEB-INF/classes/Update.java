import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import oracle.jdbc.driver.*;

public class Update extends HttpServlet {
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
		out = res.getWriter ();

	  	if(request.getParameter("bSubmit")!=null){
			Database.dbConnection newDB = new Database.dbConnection();
			Connection conn = newDB.connection();
			
			out.println("<HTML><HEAD><TITLE>Update query</TITLE></HEAD><BODY>");
	  		tableType = request.getParameter("tableType");
	        try{
				String sql = "";
				if(tableType.equals("Users")){
					sql = "SELECT password, date_registered FROM USERS";
					out.println("<H1><CENTER>Update Users</CENTER></H1>");
				}
				else if(tableType.equals("Persons")){
					sql = "SELECT first_name, last_name, address, email, phone FROM PERSONS";
					out.println("<H1><CENTER>Update Persons</CENTER></H1>");
				}
				// else if(tableType.equals("Family_doctor")){
					// sql = "SELECT * FROM family_doctor";
					// out.println("<H1><CENTER>Update Doctors</CENTER></H1>");
				// }

				Statement stmt = conn.createStatement();
				ResultSet results = stmt.executeQuery(sql);
				ResultSetMetaData meta = results.getMetaData();
				
				if (tableType.equals("Users")) {
					out.println("<TABLE>");
					out.println("<FORM METHOD=GET ACTION=update>");
					out.println("<TR VALIGN=TOP ALIGN=LEFT>");
					out.println("Value to update: ");
					out.println("<select name=setCol id=dropdown>");
					for(int i = 1; i <= meta.getColumnCount(); i++){
						String col = meta.getColumnName(i);
						out.println("<option value="+col+">"+col+"</option>");
					}
					out.println("</select>");
					out.println("New value: ");
					out.println("<INPUT TYPE=text NAME=setValue><BR>");
					out.println("</TR>");
					
					out.println("<TR VALIGN=TOP ALIGN=LEFT>");
					out.println("For the user: ");
					out.println("<select name=setWhere id=dropdown>");
					String query = "SELECT user_name FROM users";
					Statement statment = conn.createStatement();
					ResultSet rset = stmt.executeQuery(query);
					while (rset.next()) {
						out.println("<option vaule=\""+rset.getString(1)+"\">"+rset.getString(1)+"</option>");
					}
					out.println("</select>");
					out.println("</TR>");
					out.println("</TABLE>");
					out.println("<INPUT TYPE=hidden NAME=setWhereCol VALUE=user_name>");
					out.println("<INPUT TYPE=hidden NAME=tableType VALUE=users>");
					out.println("<INPUT TYPE=submit NAME=submitUpdate VALUE=Submit>");
					out.println("</FORM");
					out.println("<BR><p><a href=\"/radiology-proj/home\">Return to home</a></p>");
					out.println("<HR>");
					out.println("<p align=right><a href=\"/radiology-proj/help.html\">Help</a></p>");
					out.println("</BODY></HTML>");
				}
				
				if (tableType.equals("Persons")) {
					out.println("<TABLE>");
					out.println("<FORM METHOD=GET ACTION=update>");
					out.println("<TR VALIGN=TOP ALIGN=LEFT>");
					out.println("Value to update: ");
					out.println("<select name=setCol id=dropdown>");
					for(int i = 1; i <= meta.getColumnCount(); i++){
						String col = meta.getColumnName(i);
						out.println("<option value="+col+">"+col+"</option>");
					}
					out.println("</select>");
					out.println("New value: ");
					out.println("<INPUT TYPE=text NAME=setValue><BR>");
					out.println("</TR>");
					
					out.println("<TR VALIGN=TOP ALIGN=LEFT>");
					out.println("For the person with an ID of: ");
					out.println("<select name=setWhere id=dropdown>");
					String query = "SELECT person_id FROM persons";
					Statement statment = conn.createStatement();
					ResultSet rset = stmt.executeQuery(query);
					while (rset.next()) {
						out.println("<option vaule=\""+rset.getString(1)+"\">"+rset.getString(1)+"</option>");
					}
					out.println("</select>");
					out.println("</TR>");
					out.println("</TABLE>");
					out.println("<INPUT TYPE=hidden NAME=setWhereCol VALUE=person_id>");
					out.println("<INPUT TYPE=hidden NAME=tableType VALUE=persons>");
					out.println("<INPUT TYPE=submit NAME=submitUpdate VALUE=Submit>");
					out.println("</FORM");
					out.println("<BR><p><a href=\"/radiology-proj/home\">Return to home</a></p>");
					out.println("<HR>");
					out.println("<p align=right><a href=\"/radiology-proj/help.html\">Help</a></p>");
					out.println("</BODY></HTML>");
				}
				
				// if (tableType.equals("Family_doctor")) {
					// out.println("<TABLE>");
					// out.println("<FORM METHOD=GET ACTION=update>");
					// out.println("<TR VALIGN=TOP ALIGN=LEFT>");
					// out.println("Value to update: ");
					// out.println("<select name=setCol id=dropdown>");
					// for(int i = 1; i <= meta.getColumnCount(); i++){
						// String col = meta.getColumnName(i);
						// out.println("<option value="+col+">"+col+"</option>");
					// }
					// out.println("</select>");
					// out.println("New value: ");
					// out.println("<INPUT TYPE=text NAME=setValue><BR>");
					// out.println("</TR>");
					
					// out.println("<TR VALIGN=TOP ALIGN=LEFT>");
					// out.println("For the person with an ID of: ");
					// out.println("<select name=setWhere id=dropdown>");
					// String query = "SELECT person_id FROM persons";
					// Statement statment = conn.createStatement();
					// ResultSet rset = stmt.executeQuery(query);
					// while (rset.next()) {
						// out.println("<option vaule=\""+rset.getString(1)+"\">"+rset.getString(1)+"</option>");
					// }
					// out.println("</select>");
					// out.println("</TR>");
					// out.println("</TABLE>");
					// out.println("<INPUT TYPE=hidden NAME=setWhereCol VALUE=person_id>");
					// out.println("<INPUT TYPE=hidden NAME=tableType VALUE=persons>");
					// out.println("<INPUT TYPE=submit NAME=submitUpdate VALUE=Submit>");
					// out.println("</FORM");
					// out.println("<BR><p><a href=\"/radiology-proj/home\">Return to home</a></p>");
					// out.println("<HR>");
					// out.println("<p align=right><a href=\"/radiology-proj/help.html\">Help</a></p>");
					// out.println("</BODY></HTML>");
				// }
				conn.close();
			} catch (Exception ex) {
				out.println(ex);
			}					
	  	}
	  	else{
	  		if(request.getParameter("submitUpdate")!=null){
				try {
					Database.dbConnection newDB = new Database.dbConnection();
					Connection conn = newDB.connection();
					String setColName = request.getParameter("setCol");
					String setValue = request.getParameter("setValue");

					//out.println(setColName + " " + setValue);

					String whereColName = request.getParameter("setWhereCol");
					String whereValue = request.getParameter("setWhere");
					//out.println(whereColName+ " " + whereValue);
					
					String sql = "UPDATE " + request.getParameter("tableType") + " SET COLNAME1 = ? where COLNAME2 = ?";
					sql = sql.replace("COLNAME1",setColName);
					sql = sql.replace("COLNAME2",whereColName);

					out.println(sql);
					PreparedStatement stmt = conn.prepareStatement(sql);
					
					stmt.setString(1,setValue);
					stmt.setString(2,whereValue);
					stmt.executeUpdate();
					conn.commit();
					stmt.close();
					conn.close();
					response_message = "Update OK!";
		        }catch(Exception ex){
		            response_message = ex.getMessage();
					if (response_message.startsWith("ORA-01841") || response_message.startsWith("ORA-01861"))
						response_message = "Date must be in the format of YYYY/MM/DD!";
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
			//Default page if submit hasn't been submitted yet
			out.println("<HTML><HEAD><TITLE>Update query</TITLE></HEAD><BODY>");
	        out.println("<H1><CENTER>Update Queries</CENTER></H1>");
	        out.println("<FORM method=GET action=update name=tableForm>");
	        out.println("<select name=tableType id=dropdown>");
			out.println("<option value=Users>User</option>");
	        out.println("<option value=Persons>Persons</option>");
	        // out.println("<option value=Family_doctor>Family Doctor</option>");
	        out.println("</select>");
	        out.println("<input type=submit value=Enter NAME=bSubmit>");
	        out.println("</FORM>");
			out.println("<BR><p><a href=\"/radiology-proj/home\">Return to home</a></p>");
			out.println("<HR>");
			out.println("<p align=right><a href=\"/radiology-proj/help.html\">Help</a></p>");
			out.println("</BODY></HTML>");
	    }
	}
}