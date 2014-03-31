import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import oracle.jdbc.driver.*;

public class Personal extends HttpServlet {
	Connection conn = null;

    public void doGet(HttpServletRequest request,HttpServletResponse res) throws ServletException, IOException {
    	HttpSession session = request.getSession(true);
		if (session.getAttribute("id") == null) {
			res.sendRedirect("/radiology-proj/login");
			return;
		}

    	res.setContentType("text/html");
		PrintWriter out = res.getWriter ();

		String tempID =  (String)(session.getAttribute("id"));
	  	int personID = Integer.valueOf(tempID);

	  	ResultSet results = null;
		String sql = "select first_name, last_name, " 
					+ "address, email, phone "
					+ "from persons where person_id = ?";

		String first_name="";
		String last_name="";
		String address="";
		String phone="";
		String email="";

		Database.dbConnection newDB = new Database.dbConnection();
        Connection conn = newDB.connection();


		try{
	    	PreparedStatement stmt = conn.prepareStatement(sql);
	    	stmt.setInt(1,personID);
	        results = stmt.executeQuery();

			while(results != null && results.next()){
				if (results.getString("first_name") != null)
					first_name = results.getString("first_name").trim();
				else
					first_name = "null";
				if (results.getString("last_name") != null)
					last_name = results.getString("last_name").trim();
				else
					last_name = "null";
				if (results.getString("address") != null)
					address = results.getString("address").trim();
				else
					address = "null";
				if (results.getString("phone") != null)
					phone = results.getString("phone").trim();
				else
					phone = "null";
				if (results.getString("email") != null)
					email = results.getString("email").trim();
				else
					email = "null";
			}
			conn.close();
	    }catch(Exception ex){
	        out.println("Unexpected error. Database may be having issues.<br>Error: " + ex);
		}
		out.println("<HTML>");
		out.println("<HEAD>");
		out.println("<TITLE>User Information </TITLE>");

		out.println("<BODY>");
		out.println("<H1><CENTER>Personal Information</CENTER></H1>");
		out.println("<TABLE ALIGN=CENTER>");
		out.println("<TABLE BORDER=1");
		
		out.println("<TR>");
		out.println("<TD>ID: "+ session.getAttribute("id") +"</TD>");
		out.println("</TR>");

		out.println("<TR>");
		out.println("<TD>First Name: "+ first_name +"</TD>");
		out.println("<TD><a href=/radiology-proj/editinfo?colNum=1>Edit</a></TD>");
		out.println("</TR>");

		out.println("<TR>");
		out.println("<TD>Last Name: "+ last_name +"</TD>");
		out.println("<TD><a href=/radiology-proj/editinfo?colNum=2>Edit</a></TD>");
		out.println("</TR>");

		out.println("<TR>");
		out.println("<TD>Address: "+ address +"</TD>");
		out.println("<TD><a href=/radiology-proj/editinfo?colNum=3>Edit</a></TD>");
		out.println("</TR>");

		out.println("<TR>");
		out.println("<TD>Email: "+ email +"</TD>");
		out.println("<TD><a href=/radiology-proj/editinfo?colNum=4>Edit</a></TD>");
		out.println("</TR>");

		out.println("<TR>");
		out.println("<TD>Phone: "+ phone +"</TD>");
		out.println("<TD><a href=/radiology-proj/editinfo?colNum=5>Edit</a></TD>");
		out.println("</TR>");

		out.println("<TR>");
		out.println("<TD>Password</TD>");
		out.println("<TD><a href=/radiology-proj/editinfo?colNum=6>Edit</a></TD>");
		out.println("</TR>");

		out.println("</TABLE>");
		out.println("</TABLE>");

		out.println("<BR><p><a href=\"/radiology-proj/home\">Return to home</a></p>");

		out.println("<HR>");
		out.println("<p align=right><a href=\"/radiology-proj/help.html\">Help</a></p>");
		out.println("</BODY>");
		out.println("</HTML>");
	} 
}
