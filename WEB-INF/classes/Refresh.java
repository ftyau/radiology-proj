import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class Refresh extends HttpServlet implements SingleThreadModel {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter ();
		String username = "chautran";
		String password = "davidchau1";
		String drivername = "oracle.jdbc.driver.OracleDriver";
		String dbstring ="jdbc:oracle:thin:@localhost:1525:crs";
		Connection conn = null;
		
		try{
			conn = getConnected(drivername,dbstring, username,password);
			String query = "CREATE OR REPLACE VIEW olap_view AS " + 
						   "SELECT r.patient_id, r.test_type, r.test_date, pi.image_id " +
						   "FROM radiology_record r FULL JOIN pacs_images pi ON r.record_id = pi.record_id";
			Statement createStatement = conn.createStatement();
			createStatement.execute(query);
			conn.commit();
			
			out.println("<html><head><title>Success</title></head>");
			out.println("<body><h1>Table successfully refreshed!</h1>");
			out.println("<a href=\"/radiology-proj/olap\">Return to data analysis</a>");
			out.println("</body></html>");
			
			conn.close();
		} catch(Exception ex) {
				out.println(ex.getMessage());
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