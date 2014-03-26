import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import oracle.jdbc.driver.*;

public class Generate extends HttpServlet {
	Connection conn = null;
	String tableType = "";
	PrintWriter out;
    public void doGet(HttpServletRequest request,HttpServletResponse res) throws ServletException, IOException {
    	HttpSession session = request.getSession(true);
    	
    	res.setContentType("text/html");
		out = res.getWriter ();

		Database.dbConnection newDB = new Database.dbConnection();
        Connection conn = newDB.connection();


	  	if(request.getParameter("bSubmit")!=null){

			ArrayList<ArrayList<String>> outer = new ArrayList<ArrayList<String>>();
		
			String diagnosis = request.getParameter("diagnosis").trim();
			String testdate1 = request.getParameter("inputDate1").trim();
			String testdate2 = request.getParameter("inputDate2").trim();
				// out.println(diagnosis);
				// out.println(testdate1);
				// out.println(testdate2);
			try{
				String sql = "SELECT DISTINCT FIRST_NAME,LAST_NAME,ADDRESS,PHONE FROM PERSONS p, RADIOLOGY_RECORD r " 
							+"WHERE p.PERSON_ID = r.PATIENT_ID  AND r.DIAGNOSIS LIKE" + "'%" +diagnosis+ "%'" +
							" AND r.test_date BETWEEN" + "'"+ testdate1 +"'" + "AND" + "'" + testdate2 + "'";

		       	Statement stmt = conn.createStatement();
		        ResultSet results = stmt.executeQuery(sql);

		        while(results != null && results.next()){	
		    		String firstname = results.getString("FIRST_NAME");
		    		String lastname  = results.getString("LAST_NAME");
		    		String address = results.getString("ADDRESS");
		    		String phone = results.getString("PHONE");
		    		
		    		ArrayList<String> inner = new ArrayList<String>();
		    		inner.add(firstname);
		    		inner.add(lastname);
		    		inner.add(address);
		    		inner.add(phone);

		    		outer.add(inner);
		    	}
				stmt.close();
		        conn.close();
		    }catch(SQLException e){
		    	e.printStackTrace();
		    }

			//Print data out into a table
			out.println("<H1><CENTER>Report</CENTER></H1>");
			out.println("<table border=1>");
			out.println("<TR>");
			out.println("<TH>First Name</TH>");
			out.println("<TH>Last Name</TH>");
			out.println("<TH>Address</TH>");
			out.println("<TH>Phone Number</TH>");
			out.println("</TR>");
			out.println("<BR>");

			for(int i =0; i<outer.size();i++){
				out.println("<TR>");
				out.println("<TH>" + outer.get(i).get(0)+ "</TH>"); //First Name
				out.println("<TH>" + outer.get(i).get(1)+ "</TH>"); //Last Name
				out.println("<TH>" + outer.get(i).get(2)+ "</TH>"); //Address
				out.println("<TH>" + outer.get(i).get(3)+ "</TH>"); //Phone
				out.println("</TR>");
			}
			out.println("</TABLE>");
			out.println("<BR><p><a href=\"/radiology-proj/home\">Return to home</a></p>");
	  	}
	  	else{
	  		out.println("<H1><CENTER>Generate Report</CENTER></H1>");
			out.println("<P>Enter Diagnosis and/or date of test</P>");

			out.println("<FORM METHOD=GET ACTION=Generate>");

			out.println("<TABLE>");

			out.println("<TR VALIGN=TOP ALIGN=LEFT>");
			out.println("<TD><B>Diagnosis: </B></TD>");
			out.println("<TD><INPUT TYPE=text NAME=diagnosis><BR></TD>");
			out.println("</TR>");

			out.println("<TR VALIGN=TOP ALIGN=LEFT>");
			out.println("<TD><B>Test Date (YYYY-MM-DD): </B></TD>");
			out.println("<TD><INPUT TYPE=text NAME=inputDate1><BR></TD>");
			out.println("</TR>");

			out.println("<TR VALIGN=TOP ALIGN=LEFT>");
			out.println("<TD><B>Test Date (YYYY-MM-DD): </B></TD>");
			out.println("<TD><INPUT TYPE=text NAME=inputDate2><BR></TD>");
			out.println("</TR>");

			out.println("</TABLE>");

			out.println("<INPUT TYPE=submit NAME=bSubmit VALUE=Submit>");
			out.println("</FORM>");
			out.println("<HR>");
			out.println("<BR><p><a href=\"/radiology-proj/home\">Return to home</a></p>");
	    }
	}
}