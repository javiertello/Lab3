package toDows;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.OnError;
import javax.websocket.Session;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;

@ServerEndpoint(value = "/todo")
public class ToDoServerEndpoint {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	public final static String DEFAULT_FILE_NAME = "todo_list.json";
	
	@OnOpen
	public void onOpen(Session session) {
		logger.info("Connected ... " + session.getId());
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		String operation = message.substring(0, 6);
		String mesg = "";
		try{
			mesg = message.substring(7);
		}catch (Exception e){
			// No hay cuerpo del mensaje
		}
		switch (operation) {
		case "quit  ":
			try {
				session.close(new CloseReason(CloseCodes.NORMAL_CLOSURE,
						"Service ended"));
			} catch (IOException e) {
				//throw new RuntimeException(e);
			}
			break;
		case "add   ":
			boolean bien = addToDo(mesg);
			try{
				if(bien){
					session.getBasicRemote().sendText("Added successfully");
				}else{
					session.getBasicRemote().sendText("A problem ocurred when added...");
				}
			}catch(IOException ioe){
				System.out.printf(ioe.getMessage());
			}
			break;
			
		case "delete":
			
			break;
			
		case "get   ":
			
			break;
			
		}
	}

	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		logger.info(String.format("Session %s closed because of %s",
				session.getId(), closeReason));
	}
	
	@OnError
	public void onError(Session s, Throwable t) {
		logger.info(String.format("Session closed (error)"));
	}
	
	public boolean addToDo(String json){
		String filename = DEFAULT_FILE_NAME;

		ToDoList list = new ToDoList();
		ToDo t = new ToDo();
		Gson gson = new Gson();

		// Read the existing ToDo list.
		try {
			list = gson.fromJson(new FileReader(filename),
					ToDoList.class);
		} catch (FileNotFoundException e) {
			System.out.println(filename
					+ ": File not found.  Creating a new file.");
			return false;
		}
        
		t = gson.fromJson(json, ToDo.class);
		// Add a ToDo.
		list.addToDo(t);
        try{
        	// Write the new ToDo list back to disk.
    		FileWriter output = new FileWriter(filename);
    		output.write(gson.toJson(list));
    		output.close();
        }catch(IOException e){
        	System.out.printf(e.getMessage());
        	return false;
        }
		return true;
	}
}
