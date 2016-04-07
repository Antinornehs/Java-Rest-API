package fr.univtln.mgajovski482;
//
//import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
//import com.sun.jersey.api.core.PackagesResourceConfig;
import fr.univtln.mgajovski482.JPA.DAO.Player.PlayerDAO;
import fr.univtln.mgajovski482.JPA.DAO.Player.PlayerDAOJPA;
import fr.univtln.mgajovski482.JPA.Entities.PersonalInformations.PersonalInformationsJPA;
import fr.univtln.mgajovski482.JPA.Entities.Player.PlayerJPA;
import fr.univtln.mgajovski482.GenericBuilder.GenericBuilder;
import fr.univtln.mgajovski482.JPA.Exception.DAOException;
import fr.univtln.mgajovski482.WebSocket.Application;
import fr.univtln.mgajovski482.WebSocket.EchoEndPoint;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.grizzly.websockets.WebSocketAddOn;
import org.glassfish.grizzly.websockets.WebSocketEngine;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpContainer;
import org.glassfish.jersey.server.ContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.tyrus.server.Server;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.websocket.server.ServerEndpointConfig;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class App 
{


//    private static int getPort(int defaultPort) {
//        //grab port from environment, otherwise fall back to default port 9998
//        String httpPort = System.getProperty("jersey.test.port");
//        if (null != httpPort) {
//            try {
//                return Integer.parseInt(httpPort);
//            } catch (NumberFormatException e) {
//            }
//        }
//        return defaultPort;
//    }
//TODO code a decommenté
//    private static URI getBaseURI() {
//        return UriBuilder.fromUri("http://localhost/").port(getPort(9998)).build();
//    }

//    public static final URI BASE_URI = getBaseURI();


//    protected static HttpServer startServer() throws IOException {
//        ResourceConfig resourceConfig = new PackagesResourceConfig("fr.univtln.mgajovski482");
//        final Map<String, Object> config = new HashMap<String, Object>();
//        config.put("com.sun.jersey.api.json.POJOMappingFeature", true);
//        //config.pu
//        resourceConfig.setPropertiesAndFeatures(config);
//        System.out.println("Starting grizzly2...");
//        return GrizzlyServerFactory.createHttpServer(BASE_URI, resourceConfig);
//    }

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main(String[] args) throws IOException, DAOException {

          final String IP = "127.0.0.1";
          final int PORT = 8080;
          final String ROOT = "elementary";

        URI baseUri = UriBuilder.fromUri("http://" + IP + "/").port(PORT).path(ROOT).build();

        final ResourceConfig resourceConfig = new ResourceConfig()
                .property("jersey.config.server.provider.classnames","org.glassfish.jersey.filter.LoggingFilter")
                .property("jersey.config.xml.formatOutput", "true")
                .property("jersey.config.server.tracing", "ALL")
                        //.property("jersey.config.server.tracing.threshold", "VERBOSE") //VERBOSE
                .property("jersey.config.server.tracing.threshold", "SUMMARY") //VERBOSE
                /*.property("com.sun.jersey.spi.container.ContainerRequestFilters",
                        "com.sun.jersey.api.container.filter.LoggingFilter")*/
                .packages("fr.univtln.mgajovski482")
                .register(CORSResponseFilter.class);


        HttpServer httpServer = new HttpServer();
        NetworkListener listener = new NetworkListener("elementary", baseUri.getHost(), baseUri.getPort());

        httpServer.addListener(listener);
        ServerConfiguration config = httpServer.getServerConfiguration();


        GrizzlyHttpContainer grizzlyHttpJerseyContainer = ContainerFactory.createContainer(GrizzlyHttpContainer.class, resourceConfig);

        config.addHttpHandler(grizzlyHttpJerseyContainer, baseUri.getPath() + "/api");


        final WebSocketAddOn addon = new WebSocketAddOn();
        for (NetworkListener l : httpServer.getListeners()) {
            l.registerAddOn(addon);
        }

        ServerEndpointConfig serverEndpointConfig = ServerEndpointConfig.Builder.create(EchoEndPoint.class, baseUri.getPath()+"/ws").build();

        WebSocketEngine.getEngine().register("/chat", "/*", new Application());


        /**
         * Fin essai
         */
        EntityManager em = AppParameters.EMF.createEntityManager();

        PlayerDAO playerDAO     = new PlayerDAOJPA(em);

        EntityTransaction transaction = em.getTransaction();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        PersonalInformationsJPA personalInformationsJPA =
                new GenericBuilder<>(PersonalInformationsJPA.class)
                        .with("lastName", "Gajovski")
                        .with("firstName", "Maxime")
                        .with("email", "yaaaa@gmail.com")
                        .with("birthDate", Calendar.getInstance().getTime())
                        .build();
PersonalInformationsJPA personalInformationsJPA2 =
                new GenericBuilder<>(PersonalInformationsJPA.class)
                        .with("lastName", "Gajovski")
                        .with("firstName", "Maxime")
                        .with("email", "dd@gmail.com")
                        .with("birthDate", Calendar.getInstance().getTime())
                        .build();
        System.out.println(format1.format(Calendar.getInstance().getTime()));
        PlayerJPA player = new GenericBuilder<>(PlayerJPA.class)
                .with("personalInformations", personalInformationsJPA)
                .with("nickName", "Antinornehs")
                .build();


        PlayerJPA player2 = new GenericBuilder<>(PlayerJPA.class)
                .with("personalInformations", personalInformationsJPA2)
                .with("nickName", "Maxime")
                .build();

        try{
            transaction = em.getTransaction();
            transaction.begin();
            playerDAO.create(player);
            playerDAO.create(player2);
            //playerDAO.delete();
            //playerDAO.delete( 1);
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                LOGGER.severe("Transaction annulée.");
            } else
                LOGGER.severe("Pas de transaction.");
            e.printStackTrace();
        }
//
//        try{
//            transaction = em.getTransaction();
//            transaction.begin();
//            playerDAO.add(player2);
//            transaction.commit();
//        } catch (Exception e) {
//            if (transaction != null) {
//                transaction.rollback();
//                LOGGER.severe("Transaction annulée.");
//            } else
//                LOGGER.severe("Pas de transaction.");
//            e.printStackTrace();
//        }
        try {
            transaction.begin();

            LOGGER.info("Toutes les personnes     : " + playerDAO.findAll());
            transaction.commit();


        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                LOGGER.severe("Transaction annulée.");
            } else
                LOGGER.severe("Pas de transaction.");
            e.printStackTrace();
        } finally {
            em.close();
        }
        //TODO Code a decommenté
        // Grizzly 2 initialization
