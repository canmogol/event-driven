package com.lambstat.module.disc.event;

import com.lambstat.stat.event.Event;

public class WriteToDiscEvent implements Event {

    private final byte[] bytes;
    private final String fileName;

    public WriteToDiscEvent(byte[] bytes, String fileName) {
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
