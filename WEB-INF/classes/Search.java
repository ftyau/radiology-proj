import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.Arrays;

public class Search extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{
		HttpSession session = request.getSession(true);
		if (session.getAttribute("id") == null) {
			response.sendRedirect("/radiology-proj/login");
			return;
		}
		response.setContentType("text/html");
		PrintWriter out = response.getWriter ();
		Connection conn = null;
		boolean in_image_col = false;
		int id_check = -1;
		
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Search</title>");
		out.println("</head>");
		out.println("<body>");
		
		String user_class = (String) session.getAttribute("class");
		String user_id = (String) session.getAttribute("id");
		
		String request_keyword = request.getParameter("keyword");
		String request_time = request.getParameter("time");
		String request_sort = request.getParameter("sort");
		
		try {
			if (request.getParameter("search") != null) {
				if (request_keyword.equals("") && request_time.equals("")){
					out.println("Not searching anything!");
					return;
				}
			
				Database.dbConnection newDB = new Database.dbConnection();
				conn = newDB.connection();
				
				//Print out what the user searched
				if ((!(request_keyword.equals(""))) && (!(request_time.equals("")))) {
					out.println("Query is " + request_keyword + " with time period between " + request_time);
				} else if ((request_keyword.equals("")) && (!(request_time.equals("")))) {
					out.println("Query is a time period between " + request_time);
				} else if ((!(request_keyword.equals(""))) && (request_time.equals(""))) {
					out.println("Query is " + request_keyword);
				}
				
				//Create query to send to DB
				if((!(request_keyword.equals(""))) || (!(request_time.equals("")))) {
					String[] keyword_list = request_keyword.split("\\s+");
					String query = "SELECT p.first_name, p.last_name, r.*, pi.image_id ";
					if (!(request_keyword.equals(""))) {
						query = query + ",";
						int count_score = 0;
						for (int i=0;i<keyword_list.length;i++) {
							query = query + "6*score("+ Integer.toString(count_score+3) +")+6*score("+ Integer.toString(count_score+4) +")+3*score("+ Integer.toString(count_score+1) +")+score("+ Integer.toString(count_score+2)+")";
							if (i != keyword_list.length-1)
								query = query + "+";
							count_score += 4;
						}
						query = query + " AS rank ";
						//query = query + "6*score(3)+6*score(4)+3*score(1)+score(2) AS rank ";
					}
					query = query + "FROM radiology_record r FULL JOIN persons p ON r.patient_id = p.person_id FULL JOIN pacs_images pi ON pi.record_id = r.record_id FULL JOIN users u ON p.person_id = u.person_id " +
					"WHERE ";
					
					if (!(request_keyword.equals(""))) {
						//query = query + "(";
						int count_contains = 0;
						for (int i=0;i<keyword_list.length;i++) {
							query = query + "contains(r.diagnosis, ?, "+ Integer.toString(count_contains+1) +") > 0 OR contains(r.description, ?, "+ Integer.toString(count_contains+2) +") > 0 OR contains(p.first_name, ?, "+ Integer.toString(count_contains+3) +") > 0 OR contains(p.last_name, ?, "+ Integer.toString(count_contains+4) +") > 0 ";
							if (i != keyword_list.length-1)
								query = query + "OR ";
							count_contains += 4;
						}
						//query = query + ") ";
					}
					
					//Security module
					if (user_class.equals("p"))
						query = query + "AND r.patient_id = '" + user_id + "' ";
					else if (user_class.equals("d"))
						query = query + "AND r.doctor_id = '" + user_id + "' ";
					else if (user_class.equals("r"))
						query = query + "AND r.radiologist_id '" + user_id + "' ";
						
					if((!(request_keyword.equals(""))) && (!(request_time.equals(""))))
						query = query + "AND ";
					
					String[] request_time_array = request_time.split("\\s+");
					if ((request_time_array.length != 3) && (request_time_array.length != 1 || request_time_array[0] != "")){
						out.println("<br>Incorrect date input!");
						out.println("<p><a href=\"/radiology-proj/home\">Return to home</a></p>");
						out.println("<hr>");
						out.println("<p align=right><a href=\"/radiology-proj/help.html\">Help</a></p>");
						out.println("</body>");
						out.println("</html>");
						return;
					}
						
					if (!(request_time.equals(""))){
						String request_time_begin = request_time_array[0];
						String request_time_end = request_time_array[2];
						query = query + "(r.prescribing_date BETWEEN '" + request_time_begin + "' AND '" + request_time_end + "' OR r.test_date BETWEEN '" + request_time_begin + "' AND '" + request_time_end + "') ";
					}
					if (request_sort.equals("rank"))
						query = query + "ORDER BY ";
					else if (request_sort.equals("pasc"))
						query = query + "ORDER BY r.prescribing_date ASC, ";
					else if (request_sort.equals("pdesc"))
						query = query + "ORDER BY r.prescribing_date DESC, ";
					else if (request_sort.equals("tasc"))
						query = query + "ORDER BY r.test_date ASC, ";
					else if (request_sort.equals("tdesc"))
						query = query + "ORDER BY r.test_date DESC, ";
						
					if (!(request_keyword.equals("")))
						query = query + "rank desc, r.record_id";
					else
						query = query + "r.record_id";
					out.println("<br>" + query + "<br>");
					
					//Replaces ? with the search keywords
					PreparedStatement searchStatement = conn.prepareStatement(query);
					if (!(request_keyword.equals(""))) {
						int count_replace = 0;
						for (int i=0;i<keyword_list.length;i++) {
							searchStatement.setString(count_replace+1, keyword_list[i]);
							searchStatement.setString(count_replace+2, keyword_list[i]);
							searchStatement.setString(count_replace+3, keyword_list[i]);
							searchStatement.setString(count_replace+4, keyword_list[i]);
							count_replace += 4;
						}
					}
					ResultSet rset = searchStatement.executeQuery();
					
					//Create table
					if (rset.isBeforeFirst()){
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
						if (!(request_keyword.equals("")))
							out.println("<th>Score</th>");
						out.println("<th>Images</th>");
						//Create image column
						while(rset.next()) {
							if (id_check == rset.getInt(3)){
								in_image_col = true;
								out.println("<a href=\"/radiology-proj/images/view?norm"+rset.getInt(12)+"\">");
								out.println("<img src=\"/radiology-proj/images/getpic?thumb"+rset.getInt(12)+"\"></a>");
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
								if (!(request_keyword.equals(""))) {
									out.println("<td>");
									out.println(rset.getInt(13));
									out.println("</td>");
								}
								//Also image column
								if (!(rset.getInt(12) == 0)) {
									out.println("<td>");
									out.println("<a href=\"/radiology-proj/images/view?norm"+rset.getInt(12)+"\">");
									out.println("<img src=\"/radiology-proj/images/getpic?thumb"+rset.getInt(12)+"\"></a>");
									id_check = rset.getInt(3);
								}
							}
						}
						if (in_image_col == true) {
							out.println("</td>");
							in_image_col = false;
						}
						out.println("</tr>");
						out.println("</table>");
						
						//Strings for sort
						String new_query1;
						String new_query2;
						String new_query3;
						String new_query4;
						
						//URL for sort
						String uri = request.getScheme() + "://" +
							request.getServerName() +
							":" + request.getServerPort() +
							request.getRequestURI() + "?";
						
						//Create sort links and output them
						if (request.getParameter("sort").equals("rank")) {
							new_query1 = request.getQueryString().replaceAll("&sort=rank", "&sort=pasc");
							new_query2 = request.getQueryString().replaceAll("&sort=rank", "&sort=pdesc");
							new_query3 = request.getQueryString().replaceAll("&sort=rank", "&sort=tasc");
							new_query4 = request.getQueryString().replaceAll("&sort=rank", "&sort=tdesc");
							out.println("<a href=\"" + uri + new_query1 + "\">Sort by prescribing date ascending</a>");
							out.println("<a href=\"" + uri + new_query2 + "\">Sort by prescribing date descending</a>");
							out.println("<a href=\"" + uri + new_query3 + "\">Sort by test date ascending</a>");
							out.println("<a href=\"" + uri + new_query4 + "\">Sort by test date descending</a>");
						} else if (request.getParameter("sort").equals("pasc")) {
							new_query1 = request.getQueryString().replaceAll("&sort=pasc", "&sort=rank");
							new_query2 = request.getQueryString().replaceAll("&sort=pasc", "&sort=pdesc");
							new_query3 = request.getQueryString().replaceAll("&sort=pasc", "&sort=tasc");
							new_query4 = request.getQueryString().replaceAll("&sort=pasc", "&sort=tdesc");
							out.println("<a href=\"" + uri + new_query1 + "\">Sort by rank</a>");
							out.println("<a href=\"" + uri + new_query2 + "\">Sort by prescribing date descending</a>");
							out.println("<a href=\"" + uri + new_query3 + "\">Sort by test date ascending</a>");
							out.println("<a href=\"" + uri + new_query4 + "\">Sort by test date descending</a>");
						} else if (request.getParameter("sort").equals("pdesc")) {
							new_query1 = request.getQueryString().replaceAll("&sort=pdesc", "&sort=rank");
							new_query2 = request.getQueryString().replaceAll("&sort=pdesc", "&sort=pasc");
							new_query3 = request.getQueryString().replaceAll("&sort=pdesc", "&sort=tasc");
							new_query4 = request.getQueryString().replaceAll("&sort=pdesc", "&sort=tdesc");
							out.println("<a href=\"" + uri + new_query1 + "\">Sort by rank</a>");
							out.println("<a href=\"" + uri + new_query2 + "\">Sort by prescribing date ascending</a>");
							out.println("<a href=\"" + uri + new_query3 + "\">Sort by test date ascending</a>");
							out.println("<a href=\"" + uri + new_query4 + "\">Sort by test date descending</a>");
						} else if (request.getParameter("sort").equals("tasc")) {
							new_query1 = request.getQueryString().replaceAll("&sort=tasc", "&sort=rank");
							new_query2 = request.getQueryString().replaceAll("&sort=tasc", "&sort=pasc");
							new_query3 = request.getQueryString().replaceAll("&sort=tasc", "&sort=pdesc");
							new_query4 = request.getQueryString().replaceAll("&sort=tasc", "&sort=tdesc");
							out.println("<a href=\"" + uri + new_query1 + "\">Sort by rank</a>");
							out.println("<a href=\"" + uri + new_query2 + "\">Sort by prescribing date ascending</a>");
							out.println("<a href=\"" + uri + new_query3 + "\">Sort by prescribing date descending</a>");
							out.println("<a href=\"" + uri + new_query4 + "\">Sort by test date descending</a>");
						} else if (request.getParameter("sort").equals("tdesc")) {
							new_query1 = request.getQueryString().replaceAll("&sort=tdesc", "&sort=rank");
							new_query2 = request.getQueryString().replaceAll("&sort=tdesc", "&sort=pasc");
							new_query3 = request.getQueryString().replaceAll("&sort=tdesc", "&sort=pdesc");
							new_query4 = request.getQueryString().replaceAll("&sort=tdesc", "&sort=tasc");
							out.println("<a href=\"" + uri + new_query1 + "\">Sort by rank</a>");
							out.println("<a href=\"" + uri + new_query2 + "\">Sort by prescribing date ascending</a>");
							out.println("<a href=\"" + uri + new_query3 + "\">Sort by prescribing date descending</a>");
							out.println("<a href=\"" + uri + new_query4 + "\">Sort by test date ascending</a>");
						}
					}
				} else {
					out.println("Not searching anything!");
				}
				conn.close();
			} else {
				out.println("<html><head><title>Search</title></head>");
				out.println("<body>");
				out.println("<form name=\"search\" method=\"get\" action=\"search\">");
				out.println("<table>");
				out.println("<tr><td>Search by keywords: </td><td><input type=\"text\" name=keyword></td></tr>");
				out.println("<tr><td>Search by time period (format of \"YYYY/MM/DD to YYYY/MM/DD\"): </td><td><input type=\"text\" name=time></td>");
				out.println("<tr><td><input type=hidden name=sort value=\"rank\"></td></tr></tr><tr>");
				out.println("<td><input type=\"submit\" value=\"Search\" name=search></td>");
				out.println("</tr></table></form>");
			}
				
		} catch(Exception ex) {
			out.println("<br>Unexpected error. Date format may be incorrect or the database may be having an error.");
		}
		out.println("<p><a href=\"/radiology-proj/home\">Return to home</a></p>");
		out.println("<hr>");
		out.println("<p align=right><a href=\"/radiology-proj/help.html\">Help</a></p>");
		out.println("</body>");
		out.println("</html>");
	}
}