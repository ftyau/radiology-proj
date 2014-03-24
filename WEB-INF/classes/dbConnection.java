package Database;

import java.sql.*;

public class dbConnection{

	private Connection conn = null;

    public Connection connection(){
	    try{
	        String driverName = "oracle.jdbc.driver.OracleDriver";
			Class drvClass = Class.forName(driverName); 
	    	DriverManager.registerDriver((Driver) drvClass.newInstance());
		}
	    catch(Exception ex){
	        System.out.println("<hr>" + ex.getMessage() + "<hr>");
	    }

		try{
	        String dbstring = "jdbc:oracle:thin:@localhost:1525:crs"; 
	        //String dbstring = "jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";
	        conn = DriverManager.getConnection(dbstring,"chautran","davidchau1");
			conn.setAutoCommit(false);
	    }
		catch(Exception ex){
	        System.out.println("<hr>" + ex.getMessage() + "<hr>");
		}
		return conn;
	}
}