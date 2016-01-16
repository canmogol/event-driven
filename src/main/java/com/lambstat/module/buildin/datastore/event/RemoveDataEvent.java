package com.lambstat.module.buildin.datastore.event;

import com.lambstat.core.event.BaseEvent;
import com.lambstat.core.event.Event;

public class RemoveDataEvent extends BaseEvent {

    private String key;

    public RemoveDataEvent(String key) {
        this.key = key;
    }

    public RemoveDataEvent(Event parent, String key) {
        super(parent);
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
