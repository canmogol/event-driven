package com.lambstat.module.webserver.resource;

import com.lambstat.core.endpoint.AbstractEndpointListener;
import com.lambstat.core.endpoint.EndpointListener;
import com.lambstat.core.endpoint.EndpointObserver;
import com.lambstat.core.event.BaseEvent;
import com.lambstat.core.event.Event;

import javax.servlet.ServletConfig;
import java.util.concurrent.*;

public abstract class BaseResource {

    @javax.ws.rs.core.Context
    private ServletConfig context;
    private final ExecutorService pool = Executors.newSingleThreadExecutor();

    public EndpointListener getEndpointListener() {
        return (AbstractEndpointListener) context.getServletContext().getAttribute(EndpointListener.class.getName());
    }

    /**
     * Broadcast event via web server, web server will deliver this event to its own service which is in the event loop
     */
    public void broadcast(Event event) {
        getEndpointListener().broadcast(event);
    }

    <T extends Event> Future<T> broadcast(BaseEvent event, Class<T> eventClass) {
        return pool.submit(new Callable<T>() {
            private BlockingQueue<Event> blockingQueue = new LinkedBlockingQueue<>();

            @Override
            @SuppressWarnings("unchecked")
            public T call() throws Exception {
                getEndpointListener().broadcast(event, eventClass, new EndpointObserver<Event>() {
                    @Override
                    public void handleEvent(Event event) {
                        blockingQueue.add(event);
                    }
                });
                return (T) blockingQueue.take();
            }
        });
    }

    void async(Runnable runnable) {
        pool.execute(runnable);
    }

}
