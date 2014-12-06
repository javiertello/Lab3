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

import todos.AddClientEndpoint;
import todos.DeleteClientEndpoint;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/delete" })
public class ToDoDeleteServlet extends HttpServlet{
	
	public static CountDownLatch latch;
	public static String json;
	public static String response;
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		latch = new CountDownLatch(1);
		String name = req.getParameter("id");

		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		
		if(name.equals("")){
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			out.println("<html><head><title>ToDo WebSocket WebApp</title></head>"
					+ "<body>"
					+ "<h2>Please enter a task name</h2>"
					+ "</body></html>");
		}else{
			ClientManager client = ClientManager.createClient();
			try {
				json = name;
				client.connectToServer(DeleteClientEndpoint.class, new URI(
						"ws://localhost:8025/todos/todo"));
				latch.await();
		
				resp.setStatus(HttpServletResponse.SC_OK);
				out.println("<html><head><title>Deleted!</title></head>"
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
