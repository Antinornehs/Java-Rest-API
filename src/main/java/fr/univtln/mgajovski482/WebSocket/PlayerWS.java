package fr.univtln.mgajovski482.WebSocket;

import fr.univtln.mgajovski482.GenericBuilder.GenericBuilder;
import fr.univtln.mgajovski482.JPA.Entities.PersonalInformations.PersonalInformationsJPA;
import fr.univtln.mgajovski482.JPA.Entities.Player.IPlayer;
import fr.univtln.mgajovski482.JPA.Entities.Player.PlayerJPA;
import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Maxime on 05/04/2016.
 */
public class PlayerWS {
    public final static String SERVER_IP;
    public final static int SERVER_PORT;
    private IPlayer sender;

    public PlayerWS(IPlayer sender) {
        this.sender = sender;
    }

    static {
        String ip = "127.0.0.1";
        int port = 8080;
        SERVER_IP = ip;
        SERVER_PORT = port;
        System.out.println("Server IP:" + SERVER_IP + " Port: " + SERVER_PORT);
    }

    private Session session;

    @OnOpen
    public void onOpen(final Session session, EndpointConfig endpointConfig) throws IOException, EncodeException {
        this.session = session;
        System.out.println("I am " + session.getId());
        System.out.println("Sending Hello message to server");
        session.getBasicRemote().sendObject(new PayLoadBean(new Date(), sender, "Hello"));
    }

    @OnMessage
    public void OnMessage(PayLoadBean bean) {
        System.out.println("RECU !");
        System.out.println(bean.getDate() + " (" //+ bean.getSender()
                + ") " + bean.getMessage());
    }

    @OnClose
    public void OnClose(final Session session, EndpointConfig endpointConfig) {
        System.out.println("Session closed");
    }

    public void closeSession() throws IOException {
        if (session.isOpen())
            session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "OK"));
    }

    public void sendMessage(String message) {
        PayLoadBean bean = new PayLoadBean(new Date(), sender, message);
        /*        System.out.println("The PayloadBean toString(): "+bean);

        //To print the JSON encoded version
        try {
            StringWriter sw = new StringWriter();
            new PayloadBean.PayloadBeanCode().encode(bean, sw);
            System.out.println("The JSON from the Payload: "+sw.toString());
        } catch (EncodeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
          */
        try {
            session.getBasicRemote().sendObject(bean);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EncodeException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        PersonalInformationsJPA personalInformationsJPA =
                new GenericBuilder<>(PersonalInformationsJPA.class)
                        .with("lastName", "Gajovski")
                        .with("firstName", "Maxime")
                        .with("email", "yaaaa@gmail.com")
                        .with("birthDate", Calendar.getInstance().getTime())
                        .build();
        PlayerJPA player = new GenericBuilder<>(PlayerJPA.class)
                .with("personalInformations", personalInformationsJPA)
                .with("nickName", "Antinornehs")
                .build();


        PlayerWS beanClient = new PlayerWS(player);
        try {
            final ClientManager client = ClientManager.createClient();
            client.connectToServer(
                    beanClient,
                    URI.create("ws://" + SERVER_IP + ":" + SERVER_PORT + "/elementary/chat")
            );

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Send empty line to stop the client.");
            String line;
            do {
                line = reader.readLine();
                if (!"".equals(line))
                    beanClient.sendMessage(line);
            } while (!"".equals(line));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                beanClient.closeSession();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}