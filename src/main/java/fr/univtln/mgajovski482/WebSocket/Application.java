package fr.univtln.mgajovski482.WebSocket;

import org.glassfish.grizzly.Grizzly;
import org.glassfish.grizzly.http.HttpRequestPacket;
import org.glassfish.grizzly.utils.DataStructures;
import org.glassfish.grizzly.websockets.*;

import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Maxime on 05/04/2016.
 */
public class Application extends WebSocketApplication {
    private static final Logger logger = Grizzly.logger(Application.class);

    // Logged in members
    private Set<WebSocket> members = Collections.newSetFromMap(
            DataStructures.<WebSocket, Boolean>getConcurrentMap());

    // initialize optimized broadcaster
    private final Broadcaster broadcaster = new OptimizedBroadcaster();


    @Override
    public WebSocket createSocket(ProtocolHandler handler,
                                  HttpRequestPacket request,
                                  WebSocketListener... listeners) {
        return new ChatWebSocket(handler, request, listeners);
    }

    @Override
    public void onMessage(WebSocket websocket, String data) {
        // check if it's login notification
        if (data.startsWith("login:")) {
            // process login
            login((ChatWebSocket) websocket, data);
            members.add(websocket);
        } else {
            // broadcast the message
            broadcast(((ChatWebSocket)websocket).getUser(), data);
        }
    }

    @Override
    public void onConnect(WebSocket socket) {
        // do nothing
        // override this method to take control over members list
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClose(WebSocket websocket, DataFrame frame) {
        members.remove(websocket);
        broadcast("system", ((ChatWebSocket)websocket).getUser() + " left the chat");
    }

    /**
     * Broadcasts the text message from the user.
     *
     * @param user the user name
     * @param text the text message
     */
    private void broadcast(String user, String text) {
        logger.log(Level.INFO, "Broadcasting: {0} from: {1}", new Object[]{text, user});
        final String jsonMessage = toJsonp(user, text);

        broadcaster.broadcast(members, jsonMessage);
    }

    private void login(ChatWebSocket websocket, String frame) {
        if (websocket.getUser() == null) { // check if it's not registered user
            logger.info("ChatApplication.login");
            // set the user name
            websocket.setUser(frame.split(":")[1].trim());
            // broadcast the login notification
            broadcast("system", websocket.getUser() + " has joined the chat.");
        }
    }

    private static String toJsonp(String name, String message) {
        return "window.parent.app.update({ name: \"" + escape(name) +
                "\", message: \"" + escape(message) + "\" });\n";
    }

    private static String escape(String orig) {
        StringBuilder buffer = new StringBuilder(orig.length());

        for (int i = 0; i < orig.length(); i++) {
            char c = orig.charAt(i);
            switch (c) {
                case '\b':
                    buffer.append("\\b");
                    break;
                case '\f':
                    buffer.append("\\f");
                    break;
                case '\n':
                    buffer.append("<br />");
                    break;
                case '\r':
                    // ignore
                    break;
                case '\t':
                    buffer.append("\\t");
                    break;
                case '\'':
                    buffer.append("\\'");
                    break;
                case '\"':
                    buffer.append("\\\"");
                    break;
                case '\\':
                    buffer.append("\\\\");
                    break;
                case '<':
                    buffer.append("<");
                    break;
                case '>':
                    buffer.append(">");
                    break;
                case '&':
                    buffer.append("&");
                    break;
                default:
                    buffer.append(c);
            }
        }

        return buffer.toString();
    }
}