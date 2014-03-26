import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class Home extends HttpServlet implements SingleThreadModel {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter ();
		
		HttpSession session = request.getSession(true);
		if (session.getAttribute("id") == null) {
			response.sendRedirect("/radiology-proj/login");
			return;
		}
			
		String user_class = (String) session.getAttribute("class");
		
		out.println("<HTML><HEAD><TITLE>Home</TITLE></HEAD><BODY>");
		out.println("<H1><CENTER>Welcome</CENTER></H1><TABLE ALIGN=\"RIGHT\"><TABLE  BORDER=\"1\"><TR>");
		out.println("<TH COLSPAN=\"2\"><H4><BR>Navigation</H4></TR>");
		out.println("<TR><TD><a href=\"/radiology-proj/personal\">Personal Info</a></TD></TR>");
		if (user_class.equals("a")) {
			out.println("<TR><TD><a href=\"/radiology-proj/adminpanel\">System Admin Panel</a></TD></TR>");
		}
		out.println("<TR><TD><a href=\"/radiology-proj/upload\">Upload Pictures</a></TD></TR>");
		out.println("<TR><TD><a href=\"/radiology-proj/search\">Search Database</a></TD></TR>"); 
		
		if (user_class.equals("a")) {
			out.println("<TR><TD><a href=\"/radiology-proj/olap\">Data Analysis</a></TD></TR>");
		}
		out.println("<TR><TD><a href=\"/radiology-proj/login?action=logout\">Logout</a></TD></TR>");
		
		out.println("</TABLE></TABLE></BODY></HTML>");
	}
}
