package com.lambstat.module.disc.service;

import com.lambstat.core.event.BaseEvent;
import com.lambstat.core.event.Event;
import com.lambstat.core.service.AbstractService;
import com.lambstat.module.disc.event.FileAvailableEvent;
import com.lambstat.module.disc.event.WriteToDiscEvent;

import java.util.HashSet;

public class DiscService extends AbstractService {

    public DiscService() {
        super(new HashSet<Class<? extends Event>>() {{
            add(WriteToDiscEvent.class);
        }});
    }

    public void handleEvent(WriteToDiscEvent event) {
        log("WriteToDiscEvent: " + event);
        log("written file to disk, " + event.getFileName() + " size: " + event.getBytes().length);
        BaseEvent e = new FileAvailableEvent(
                event,
                event.getFileName(),
                event.getBytes()
        );
        log("will send WriteToDiscEvent: " + e);
        broadcast(e);

    }

}
