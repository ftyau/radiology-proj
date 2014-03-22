<HTML>
<HEAD>
<TITLE>Login Result</TITLE>
</HEAD>
<BODY>
<%@ page import="java.sql.*,Database.dbConnection" %><%
    if(request.getParameter("bSubmit") != null){
		
        //Get input username and password
    	String inputUsername = (request.getParameter("USERID")).trim();
        String inputPassword = (request.getParameter("PASSWD")).trim();

        dbConnection newDB = new dbConnection();
        Connection conn = newDB.connection();
        
        //Execute Queries
        String sql = "select user_name, password, person_id, class from users where user_name = '" + inputUsername + "'";
        ResultSet results = null;

        try{
            Statement stmt = conn.createStatement();
            results = stmt.executeQuery(sql);
        }
        catch(Exception ex){
            out.println("<hr>" + ex.getMessage() + "<hr>");
        }

        String dbLogin = "";
        String dbPassword = "";
        int personID = 0;
		
		if (results != null)
			results.next(); 
			
		dbPassword = results.getString("password").trim();
		if (inputPassword.equals(dbPassword)) {
			String id = Integer.toString(results.getInt("person_id"));
			session.setAttribute("id",id);
			session.setAttribute("class", results.getString("class").trim());
			response.sendRedirect("home.jsp");
			conn.close();
		}
		
		out.println("Username or password not valid!");
    }
    else{
        out.println("<H1><CENTER>Radiology Database Login</CENTER></H1>");
        out.println("<P>To login successfully, you need to enter a valid username and password.</P>");

        out.println("<FORM METHOD=post ACTION=login.jsp>");

        out.println("<TABLE>");

        out.println("<TR VALIGN=TOP ALIGN=LEFT>");
        out.println("<TD><B> Username: </B></TD>");
        out.println("<TD><INPUT TYPE=text NAME=USERID VALUE=harrysmith><BR></TD>");
        out.println("</TR>");

        out.println("<TR VALIGN=TOP ALIGN=LEFT>");
        out.println("<TD><B> Password: </B></TD>");
        out.println("<TD><INPUT TYPE=password NAME=PASSWD VALUE=111><BR></TD>");
        out.println("</TR>");

        out.println("</TABLE>");

        out.println("<INPUT TYPE=submit NAME=bSubmit VALUE=Submit>");
        out.println("</FORM>");
        out.println("<HR>");
    }

		
%>
</BODY>
</HTML>