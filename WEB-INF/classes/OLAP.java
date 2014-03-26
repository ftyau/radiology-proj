import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class OLAP extends HttpServlet implements SingleThreadModel {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter ();
		HttpSession session = request.getSession(true);
		if (session.getAttribute("id") == null) {
			response.sendRedirect("/radiology-proj/login");
			return;
		}
		if (request.getParameter("submit") != null) {
			Connection conn = null;
			
			try {
				if (request.getParameter("patientName") != null || request.getParameter("testType") != null || request.getParameter("time") != null) {
					Database.dbConnection newDB = new Database.dbConnection();
					conn = newDB.connection();
					
					//Create query
					String query = "SELECT ";
					if (request.getParameter("patientName") != null)
						query = query + "patient_id, ";
					if (request.getParameter("testType") != null)
						query = query + "test_type, ";	
					if (request.getParameter("time") != null) {
						query = query + "trunc(test_date, ";
						if (request.getParameter("hierarchy").equals("weekly"))
							query = query + "'d'), ";
						else if (request.getParameter("hierarchy").equals("monthly"))
							query = query + "'mm'), ";
						else if (request.getParameter("hierarchy").equals("yearly"))
							query = query + "'y'), ";
						else
							query = query + "'iw'), ";
					}
					query = query + "COUNT(image_id) FROM olap_view GROUP BY CUBE (";
					
					if (request.getParameter("patientName") != null)
						query = query + "patient_id,";
					if (request.getParameter("testType") != null)
						query = query + "test_type,";	
					if (request.getParameter("time") != null){
						query = query + "trunc(test_date, ";
						if (request.getParameter("hierarchy").equals("weekly"))
							query = query + "'d'),";
						else if (request.getParameter("hierarchy").equals("monthly"))
							query = query + "'mm'),";
						else if (request.getParameter("hierarchy").equals("yearly"))
							query = query + "'y'),";
						else
							query = query + "'iw'),";
					}
					query = query.substring(0,query.length()-1);
					query = query + ")";
					PreparedStatement searchStatement = conn.prepareStatement(query);
					ResultSet rset = searchStatement.executeQuery();
					ResultSetMetaData rsetmd = rset.getMetaData();
					int column_count = rsetmd.getColumnCount();
					//Output table
					out.println("<html>");
					out.println("<head><title>Data analysis</title></head>");
					out.println("<body>");
					//out.println(query);
					//out.println("<br>");
					out.println("<table border=1>");
					out.println("<tr>");
					if (request.getParameter("patientName") != null) {
						out.println("<th>Patient ID</th>");
						out.println("<th>First name</th>");
						out.println("<th>Last name</th>");
						}
					if (request.getParameter("testType") != null)
						out.println("<th>Test type</th>");
					if (request.getParameter("time") != null) {
						if (request.getParameter("hierarchy").equals("weekly"))
							out.println("<th>Week starting on</th>");
						else if(request.getParameter("hierarchy").equals("monthly"))
							out.println("<th>Month starting on</th>");
						else if(request.getParameter("hierarchy").equals("yearly"))
							out.println("<th>Year starting on</th>");
						else
							out.println("<th>Week starting on</th>");
						}
					out.println("<th># of images</th>");
					out.println("</tr>");
					
					while (rset.next()){
						out.println("<tr>");
						for (int i=1;i<column_count+1;i++) {
							out.println("<td>");
							if (rset.getString(i) != null) {
								out.println(rset.getString(i));
								if (request.getParameter("patientName") != null && i == 1) {
									String query2 = "SELECT first_name, last_name FROM persons WHERE person_id = " + rset.getString(i);
									Statement stmt = conn.createStatement();
									ResultSet rset2 = stmt.executeQuery(query2);
									while (rset2.next()) {
										out.println("</td>");
										out.println("<td>");
										out.println(rset2.getString(1));
										out.println("</td>");
										out.println("<td>");
										out.println(rset2.getString(2));
									}
									rset2.close();
								}
							} else {
								out.println("ANY");
								if (request.getParameter("patientName") != null && i == 1) {
									out.println("</td>");
									out.println("<td>");
									out.println("-");
									out.println("</td>");
									out.println("<td>");
									out.println("-");
								}
							}
							out.println("</td>");
						}
					}
					out.println("</table>");
					out.println("<p><a href=\"/radiology-proj/refresh\">Refresh table data</a></p>");
					out.println("<p><a href=\"/radiology-proj/home\">Return to home</a></p>");
					out.println("</body>");
					out.println("</html>");
					
					conn.close();
				} else { 
					out.println("<html><head><title>Data analysis</title></head><body>");
					out.println("No options selected!");
					out.println("</body></html>");
				}
			} catch(Exception ex) {
				out.println("<br>Unexpected error: " + ex.getMessage());
			}
		} else {
			out.println("<html>");
			out.println("<head><title>Data analysis</title></head>");
			out.println("<body>");
			out.println("<form name=\"olap\" method=\"get\" action=\"olap\">");
			out.println("<input type=\"checkbox\" name=\"patientName\" value=\"1\">Patient name<br>");
			out.println("<input type=\"checkbox\" name=\"testType\" value=\"1\">Test type<br>");
			out.println("<input type=\"checkbox\" name=\"time\" value=\"1\">Time period<br>");
			
			out.println("<select name=\"hierarchy\">");
			out.println("<option value=\"\" style=\"display:none\">Choose a time hierarchy (if \"Time period\" is selected)</option>");
			out.println("<option value=\"weekly\">Weekly</option>");
			out.println("<option value=\"monthly\">Monthly</option>");
			out.println("<option value=\"yearly\">Yearly</option>");
			out.println("</select>");
			
			out.println("<input type=\"submit\" name=\"submit\" value=\"Submit\">");
			out.println("</form>");
			out.println("<p><a href=\"/radiology-proj/home\">Return to home</a></p>");
			out.println("</body>");
			out.println("</html>");
			}
	}
}
