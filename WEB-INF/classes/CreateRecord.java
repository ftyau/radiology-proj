import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class CreateRecord extends HttpServlet {
    public String response_message;
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{
		
		HttpSession session = request.getSession(true);
		if (session.getAttribute("id") == null) {
			response.sendRedirect("/radiology-proj/login");
			return;
		}
		response.setContentType("text/html");
		PrintWriter out = response.getWriter ();
		out.println("<html>");
		out.println("<head><title>Create a new record</title></head>");
		out.println("<body>");
		
		if (request.getParameter("create") == null) {
			out.println("<form name=\"create\" method=\"post\" action=\"newrecord\">");
			out.println("<table>");
			out.println("<tr><td>Patient ID: </td><td><input type=\"text\" name=patientid></td></tr>");
			out.println("<tr><td>Doctor ID: </td><td><input type=\"text\" name=doctorid></td></tr>");
			out.println("<tr><td>Radiologist ID: </td><td><input type=\"text\" name=radiologistid></td></tr>");
			out.println("<tr><td>Test type : </td><td><input type=\"text\" name=testtype></td></tr>");
			out.println("<tr><td>Prescribing date (YYYY/MM/DD) : </td><td><input type=\"text\" name=prescribingdate></td></tr>");
			out.println("<tr><td>Test date (YYYY/MM/DD) : </td><td><input type=\"text\" name=testdate></td></tr>");
			out.println("<tr><td>Diagnosis: </td><td><input type=\"text\" name=diagnosis></td></tr>");
			out.println("<tr><td>Description: </td><td><input type=\"text\" name=description></td></tr>");
			out.println("<td><input type=\"submit\" value=\"Create\" name=create></td>");
			out.println("</tr></table></form>");
		}

		
		out.println("<p><a href=\"/radiology-proj/home\">Return to home</a></p>");
		out.println("<hr>");
		out.println("<p align=right><a href=\"/radiology-proj/help.html\">Help</a></p>");
		out.println("</body>");
		out.println("</html>");
	}
	
	public void doPost(HttpServletRequest request,HttpServletResponse response)
		throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		try{
			String patient_id = request.getParameter("patientid");
			String doctor_id = request.getParameter("doctorid");
			String radiologist_id = request.getParameter("radiologistid");
			String test_type = request.getParameter("testtype");
			String prescribing_date = request.getParameter("prescribingdate");
			String test_date = request.getParameter("testdate");
			String diagnosis = request.getParameter("diagnosis");
			String description = request.getParameter("description");
			
			Database.dbConnection newDB = new Database.dbConnection();
			Connection conn = newDB.connection();
			
			int id = -1;
			String sql1 = "SELECT MAX(record_id) AS id FROM radiology_record";
			Statement stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery(sql1);
			while(results.next()){
				id = results.getInt("id");
			}
			id = id + 1;

			String query = "INSERT INTO radiology_record (record_id,patient_id,doctor_id,radiologist_id,test_type,prescribing_date,test_date,diagnosis,description) " +
						   "VALUES ('"+String.valueOf(id)+"', '"+patient_id+"', '"+doctor_id+"', '"+radiologist_id+"', '"+test_type+"', '"+prescribing_date+"', '"+test_date+"', '"+diagnosis+"', '"+description+"')";

			stmt.executeUpdate(query);
			response_message = "Record created with ID of "+String.valueOf(id)+"!";
			conn.close();
		} catch(Exception ex) {
			response_message = ex.getMessage();
			out.println(ex);
			out.println("Unexpected error. Date format may be incorrect or inputs may be too long or the database may be having an error.");
		}
		out.println(
			"<HTML>\n" +
			"<HEAD><TITLE>Create Record Message</TITLE></HEAD>\n" +
			"<BODY>\n" +
			"<H1>" +
				response_message +
			"</H1>\n" +
			"<p><a href=\"/radiology-proj/home\">Return to home</a></p>"+
			"</BODY></HTML>");
	}
}