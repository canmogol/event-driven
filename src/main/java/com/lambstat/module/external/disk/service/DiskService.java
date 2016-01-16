package com.lambstat.module.external.disk.service;

import com.lambstat.core.event.BaseEvent;
import com.lambstat.core.event.Event;
import com.lambstat.core.service.AbstractService;
import com.lambstat.module.external.disk.event.FileAvailableEvent;
import com.lambstat.module.external.disk.event.WriteToDiskEvent;
import com.lambstat.module.external.disk.log.DiscServiceLogger;

import java.util.HashSet;

public class DiskService extends AbstractService {

    private DiscServiceLogger logger = new DiscServiceLogger();

    public DiskService() {
        super(new HashSet<Class<? extends Event>>() {{
            add(WriteToDiskEvent.class);
        }});
    }

    public void handleEvent(WriteToDiskEvent event) {
        logger.willWriteFileToDisk(event.toString());
        logger.fileWrittenToDisk(event.getFileName(), event.getBytes().length);
        BaseEvent e = new FileAvailableEvent(
                event,
                event.getFileName(),
                event.getBytes()
        );
        logger.willSendFileAvailableEvent(e.toString());
        broadcast(e);
    }

}
