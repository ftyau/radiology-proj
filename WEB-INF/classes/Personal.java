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
		    	first_name = (results.getString("first_name")).trim();
				last_name = (results.getString("last_name")).trim();
				address = (results.getString("address")).trim();
				phone = (results.getString("phone")).trim();
				email = (results.getString("email")).trim();
			}
	    }catch(Exception ex){
	        out.println("<hr>" + ex.getMessage() + "<hr>");
		}
		out.println("<HTML>");
		out.println("<HEAD>");
		out.println("<TITLE>User Information </TITLE>");

		out.println("<BODY>");
		out.println("<H1><CENTER>Personal Information</CENTER></H1>");
		out.println("<TABLE ALIGN=CENTER>");
		out.println("<TABLE BORDER=1");

		out.println("<TR>");
		out.println("<TD>First Name: "+ first_name +"</TD>");
		out.println("<TD><a href=/radiology-proj/EditInfo?colNum=1>Edit</a></TD>");
		out.println("</TR>");

		out.println("<TR>");
		out.println("<TD>Last Name: "+ last_name +"</TD>");
		out.println("<TD><a href=/radiology-proj/EditInfo?colNum=2>Edit</a></TD>");
		out.println("</TR>");

		out.println("<TR>");
		out.println("<TD>Addres: "+ address +"</TD>");
		out.println("<TD><a href=/radiology-proj/EditInfo?colNum=3>Edit</a></TD>");
		out.println("</TR>");

		out.println("<TR>");
		out.println("<TD>Phone: "+ phone +"</TD>");
		out.println("<TD><a href=/radiology-proj/EditInfo?colNum=4>Edit</a></TD>");
		out.println("</TR>");

		out.println("<TR>");
		out.println("<TD>Email: "+ email +"</TD>");
		out.println("<TD><a href=/radiology-proj/EditInfo?colNum=5>Edit</a></TD>");
		out.println("</TR>");

		out.println("</TABLE>");
		out.println("</TABLE>");

		out.println("<BR><p><a href=\"/radiology-proj/home.jsp\">Return to home</a></p>");

		out.println("</BODY>");
		out.println("</HTML>");
	} 
}
