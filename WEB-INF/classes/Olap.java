import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class Olap extends HttpServlet implements SingleThreadModel {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter ();
		if (request.getParameter("submit") != null) {
			String username = "chautran";
			String password = "davidchau1";
			String drivername = "oracle.jdbc.driver.OracleDriver";
			String dbstring ="jdbc:oracle:thin:@localhost:1525:crs";
			Connection conn = null;
			
			try {
				conn = getConnected(drivername,dbstring, username,password);
				
				//String query = "SELECT r.patient_id, r.test_type, r.test_date, COUNT(pi.image_id) FROM radiology_record r FULL JOIN pacs_images pi ON r.record_id = pi.record_id GROUP BY CUBE (r.patient_id, r.test_type, r.test_date)";
				String query = "SELECT ";
				if (request.getParameter("patientName") != null)
					query = query + "r.patient_id, ";
				if (request.getParameter("testType") != null)
					query = query + "r.test_type, ";	
				if (request.getParameter("time") != null)
					query = query + "r.test_date, ";
					
				query = query + "COUNT(pi.image_id) FROM radiology_record r FULL JOIN pacs_images pi ON r.record_id = pi.record_id GROUP BY CUBE (";
				
				if (request.getParameter("patientName") != null)
					query = query + "r.patient_id,";
				if (request.getParameter("testType") != null)
					query = query + "r.test_type,";	
				if (request.getParameter("time") != null)
					query = query + "r.test_date,";
				query = query.substring(0,query.length()-1);
				query = query + ")";
				
				PreparedStatement searchStatement = conn.prepareStatement(query);
				ResultSet rset = searchStatement.executeQuery();
				ResultSetMetaData rsetmd = rset.getMetaData();
				int column_count = rsetmd.getColumnCount();
				out.println("<html>");
				out.println("<head><title>Data analysis</title></head>");
				out.println("<body>");
				out.println(query);
				out.println("<br>");
				out.println("<table border=1>");
				out.println("<tr>");
				if (request.getParameter("patientName") != null)
					out.println("<th>Patient name</th>");
				if (request.getParameter("testType") != null)
					out.println("<th>Test type</th>");
				if (request.getParameter("time") != null)
					out.println("<th>Time period</th>");
				out.println("<th># of images</th>");
				out.println("</tr>");
				
				
				
				while (rset.next()){
					out.println("<tr>");
					for (int i=1;i<column_count+1;i++) {
						out.println("<td>");
						out.println(rset.getString(i));
						out.println("</td>");
					}
				}
				out.println("</body>");
				out.println("</html>");
				
				
				conn.close();
			} catch(Exception ex) {
				out.println(ex.getMessage());
			}
		} else {
			out.println("<html>");
			out.println("<head><title>Data analysis</title></head>");
			out.println("<body>");
			out.println("<form name=\"olap\" method=\"get\" action=\"olap\">");
			out.println("<input type=\"checkbox\" name=\"patientName\" value=\"1\">Patient name<br>");
			out.println("<input type=\"checkbox\" name=\"testType\" value=\"1\">Test type<br>");
			out.println("<input type=\"checkbox\" name=\"time\" value=\"1\">Time period<br>");
			out.println("<input type=\"submit\" name=\"submit\" value=\"Submit\">");
			out.println("</form>");
			out.println("</body>");
			out.println("</html>");
			}
	}
		
		
		





	
	private static Connection getConnected( String drivername,
					    String dbstring,
					    String username, 
					    String password  ) 
	throws Exception {
		Class drvClass = Class.forName(drivername); 
		DriverManager.registerDriver((Driver) drvClass.newInstance());
		return( DriverManager.getConnection(dbstring,username,password));
	}
}