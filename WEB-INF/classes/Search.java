import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class Search extends HttpServlet implements SingleThreadModel {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter ();
		String username = "chautran";
		String password = "davidchau1";
		String drivername = "oracle.jdbc.driver.OracleDriver";
		String dbstring ="jdbc:oracle:thin:@localhost:1525:crs";
		Connection conn = null;
		boolean in_image_col = false;
		int id_check = -1;
		
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Search</title>");
		out.println("</head>");
		out.println("<body>");
		
		try {
			conn = getConnected(drivername,dbstring, username,password);
			if (request.getParameter("search") != null) {  
			out.println("Query is " + request.getParameter("query"));
				if(!(request.getParameter("query").equals(""))) {
					PreparedStatement searchStatement = conn.prepareStatement("SELECT p.first_name, p.last_name, r.*, pi.image_id, 6*score(3)+6*score(4)+3*score(1)+score(2) AS rank " + 
					"FROM radiology_record r INNER JOIN persons p ON r.patient_id = p.person_id INNER JOIN pacs_images pi ON pi.record_id = r.record_id " +
					"WHERE contains(r.diagnosis, ?, 1) > 0 OR contains(r.description, ?, 2) > 0 OR contains(p.first_name, ?, 3) > 0 OR contains(p.last_name, ?, 4) > 0 " +
					"ORDER BY rank desc, r.record_id");
					searchStatement.setString(1, request.getParameter("query"));
					searchStatement.setString(2, request.getParameter("query"));
					searchStatement.setString(3, request.getParameter("query"));
					searchStatement.setString(4, request.getParameter("query"));
					ResultSet rset = searchStatement.executeQuery();
					out.println("<table border=1>");
					out.println("<tr>");
					out.println("<th>First Name</th>");
					out.println("<th>Last Name</th>");
					out.println("<th>Record ID</th>");
					out.println("<th>Patient ID</th>");
					out.println("<th>Doctor ID</th>");
					out.println("<th>Radiologist ID</th>");
					out.println("<th>Test Type</th>");
					out.println("<th>Prescribing Date</th>");
					out.println("<th>Test Date</th>");
					out.println("<th>Diagnosis</th>");
					out.println("<th>Description</th>");
					out.println("<th>Score</th>");
					out.println("<th>Images</th>");
					while(rset.next()) {
						if (id_check == rset.getInt(3)){
							in_image_col = true;
							out.println("<a href=\"/radiology-proj/images/view?norm"+rset.getInt(12)+"\">");
							out.println("<img src=\"/radiology-proj/images/GetOnePic?thumb"+rset.getInt(12)+"\"></a>");
						} else {
							if (in_image_col == true) {
								out.println("</td>");
								in_image_col = false;
							}
							out.println("</tr>");
							out.println("<tr>");
							out.println("<td>");
							out.println(rset.getString(1));
							out.println("</td>");
							out.println("<td>");
							out.println(rset.getString(2));
							out.println("</td>");
							out.println("<td>"); 
							out.println(rset.getInt(3));
							out.println("</td>");
							out.println("<td>"); 
							out.println(rset.getInt(4)); 
							out.println("</td>");
							out.println("<td>");
							out.println(rset.getInt(5));
							out.println("</td>");
							out.println("<td>");
							out.println(rset.getInt(6));
							out.println("</td>");
							out.println("<td>");
							out.println(rset.getString(7));
							out.println("</td>");
							out.println("<td>");
							out.println(rset.getString(8));
							out.println("</td>");
							out.println("<td>");
							out.println(rset.getString(9));
							out.println("</td>");
							out.println("<td>");
							out.println(rset.getString(10));
							out.println("</td>");
							out.println("<td>");
							out.println(rset.getString(11));
							out.println("</td>");
							out.println("<td>");
							out.println(rset.getInt(13));
							out.println("</td>");
							out.println("<td>");
							out.println("<a href=\"/radiology-proj/images/view?norm"+rset.getInt(12)+"\">");
							out.println("<img src=\"/radiology-proj/images/GetOnePic?thumb"+rset.getInt(12)+"\"></a>");
							id_check = rset.getInt(3);
						}
					}
				if (in_image_col == true) {
					out.println("</td>");
					in_image_col = false;
				}
				out.println("</tr>");
                out.println("</table>");
				conn.close();
				}
			}
		} catch(Exception ex) {
			out.println(ex.getMessage());
		}
		
		out.println("</body>");
		out.println("</html>");
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