import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class Login extends HttpServlet implements SingleThreadModel {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter ();
		HttpSession session = request.getSession(true);
		if (request.getParameter("action") != null) {
			session.invalidate();
			session = request.getSession(true);
		}
		out.println("<HTML><HEAD><TITLE>Login</TITLE></HEAD><BODY>");
		if(request.getParameter("bSubmit") != null){
			if(request.getParameter("USERID").length() < 1 || request.getParameter("PASSWD").length() < 1 ||
				request.getParameter("USERID") == null || request.getParameter("PASSWD") == null){
				out.println("Username and/or password cannot be empty");
				response.setHeader("Refresh", "5; URL=/radiology-proj/login");
				return;
			}

			//Get input username and password
			String inputUsername = (request.getParameter("USERID")).trim();
			String inputPassword = (request.getParameter("PASSWD")).trim();

			Database.dbConnection newDB = new Database.dbConnection();
			Connection conn = newDB.connection();
			
			//Execute Queries
			String sql = "select user_name, password, person_id, class from users where user_name = '" + inputUsername + "'";
			ResultSet results = null;

			try{
				Statement stmt = conn.createStatement();
				results = stmt.executeQuery(sql);
			

				String dbLogin = "";
				String dbPassword = "";
				int personID = 0;
				
				while (results.next()) {
					dbPassword = results.getString("password").trim();
					
					if (inputPassword.equals(dbPassword)) {
						String id = Integer.toString(results.getInt("person_id"));
						session.setAttribute("id",id);
						session.setAttribute("class", results.getString("class").trim());
						response.sendRedirect("home");
						conn.close();
					}
					
					out.println("Username or password not valid!");
					return;
				}
				out.println("Username does not exist!");
			}
			catch(Exception ex){
				out.println("<hr>" + ex.getMessage() + "<hr>");
			}
		}
		else{
			out.println("<H1><CENTER>Radiology Database Login</CENTER></H1>");
			out.println("<P>To login successfully, you need to enter a valid username and password.</P>");

			out.println("<FORM METHOD=get ACTION=login>");

			out.println("<TABLE>");

			out.println("<TR VALIGN=TOP ALIGN=LEFT>");
			out.println("<TD><B> Username: </B></TD>");
			out.println("<TD><INPUT TYPE=text NAME=USERID><BR></TD>");
			out.println("</TR>");

			out.println("<TR VALIGN=TOP ALIGN=LEFT>");
			out.println("<TD><B> Password: </B></TD>");
			out.println("<TD><INPUT TYPE=password NAME=PASSWD><BR></TD>");
			out.println("</TR>");

			out.println("</TABLE>");


			out.println("<INPUT TYPE=submit NAME=bSubmit VALUE=Submit>");
			out.println("</FORM>");
			out.println("<HR>");
		}
		out.println("</BODY></HTML>");
	}
}