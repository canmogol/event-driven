package com.lambstat.module.external.webserver.listener;

import com.lambstat.core.endpoint.AbstractEndpointListener;
import com.lambstat.core.endpoint.EndpointListener;
import com.lambstat.core.service.Service;
import com.lambstat.module.external.webserver.log.JettyEndpointLogger;
import com.lambstat.module.external.webserver.resource.UserResource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ServerProperties;


public class JettyEndpointListener extends AbstractEndpointListener {

    private JettyEndpointLogger logger = new JettyEndpointLogger();
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

        jettyServer = new Server(getService().getConfiguration().getWebServerPort());
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class,
                "/*"
        );
        jerseyServlet.setAsyncSupported(true);
        jerseyServlet.setInitOrder(0);
        jerseyServlet.setInitParameter(
                ServerProperties.PROVIDER_PACKAGES,
                UserResource.class.getPackage().getName()
        );

        try {
            jettyServer.start();
            jettyServer.join();
        } catch (Exception e) {
            logger.couldNotStartJoin(e.getMessage());
        }
    }

    @Override
    public void close() {
        try {
            jettyServer.stop();
        } catch (Exception e) {
            logger.couldNotStop(e.getMessage());
        }
    }

    public String getStatus() {
        return jettyServer.getState();
    }

}
