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
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import todos.*;
@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/post" })
public class ToDoPostServlet extends HttpServlet{
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String task = req.getParameter("task");
		String context = req.getParameter("context");
		String project = req.getParameter("project");
		int priority;
		if(req.getParameter("priority").equals("")){
			priority = 5;
		}else{
			priority = Integer.parseInt(req.getParameter("priority"));
		}	
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		
		if(task.equals("")){
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			out.println("<html><head><title>ToDo REST WebApp</title></head>"
					+ "<body>"
					+ "<h2>Please enter a task name</h2>"
					+ "</body></html>");
		}else{
			ToDo t = new ToDo();
			t.setContext(context);
			t.setPriority(priority);
			t.setTask(task);
			t.setProject(project);
			
			Client client = ClientBuilder.newClient();
			Response response = client.target("http://localhost:8081/todos")
					.request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(t, MediaType.APPLICATION_JSON));
			if(response.getStatus() == 201){
				resp.setStatus(HttpServletResponse.SC_CREATED);
				out.println("<html><head><title>Created!</title></head>"
						+ "<body>"
						+ response.getStatusInfo()
						+ "</body></html>");
			}
			else{
				resp.setStatus(response.getStatus());
				out.println("<html><head><title>Error!</title></head>"
						+ "<body>"
						+ "<b>"+ response.getStatusInfo()+ "</b>"
						+ "</body></html>");
			}
			
		}
		
	}
}
