package fr.univtln.mgajovski482.WebSocket;

import org.glassfish.grizzly.websockets.WebSocketEngine;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Created by Maxime on 05/04/2016.
 */
public class WebSocketServlet extends HttpServlet {

    private Application app;

    @Override
    public void init(ServletConfig config) throws ServletException {
        app = new Application();
        WebSocketEngine.getEngine().register(config.getServletContext().getContextPath(), "/chat", app);
    }

    @Override
    public void destroy() {
        WebSocketEngine.getEngine().unregister(app);
    }
}