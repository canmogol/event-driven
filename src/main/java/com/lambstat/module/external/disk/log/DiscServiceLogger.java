package com.lambstat.module.external.disk.log;

import com.lambstat.core.log.BaseLogger;

import java.util.logging.Logger;

public class DiscServiceLogger extends BaseLogger {

    public void willWriteFileToDisk(String event) {
        log("WriteToDiskEvent: " + event);
    }

    public void fileWrittenToDisk(String fileName, int fileSizeInBytes) {
        log("written file to disk, " + fileName + " size: " + fileSizeInBytes);
    }

    public void willSendFileAvailableEvent(String event) {
        log("will send FileAvailableEvent: " + event);
    }
}
