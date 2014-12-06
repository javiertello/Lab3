package servlets;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.DeploymentException;

import org.glassfish.tyrus.client.ClientManager;

import com.google.gson.Gson;

import todos.*;
@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/post" })
public class ToDoAddServlet extends HttpServlet{
	public static CountDownLatch latch;
	public static String json;
	public static String response;
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		latch = new CountDownLatch(1);
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
			out.println("<html><head><title>ToDo WebSockets WebApp</title></head>"
					+ "<body>"
					+ "<h2>Please enter a task name</h2>"
					+ "</body></html>");
		}else{
			ToDo t = new ToDo();
			t.setContext(context);
			t.setPriority(priority);
			t.setTask(task);
			t.setProject(project);
			
			Gson gson = new Gson();
			json = gson.toJson(t);
					
			ClientManager client = ClientManager.createClient();
			try {
				client.connectToServer(AddClientEndpoint.class, new URI(
						"ws://localhost:8025/todos/todo"));
				latch.await();
		
				resp.setStatus(HttpServletResponse.SC_CREATED);
				out.println("<html><head><title>Created!</title></head>"
						+ "<body>"
						+ response
						+ "</body></html>");
				} catch (DeploymentException | URISyntaxException
					| InterruptedException | IOException e) {
					resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					out.println("<html><head><title>Error</title></head>"
							+ "<body>"
							+ "An error occurred..."
							+ "</body></html>");
			}
			
		}
		
	}
}
