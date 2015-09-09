package com.lambstat.module.disc.event;

import com.lambstat.core.event.BaseEvent;
import com.lambstat.core.event.Event;

public class FileAvailableEvent extends BaseEvent {

    private final String fileName;
    private final byte[] bytes;

    public FileAvailableEvent(Event event, String fileName, byte[] bytes) {
        super(event);
        this.fileName = fileName;
        this.bytes = bytes;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getBytes() {
        return bytes;
    }

}
