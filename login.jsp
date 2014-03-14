<HTML>
<HEAD>
<TITLE>Login Result</TITLE>
</HEAD>
<BODY>
<%@ page import="java.sql.*,Database.dbConnection" %><%
    if(request.getParameter("bSubmit") != null){

        //Get inputted Username and pasword
    	String inputUsername = (request.getParameter("USERID")).trim();
        String inputPassword = (request.getParameter("PASSWD")).trim();

        dbConnection newDB = new dbConnection();
        Connection conn = newDB.connection();
        

        //Execute Queries
        String sql = "select user_name, password, person_id from users";
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
        int personID = 1;

        //Get results and assign them to variables
    	while(results != null && results.next()){
            personID = (results.getInt("person_id"));
        	dbLogin = (results.getString("user_name")).trim();
            dbPassword = (results.getString("password")).trim();

            out.println("<p>PersonID: "+ String.valueOf(personID)+"</p>");
            out.println("<p>Login: "+ dbLogin+"</p>");
            out.println("<p>Password: " + dbPassword+"</p>");
        }
        results.close();
        conn.close();

        if(inputPassword.equals(dbPassword)){
            String id = String.valueOf(personID);
            session.setAttribute("id",id);
            response.sendRedirect("home.jsp");
        }
    }
    else{
        out.println("<H1><CENTER>Radiology Database Login</CENTER></H1>");
        out.println("<P>To login successfully, you need to submit a valid userid and password</P>");

        out.println("<FORM METHOD=post ACTION=login.jsp>");

        out.println("<TABLE>");

        out.println("<TR VALIGN=TOP ALIGN=LEFT>");
        out.println("<TD><B> UserName: </B></TD>");
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