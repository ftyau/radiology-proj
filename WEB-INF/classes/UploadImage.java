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
	
	public void doGet(HttpServletRequest request,HttpServletResponse response)
		throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		//Gets all the existing record IDs
		try {
			Database.dbConnection newDB = new Database.dbConnection();
			Connection conn = newDB.connection();
			String query = "SELECT record_id FROM radiology_record";
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			
			
			if (request.getParameter("submit") == null) {
				out.println("<html><head><title>Upload image</title></head>");
				out.println("<body>");
				out.println("<form name=\"upload-image\" method=\"POST\" enctype=\"multipart/form-data\" action=\"upload\">");
				out.println("<table><tr><th>Radiology record to upload to: </th><td>");
				out.println("<select name=\"recordId\">");
				while (rset.next()) {
					out.println("<option vaule=\""+rset.getString(1)+"\">"+rset.getString(1)+"</option>");
				}
				out.println("</select>");
				
				out.println("<tr><th>File path: </th><td><input name=file-path type=\"file\" size=\"30\"></input></td></tr>");
				out.println("<tr><td ALIGN=CENTER COLSPAN=\"2\"><input type=\"submit\" name=\".submit\" value=\"Upload\"></td></tr>");
				out.println("</table></form></body></html>");
			}
		} catch(Exception ex) {
			out.println(ex.getMessage());
		}
	}
    public void doPost(HttpServletRequest request,HttpServletResponse response)
		throws ServletException, IOException {
	response.setContentType("text/html");
	PrintWriter out = response.getWriter();
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
		Database.dbConnection newDB = new Database.dbConnection();
        Connection conn = newDB.connection();
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
	out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " +
		"Transitional//EN\">\n" +
		"<HTML>\n" +
		"<HEAD><TITLE>Upload Message</TITLE></HEAD>\n" +
		"<BODY>\n" +
		"<H1>" +
	        response_message +
		"</H1>\n" +
		"<p><a href=\"/radiology-proj/home.jsp\">Return to home</a></p>"+
		"</BODY></HTML>");
	}
}	