package com.lambstat.stat;

import com.lambstat.stat.event.Event;
import com.lambstat.stat.service.StatService;

public class Main {

    public static void main(String[] args) {
        StatService<Event> statService = new StatService<Event>();
        new Thread(statService).start();

        new Thread(new EventProducerTester(statService)).start();
    }

}
