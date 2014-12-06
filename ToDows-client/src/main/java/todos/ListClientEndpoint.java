package todos;

import java.io.IOException;
import java.util.logging.Logger;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import servlets.ToDoGetServlet;

@ClientEndpoint
public class ListClientEndpoint {

	private Logger logger = Logger.getLogger(this.getClass().getName());
    private String json = ToDoGetServlet.json;
    
	@OnOpen
	public void onOpen(Session session) {
		logger.info("Connected ... " + session.getId());
		try {
			session.getBasicRemote().sendText("get   :"+json);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@OnMessage
	public String onMessage(String message, Session session) {
		logger.info("Received ...." + message);
		ToDoGetServlet.response = message;
		ToDoGetServlet.latch.countDown();
		return message;
		
	}

	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		logger.info(String.format("Session %s close because of %s",
				session.getId(), closeReason));
		ToDoGetServlet.latch.countDown();
	}
}