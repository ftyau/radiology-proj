
<HTML>
<HEAD>
<TITLE>Update Tables</TITLE>
</HEAD>
<BODY>
<%@ page import="java.sql.*,Database.dbConnection,java.util.*" %>
<%! String tableType;%>
<%
tableType = request.getParameter("tableType");
if(request.getParameter("bSubmit") != null){
    if(tableType.equals("persons")){
        dbConnection newDB = new dbConnection();
        Connection conn = newDB.connection(); 
        Statement stmt = conn.createStatement();

        ResultSet results = stmt.executeQuery("SELECT * FROM PERSONS");

        ResultSetMetaData meta = results.getMetaData();

        out.println("<H1><CENTER>Update Table Persons</CENTER></H1>");
        out.println("<TABLE>");
        out.println("<FORM METHOD=POST ACTION=updateQuery.jsp>");
        out.println("<TR>Update Persons</TR><BR>");

        out.println("<TR VALIGN=TOP ALIGN=LEFT>");
        out.println("SET");
        out.println("<select name=setCol id=dropdown>");
        for(int i =1; i<meta.getColumnCount(); i++){
            String col = meta.getColumnName(i);
            out.println("<option value="+col+">"+col+"</option>");
        }
        out.println("<INPUT TYPE=text NAME=setValue><BR>");
        out.println("</select></TR>");
        
        out.println("<TR VALIGN=TOP ALIGN=LEFT>");
        out.println("Where");
        out.println("<select name=setWhereCol id=dropdown>");
        for(int i =1; i<meta.getColumnCount(); i++){
            String col = meta.getColumnName(i);
            out.println("<option value="+col+">"+col+"</option>");
        }
        out.println("<INPUT TYPE=text NAME=setWhere><BR>");
        out.println("</select></TR>");

        out.println("</TABLE>");
        out.println("<INPUT TYPE=submit NAME=submitUpdatePersons VALUE=Submit>");
        out.println("</FORM");
    }


    if(tableType.equals("users")){
    out.println("Users");

    }

    if(tableType.equals("family_doctor")){
    out.println("Fam Doc");
    }   
}
else{
    dbConnection newDB = new dbConnection();
    Connection conn = newDB.connection(); 
    if(request.getParameter("submitUpdatePersons")!=null){
        String setColName = request.getParameter("setCol");
        String setValue = request.getParameter("setValue");

        //out.println(setColName + " " + setValue);

        String whereColName = request.getParameter("setWhereCol");
        String whereValue = request.getParameter("setWhere");
        //out.println(whereColName+ " " + whereValue);
        
        String sql = "UPDATE PERSONS " + "SET COLNAME1 = ? where COLNAME2 = ?";
        sql = sql.replace("COLNAME1",setColName);
        sql = sql.replace("COLNAME2",whereColName);


        //out.println("SQL: " + sql);
        try{
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setString(1,setValue);
            stmt.setString(2,whereValue);
            stmt.executeUpdate();
            conn.commit();
            stmt.close();
            conn.close();
        }catch(SQLException e){
            e.printStackTrace();
            out.println(e);
        }
        response.sendRedirect("/radiology-proj/home.jsp");
    }
    else{
        out.println("<H1><CENTER>Update Queries</CENTER></H1>");
        out.println("<P>Pick a table to update</P>");
        out.println("<FORM method=post action=updateQuery.jsp name=tableForm>");
        out.println("<select name=tableType id=dropdown>");
        out.println("<option value=dropdown>Select Table</option>");
        out.println("<option value=persons>Persons</option>");
        out.println("<option value=users>User</option>");
        out.println("<option value=family_doctor>Family Doctor</option>");
        out.println("</select>");
        out.println("<input type=submit value=Enter NAME=bSubmit>");
        out.println("</FORM>");
    }
}
%>