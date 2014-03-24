import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import oracle.jdbc.driver.*;

public class AdminPanel extends HttpServlet{

	Connection conn = null;

    public void doGet(HttpServletRequest request,HttpServletResponse res) throws ServletException, IOException {
    	HttpSession session = request.getSession(true);
    	
    	res.setContentType("text/html");
		PrintWriter out = res.getWriter ();

		out.println("<HTML>");
		out.println("<HEAD>");
		out.println("<TITLE>Admin Panel</TITLE>");
		out.println("</HEAD>");

		out.println("<BODY>");
		out.println("<H1><CENTER>Admin Panel</CENTER></H1>");
		out.println("<TABLE ALIGN=CENTER>");
		out.println("<TABLE BORDER=1");

		out.println("<TR>");
		out.println("<TD><a href=/radiology-proj/Insert>Insert Query</a></TD>");
		out.println("</TR>");

		out.println("<TR>");
		out.println("<TD><a href=/radiology-proj/Udate>Update Query</a></TD>");
		out.println("</TR>");

		out.println("<TR>");
		out.println("<TD><a href=/radiology-proj/Generate>Generate Report</a></TD>");
		out.println("</TR>");

		out.println("</TABLE>");
		out.println("</TABLE>");

		out.println("<BR><p><a href=\"/radiology-proj/home.jsp\">Return to home</a></p>");

		out.println("</BODY>");
		out.println("</HTML>");
	}
}
