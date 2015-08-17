package com.lambstat.module.disc.service;

import com.lambstat.module.disc.event.WriteToDiscEvent;
import com.lambstat.stat.event.Event;
import com.lambstat.stat.service.AbstractService;

import java.util.HashSet;

public class DiscService extends AbstractService {

    public DiscService() {
        registerEvents(new HashSet<Class<? extends Event>>() {{
            add(WriteToDiscEvent.class);
        }});
    }

    public void handleEvent(WriteToDiscEvent event) {
        log("WriteToDiscEvent: " + event);
        log("written file to disk, " + event.getFileName() + " size: " + event.getBytes().length);
    }

}
