package todos;

import java.io.IOException;
import java.util.logging.Logger;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import servlets.ToDoDeleteServlet;

@ClientEndpoint
public class DeleteClientEndpoint {

	private Logger logger = Logger.getLogger(this.getClass().getName());
    private String json = ToDoDeleteServlet.json;
    
	@OnOpen
	public void onOpen(Session session) {
		logger.info("Connected ... " + session.getId());
		try {
			session.getBasicRemote().sendText("delete:"+json);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@OnMessage
	public String onMessage(String message, Session session) {
		logger.info("Received ...." + message);
		ToDoDeleteServlet.response = message;
		ToDoDeleteServlet.latch.countDown();
		return message;
		
	}

	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		logger.info(String.format("Session %s close because of %s",
				session.getId(), closeReason));
		ToDoDeleteServlet.latch.countDown();
	}
}