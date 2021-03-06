import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

//Refreshes the existing view with updated info
public class Refresh extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{
		response.setContentType("text/html");
		
		HttpSession session = request.getSession(true);
		if (session.getAttribute("id") == null) {
			response.sendRedirect("/radiology-proj/login");
			return;
		}
		
		PrintWriter out = response.getWriter ();
		Connection conn = null;
		
		try{
			Database.dbConnection newDB = new Database.dbConnection();
			conn = newDB.connection();
			String query = "CREATE OR REPLACE VIEW olap_view AS " + 
						   "SELECT r.patient_id, r.test_type, r.test_date, pi.image_id " +
						   "FROM radiology_record r FULL JOIN pacs_images pi ON r.record_id = pi.record_id";
			Statement createStatement = conn.createStatement();
			createStatement.execute(query);
			conn.commit();
			
			out.println("<html><head><title>Success</title></head>");
			out.println("<body><h1>Table successfully refreshed!</h1>");
			out.println("<a href=\"/radiology-proj/olap\">Return to data analysis</a>");
			out.println("<hr>");
			out.println("<p align=right><a href=\"/radiology-proj/help.html\">Help</a></p>");
			out.println("</body></html>");
			
			conn.close();
		} catch(Exception ex) {
				out.println(ex.getMessage());
		}
	}
}