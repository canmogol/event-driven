package com.lambstat.module.external.disk.event;

import com.lambstat.core.event.BaseEvent;
import com.lambstat.core.event.Event;

public class WriteToDiskEvent extends BaseEvent {

    private final byte[] bytes;
    private final String fileName;

    public WriteToDiskEvent(byte[] bytes, String fileName) {
        this(null, bytes, fileName);
    }

    public WriteToDiskEvent(Event parent, byte[] bytes, String fileName) {
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
