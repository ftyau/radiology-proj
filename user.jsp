<HTML>
<HEAD>
<TITLE>User</TITLE>
</HEAD>
<BODY>
<%@ page import="java.sql.*, User.UserModel" %>
<%
	UserModel newUser =  new UserModel();

    String tempID =  (String)(session.getAttribute("id"));
    int personID = Integer.valueOf(tempID);

    //establish the connection to the underlying database
    try{
        String driverName = "oracle.jdbc.driver.OracleDriver";
		Class drvClass = Class.forName(driverName); 
    	DriverManager.registerDriver((Driver) drvClass.newInstance());
	}
    catch(Exception ex){
        out.println("<hr>" + ex.getMessage() + "<hr>");
    }

    Connection conn = null;
	try{
        String dbstring = "jdbc:oracle:thin:@localhost:1525:crs"; 
        //String dbstring = "jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";
        conn = DriverManager.getConnection(dbstring,"chautran","davidchau1");
		conn.setAutoCommit(false);
    }
	catch(Exception ex){
        out.println("<hr>" + ex.getMessage() + "<hr>");
	}  

    ResultSet results = null;
	String sql = "select first_name, last_name, " 
				+ "address, email, phone "
				+ "from persons where person_id = ?";

	try{
    	PreparedStatement stmt = conn.prepareStatement(sql);
    	stmt.setInt(1,personID);
        results = stmt.executeQuery();
	}
    catch(Exception ex){
        out.println("<hr>" + ex.getMessage() + "<hr>");
	}

	while(results != null && results.next()){
    	newUser.setFirstName((results.getString("first_name")).trim());
    	newUser.setLastName((results.getString("last_name")).trim());
    	newUser.setAddress((results.getString("address")).trim());
    	newUser.setEmail((results.getString("email")).trim());
    	newUser.setPhone((results.getString("phone")).trim());
    }
%>

<H1><CENTER>Personal Information</CENTER></H1>
<TABLE ALIGN="CENTER">
    <TABLE BORDER="1">
        <%session.setAttribute("id",tempID);%>
        <TR>
            <TD>First Name: <%=newUser.getFirstName()%> </TD>
            <TD><a href="editInfo.jsp?colNum=1">Edit</a></TD>
        </TR>

       <TR>
            <TD>Last Name: <%=newUser.getLastName()%> </TD>
            <TD><a href="editInfo.jsp?colNum=2">Edit</a></TD>
       </TR>

       <TR>
            <TD>Address: <%=newUser.getAddress()%> </TD>
            <TD><a href="editInfo.jsp?colNum=3">Edit</a></TD>
       </TR>

       <TR>
            <TD>Email: <%=newUser.getEmail()%> </TD>
            <TD><a href="editInfo.jsp?colNum=4">Edit</a></TD>
       </TR>

        <TR>
            <TD>Phone: <%=newUser.getPhone()%> </TD>
            <TD><a href="editInfo.jsp?colNum=5">Edit</a></TD>
       </TR>
   </TABLE>
</TABLE>


</BODY>
</HTML>