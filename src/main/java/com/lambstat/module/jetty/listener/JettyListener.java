package com.lambstat.module.jetty.listener;

import com.lambstat.core.listener.AbstractListener;
import com.lambstat.core.service.Service;
import com.lambstat.module.jetty.resource.JettyResource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;


public class JettyListener extends AbstractListener {

    private Server jettyServer;

    public JettyListener(Service service) {
        super(service);
    }

    @Override
    public void run() {
        ServletContextHandler context = new ServletContextHandler(
                ServletContextHandler.SESSIONS | ServletContextHandler.GZIP
        );
        context.setContextPath("/");
        context.setAttribute(AbstractListener.class.getName(), this);

        jettyServer = new Server(8080);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class,
                "/*"
        );
        jerseyServlet.setInitOrder(0);
        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                JettyResource.class.getCanonicalName()
        );

        try {
            jettyServer.start();
            jettyServer.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            if (jettyServer != null) {
                jettyServer.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getStatus() {
        return jettyServer.getState();
    }
}
