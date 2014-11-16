package toDows;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.grizzly.Grizzly;
import org.glassfish.tyrus.server.Server;
 
public class WebSocketServer {
	private static final Logger LOGGER = Grizzly.logger(Server.class);
 
    public static void main(String[] args) {
        runServer();
    } 
 
    public static void runServer() {
        Server server = new Server("localhost", 8025, "/todos", new HashMap<String,Object>(), ToDoServerEndpoint.class);
 
        try {
            server.start();
			LOGGER.info("Press 's' to shutdown now the server...");
			while(true){
				int c = System.in.read();
				if (c == 's')
					break;
			}
        } catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
        } finally {
            server.stop();
			LOGGER.info("Server stopped");
        }
    }
}
