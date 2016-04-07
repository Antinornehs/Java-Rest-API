package fr.univtln.mgajovski482.WebSocket;

/**
 * Created by Maxime on 05/04/2016.
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import fr.univtln.mgajovski482.JPA.DAO.AbstractDAOJPA;
import fr.univtln.mgajovski482.JPA.DAO.DAOEvent;
import fr.univtln.mgajovski482.JPA.DAO.DAOEventListener;
import org.glassfish.grizzly.websockets.WebSocketApplication;
import org.slf4j.LoggerFactory;

import javax.json.spi.JsonProvider;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by bruno on 28/03/15.
 */
@ServerEndpoint(value = "/elementary")
public class EchoEndPoint extends WebSocketApplication implements DAOEventListener {
    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EchoEndPoint.class);

    private static List<Session> sessions = new ArrayList<>();

    public EchoEndPoint() {
        System.out.println("lol");
        AbstractDAOJPA.addEventListener(this);
    }

    @OnClose
    public void onClose(final Session session, EndpointConfig endpointConfig) {
        System.out.println(session.getId() + " leaved.");
        sessions.remove(session);
    }

//    @Override
//    public void onDAOEvent(DAOEvent daoEvent) {
//        LOGGER.info("DAO Event " + daoEvent);
//        ObjectWriter ow = new JsonProvider().getObjectMapper().writer();
//        String json = null;
//        try {
//            json = ow.writeValueAsString(daoEvent);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        for (Session s : sessions)
//            try {
//                s.getBasicRemote().sendText(json);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        ;
//    }

    @OnMessage
    public String onMessage(String message, Session session) {
        LOGGER.info(message + " " + session);
        return message;
    }

    @OnOpen
    public void onOpen(final Session session, EndpointConfig endpointConfig) throws IOException {
        LOGGER.info("new Client connected in session " + session.getId());
        //session.getBasicRemote().sendText("Hello " + session.getId());
        sessions.add(session);
    }

    @Override
    public void onDAOEvent(DAOEvent daoEvent) {

    }
}