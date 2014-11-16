package toDows;

import java.io.IOException;
import java.util.logging.Logger;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.server.ServerEndpoint;

@ClientEndpoint
public class ToDoClientEndpoint {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	@OnOpen
	public void onOpen(String message, Session session) {
		logger.info("Connected ... " + session.getId());
		try {
			session.getBasicRemote().sendText(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@OnMessage
	public String onMessage(String message, Session session) {
	
		logger.info("Received ...." + message + " from "+session.getId());
		return message;
	}

	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		logger.info(String.format("Session %s close because of %s",
				session.getId(), closeReason));
	}
}
