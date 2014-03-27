import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import oracle.jdbc.driver.*;

public class Update extends HttpServlet {
	Connection conn = null;
	String tableType = "";
	PrintWriter out;
    public void doGet(HttpServletRequest request,HttpServletResponse res) throws ServletException, IOException {
    	HttpSession session = request.getSession(true);
		if (session.getAttribute("id") == null) {
			res.sendRedirect("/radiology-proj/login");
			return;
		}
    	
    	res.setContentType("text/html");
		out = res.getWriter ();
		
		out.println("<HTML><HEAD><TITLE>Update query</TITLE></HEAD><BODY>");

		Database.dbConnection newDB = new Database.dbConnection();
        Connection conn = newDB.connection();


	  	if(request.getParameter("bSubmit")!=null){
	  		//out.println("Werking");
	  		tableType = request.getParameter("tableType");
	        try{
				String sql = "";
				if(tableType.equals("persons")){
					sql = "SELECT * FROM PERSONS";
					out.println("<H1><CENTER>Update Persons</CENTER></H1>");
				}
				else if(tableType.equals("users")){
					sql = "SELECT * FROM USERS";
					out.println("<H1><CENTER>Update Users</CENTER></H1>");
				}
				else if(tableType.equals("family_doctor")){
					sql = "SELECT * FROM family_doctor";
					out.println("<H1><CENTER>Update Doctors</CENTER></H1>");
				}

				Statement stmt = conn.createStatement();
				ResultSet results = stmt.executeQuery(sql);
				ResultSetMetaData meta = results.getMetaData();

		        out.println("<TABLE>");
		        out.println("<FORM METHOD=GET ACTION=update>");
		        out.println("<TR>Update \"" + tableType + "\"</TR><BR>");

		        out.println("<TR VALIGN=TOP ALIGN=LEFT>");
		        out.println("SET");
		        out.println("<select name=setCol id=dropdown>");
		        for(int i = 1; i <= meta.getColumnCount(); i++){
		            String col = meta.getColumnName(i);
		            out.println("<option value="+col+">"+col+"</option>");
		        }
		        out.println("<INPUT TYPE=text NAME=setValue><BR>");
		        out.println("</select></TR>");
		        
		        out.println("<TR VALIGN=TOP ALIGN=LEFT>");
		        out.println("Where");
		        out.println("<select name=setWhereCol id=dropdown>");
		        for(int i =1; i<=meta.getColumnCount(); i++){
		            String col = meta.getColumnName(i);
		            out.println("<option value="+col+">"+col+"</option>");
		        }
		        out.println("<INPUT TYPE=text NAME=setWhere><BR>");
		        out.println("</select></TR>");

		        out.println("</TABLE>");
		        out.println("<INPUT TYPE=submit NAME=submitUpdate VALUE=Submit>");
		        out.println("</FORM");
				out.println("<BR><p><a href=\"/radiology-proj/home\">Return to home</a></p>");
				out.println("<HR>");
				out.println("<p align=right><a href=\"/radiology-proj/help.html\">Help</a></p>");
				out.println("</BODY></HTML>");
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
	  	}
	  	else{
	  		if(request.getParameter("submitUpdate")!=null){
		        String setColName = request.getParameter("setCol");
		        String setValue = request.getParameter("setValue");

		        //out.println(setColName + " " + setValue);

		        String whereColName = request.getParameter("setWhereCol");
		        String whereValue = request.getParameter("setWhere");
		        //out.println(whereColName+ " " + whereValue);
		        
		        String sql = "UPDATE " + tableType + " SET COLNAME1 = ? where COLNAME2 = ?";
		        //ut.println("TableType: " + tableType);
		        sql = sql.replace("COLNAME1",setColName);
		        sql = sql.replace("COLNAME2",whereColName);

		        //out.println("SQL: " + sql);
		        try{
		            PreparedStatement stmt = conn.prepareStatement(sql);
		            
		            stmt.setString(1,setValue);
		            stmt.setString(2,whereValue);
		            stmt.executeUpdate();
		            conn.commit();
		            stmt.close();
		            conn.close();
		        }catch(SQLException e){
		            e.printStackTrace();
		            out.println(e);
		        }
		        res.sendRedirect("/radiology-proj/home");
	  		}
	        out.println("<H1><CENTER>Update Queries</CENTER></H1>");
	        out.println("<FORM method=GET action=update name=tableForm>");
	        out.println("<select name=tableType id=dropdown>");
	        out.println("<option value=dropdown>Select Table</option>");
	        out.println("<option value=persons>Persons</option>");
	        out.println("<option value=users>User</option>");
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
}