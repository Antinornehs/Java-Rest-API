package fr.univtln.mgajovski482.WebSocket;

import org.glassfish.grizzly.Grizzly;
import org.glassfish.grizzly.http.HttpRequestPacket;
import org.glassfish.grizzly.websockets.DefaultWebSocket;
import org.glassfish.grizzly.websockets.ProtocolHandler;
import org.glassfish.grizzly.websockets.WebSocketListener;

import java.util.logging.Logger;

/**
 * Created by Maxime on 05/04/2016.
 */
public class ChatWebSocket extends DefaultWebSocket {
    private static final Logger logger = Grizzly.logger(ChatWebSocket.class);

    // chat user name
    private volatile String user;

    public ChatWebSocket(ProtocolHandler protocolHandler,
                         HttpRequestPacket request,
                         WebSocketListener... listeners) {
        super(protocolHandler, request, listeners);
    }

    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
}