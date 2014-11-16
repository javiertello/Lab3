package toDows;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;

import javax.websocket.DeploymentException;

import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.server.Server;
import org.junit.After;
import org.junit.Test;

import com.google.gson.Gson;


/**
 * A simple test suite
 *
 */
public class ToDoServiceTest {
	
	Server server;
	
	@Test
	private void addToDo(){
		try{
			launchServer();		
			ToDo t = new ToDo();
			t.setTask("Tarea JUNIT");
			t.setContext("Contexto JUNIT");
			t.setPriority(9);
			t.setProject("TESTS JUNIT");
			Gson g = new Gson();
			String mensaje = "add   :";
			mensaje += g.toJson(t);
			
			ClientManager client = ClientManager.createClient();
			client.connectToServer(ToDoClientEndpoint.class, new URI(
					"ws://localhost:8025/todos/todo"));
			
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}

	private void launchServer() throws IOException {
		Server server = new Server("localhost", 8025, "/todos", new HashMap<String,Object>(), ToDoServerEndpoint.class);
        try {
			server.start();
		} catch (DeploymentException e) {
			e.printStackTrace();
		}
	}

	@After
	public void shutdown() {
		if (server != null) {
			server.stop();
		}
		server = null;
	}

}
