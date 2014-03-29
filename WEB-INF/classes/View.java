import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

//Gets images from GetOnePic and outputs them as clickable images for zooming in
public class View extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		String picid  = request.getQueryString();
		response.setContentType("text/html");
		
		HttpSession session = request.getSession(true);
		if (session.getAttribute("id") == null) {
			response.sendRedirect("/radiology-proj/login");
			return;
		}
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
			out.println("<a href=\"/radiology-proj/images/view?norm"+picid+"\">");
			out.println("<img src=\"/radiology-proj/images/getpic?"+picid+"\"></a>");
		}
		out.println("<p><a href=\"/radiology-proj/home\">Return to home</a></p>");
		out.println("<hr>");
		out.println("<p align=right><a href=\"/radiology-proj/help.html\">Help</a></p>");
		out.println("</body>");
		out.println("</html>");
	}
}