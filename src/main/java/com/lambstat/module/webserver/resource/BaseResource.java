package com.lambstat.module.webserver.resource;

import com.lambstat.core.endpoint.AbstractEndpointListener;
import com.lambstat.core.endpoint.EndpointListener;
import com.lambstat.core.endpoint.EndpointObserver;
import com.lambstat.core.event.BaseEvent;
import com.lambstat.core.event.Event;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.util.Date;
import java.util.concurrent.*;
import java.util.logging.Logger;

public abstract class BaseResource {

    private final ExecutorService pool = Executors.newFixedThreadPool(10);

    private Logger L = Logger.getLogger(getClass().getSimpleName());

    /**
     * Broadcast event via web server, web server will deliver this event to its own service which is in the event loop
     *
     * @param event
     */
    public void broadcast(Event event) {
        getEndpointListener().broadcast(event);
    }

    public EndpointListener getEndpointListener() {
        return (AbstractEndpointListener) ServletContextHandler.getCurrentContext().getAttribute(EndpointListener.class.getName());
    }

    Future<Event> broadcast(BaseEvent event, Class<? extends Event> eventClass) {
        return pool.submit(new Callable<Event>() {
            private BlockingQueue<Event> blockingQueue = new LinkedBlockingQueue<>();

            @Override
            public Event call() throws Exception {
                getEndpointListener().broadcast(event, eventClass, new EndpointObserver() {
                    @Override
                    public void handleEvent(Event event) {
                        blockingQueue.add(event);
                    }
                });
                return blockingQueue.take();
            }
        });
    }

    public void log(String log) {
        L.info("[" + new Date() + "] [" + Thread.currentThread().getId() + "] [" + getClass().getSimpleName() + "] " + log);
    }

    public void error(String log) {
        L.severe("[" + new Date() + "] [" + Thread.currentThread().getId() + "] [" + getClass().getSimpleName() + "] " + log);
    }

}
