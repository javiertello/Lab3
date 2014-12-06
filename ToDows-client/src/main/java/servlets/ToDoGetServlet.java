package servlets;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
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

@WebServlet(urlPatterns = { "/get" })
public class ToDoGetServlet extends HttpServlet {
	
	public static CountDownLatch latch;
	public static String json;
	public static String response;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		latch = new CountDownLatch(1);
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		String list = "";
		
		ClientManager client = ClientManager.createClient();
		try {
			client.connectToServer(ListClientEndpoint.class, new URI(
					"ws://localhost:8025/todos/todo"));
			latch.await();
			// Despierto
			if(!response.toLowerCase().equals("error")){
				Gson g = new Gson();
				ToDoList tl = g.fromJson(response, ToDoList.class);
				
				for(ToDo t : tl.getList()){
					list = addToToDoList(list, t);
				}
				resp.setStatus(HttpServletResponse.SC_OK);
				out.println("<html><head><title>ToDo List</title></head>"
						+ "<body>"
						+ list
						+ "</body></html>");
			}
			else{
				resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				out.println("<html><head><title>Error</title></head>"
						+ "<body>"
						+ "An error occurred..."
						+ "</body></html>");
			}
			} catch (DeploymentException | URISyntaxException
				| InterruptedException | IOException e) {
				resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				out.println("<html><head><title>Error</title></head>"
						+ "<body>"
						+ "An error occurred..."
						+ "</body></html>");
		}
			
	}

	private String addToToDoList(String list, ToDo todo) {
		list +=   "<b>Task: </b>" + todo.getTask()
				+ "<br/><b>Context: </b>" + todo.getContext()
				+ "<br/><b>Project: </b>" +  todo.getProject()
				+ "<br/><b>Priority (1-10): </b>" + todo.getPriority() + "<br/><hr>";	
		return list;
	}

}
