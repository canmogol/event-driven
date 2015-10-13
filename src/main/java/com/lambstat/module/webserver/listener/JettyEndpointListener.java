package com.lambstat.module.webserver.listener;

import com.lambstat.core.listener.AbstractEndpointListener;
import com.lambstat.core.listener.EndpointListener;
import com.lambstat.core.service.Service;
import com.lambstat.module.webserver.resource.UserResource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;


public class JettyEndpointListener extends AbstractEndpointListener {

    private Server jettyServer;

    public JettyEndpointListener(Service service) {
        super(service);
    }

    @Override
    public void run() {
        ServletContextHandler context = new ServletContextHandler(
                ServletContextHandler.SESSIONS | ServletContextHandler.GZIP
        );
        context.setContextPath("/");
        context.setAttribute(EndpointListener.class.getName(), this);

        jettyServer = new Server(8080);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class,
                "/*"
        );
        jerseyServlet.setInitOrder(0);
        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                UserResource.class.getCanonicalName()
        );

        try {
            jettyServer.start();
            jettyServer.join();
        } catch (Exception e) {
            log("Could not start/join jetty, exception: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        try {
            jettyServer.stop();
        } catch (Exception e) {
            log("Could not stop jetty, exception: " + e.getMessage());
        }
    }

    public String getStatus() {
        return jettyServer.getState();
    }
}
