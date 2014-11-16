package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import javax.ws.rs.core.Response;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/delete" })
public class ToDoDeleteServlet extends HttpServlet{
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String id = req.getParameter("id");

		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		
		if(id.equals("")){
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			out.println("<html><head><title>ToDo REST WebApp</title></head>"
					+ "<body>"
					+ "<h2>Please enter a ID</h2>"
					+ "</body></html>");
		}else{
			Client client = ClientBuilder.newClient();
			Response response = client
					.target("http://localhost:8081/todos/todo/"+id).request()
					.delete();
			resp.setStatus(response.getStatus());
			out.println("<html><head><title>ToDo REST</title></head>"
					+ "<body>"
					+ "<b>"+ response.getStatusInfo()+ "</b>"
					+ "</body></html>");

		}
		
	}
}
