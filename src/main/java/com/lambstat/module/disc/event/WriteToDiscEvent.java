package com.lambstat.module.disc.event;

import com.lambstat.core.event.BaseEvent;
import com.lambstat.core.event.Event;

public class WriteToDiscEvent extends BaseEvent {

    private final byte[] bytes;
    private final String fileName;

    public WriteToDiscEvent(byte[] bytes, String fileName) {
        this(null, bytes, fileName);
    }

    public WriteToDiscEvent(Event parent, byte[] bytes, String fileName) {
        super(parent);
        this.bytes = bytes;
        this.fileName = fileName;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public String getFileName() {
        return fileName;
    }
}
