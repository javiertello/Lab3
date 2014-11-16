package servlets;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import todos.*;

@WebServlet(urlPatterns = { "/get" })
public class ToDoGetServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		String id = req.getParameter("id");
		String list = "";
		if(id.equals("")){ //lista todas
			Client client = ClientBuilder.newClient();
			Response response = client
					.target("http://localhost:8081/todos")
					.request(MediaType.APPLICATION_JSON).get();
			if(response.getStatus() == HttpServletResponse.SC_OK){
				ToDoList tl = response.readEntity(ToDoList.class);
				for(ToDo t : tl.getList()){
					list = addToToDoList(list, t);
				}
				
				resp.setStatus(HttpServletResponse.SC_OK);
				out.println("<html><head><title>OK!</title></head>"
						+ "<body>"
						+ list
						+ "</body></html>");
			}
			else{
				resp.setStatus(response.getStatus());
				out.println("<html><head><title>Error!</title></head>"
						+ "<body>"
						+ "<b>"+ response.getStatusInfo()+ "</b>"
						+ "</body></html>");
			}
			
		}else{
			Client client = ClientBuilder.newClient();
			Response response = client
					.target("http://localhost:8081/todos/todo/"+id)
					.request(MediaType.APPLICATION_JSON).get();
			if(response.getStatus() == HttpServletResponse.SC_OK){
				ToDo t = response.readEntity(ToDo.class);
				list = addToToDoList(list, t);
				
				resp.setStatus(HttpServletResponse.SC_OK);
				out.println("<html><head><title>OK!</title></head>"
						+ "<body>"
						+ list
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

	private String addToToDoList(String list, ToDo todo) {
		list +=   "<b>Task: </b>" + todo.getTask()
				+ "<br/><b>Context: </b>" + todo.getContext()
				+ "<br/><b>Project: </b>" +  todo.getProject()
				+ "<br/><b>Priority (1-10): </b>" + todo.getPriority() + "<br/><hr>";	
		return list;
	}

}
