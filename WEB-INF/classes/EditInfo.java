import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import oracle.jdbc.driver.*;

public class EditInfo extends HttpServlet {
	String colName = "";
	Connection conn = null;

    public void doGet(HttpServletRequest request,HttpServletResponse res) throws ServletException, IOException {
    	HttpSession session = request.getSession(true);
    	
    	res.setContentType("text/html");
		PrintWriter out = res.getWriter ();

		
		String tempID =  (String)(session.getAttribute("id"));
	  	int personID = Integer.valueOf(tempID);


		Database.dbConnection newDB = new Database.dbConnection();
        Connection conn = newDB.connection();


	  	if(request.getParameter("bSubmit")!=null){
	  		String input = (request.getParameter("userInput")).trim();
			ResultSet results = null;
			String sql = getQuery(colName);

			try{
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setString(1,input);
				stmt.setInt(2,personID);
		        stmt.executeUpdate();
		        conn.commit();
				conn.close();
				stmt.close();
		    }catch(Exception ex){
		        out.println("<hr>" + ex.getMessage() + "<hr>");
			}
			res.sendRedirect("/radiology-proj/personal");
	  	}
	  	else{
	  		out.println("<H1><CENTER>Edit User Information</CENTER></H1>");
			out.println("<FORM METHOD=GET ACTION=editinfo>");

			out.println("<TABLE>");
			out.println("<TR VALIGN=TOP ALIGN=LEFT>");

			out.println("<TD><B> New Value </B></TD>");
			out.println("<TD><INPUT TYPE=text NAME=userInput><BR></TD>");
			colName = (String) request.getParameter("colNum");

			out.println("</TR>");
			out.println("</TABLE>");

			out.println("<INPUT TYPE=submit NAME=bSubmit VALUE=Submit>");
			out.println("<BR><p><a href=\"/radiology-proj/home.jsp\">Return to home</a></p>");
			out.println("</FORM>");
			out.println("<HR>");
	  	}
	} 

	public String getQuery(String colName){
		String sql = "";
		if(colName.equals("1")){
			sql = "UPDATE persons SET first_name =? "+ "WHERE person_id =?";
		}else if(colName.equals("2")){
			sql = "UPDATE persons SET last_name =? "+ "WHERE person_id =?";
		}else if(colName.equals("3")){
			sql = "UPDATE persons SET address =? "+ "WHERE person_id =?";
		}else if(colName.equals("4")){
			sql = "UPDATE persons SET email =? "+ "WHERE person_id =?";
		}else if(colName.equals("5")){
			sql = "UPDATE persons SET phone =? "+ "WHERE person_id =?";
		}
		return sql;
	}
}
