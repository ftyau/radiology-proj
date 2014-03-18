<HTML>
<HEAD>
<TITLE>Report Generator</TITLE>
</HEAD>
<BODY>
<%@ page import="java.sql.*" %>
<form name="managementHome.jsp" method="GET">
	<%
		
	String [] monfs = {"Jan","Feb","Mar","Apr","May","Jun","Jul",
										"Aug","Sept","Oct","Nov","Dec"};

	ArrayList<String> list1 = new ArrayList<String>();

	for(String s: monfs){
		if(s.equals("Apr") || s.equals("Jun") || s.equals("Sept") || s.equals("Nov")){
			list1.add("30");
		}
		else if(s.equals("Feb")){
			list1.add("28");
		}
		else{
			list1.add("31");
		}
		//out.println(s);
	}

	//Month
	out.println("<select name=month>");
	for(int i =0;i<monfs.length;i++){
		out.println("<option value="+String.valueOf(i+1)+ ">"+monfs[i]+"</option>");
		if(monfs[i])
	}
	out.println("</select>");


	//


	//End
	%>

<H1><CENTER>Generate Report</CENTER></H1>
</TABLE>
</BODY>
</HTML>