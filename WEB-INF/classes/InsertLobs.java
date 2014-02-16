import java.io.*;
import java.sql.*;

  /**
   *  A simple example to demonstrate how to use JDBC's PreparedStatement
   *  to insert a Blob into a table. The table photos used in the example
   *  is created with
   *    create table photos (
              photo_id integer, 
	      image blob,
	      primary key(photo_id) 
	);
   *
   *  @author  Li-Yan Yuan
   *
   *  Please use a newer version of the JDBC driver. Otherwise, one may 
   *  encounter the error message: 
   *     ORA-01460: unimplemented or unreasonable conversion requested 
   */
  public class InsertLobs {

      public static void main( String[] args)  {

          //  change the following parameters to connect to other databases
          String username = "user_name";
          String password = "*****";

	  // the following is for accessing Oracle in our Labs
          String dbstring = "jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";
	  String drivername = "oracle.jdbc.driver.OracleDriver";


          // two local files of pictures
          String file_name = "window.jpg";

          try {
            // to connect to the database
            Connection conn = 
              getConnected(drivername,dbstring, username,password);

            //  create a preparedStatement with 
            //   ?  represents the lobs to be inserted
            PreparedStatement stmt = conn.prepareStatement(
                "insert into photos (photo_id,image) values (100,?)" );

            // Set the first parameter 
            File file = new File( file_name );
            stmt.setBinaryStream(1,new FileInputStream(file),(int)file.length());

            // execute the insert statement
            stmt.executeUpdate();
            System.out.println( "the execution succeeds");
            stmt.executeUpdate("commit");
            conn.close();
          } catch( Exception ex ) { 
              System.out.println( ex.getMessage());
          }
      }

      /*
       *   To connect to the specified database
       */
      private static Connection getConnected( String drivername,
                                              String dbstring,
                                              String username, 
                                              String password  ) 
          throws Exception {
          Class drvClass = Class.forName(drivername); 
          DriverManager.registerDriver((Driver) drvClass.newInstance());
          return( DriverManager.getConnection(dbstring,username,password));
      }
  } 
