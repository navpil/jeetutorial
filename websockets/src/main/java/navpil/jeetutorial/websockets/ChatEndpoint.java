package navpil.jeetutorial.websockets;

import jakarta.websocket.EncodeException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(
        value="/chat/{username}",
        encoders = StringCodec.class,
        decoders = StringCodec.class
)
public class ChatEndpoint {
    private Session session;
    private static Set<ChatEndpoint> chatEndpoints
            = new CopyOnWriteArraySet<>();
    private static HashMap<String, String> users = new HashMap<>();

    @OnOpen
    public void onOpen(
            Session session,
            @PathParam("username") String username) {

        this.session = session;
        chatEndpoints.add(this);
        users.put(session.getId(), username);

        String message = "User " + username + " connected";
        broadcast(message);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        broadcast("Message from " +  users.get(session.getId()) + ": " + message);
    }

    @OnClose
    public void onClose(Session session) throws IOException {

        chatEndpoints.remove(this);
        broadcast("User " + users.get(session.getId()) + " disconnected");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        System.out.println("Error occured for " + users.get(session.getId()) + ": " + throwable.toString());
    }

    private static void broadcast(String message) {

        chatEndpoints.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote().
                            sendObject(message);
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
