<HTML>
<HEAD>
<TITLE>Edit User Info</TITLE>
</HEAD>
<BODY>
<%@ page import="java.sql.*" %>
<%! String colName;%>
<% 	
	String tempID =  (String)(session.getAttribute("id"));
	int personID = Integer.valueOf(tempID);

	if(request.getParameter("bSubmit") != null){
		String input = (request.getParameter("userInput")).trim();

		Connection conn = null;


	    try{
	        String driverName = "oracle.jdbc.driver.OracleDriver";
			Class drvClass = Class.forName(driverName); 
	    	DriverManager.registerDriver((Driver) drvClass.newInstance());
		}
	    catch(Exception ex){
	        out.println("<hr>" + ex.getMessage() + "<hr>");
	    }

		try{
	        String dbstring = "jdbc:oracle:thin:@localhost:1525:crs"; 
	        //String dbstring = "jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";
	        conn = DriverManager.getConnection(dbstring,"chautran","davidchau1");
			conn.setAutoCommit(false);
	    }
		catch(Exception ex){
	        out.println("<hr>" + ex.getMessage() + "<hr>");
		}

		try{
			String sql = getQuery(colName);
	    	PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1,input);
			stmt.setInt(2,personID);
	        stmt.executeUpdate();
	        conn.commit();
			conn.close();
			stmt.close();
			out.println("<hr>" + "DB UPDATED" + "<hr>");
		}
	    catch(Exception ex){
	        out.println("<hr>" + ex.getMessage() + "11<hr>");
		}
		response.sendRedirect("home.jsp");
	}
	else{
		out.println("<H1><CENTER>Edit User Information</CENTER></H1>");
		out.println("<FORM METHOD=post ACTION=editInfo.jsp>");

		out.println("<TABLE>");
		out.println("<TR VALIGN=TOP ALIGN=LEFT>");

		out.println("<TD><B> New Value </B></TD>");
		out.println("<TD><INPUT TYPE=text NAME=userInput><BR></TD>");
		colName = (String) request.getParameter("colNum");

		out.println("</TR>");
		out.println("</TABLE>");

		out.println("<INPUT TYPE=submit NAME=bSubmit VALUE=Submit>");
		out.println("</FORM>");
		out.println("<HR>");
}
%>
</BODY>
</HTML>

<!--Methods -->
<%!
public String getQuery(String colName){
	if(colName.equals("1")){
		String sql = "UPDATE persons SET first_name =? "
						+ "WHERE person_id =?";
		return sql;
	}
	else if(colName.equals("2")){
		String sql = "UPDATE persons SET last_name =? "
						+ "WHERE person_id =?";
		return sql;
	}
	else if(colName.equals("3")){
		String sql = "UPDATE persons SET address =? "
						+ "WHERE person_id =?";
		return sql;
	}
	else if(colName.equals("4")){
		String sql = "UPDATE persons SET email =? "
						+ "WHERE person_id =?";
		return sql;
	}
	else if(colName.equals("5")){
		String sql = "UPDATE persons SET phone =? "
						+ "WHERE person_id =?";
		return sql;
	}
	return "Error";
}
%>