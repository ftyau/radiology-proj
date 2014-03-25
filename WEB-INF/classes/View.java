import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class View extends HttpServlet implements SingleThreadModel {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		String picid  = request.getQueryString();
		response.setContentType("text/html");
		PrintWriter out = response.getWriter ();
		out.println("<html>");
		out.println("<head><title>");
		if (picid.startsWith("norm"))
			out.println(picid.substring(4));
		else
			out.println(picid);
		out.println("</title></head>");
		out.println("<body>");
		if (picid.startsWith("norm")) {
			out.println("<a href=\"/radiology-proj/images/view?"+picid.substring(4)+"\">");
			out.println("<img src=\"/radiology-proj/images/getpic?"+picid+"\"></a>");
		} else {
			out.println("<img src=\"/radiology-proj/images/getpic?"+picid+"\"></a>");
		}
		out.println("<p><a href=\"/radiology-proj/home\">Return to home</a></p>");
		out.println("</body>");
		out.println("</html>");
	}
}