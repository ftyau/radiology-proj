<%

if(request.getParameter("bSubmit") != null){
    dbConnection newDB = new dbConnection();
    Connection conn = newDB.connection();

    //Get duh input
    String x = request.getParameter("SQLStatement");
    out.println("x:" + x);

    ResultSet results = null;

 
}
else{
    out.println("<H1><CENTER>Insert New Query</CENTER></H1>");
    out.println("<form name=executeQuery.jsp method=post>");
    out.println("<TABLE ALIGN=CENTER>");
    out.println("<TR>");
    out.println("<TH>SQL Statement: </TH>");
    out.println("<TD><input name=SQLStatement type=textfield size=30></input></TD>");
    out.println("</TR>");
    out.println("<TR>");
    out.println("<TD ALIGN=CENTER COLSPAN=2>");
    out.println("<input type=submit name=bSubmit value=Submit></TD>");
    out.println("</TR>");
    out.println("</TABLE>");
    out.println("</form>");
}
%>