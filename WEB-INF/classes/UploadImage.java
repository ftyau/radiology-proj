import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;
import oracle.sql.*;
import oracle.jdbc.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;

public class UploadImage extends HttpServlet {
    public String response_message;
    public void doPost(HttpServletRequest request,HttpServletResponse response)
		throws ServletException, IOException {
	String username = "chautran";
	String password = "davidchau1";
	String drivername = "oracle.jdbc.driver.OracleDriver";
	String dbstring ="jdbc:oracle:thin:@localhost:1525:crs";
	int image_id;
	int record_id = 0;

	try {
	    //Parse the HTTP request to get the image stream
	    DiskFileUpload fu = new DiskFileUpload();
	    List FileItems = fu.parseRequest(request);
	    
	    // Process the uploaded items, assuming only 1 image file uploaded
	    Iterator i = FileItems.iterator();
	    FileItem item = (FileItem) i.next();
	    while (i.hasNext() && item.isFormField()) {
			record_id = Integer.parseInt(item.getString());
		    item = (FileItem) i.next();
	    }

	    //Get the image stream
	    InputStream instream = item.getInputStream();
		
		//Get file extension
		int index = item.getName().lastIndexOf(".");
		String extension = item.getName().substring(index+1);
		
        // Connect to the database and create a statement
        Connection conn = getConnected(drivername,dbstring, username,password);
	    Statement stmt = conn.createStatement();

	    /*
	     *  First, to generate a unique pic_id using an SQL sequence
	     */
	    ResultSet rset1 = stmt.executeQuery("SELECT pic_id_sequence.nextval from dual");
	    rset1.next();
	    image_id = rset1.getInt(1);

	    //Insert an empty blob into the table first. Note that you have to 
	    //use the Oracle specific function empty_blob() to create an empty blob
	    stmt.execute("INSERT INTO pacs_images VALUES("+record_id+","+image_id+",empty_blob(),empty_blob(),empty_blob())");
 
	    // to retrieve the lob_locator 
	    // Note that you must use "FOR UPDATE" in the select statement
	    String cmd = "SELECT * FROM pacs_images WHERE image_id = "+image_id+" FOR UPDATE";
	    ResultSet rset = stmt.executeQuery(cmd);
	    rset.next();
		BLOB thumbblob = ((OracleResultSet)rset).getBLOB(3);
		BLOB normblob = ((OracleResultSet)rset).getBLOB(4);
	    BLOB fullblob = ((OracleResultSet)rset).getBLOB(5);


	    //Write the image to the blob object
		OutputStream fulloutstream = fullblob.getBinaryOutputStream();
		BufferedImage fullBufferedImg = ImageIO.read(instream);
		ImageIO.write(fullBufferedImg, extension, fulloutstream);
		
		OutputStream normoutstream = normblob.getBinaryOutputStream();
		Image normimg = fullBufferedImg.getScaledInstance(Math.min(250, 
			Math.min(fullBufferedImg.getHeight(),fullBufferedImg.getWidth())), -1, Image.SCALE_SMOOTH);
		BufferedImage normBufferedImg = new BufferedImage(normimg.getWidth(null),normimg.getHeight(null),
		BufferedImage.TYPE_INT_RGB);
		normBufferedImg.getGraphics().drawImage(normimg,0,0,null);
		ImageIO.write(normBufferedImg, extension, normoutstream);
		
		OutputStream thumboutstream = thumbblob.getBinaryOutputStream();
		Image thumbimg = fullBufferedImg.getScaledInstance(Math.min(100,
			Math.min(fullBufferedImg.getHeight(),fullBufferedImg.getWidth())), -1, Image.SCALE_SMOOTH);
		BufferedImage thumbBufferedImg = new BufferedImage(thumbimg.getWidth(null),thumbimg.getHeight(null),
		BufferedImage.TYPE_INT_RGB);
		thumbBufferedImg.getGraphics().drawImage(thumbimg,0,0,null);
		ImageIO.write(thumbBufferedImg, extension, thumboutstream);

	    instream.close();
		
        //stmt.executeUpdate("commit");
	    response_message = "Upload OK!";
        conn.close();

	} catch( Exception ex ) {
	    //System.out.println( ex.getMessage());
	    response_message = ex.getMessage();
	}

	//Output response to the client
	response.setContentType("text/html");
	PrintWriter out = response.getWriter();
	out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " +
		"Transitional//EN\">\n" +
		"<HTML>\n" +
		"<HEAD><TITLE>Upload Message</TITLE></HEAD>\n" +
		"<BODY>\n" +
		"<H1>" +
	        response_message +
		"</H1>\n" +
		"</BODY></HTML>");
	}

    /*
    /*   To connect to the specified database
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