//        HttpServer httpServer = startServer();
//        System.out.println(String.format("Jersey app started with WADL available at "
//                        + "%sapplication.wadl\nHit enter to stop it...",
//                BASE_URI));
//        System.in.read();
//        httpServer.stop();

        try {
            httpServer.start();

            System.out.println();
            System.out.println("Press enter to stop the server...");
            System.in.read();
        } finally {
            httpServer.shutdownNow();
        }
//
// EntityTransaction transac = em.getTransaction();
//
//        PersonalInformationsJPA personalInformationsJPA =
//                new GenericBuilder<>(PersonalInformationsJPA.class)
//                        .with("lastName", "Gajovski")
//                        .with("firstName", "Maxime")
//                        .with("email", "yaaaa@gmail.com")
//                        .with("birthDate", Calendar.getInstance().getTime())
//                        .build();
//
//        PlayerJPA player = new GenericBuilder<>(PlayerJPA.class)
//                .with("personalInformations", personalInformationsJPA)
//                .with("nickName", "Antinornehs")
//                .build();
//
//        transac.begin();
//        em.persist(player);
//        transac.commit();

//        Server websocketServer = new Server(IP, TYRUS_PORT, "/websocket", null, EchoEndPoint.class);
//
//        websocketServer.start();
//
//        LOGGER.info(String.format("Tyrus websocket listening at %s:%s", IP, TYRUS_PORT));
//
//        final HttpServer grizzlyServer = startServer();
//        LOGGER.info(String.format("Jersey app started with WADL available at "
//                + "%sapplication.wadl\nHit enter to stop it...", JERSEY_URI));
//
//        System.in.read();
//
//        grizzlyServer.shutdownNow();
//        websocketServer.stop();
    }
}
